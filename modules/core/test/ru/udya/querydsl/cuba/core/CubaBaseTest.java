package ru.udya.querydsl.cuba.core;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.TransactionalDataManager;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.jpa.JPQLQuery;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import ru.udya.querydsl.cuba.QuerydslcubaTestContainer;

public class CubaBaseTest extends AbstractCubaTest {

    @ClassRule
    public static QuerydslcubaTestContainer cont = QuerydslcubaTestContainer.Common.INSTANCE;

    private static Metadata metadata;
    private static Persistence persistence;
    private static TransactionalDataManager txDm;

    @BeforeClass
    public static void classSetUp() throws Exception {
        metadata = cont.metadata();
        persistence = cont.persistence();
        txDm = AppBeans.get(TransactionalDataManager.class);
    }

    @AfterClass
    public static void classTearDown() throws Exception {
    }

    @Override
    protected JPQLQuery<?> query() {
        return new CubaQuery<>(txDm, metadata);
    }

    @Override
    protected JPQLQuery<?> testQuery() {
        return new CubaQuery<>(txDm, metadata, new DefaultQueryMetadata());
    }

    @Override
    protected void save(Object entity) {
        txDm.save((Entity) entity);
    }
}
