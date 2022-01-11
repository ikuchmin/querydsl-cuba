package ru.udya.querydsl.cuba.core.view;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.CollectionPathBase;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.core.types.dsl.SetPath;
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

    public Q view(T arg, Expression<?>... expr) {
//        return queryMixin.from(arg);
        return null;
    }

    public Q view(T arg, Function<T, List<Expression<?>>> expr) {
//        return queryMixin.from(arg);
        return null;
    }

    public Q fields(Expression<?>... expr) {
//        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }

    public Q fields(Function<T, List<Expression<?>>> expr) {
//        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }

    // --- entity methods

    public <P> Q entityField(Expression<P> complexExpr, Expression<?>... expr) {
//        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }

    public <P> Q entityField(Function<T, Expression<P>> complexExpr, Expression<?>... expr) {
//        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }


    public <P> Q entityField(P complexExpr, Function<P, List<Expression<?>>> expr) {
//        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }
    public <P> Q entityField(Function<T, P> complexExpr, Function<P, List<Expression<?>>> expr) {
//        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }

    public <E, P extends Expression<E>> Q entityField(P complexExpr, BiConsumer<P, TypedViewBuilder<E>> complexViewBuilder) {
//        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }

    public <E, P extends Expression<E>> Q entityField(Function<T, P> complexExpr, BiConsumer<P, TypedViewBuilder<E>> complexViewBuilder) {
//        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }

    // --- common collection methods

    public <P> Q collectionField(Expression<P> complexExpr, Expression<?>... expr) {
//        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }

    public <P> Q collectionField(Function<T, Expression<P>> complexExpr, Expression<?>... expr) {
//        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }


    public <E, C extends Collection<E>, CQ extends SimpleExpression<E>, P extends CollectionPathBase<C, E, CQ>>
    Q collectionField(P complexExpr, Function<CQ, List<Expression<?>>> expr) {
//        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }
    public <E, C extends Collection<E>, CQ extends SimpleExpression<E>, P extends CollectionPathBase<C, E, CQ>> Q  collectionField(Function<T, P> complexExpr, Function<CQ, List<Expression<?>>> expr) {
//        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }
    //
    public <E, C extends Collection<E>, CQ extends SimpleExpression<E>, P extends CollectionPathBase<C, E, CQ>> Q collectionField(P complexExpr, BiConsumer<CQ, TypedViewBuilder<CQ>> complexViewBuilder) {
//        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }
    //
    public <E, C extends Collection<E>, CQ extends SimpleExpression<E>, P extends CollectionPathBase<C, E, CQ>> Q collectionField(Function<T, P> complexExpr, BiConsumer<CQ, TypedViewBuilder<CQ>> complexViewBuilder) {
//        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }

//    public <E, C extends Collection<E>, CQ extends SimpleExpression<E>, P extends CollectionPathBase<C, E, CQ>> Q collectionField(P complexExpr, BiConsumer<CQ, TypedViewBuilder<CQ>> complexViewBuilder) {
////        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
////        return newType;
//        return null;
//    }


    //  --- list methods

    public <P> Q listField(Expression<P> complexExpr, Expression<?>... expr) {
//        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }

    public <P> Q listField(Function<T, Expression<P>> complexExpr, Expression<?>... expr) {
//        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }


    public <E, CQ extends SimpleExpression<E>, P extends ListPath<E, CQ>> Q listField(P complexExpr, Function<CQ, List<Expression<?>>> expr) {
//        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }
    public <E, CQ extends SimpleExpression<E>, P extends ListPath<E, CQ>> Q  listField(Function<T, P> complexExpr, Function<CQ, List<Expression<?>>> expr) {
//        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }
//
    public <E, CQ extends SimpleExpression<E>, P extends ListPath<E, CQ>> Q listField(P complexExpr, BiConsumer<CQ, TypedViewBuilder<CQ>> complexViewBuilder) {
//        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }
//
    public <E, CQ extends SimpleExpression<E>, P extends ListPath<E, CQ>> Q listField(Function<T, P> complexExpr, BiConsumer<CQ, TypedViewBuilder<CQ>> complexViewBuilder) {
//        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }

    // --- set methods

    public <P> Q setField(Expression<P> complexExpr, Expression<?>... expr) {
//        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }

    public <P> Q setField(Function<T, Expression<P>> complexExpr, Expression<?>... expr) {
//        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }


    public <E, CQ extends SimpleExpression<E>, P extends SetPath<E, CQ>> Q setField(P complexExpr, Function<CQ, List<Expression<?>>> expr) {
//        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }
    public <E, CQ extends SimpleExpression<E>, P extends SetPath<E, CQ>> Q  setField(Function<T, P> complexExpr, Function<CQ, List<Expression<?>>> expr) {
//        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }
    //
    public <E, CQ extends SimpleExpression<E>, P extends SetPath<E, CQ>> Q setField(P complexExpr, BiConsumer<CQ, TypedViewBuilder<CQ>> complexViewBuilder) {
//        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }
    //
    public <E, CQ extends SimpleExpression<E>, P extends SetPath<E, CQ>> Q setField(Function<T, P> complexExpr, BiConsumer<CQ, TypedViewBuilder<CQ>> complexViewBuilder) {
//        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
//        return newType;
        return null;
    }

}
