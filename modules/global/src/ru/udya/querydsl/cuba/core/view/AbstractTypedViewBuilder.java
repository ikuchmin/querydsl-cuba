package ru.udya.querydsl.cuba.core.view;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.CollectionPathBase;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.SimpleExpression;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class AbstractTypedViewBuilder<T, Q extends AbstractTypedViewBuilder<T, Q>> {



    public Q view(T arg) {
//        return queryMixin.from(arg);
        return null;
    }

    public Q view(T arg, Expression<?>... fields) {
//        return queryMixin.from(arg);
        return null;
    }

    public Q view(T arg, Function<T, List<Expression<?>>> fields) {
//        return queryMixin.from(arg);
        return null;
    }

    public Q properties(Expression<?>... fields) {
//        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }

    public Q properties(Function<T, List<Expression<?>>> fields) {
//        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }

    public Q property(Expression<?> field) {
//        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }

    // --- entity methods

    public <P extends EntityPathBase<?>> Q property(P entityField, Function<P, List<Expression<?>>> fields) {
        new EntityPathBase<>(String.class, "");
        //        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }

    public <P extends EntityPathBase<?>> Q property(P entityField, BiConsumer<P, TypedViewBuilder<P>> builder) {

//        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }

    public <E, CQ extends SimpleExpression<E>> Q property(CollectionPathBase<? extends Collection<E>, E, CQ> collectionField,
                                                          Function<CQ, List<Expression<?>>> fields) {

        //        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }

    public <E, CQ extends SimpleExpression<E>> Q property(CollectionPathBase<? extends Collection<E>, E, CQ> collectionField,
                                                          BiConsumer<CQ, TypedViewBuilder<CQ>> builder) {
        //        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }
}
