package ru.udya.querydsl.cuba.core;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.haulmont.cuba.core.TransactionalDataManager;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.FluentValueLoader;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Metadata;
import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ParamExpression;
import com.querydsl.core.types.ParamNotSetException;
import com.querydsl.core.types.dsl.Param;
import com.querydsl.jpa.EclipseLinkTemplates;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.QueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public abstract class AbstractCubaQuery<T, Q extends AbstractCubaQuery<T, Q>> extends CubaQueryBase<T, Q> {

    private static final long serialVersionUID = 5397497620388267860L;

    private static final Logger logger = LoggerFactory.getLogger(AbstractCubaQuery.class);

    protected final Multimap<String, Object> hints = LinkedHashMultimap.create();

    protected final TransactionalDataManager txDm;

    protected final Metadata metadata;

    protected final QueryHandler queryHandler;

    public AbstractCubaQuery(TransactionalDataManager dm, Metadata metadata) {
        this(dm, metadata, EclipseLinkTemplates.DEFAULT, new DefaultQueryMetadata());
    }

    public AbstractCubaQuery(TransactionalDataManager dm, Metadata metadata, JPQLTemplates templates, QueryMetadata queryMetadata) {
        super(queryMetadata, templates);
        this.queryHandler = templates.getQueryHandler();
        this.txDm = dm;
        this.metadata = metadata;
    }

    @Override
    public long fetchCount() {
        try {
            return internalFetchCount();
        } finally {
            reset();
        }
    }

    protected long internalFetchCount() {
        return new CountQueryFlow().applyFlow().one();
    }

    @SuppressWarnings("unchecked")
    protected <CT> Class<CT> getQueryResultType() {
        return (Class<CT>) getMetadata().getProjection().getType();
    }

    @Override
    public CloseableIterator<T> iterate() {
        throw new UnsupportedOperationException("CUBA.platform doesn't support Cursors");
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> fetch() {

        try {
            return internalFetch();
        } finally {
            reset();
        }
    }

    private List<T> internalFetch() {
        Class<?> queryResultType = getQueryResultType();

        if (Entity.class.isAssignableFrom(queryResultType)) {

            LoadContext<Entity> loadContext = new EntityQueryFlow<>()
                    .modifiers(getMetadata().getModifiers()).applyFlow();

            return (List<T>) txDm.loadList(loadContext);

        } else {

            return new OneColumnQueryFlow<T>().modifiers(getMetadata().getModifiers()).applyFlow().list();
        }
    }

    @Override
    public QueryResults<T> fetchResults() {
        try {
            long total = internalFetchCount();

            if (total > 0) {
                List<T> list = internalFetch();

                return new QueryResults<>(list,
                        getMetadata().getModifiers(), total);

            } else {
                return QueryResults.emptyResults();
            }
        } finally {
            reset();
        }

    }

    protected void logQuery(String queryString, Map<Object, String> parameters) {
        if (logger.isDebugEnabled()) {
            String normalizedQuery = queryString.replace('\n', ' ');
            MDC.put(MDC_QUERY, normalizedQuery);
            MDC.put(MDC_PARAMETERS, String.valueOf(parameters));
            logger.debug(normalizedQuery);
        }
    }

    protected void cleanupMDC() {
        MDC.remove(MDC_QUERY);
        MDC.remove(MDC_PARAMETERS);
    }

    @Override
    protected void reset() {
        cleanupMDC();
    }

    @Nullable
    @SuppressWarnings("unchecked")
    @Override
    public T fetchOne() {
        Class<?> queryResultType = getQueryResultType();

        try {
            if (Entity.class.isAssignableFrom(queryResultType)) {

                LoadContext<Entity> loadContext = new EntityQueryFlow<>()
                        .modifiers(getMetadata().getModifiers()).applyFlow();

                return (T) txDm.load(loadContext);

            } else {

                return new OneColumnQueryFlow<T>().applyFlow()
                        .optional().orElse(null);
            }

        } catch (javax.persistence.NoResultException e) {
            logger.trace(e.getMessage(),e);
            return null;
        } catch (javax.persistence.NonUniqueResultException e) {
            throw new NonUniqueResultException();
        } finally {
            reset();
        }
    }

    @SuppressWarnings("unchecked")
    public Q setHint(String name, Object value) {
        hints.put(name, value);
        return (Q) this;
    }

    @Override
    protected CubaJpqlSerializer createSerializer() {
        return new CubaJpqlSerializer(getTemplates(), txDm, metadata);
    }

    protected void clone(Q query) {
        hints.putAll(query.hints);
    }

    /**
     * Clone the state of this query to a new instance with the given EntityManager
     *
     * @param transactionalDataManager entity manager
     * @return cloned query
     */
    public abstract Q clone(TransactionalDataManager transactionalDataManager);

    /**
     * Clone the state of this query to a new instance with the given EntityManager
     * and the specified templates
     *
     * @param transactionalDataManager entity manager
     * @param templates templates
     * @return cloned query
     */
    public abstract Q clone(TransactionalDataManager transactionalDataManager, JPQLTemplates templates);

    /**
     * Clone the state of this query to a new instance
     *
     * @return cloned query
     */
    @Override
    public Q clone() {
        return clone(txDm, getTemplates());
    }

    public interface CommonQueryFlow<Q, F extends CommonQueryFlow<Q, F>> {

        Q createQuery();

        Q applyParameters(Q query);

        Q applyModifiers(Q query);

        Q applyHints(Q query);

        default Q applyFlow() {
            Q q = createQuery();
            Q qWithParams = applyParameters(q);
            Q qWithParamsAndModifiers = applyModifiers(qWithParams);
            return applyHints(qWithParamsAndModifiers);
        }
    }

    public abstract class AbstractFluentValueLoaderQueryFlow<FT, F extends AbstractFluentValueLoaderQueryFlow<FT, F>>
            implements CommonQueryFlow<FluentValueLoader<FT>, F> {

        protected QueryModifiers queryModifiers = QueryModifiers.EMPTY;
        protected CubaJpqlSerializer serializer;

        public F modifiers(QueryModifiers modifiers) {
            this.queryModifiers = modifiers;

            return (F) this;
        }

        @Override
        public FluentValueLoader<FT> createQuery() {
            serializer = serialize(false);
            String queryString = serializer.toString();

            return txDm.loadValue(queryString, getQueryResultType());
        }

        @Override
        public FluentValueLoader<FT> applyParameters(FluentValueLoader<FT> query) {

            Map<Object, String> constants = serializer.getConstantToLabel();
            Map<ParamExpression<?>, Object> params = getMetadata().getParams();

            for (Map.Entry<Object, String> entry : constants.entrySet()) {

                String key = entry.getValue();

                Object val = resolveValByParams(entry.getKey(), params);

                query.parameter(CubaJpqlSerializer.QUERY_PARAM_HOLDER + key, val);
            }

            return query;
        }

        @Override
        public FluentValueLoader<FT> applyModifiers(FluentValueLoader<FT> query) {
            if (queryModifiers != null && queryModifiers.isRestricting()) {
                Integer limit = queryModifiers.getLimitAsInteger();
                Integer offset = queryModifiers.getOffsetAsInteger();
                if (limit != null) {
                    query.maxResults(limit);
                }
                if (offset != null) {
                    query.firstResult(offset);
                }
            }

            return query;
        }

        @Override
        public FluentValueLoader<FT> applyHints(FluentValueLoader<FT> query) {
            return query;
        }
    }

    public class CountQueryFlow extends AbstractFluentValueLoaderQueryFlow<Long, CountQueryFlow> {

        @Override
        public FluentValueLoader<Long> createQuery() {
            serializer = serialize(true);
            String queryString = serializer.toString();

            return txDm.loadValue(queryString, Long.class);
        }


        @Override
        public FluentValueLoader<Long> applyModifiers(FluentValueLoader<Long> query) {
            return query;
        }

        @Override
        public FluentValueLoader<Long> applyHints(FluentValueLoader<Long> query) {
            return query;
        }
    }

    public class OneColumnQueryFlow<FT> extends AbstractFluentValueLoaderQueryFlow<FT, OneColumnQueryFlow<FT>> {}

    public abstract class AbstractLoadContextQueryFlow<E extends Entity, F extends AbstractLoadContextQueryFlow<E, F>>
            implements CommonQueryFlow<LoadContext<E>, F> {

        protected QueryModifiers queryModifiers = QueryModifiers.EMPTY;
        protected CubaJpqlSerializer serializer;

        public F modifiers(QueryModifiers modifiers) {
            this.queryModifiers = modifiers;

            return (F) this;
        }

        @Override
        public LoadContext<E> createQuery() {
            serializer = serialize(false);
            String queryString = serializer.toString();

            LoadContext<E> loadContext = LoadContext.create(getQueryResultType());
            loadContext.setQueryString(queryString);

            return loadContext;
        }

        @Override
        public LoadContext<E> applyParameters(LoadContext<E> loadContext) {

            Map<Object, String> constants = serializer.getConstantToLabel();
            Map<ParamExpression<?>, Object> params = getMetadata().getParams();

            LoadContext.Query query = loadContext.getQuery();

            for (Map.Entry<Object, String> entry : constants.entrySet()) {

                String key = entry.getValue();

                Object val = resolveValByParams(entry.getKey(), params);

                query.setParameter(CubaJpqlSerializer.QUERY_PARAM_HOLDER + key, val);
            }

            return loadContext;
        }

        @Override
        public LoadContext<E> applyModifiers(LoadContext<E> loadContext) {

            LoadContext.Query query = loadContext.getQuery();
            if (queryModifiers != null && queryModifiers.isRestricting()) {
                Integer limit = queryModifiers.getLimitAsInteger();
                Integer offset = queryModifiers.getOffsetAsInteger();
                if (limit != null) {
                    query.setMaxResults(limit);
                }
                if (offset != null) {
                    query.setFirstResult(offset);
                }
            }

            return loadContext;
        }

        @Override
        public LoadContext<E> applyHints(LoadContext<E> loadContext) {

            for (Map.Entry<String, Object> entry : hints.entries()) {
                loadContext.setHint(entry.getKey(), entry.getValue());
            }

            return loadContext;
        }
    }


    public class EntityQueryFlow<E extends Entity>
            extends AbstractLoadContextQueryFlow<E, EntityQueryFlow<E>> {

    }


    private Object resolveValByParams(Object val, Map<ParamExpression<?>, Object> params) {

        Object resolved = val;
        if (val instanceof Param) {
            resolved = params.get(val);
            if (resolved == null) {
                throw new ParamNotSetException((Param<?>) val);
            }
        }

        return resolved;
    }
}
