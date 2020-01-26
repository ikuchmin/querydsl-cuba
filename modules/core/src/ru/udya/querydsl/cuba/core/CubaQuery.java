package ru.udya.querydsl.cuba.core;

import com.haulmont.cuba.core.TransactionalDataManager;
import com.haulmont.cuba.core.global.Metadata;
import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.JPQLTemplates;

public class CubaQuery<T> extends AbstractCubaQuery<T, CubaQuery<T>> {

    private static final long serialVersionUID = - 9180183730489110259L;

    /**
     * Creates a new detached query
     * The query can be attached via the clone method
     */
    public CubaQuery() {
        super(null, null, JPQLTemplates.DEFAULT, new DefaultQueryMetadata());
    }

    /**
     * Creates a new EntityManager bound query
     *
     * @param dm entity manager
     */
    public CubaQuery(TransactionalDataManager dm, Metadata metadata) {
        super(dm, metadata, CubaTemplates.DEFAULT, new DefaultQueryMetadata());
    }

    /**
     * Creates a new EntityManager bound query
     *
     * @param dm entity manager
     * @param queryMetadata query queryMetadata
     */
    public CubaQuery(TransactionalDataManager dm, Metadata metadata, QueryMetadata queryMetadata) {
        super(dm, metadata, CubaTemplates.DEFAULT, queryMetadata);
    }

    /**
     * Creates a new query
     *
     * @param dm entity manager
     * @param templates templates
     */
    public CubaQuery(TransactionalDataManager dm, Metadata metadata, JPQLTemplates templates) {
        super(dm, metadata, templates, new DefaultQueryMetadata());
    }

    /**
     * Creates a new query
     *
     * @param dm entity manager
     * @param templates templates
     * @param queryMetadata query queryMetadata
     */
    public CubaQuery(TransactionalDataManager dm, Metadata metadata, JPQLTemplates templates, QueryMetadata queryMetadata) {
        super(dm, metadata, templates, queryMetadata);
    }

    @Override
    public CubaQuery<T> clone(TransactionalDataManager dataManager, JPQLTemplates templates) {
        CubaQuery<T> q = new CubaQuery<T>(dataManager, metadata, templates, getMetadata().clone());
        q.clone(this);
        return q;
    }

    @Override
    public CubaQuery<T> clone(TransactionalDataManager dataManager) {
        return clone(dataManager, CubaTemplates.DEFAULT);
    }

    @Override
    public <U> CubaQuery<U> select(Expression<U> expr) {
        queryMixin.setProjection(expr);
        @SuppressWarnings("unchecked") // This is the new type
                CubaQuery<U> newType = (CubaQuery<U>) this;
        return newType;
    }

    @Override
    public CubaQuery<Tuple> select(Expression<?>... exprs) {
        queryMixin.setProjection(exprs);
        @SuppressWarnings("unchecked") // This is the new type
                CubaQuery<Tuple> newType = (CubaQuery<Tuple>) this;
        return newType;
    }

}
