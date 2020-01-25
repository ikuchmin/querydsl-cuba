package ru.udya.querydsl.cuba.core;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.haulmont.cuba.core.TransactionalDataManager;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.LoadContext.Query;
import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.QueryResults;
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

    protected final QueryHandler queryHandler;

    public AbstractCubaQuery(TransactionalDataManager dm) {
        this(dm, EclipseLinkTemplates.DEFAULT, new DefaultQueryMetadata());
    }

    public AbstractCubaQuery(TransactionalDataManager dm, JPQLTemplates templates, QueryMetadata metadata) {
        super(metadata, templates);
        this.queryHandler = templates.getQueryHandler();
        this.txDm = dm;
    }

    @Override
    public long fetchCount() {
        throw new UnsupportedOperationException("Cuba TransactionDataManager doesn't support fetch count");
    }

    /**
     * Expose the original JPA query for the given projection
     *
     * @return query
     */
    public LoadContext<?> createLoadContext() {
        return createLoadContext(getMetadata().getModifiers(), false);
    }

    private LoadContext<?> createLoadContext(@Nullable QueryModifiers modifiers, boolean forCount) {
        CubaJpqlSerializer serializer = serialize(forCount);
        String queryString = serializer.toString();
        logQuery(queryString, serializer.getConstantToLabel());

        Query query = LoadContext.createQuery(queryString);

        CubaUtil.setConstants(query, serializer.getConstantToLabel(), getMetadata().getParams()); // !!!

        if (modifiers != null && modifiers.isRestricting()) {
            Integer limit = modifiers.getLimitAsInteger();
            Integer offset = modifiers.getOffsetAsInteger();
            if (limit != null) {
                query.setMaxResults(limit);
            }
            if (offset != null) {
                query.setFirstResult(offset);
            }
        }

        Class<? extends Entity> entityClass =
                (Class<? extends Entity>) getMetadata().getProjection().getType();

        LoadContext<? extends Entity> loadContext =
                LoadContext.create(entityClass)
                        .setQuery(query);

        for (Map.Entry<String, Object> entry : hints.entries()) {
            loadContext.setHint(entry.getKey(), entry.getValue());
        }

        return loadContext;
    }

    /**
     * Transforms results using FactoryExpression if ResultTransformer can't be used
     *
     * @param loadContext loadContext
     * @return results
     */
    private List<?> getResultList(LoadContext<?> loadContext) {
        return txDm.loadList(loadContext);
    }

    /**
     * Transforms results using FactoryExpression if ResultTransformer can't be used
     *
     * @param loadContext loadContext
     * @return single result
     */
    @Nullable
    private Object getSingleResult(LoadContext<?> loadContext) {
        return txDm.load(loadContext);
    }

    @Override
    public CloseableIterator<T> iterate() {

        throw new UnsupportedOperationException("Implement");
//        try {
//            Query query = createLoadContext();
//            return queryHandler.iterate(query, projection);
//        } finally {
//            reset();
//        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> fetch() {
        try {
            LoadContext loadContext = createLoadContext();
            return (List<T>) getResultList(loadContext);
        } finally {
            reset();
        }
    }

    @Override
    public QueryResults<T> fetchResults() {
        try {
            LoadContext<?> countQuery = createLoadContext(null, true);
            long total = 10; //(Long) txDm.lo(countQuery, Long.class);

            if (total > 0) {
                QueryModifiers modifiers = getMetadata().getModifiers();
                LoadContext query = createLoadContext(modifiers, false);
                @SuppressWarnings("unchecked")
                List<T> list = (List<T>) getResultList(query);
                return new QueryResults<T>(list, modifiers, total);
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
        try {
            LoadContext<?> loadContext =
                    createLoadContext(getMetadata().getModifiers(), false);

            return (T) getSingleResult(loadContext);
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
        return new CubaJpqlSerializer(getTemplates(), txDm);
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
}
