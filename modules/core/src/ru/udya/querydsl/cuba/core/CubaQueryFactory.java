package ru.udya.querydsl.cuba.core;

import com.haulmont.cuba.core.TransactionalDataManager;
import com.querydsl.core.Tuple;
import com.querydsl.core.dml.DeleteClause;
import com.querydsl.core.dml.UpdateClause;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.JPQLTemplates;

import javax.annotation.Nullable;
import javax.inject.Provider;

public class CubaQueryFactory implements JPQLQueryFactory {
    @Nullable
    private final JPQLTemplates templates;

    private final Provider<TransactionalDataManager> dataManagerProvider;

    public CubaQueryFactory(TransactionalDataManager dataManager) {
        this.dataManagerProvider = () -> dataManager;
        this.templates = null;
    }

    public CubaQueryFactory(JPQLTemplates templates, TransactionalDataManager dataManager) {
        this.dataManagerProvider = () -> dataManager;
        this.templates = templates;
    }

    public CubaQueryFactory(Provider<TransactionalDataManager> dataManagerProvider) {
        this.dataManagerProvider = dataManagerProvider;
        this.templates = null;
    }

    public CubaQueryFactory(JPQLTemplates templates, Provider<TransactionalDataManager> dataManagerProvider) {
        this.dataManagerProvider = dataManagerProvider;
        this.templates = templates;
    }

    @Override
    public DeleteClause<?> delete(EntityPath<?> path) {
        throw new UnsupportedOperationException("CUBA.platform TransactionalDataManager doesn't support delete queries");
    }

    @Override
    public <T> CubaQuery<T> select(Expression<T> expr) {
        return query().select(expr);
    }

    @Override
    public CubaQuery<Tuple> select(Expression<?>... exprs) {
        return query().select(exprs);
    }

    @Override
    public <T> CubaQuery<T> selectDistinct(Expression<T> expr) {
        return select(expr).distinct();
    }

    @Override
    public CubaQuery<Tuple> selectDistinct(Expression<?>... exprs) {
        return select(exprs).distinct();
    }

    @Override
    public CubaQuery<Integer> selectOne() {
        return select(Expressions.ONE);
    }

    @Override
    public CubaQuery<Integer> selectZero() {
        return select(Expressions.ZERO);
    }

    @Override
    public <T> CubaQuery<T> selectFrom(EntityPath<T> from) {
        return select(from).from(from);
    }

    @Override
    public CubaQuery<?> from(EntityPath<?> from) {
        return query().from(from);
    }

    @Override
    public CubaQuery<?> from(EntityPath<?>... from) {
        return query().from(from);
    }

    @Override
    public UpdateClause<?> update(EntityPath<?> path) {
        throw new UnsupportedOperationException("CUBA.platform TransactionalDataManager doesn't support update queries");
    }

    @Override
    public CubaQuery<?> query() {
        if (templates != null) {
            return new CubaQuery<Void>(dataManagerProvider.get(), templates);
        } else {
            return new CubaQuery<Void>(dataManagerProvider.get());
        }
    }
}
