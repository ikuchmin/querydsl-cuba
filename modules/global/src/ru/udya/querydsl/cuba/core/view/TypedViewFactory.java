package ru.udya.querydsl.cuba.core.view;

import com.haulmont.cuba.core.entity.Entity;
import com.querydsl.core.types.Path;

import java.util.List;
import java.util.function.Function;

public class TypedViewFactory {

    public <Q extends Path<? extends Entity<?>>>
    TypedViewBuilder<Q> view(Q viewType) {
        return this.<Q>builder().view(viewType);
    }

    public <Q extends Path<? extends Entity<?>>>
    TypedViewBuilder<Q> view(Q viewType, Path<?>... properties) {
        return this.<Q>builder().view(viewType, properties);
    }

    public <Q extends Path<? extends Entity<?>>>
    TypedViewBuilder<Q> view(Q viewType, List<Path<?>> properties) {
        return this.<Q>builder().view(viewType, properties);
    }

    public <Q extends Path<? extends Entity<?>>>
    TypedViewBuilder<Q> view(Q viewType, Function<Q, List<Path<?>>> properties) {
        return this.<Q>builder().view(viewType, properties);
    }

    protected  <Q extends Path<? extends Entity<?>>> TypedViewBuilder<Q> builder() {
        return new TypedViewBuilder<>();
    }
}
