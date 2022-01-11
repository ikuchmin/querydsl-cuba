package ru.udya.querydsl.cuba.core.view;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.ViewBuilder;
import com.querydsl.core.types.EntityPath;

public class TypedViewBuilder<Q extends EntityPath<? extends Entity<?>>> extends AbstractTypedViewBuilder<Q, TypedViewBuilder<Q>> {
    public TypedViewBuilder() {
    }

    public TypedViewBuilder(Q viewType, ViewBuilder viewBuilder) {
        super(viewType, viewBuilder);
    }


    //    public Q view(EntityPath<?> arg) {
////        return queryMixin.from(arg);
//        return null;
//    }
//
//
//    public Q properties(Expression<?>... expr) {
////        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
////        return newType;
//        return null;
//    }
//
//    public Q properties(Function<T, List<Expression<?>>> expr) {
////        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
////        return newType;
//        return null;
//    }
//
//    public <P> Q entityField(Expression<P> expr, Consumer<TypedViewBuilder<P, ?>> subViewBuilder) {
////        TypedViewBuilder<U> newType = (TypedViewBuilder<U>) this;
////        return newType;
//        return null;
//    }
//    public <P> Q properties(EntityPath<?>... target) {
//        return queryMixin.innerJoin(target);
//    }

}
