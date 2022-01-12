package ru.udya.querydsl.cuba.config;

import com.haulmont.cuba.core.TransactionalDataManager;
import com.haulmont.cuba.core.global.Metadata;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.udya.querydsl.cuba.core.CubaQueryFactory;

@SuppressWarnings("SpringFacetCodeInspection")
@Configuration
public class CubaQueryFactoryConfig {

    public static final String CUBA_QUERY_FACTORY_BEAN_NAME = "querydsl_CubaQueryFactory";
    public static final String INSECURE_CUBA_QUERY_FACTORY_BEAN_NAME = "querydsl_InsecureCubaQueryFactory";

    protected Metadata metadata;
    protected TransactionalDataManager txDataManager;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public CubaQueryFactoryConfig(Metadata metadata,
                                  TransactionalDataManager txDataManager) {
        this.metadata = metadata;
        this.txDataManager = txDataManager;
    }

    @Bean(CUBA_QUERY_FACTORY_BEAN_NAME)
    @Primary
    public CubaQueryFactory cubaQueryFactory() {
        return new CubaQueryFactory(txDataManager.secure(), metadata);
    }

    @Bean(INSECURE_CUBA_QUERY_FACTORY_BEAN_NAME)
    public CubaQueryFactory insecureCubaQueryFactory() {
        return new CubaQueryFactory(txDataManager, metadata);
    }
}