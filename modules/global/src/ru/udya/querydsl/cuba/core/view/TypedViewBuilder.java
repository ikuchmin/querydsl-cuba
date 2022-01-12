package ru.udya.querydsl.cuba.core.view;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.FetchMode;
import com.haulmont.cuba.core.global.ViewBuilder;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.CollectionPathBase;
import com.querydsl.core.types.dsl.SimpleExpression;

import java.util.Collection;
import java.util.function.BiConsumer;

public class TypedViewBuilder<Q extends Path<? extends Entity<?>>> extends AbstractTypedViewBuilder<Q, TypedViewBuilder<Q>> {

    public TypedViewBuilder() {
    }

    public TypedViewBuilder(Q viewType, ViewBuilder viewBuilder) {
        super(viewType, viewBuilder);
    }

    public <SP extends Path<? extends Entity<?>>>
    TypedViewBuilder<Q> property(SP property, BiConsumer<SP, TypedViewBuilder<SP>> builder) {

        viewBuilder().add(resolvePropertyName(property),
                vb -> builder.accept(property, new TypedViewBuilder<>(property, vb)));

        return this;
    }

    /**
     * CUBA.platform doesn't support such API. It will be enabled in the future
     */
    @Deprecated
    public <SP extends Path<? extends Entity<?>>>
    TypedViewBuilder<Q> property(SP property, BiConsumer<SP, TypedViewBuilder<SP>> builder,
                                 FetchMode fetchMode) {

        throw new UnsupportedOperationException("CUBA.platform doesn't support such API");
    }


    public <E extends Entity<?>, SP extends SimpleExpression<E> & Path<E>>
    TypedViewBuilder<Q> property(CollectionPathBase<? extends Collection<E>, E, SP> property,
               BiConsumer<SP, TypedViewBuilder<SP>> builder) {

        viewBuilder().add(resolvePropertyName(property),
                vb -> builder.accept(property.any(), new TypedViewBuilder<>(property.any(), vb)));

        return this;
    }

    /**
     * CUBA.platform doesn't support such API. It will be enabled in the future
     */
    @Deprecated
    public <E extends Entity<?>, SP extends SimpleExpression<E> & Path<E>>
    TypedViewBuilder<Q> property(CollectionPathBase<? extends Collection<E>, E, SP> property,
               BiConsumer<SP, TypedViewBuilder<SP>> builder, FetchMode fetchMode) {

        throw new UnsupportedOperationException("CUBA.platform doesn't support such API");
    }
}
