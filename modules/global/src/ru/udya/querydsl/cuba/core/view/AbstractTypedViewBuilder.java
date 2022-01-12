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

    /**
     * Initializes Builder with base type
     *
     * @param viewType base type
     * @return builder
     */
    public V view(Q viewType) {
        this.viewBuilder = viewBuilder(viewType);
        this.viewType = viewType;

        return (V) this;
    }

    /**
     * Initializes Builder with base type and basic properties
     *
     * @param viewType base type
     * @param properties basic properties
     * @return builder
     */
    public V view(Q viewType, Path<?>... properties) {
        List<Path<?>> propertiesList =
                Arrays.stream(properties).collect(toList());

        return this.view(viewType, propertiesList);
    }

    /**
     * Initializes Builder with base type and basic properties
     *
     * @param viewType base type
     * @param properties basic properties
     * @return builder
     */
    public V view(Q viewType, List<Path<?>> properties) {
        List<String> propertyNames = properties.stream()
                .map(this::resolvePropertyName).collect(toList());

        ViewBuilder localViewBuilder = viewBuilder(viewType);

        propertyNames.forEach(localViewBuilder::add);

        this.viewBuilder = localViewBuilder;
        this.viewType = viewType;

        return (V) this;
    }

    /**
     * Initializes Builder with base type and basic properties
     *
     * @param viewType base type
     * @param properties basic properties
     * @return builder
     */
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

    /**
     * Applies predefined views
     *
     * @param views predefined views
     * @return builder
     */
    public V extendByViews(View... views) {

        ViewBuilder localViewBuilder = viewBuilder();
        Arrays.stream(views).forEach(localViewBuilder::addView);

        return (V) this;
    }

    /**
     * Applies predefined views
     *
     * @param views predefined views
     * @return builder
     */
    public V extendByViews(String... views) {

        ViewBuilder localViewBuilder = viewBuilder();
        Arrays.stream(views).forEach(localViewBuilder::addView);

        return (V) this;
    }

    /**
     * Adds system properties
     *
     * @return builder
     */
    public V extendBySystem() {

        viewBuilder().addSystem();

        return (V) this;
    }

    /**
     * Adds properties
     *
     * @param properties basic properties
     * @return builder
     */
    public V properties(Path<?>... properties) {
        List<String> propertyNames = Arrays.stream(properties)
                .map(this::resolvePropertyName).collect(toList());

        propertyNames.forEach(viewBuilder()::add);

        return (V) this;
    }

    /**
     * Adds properties
     *
     * @param properties basic properties
     * @return builder
     */
    public V properties(Function<Q, List<Path<?>>> properties) {
        List<Path<?>> evaluatedProperties = properties.apply(viewType);
        List<String> propertyNames = evaluatedProperties.stream()
                .map(this::resolvePropertyName).collect(toList());

        propertyNames.forEach(viewBuilder()::add);

        return (V) this;
    }

    /**
     * Add property
     *
     * @param property property
     * @return builder
     */
    public V property(Path<?> property) {
        viewBuilder().add(resolvePropertyName(property));

        return (V) this;
    }

    // --- generic methods for paths like entity, bean and other variants

    /**
     * Adds property with view
     *
     * @param property property
     * @param view view
     * @param <SP> type of property
     * @return builder
     */
    public <SP extends Path<? extends Entity<?>>> V property(SP property, View view) {

        viewBuilder().add(resolvePropertyName(property), vb -> vb.addView(view));

        return (V) this;

    }

    /**
     * Adds property with view and fetch mode
     *
     * CUBA.platform doesn't support such API. It will be enabled in the future
     */
    @Deprecated
    public <SP extends Path<? extends Entity<?>>>
    V property(SP property, View view, FetchMode fetchMode) {

        throw new UnsupportedOperationException("CUBA.platform doesn't support such API");
    }

    /**
     * Adds property with view
     *
     * @param property property
     * @param view view
     * @param <SP> type of property
     * @return builder
     */
    public <SP extends Path<? extends Entity<?>>> V property(SP property, String view) {

        viewBuilder().add(resolvePropertyName(property), view);

        return (V) this;
    }

    /**
     * Adds property with view and fetch mode
     *
     * @param property property
     * @param view view
     * @param fetchMode fetch mode
     * @param <SP> type of property
     * @return builder
     */
    public <SP extends Path<? extends Entity<?>>>
    V property(SP property, String view, FetchMode fetchMode) {

        viewBuilder().add(resolvePropertyName(property), view, fetchMode);

        return (V) this;
    }

    /**
     * Adds property with sub-properties
     *
     * @param property property
     * @param properties sub-properties
     * @param <SP> type of property
     * @return builder
     */
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
     * Adds property with sub-properties and fetch mode
     *
     * CUBA.platform doesn't support such API. It will be enabled in the future
     */
    @Deprecated
    public <SP extends Path<? extends Entity<?>>>
    V property(SP property, Function<SP, List<Path<?>>> properties,
               FetchMode fetchMode) {

        throw new UnsupportedOperationException("CUBA.platform doesn't support such API");
    }


    // --- collection methods

    /**
     * Adds collection property with view
     *
     * @param property property
     * @param view view
     * @param <E> entity type
     * @param <SP> querydsl type of property
     * @return builder
     */
    public <E, SP extends SimpleExpression<E>>
    V property(CollectionPathBase<? extends Collection<E>, E, SP> property,
               View view) {

        viewBuilder().add(resolvePropertyName(property), vb -> vb.addView(view));

        return (V) this;
    }

    /**
     * Adds collection property with view
     *
     * CUBA.platform doesn't support such API. It will be enabled in the future
     */
    @Deprecated
    public <E, SP extends SimpleExpression<E>>
    V property(CollectionPathBase<? extends Collection<E>, E, SP> property,
               View view, FetchMode fetchMode) {

        throw new UnsupportedOperationException("CUBA.platform doesn't support such API");
    }

    /**
     * Adds collection property with view
     *
     * @param property property
     * @param view view
     * @param <E> entity type
     * @param <SP> querydsl type of property
     * @return builder
     */
    public <E, SP extends SimpleExpression<E>>
    V property(CollectionPathBase<? extends Collection<E>, E, SP> property,
               String view) {

        viewBuilder().add(resolvePropertyName(property), view);

        return (V) this;
    }

    /**
     * Adds collection property with view and fetch mode
     *
     * @param property property
     * @param view view
     * @param fetchMode fetch mode
     * @param <E> entity type
     * @param <SP> querydsl type of property
     * @return builder
     */
    public <E, SP extends SimpleExpression<E>>
    V property(CollectionPathBase<? extends Collection<E>, E, SP> property,
               String view, FetchMode fetchMode) {

        viewBuilder().add(resolvePropertyName(property), view, fetchMode);

        return (V) this;
    }


    /**
     * Adds collection property with sub-properties
     *
     * @param property property
     * @param properties sub-properties
     * @param <E> entity type
     * @param <SP> querydsl type of property
     * @return builder
     */
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
     * Adds collection property with sub-properties and fetch mode
     *
     * CUBA.platform doesn't support such API. It will be enabled in the future
     */
    @Deprecated
    public <E, SP extends SimpleExpression<E>>
    V property(CollectionPathBase<? extends Collection<E>, E, SP> property,
               Function<SP, List<Path<?>>> properties,
               FetchMode fetchMode) {

        throw new UnsupportedOperationException("CUBA.platform doesn't support such API");
    }


    /**
     * Allows configuring part of builder with native CUBA.platform API
     *
     * @param builder CUBA.platform view builder
     * @return builder
     */
    public V withViewBuilder(Consumer<ViewBuilder> builder) {
        builder.accept(viewBuilder());
        return (V) this;
    }

    // --- build

    /**
     * Builds CUBA.platform view
     *
     * @return CUBA.platform view
     */
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
