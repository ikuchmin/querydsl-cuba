package ru.udya.querydsl.cuba.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.udya.querydsl.cuba.core.view.TypedViewFactory;

@SuppressWarnings("SpringFacetCodeInspection")
@Configuration
public class TypedViewFactoryConfig {

    public static final String TYPED_VIEW_FACTORY_BEAN_NAME = "querydsl_TypedViewFactory";


    @Bean(TYPED_VIEW_FACTORY_BEAN_NAME)
    public TypedViewFactory typedViewFactory() {
        return new TypedViewFactory();
    }
}