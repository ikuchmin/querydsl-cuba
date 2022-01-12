package ru.udya.querydsl.cuba.core.view;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.FetchMode;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewBuilder;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.CollectionPathBase;
import com.querydsl.core.types.dsl.SimpleExpression;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@SuppressWarnings("unchecked")
public class AbstractTypedViewBuilder<Q extends Path<? extends Entity<?>>, V extends AbstractTypedViewBuilder<Q, V>> {

    protected Q viewType;

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
        List<Path<?>> propertiesList =
                Arrays.stream(properties).collect(toList());

        return this.view(viewType, propertiesList);
    }

    public V view(Q viewType, List<Path<?>> properties) {
        List<String> propertyNames = properties.stream()
                .map(this::resolvePropertyName).collect(toList());

        ViewBuilder localViewBuilder = viewBuilder(viewType);

        propertyNames.forEach(localViewBuilder::add);

        this.viewBuilder = localViewBuilder;
        this.viewType = viewType;

        return (V) this;
    }

    public V view(Q viewType, Function<Q, List<Path<?>>> properties) {
        List<Path<?>> evaluatedProperties = properties.apply(viewType);
        List<String> propertyNames = evaluatedProperties.stream()
                .map(this::resolvePropertyName).collect(toList());

        ViewBuilder localViewBuilder = viewBuilder(viewType);

        propertyNames.forEach(localViewBuilder::add);

        this.viewType = viewType;
        this.viewBuilder = localViewBuilder;

        return (V) this;
    }

    public V extendByViews(View... view) {

        ViewBuilder localViewBuilder = viewBuilder();
        Arrays.stream(view).forEach(localViewBuilder::addView);

        return (V) this;
    }

    public V extendByViews(String... view) {

        ViewBuilder localViewBuilder = viewBuilder();
        Arrays.stream(view).forEach(localViewBuilder::addView);

        return (V) this;
    }

    public V extendBySystem() {

        viewBuilder().addSystem();

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
        viewBuilder().add(resolvePropertyName(property));

        return (V) this;
    }

    // --- generic methods for paths like entity, bean and other variants

    public <SP extends Path<? extends Entity<?>>> V property(SP property, View view) {

        viewBuilder().add(resolvePropertyName(property), vb -> vb.addView(view));

        return (V) this;

    }

    /**
     * CUBA.platform doesn't support such API. It will be enabled in the future
     */
    @Deprecated
    public <SP extends Path<? extends Entity<?>>>
    V property(SP property, View view, FetchMode fetchMode) {

        throw new UnsupportedOperationException("CUBA.platform doesn't support such API");
    }

    public <SP extends Path<? extends Entity<?>>> V property(SP property, String view) {

        viewBuilder().add(resolvePropertyName(property), view);

        return (V) this;
    }


    public <SP extends Path<? extends Entity<?>>>
    V property(SP property, String view, FetchMode fetchMode) {

        viewBuilder().add(resolvePropertyName(property), view, fetchMode);

        return (V) this;
    }

    public <SP extends Path<? extends Entity<?>>>
    V property(SP property, Function<SP, List<Path<?>>> properties) {

        List<Path<?>> evaluatedProperties = properties.apply(property);
        List<String> propertyNames = evaluatedProperties.stream()
                .map(this::resolvePropertyName).collect(toList());

        viewBuilder().add(resolvePropertyName(property),
                vb -> propertyNames.forEach(vb::add));

        return (V) this;
    }

    /**
     * CUBA.platform doesn't support such API. It will be enabled in the future
     */
    @Deprecated
    public <SP extends Path<? extends Entity<?>>>
    V property(SP property, Function<SP, List<Path<?>>> properties,
               FetchMode fetchMode) {

        throw new UnsupportedOperationException("CUBA.platform doesn't support such API");
    }


    // --- collection methods

    public <E, SP extends SimpleExpression<E>>
    V property(CollectionPathBase<? extends Collection<E>, E, SP> property,
               View view) {

        viewBuilder().add(resolvePropertyName(property), vb -> vb.addView(view));

        return (V) this;
    }

    /**
     * CUBA.platform doesn't support such API. It will be enabled in the future
     */
    @Deprecated
    public <E, SP extends SimpleExpression<E>>
    V property(CollectionPathBase<? extends Collection<E>, E, SP> property,
               View view, FetchMode fetchMode) {

        throw new UnsupportedOperationException("CUBA.platform doesn't support such API");
    }

    public <E, SP extends SimpleExpression<E>>
    V property(CollectionPathBase<? extends Collection<E>, E, SP> property,
               String view) {

        viewBuilder().add(resolvePropertyName(property), view);

        return (V) this;
    }

    public <E, SP extends SimpleExpression<E>>
    V property(CollectionPathBase<? extends Collection<E>, E, SP> property,
               String view, FetchMode fetchMode) {

        viewBuilder().add(resolvePropertyName(property), view, fetchMode);

        return (V) this;
    }


    public <E, SP extends SimpleExpression<E>>
    V property(CollectionPathBase<? extends Collection<E>, E, SP> property,
               Function<SP, List<Path<?>>> properties) {


        List<Path<?>> evaluatedProperties = properties.apply(property.any());
        List<String> propertyNames = evaluatedProperties.stream()
                .map(this::resolvePropertyName).collect(toList());

        viewBuilder().add(resolvePropertyName(property),
                vb -> propertyNames.forEach(vb::add));

        return (V) this;
    }

    /**
     * CUBA.platform doesn't support such API. It will be enabled in the future
     */
    @Deprecated
    public <E, SP extends SimpleExpression<E>>
    V property(CollectionPathBase<? extends Collection<E>, E, SP> property,
               Function<SP, List<Path<?>>> properties,
               FetchMode fetchMode) {

        throw new UnsupportedOperationException("CUBA.platform doesn't support such API");
    }


    public V withViewBuilder(Consumer<ViewBuilder> builder) {
        builder.accept(viewBuilder());
        return (V) this;
    }

    // --- build

    public View build() {
        return viewBuilder().build();
    }

    // --- utils methods

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
