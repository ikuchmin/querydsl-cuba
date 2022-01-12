package ru.udya.querydsl.cuba.core.view;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.TransactionalDataManager;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewBuilder;
import com.haulmont.cuba.core.global.ViewProperty;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import ru.udya.querydsl.cuba.QuerydslcubaTestContainer;
import ru.udya.querydsl.cuba.core.domain.Cat;
import ru.udya.querydsl.cuba.core.domain.QAnimal;
import ru.udya.querydsl.cuba.core.domain.QCat;
import ru.udya.querydsl.cuba.core.domain.QCompany;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TypedViewFactoryTest {

    @ClassRule
    public static QuerydslcubaTestContainer cont = QuerydslcubaTestContainer.Common.INSTANCE;

    private static Metadata metadata;
    private static Persistence persistence;
    private static TransactionalDataManager txDm;
    private static TypedViewFactory testClass;

    private static final QCompany company = QCompany.company;

    private static final QAnimal animal = QAnimal.animal;

    private static final QCat cat = QCat.cat;

    private static final QCat otherCat = new QCat("otherCat");

    @BeforeClass
    public static void classSetUp() throws Exception {
        metadata = cont.metadata();
        persistence = cont.persistence();
        txDm = AppBeans.get(TransactionalDataManager.class);

        testClass = AppBeans.get(TypedViewFactory.class);
    }

    @AfterClass
    public static void classTearDown() throws Exception {
    }


    @Test
    public void empty_view() {
        View emptyCatView = testClass.view(QCat.cat).build();

        assertTrue(emptyCatView.getProperties().isEmpty());
    }


    @Test
    public void init_view_with_properties() {
        QCat qCat = QCat.cat;
        View catView = testClass.view(qCat,
                qCat.birthdate, qCat.alive).build();

        Set<String> actualProperties = catView.getProperties().stream()
                .map(ViewProperty::getName).collect(Collectors.toSet());

        HashSet<String> expectedProperties = new HashSet<>(asList(qCat.birthdate.getMetadata().getName(),
                qCat.alive.getMetadata().getName()));

        assertEquals(expectedProperties, actualProperties);
    }

    @Test
    public void init_view_with_lazy_properties() {
        QCat qCat = QCat.cat;
        View catView = testClass.view(qCat,
                qc -> asList(qc.birthdate, qc.alive)).build();

        Set<String> actualProperties = catView.getProperties().stream()
                .map(ViewProperty::getName).collect(Collectors.toSet());

        HashSet<String> expectedProperties = new HashSet<>(asList(qCat.birthdate.getMetadata().getName(),
                qCat.alive.getMetadata().getName()));

        assertEquals(expectedProperties, actualProperties);
    }

    @Test
    public void initialize_view_with_loosing_type_property() {
        QCat qCat = QCat.cat;
        View catView = testClass.view(qCat)
                .withViewBuilder(vb -> vb.addAll("birthdate", "alive")).build();

        Set<String> actualProperties = catView.getProperties().stream()
                .map(ViewProperty::getName).collect(Collectors.toSet());

        HashSet<String> expectedProperties = new HashSet<>(asList(
                qCat.birthdate.getMetadata().getName(),
                qCat.alive.getMetadata().getName()));

        assertEquals(expectedProperties, actualProperties);
    }

    @Test
    public void initialize_view_with_predefined_view() {
        View predefinedView = ViewBuilder.of(Cat.class)
                .addAll("birthdate", "alive").build();

        View predefinedViewSecond = ViewBuilder.of(Cat.class)
                .addAll("breed").build();

        QCat qCat = QCat.cat;
        View catView = testClass.view(qCat)
                .extendByViews(predefinedView, predefinedViewSecond).build();

        Set<String> actualProperties = catView.getProperties().stream()
                .map(ViewProperty::getName).collect(Collectors.toSet());

        HashSet<String> expectedProperties = new HashSet<>(asList(
                qCat.birthdate.getMetadata().getName(),
                qCat.alive.getMetadata().getName(),
                qCat.breed.getMetadata().getName()));

        assertEquals(expectedProperties, actualProperties);
    }
}
