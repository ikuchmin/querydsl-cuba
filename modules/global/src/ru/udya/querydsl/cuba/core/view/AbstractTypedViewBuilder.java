package ru.udya.querydsl.cuba.core.view;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewBuilder;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.CollectionPathBase;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.SimpleExpression;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

// todo: add backward compatibility methods (works with basic strings)
@SuppressWarnings("unchecked")
public class AbstractTypedViewBuilder<Q extends EntityPath<? extends Entity<?>>, V extends AbstractTypedViewBuilder<Q, V>> {

    private Q viewType;

    protected ViewBuilder viewBuilder;

    public AbstractTypedViewBuilder() {
    }

    public AbstractTypedViewBuilder(Q viewType, ViewBuilder viewBuilder) {
        this.viewType = viewType;
        this.viewBuilder = viewBuilder;
    }

    public V view(Q viewType) {
        this.viewBuilder = viewBuilder(viewType);
        this.viewType = viewType;

        return (V) this;
    }

    public V view(Q viewType, Path<?>... properties) {
        List<String> propertyNames = Arrays.stream(properties)
                .map(this::resolvePropertyName).collect(toList());

        ViewBuilder viewBuilder = viewBuilder(viewType);

        propertyNames.forEach(viewBuilder::add);

        this.viewBuilder = viewBuilder;
        this.viewType = viewType;

        return (V) this;
    }

    public V view(Q viewType, Function<Q, List<Path<?>>> properties) {
        List<Path<?>> evaluatedProperties = properties.apply(viewType);
        List<String> propertyNames = evaluatedProperties.stream()
                .map(this::resolvePropertyName).collect(toList());

        ViewBuilder viewBuilder = viewBuilder(viewType);

        propertyNames.forEach(viewBuilder::add);

        this.viewType = viewType;
        this.viewBuilder = viewBuilder;

        return (V) this;
    }

    public V properties(Path<?>... properties) {
        List<String> propertyNames = Arrays.stream(properties)
                .map(this::resolvePropertyName).collect(toList());

        propertyNames.forEach(viewBuilder()::add);

        return (V) this;
    }

    public V properties(Function<Q, List<Path<?>>> properties) {
        List<Path<?>> evaluatedProperties = properties.apply(viewType);
        List<String> propertyNames = evaluatedProperties.stream()
                .map(this::resolvePropertyName).collect(toList());

        propertyNames.forEach(viewBuilder()::add);

        return (V) this;
    }

    public V property(Path<?> property) {
        viewBuilder.add(resolvePropertyName(property));

        return (V) this;
    }

    // --- entity methods

    public <SP extends EntityPathBase<? extends Entity<?>>> V property(
            SP property, Function<SP, List<Path<?>>> properties) {

        List<Path<?>> evaluatedProperties = properties.apply(property);
        List<String> propertyNames = evaluatedProperties.stream()
                .map(this::resolvePropertyName).collect(toList());

        viewBuilder().add(property.getMetadata().getName(),
                vb -> propertyNames.forEach(vb::add));

        return (V) this;
    }

    public <SP extends EntityPathBase<? extends Entity<?>>> V property(
            SP property, BiConsumer<SP, TypedViewBuilder<SP>> builder) {

        viewBuilder().add(resolvePropertyName(property),
                vb -> builder.accept(property, new TypedViewBuilder<>(property, vb)));

        return (V) this;
    }

    public <E, SP extends SimpleExpression<E>> V property(CollectionPathBase<? extends Collection<E>, E, SP> property,
                                                          Function<SP, List<Path<?>>> properties) {


        List<Path<?>> evaluatedProperties = properties.apply(property.any());
        List<String> propertyNames = evaluatedProperties.stream()
                .map(this::resolvePropertyName).collect(toList());

        viewBuilder().add(property.getMetadata().getName(),
                vb -> propertyNames.forEach(vb::add));

        return (V) this;
    }

    public <E extends Entity<?>, SP extends SimpleExpression<E> & EntityPath<E>>
    V property(CollectionPathBase<? extends Collection<E>, E, SP> property,
               BiConsumer<SP, TypedViewBuilder<SP>> builder) {

        viewBuilder().add(resolvePropertyName(property),
                vb -> builder.accept(property.any(), new TypedViewBuilder<>(property.any(), vb)));

        return (V) this;
    }

    public View build() {
        return this.viewBuilder.build();
    }

    protected ViewBuilder viewBuilder(Q entityPath) {
        if (viewBuilder == null) {
            return ViewBuilder.of(entityPath.getType());
        }

        return viewBuilder;
    }

    protected ViewBuilder viewBuilder() {
        if (viewBuilder == null) {
            throw new IllegalArgumentException("Builder isn't initialized");
        }

        return this.viewBuilder;
    }

    protected String resolvePropertyName(Path<?> property) {
        return property.getMetadata().getName();
    }
}
