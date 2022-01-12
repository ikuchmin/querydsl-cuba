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
import ru.udya.querydsl.cuba.core.domain.Employee;
import ru.udya.querydsl.cuba.core.domain.QAnimal;
import ru.udya.querydsl.cuba.core.domain.QCat;
import ru.udya.querydsl.cuba.core.domain.QCompany;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TypedViewBuilderTest {

    @ClassRule
    public static QuerydslcubaTestContainer cont = QuerydslcubaTestContainer.Common.INSTANCE;

    private static Metadata metadata;
    private static Persistence persistence;
    private static TransactionalDataManager txDm;

    private static final QCompany company = QCompany.company;

    private static final QAnimal animal = QAnimal.animal;

    private static final QCat cat = QCat.cat;

    private static final QCat otherCat = new QCat("otherCat");

    @BeforeClass
    public static void classSetUp() throws Exception {
        metadata = cont.metadata();
        persistence = cont.persistence();
        txDm = AppBeans.get(TransactionalDataManager.class);
    }

    @AfterClass
    public static void classTearDown() throws Exception {
    }


    @Test
    public void empty_view() {
        View emptyCatView = new TypedViewBuilder<>().view(QCat.cat).build();

        assertTrue(emptyCatView.getProperties().isEmpty());
    }


    @Test
    public void init_view_with_properties() {
        QCat qCat = QCat.cat;
        View catView = new TypedViewBuilder<>()
                .view(qCat, qCat.birthdate, qCat.alive).build();

        Set<String> actualProperties = catView.getProperties().stream()
                .map(ViewProperty::getName).collect(Collectors.toSet());

        HashSet<String> expectedProperties = new HashSet<>(asList(qCat.birthdate.getMetadata().getName(),
                qCat.alive.getMetadata().getName()));

        assertEquals(expectedProperties, actualProperties);
    }

    @Test
    public void init_view_with_lazy_properties() {
        QCat qCat = QCat.cat;
        View catView = new TypedViewBuilder<QCat>()
                .view(qCat, qc -> asList(qc.birthdate, qc.alive)).build();

        Set<String> actualProperties = catView.getProperties().stream()
                .map(ViewProperty::getName).collect(Collectors.toSet());

        HashSet<String> expectedProperties = new HashSet<>(asList(qCat.birthdate.getMetadata().getName(),
                qCat.alive.getMetadata().getName()));

        assertEquals(expectedProperties, actualProperties);
    }

    @Test
    public void add_properties_to_existed_view() {
        QCat qCat = QCat.cat;
        View catView = new TypedViewBuilder<QCat>().view(qCat)
                .properties(qCat.birthdate, qCat.alive).build();

        Set<String> actualProperties = catView.getProperties().stream()
                .map(ViewProperty::getName).collect(Collectors.toSet());

        HashSet<String> expectedProperties = new HashSet<>(asList(qCat.birthdate.getMetadata().getName(),
                qCat.alive.getMetadata().getName()));

        assertEquals(expectedProperties, actualProperties);
    }


    @Test
    public void add_lazy_properties_to_existed_view() {
        QCat qCat = QCat.cat;
        View catView = new TypedViewBuilder<QCat>().view(qCat)
                .properties(qc -> asList(qc.birthdate, qc.alive)).build();

        Set<String> actualProperties = catView.getProperties().stream()
                .map(ViewProperty::getName).collect(Collectors.toSet());

        HashSet<String> expectedProperties = new HashSet<>(asList(qCat.birthdate.getMetadata().getName(),
                qCat.alive.getMetadata().getName()));

        assertEquals(expectedProperties, actualProperties);
    }

    @Test
    public void add_property_to_existed_view() {
        QCat qCat = QCat.cat;
        View catView = new TypedViewBuilder<>().view(qCat)
                .property(qCat.birthdate).property(qCat.alive).build();

        Set<String> actualProperties = catView.getProperties().stream()
                .map(ViewProperty::getName).collect(Collectors.toSet());

        HashSet<String> expectedProperties = new HashSet<>(asList(qCat.birthdate.getMetadata().getName(),
                qCat.alive.getMetadata().getName()));

        assertEquals(expectedProperties, actualProperties);
    }

    @Test
    public void add_complex_property_with_lazy_properties() {
        QCompany qCompany = QCompany.company;

        View companyView = new TypedViewBuilder<>()
                .view(qCompany, qCompany.name, qCompany.officialName)
                .property(qCompany.ceo, c -> asList(c.id, c.firstName)).build();

        Set<String> actualProperties = companyView.getProperties().stream()
                .flatMap(p -> p.getView() == null ? Stream.of(p.getName())
                        : p.getView().getProperties().stream()
                        .map(ip -> p.getName() + "." + ip.getName()))
                .collect(Collectors.toSet());

        String ceoName = qCompany.ceo.getMetadata().getName();
        HashSet<String> expectedProperties = new HashSet<>(asList(
                qCompany.name.getMetadata().getName(),
                qCompany.officialName.getMetadata().getName(),
                ceoName + "." + qCompany.ceo.id.getMetadata().getName(),
                ceoName + "." + qCompany.ceo.firstName.getMetadata().getName()));

        assertEquals(expectedProperties, actualProperties);
    }

    @Test
    public void add_complex_property_with_sub_builder() {
        QCompany qCompany = QCompany.company;

        View companyView = new TypedViewBuilder<>()
                .view(qCompany, qCompany.name, qCompany.officialName)
                .property(qCompany.ceo, (c, cb) -> cb.properties(c.id, c.firstName)).build();

        Set<String> actualProperties = companyView.getProperties().stream()
                .flatMap(p -> p.getView() == null ? Stream.of(p.getName())
                        : p.getView().getProperties().stream()
                        .map(ip -> p.getName() + "." + ip.getName()))
                .collect(Collectors.toSet());

        String ceoName = qCompany.ceo.getMetadata().getName();
        HashSet<String> expectedProperties = new HashSet<>(asList(
                qCompany.name.getMetadata().getName(),
                qCompany.officialName.getMetadata().getName(),
                ceoName + "." + qCompany.ceo.id.getMetadata().getName(),
                ceoName + "." + qCompany.ceo.firstName.getMetadata().getName()));

        assertEquals(expectedProperties, actualProperties);
    }

    @Test
    public void add_collection_property_with_lazy_properties() {
        QCat qCat = QCat.cat;
        View companyView = new TypedViewBuilder<>()
                .view(qCat, qCat.birthdate, qCat.alive)
                .property(qCat.kittens, (k) -> asList(k.birthdate, k.alive)).build();

        Set<String> actualProperties = companyView.getProperties().stream()
                .flatMap(p -> p.getView() == null ? Stream.of(p.getName())
                        : p.getView().getProperties().stream()
                        .map(ip -> p.getName() + "." + ip.getName()))
                .collect(Collectors.toSet());

        String kittensName = qCat.kittens.getMetadata().getName();
        HashSet<String> expectedProperties = new HashSet<>(asList(
                qCat.birthdate.getMetadata().getName(),
                qCat.alive.getMetadata().getName(),
                kittensName + "." + qCat.birthdate.getMetadata().getName(),
                kittensName + "." + qCat.alive.getMetadata().getName()));
        assertEquals(expectedProperties, actualProperties);
    }

    @Test
    public void add_collection_property_with_builder() {
        QCat qCat = QCat.cat;
        View companyView = new TypedViewBuilder<>()
                .view(qCat, qCat.birthdate, qCat.alive)
                .property(qCat.kittens, (k, kb) -> kb.properties(k.birthdate, k.alive))
                .build();

        Set<String> actualProperties = companyView.getProperties().stream()
                .flatMap(p -> p.getView() == null ? Stream.of(p.getName())
                        : p.getView().getProperties().stream()
                        .map(ip -> p.getName() + "." + ip.getName()))
                .collect(Collectors.toSet());

        String kittensName = qCat.kittens.getMetadata().getName();
        HashSet<String> expectedProperties = new HashSet<>(asList(
                qCat.birthdate.getMetadata().getName(),
                qCat.alive.getMetadata().getName(),
                kittensName + "." + qCat.birthdate.getMetadata().getName(),
                kittensName + "." + qCat.alive.getMetadata().getName()));
        assertEquals(expectedProperties, actualProperties);
    }

    @Test
    public void initialize_view_with_loosing_type_property() {
        QCat qCat = QCat.cat;
        View catView = new TypedViewBuilder<>()
                .view(qCat).withViewBuilder(vb -> vb.addAll("birthdate", "alive")).build();

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
        View catView = new TypedViewBuilder<>()
                .view(qCat).extendByViews(predefinedView, predefinedViewSecond).build();

        Set<String> actualProperties = catView.getProperties().stream()
                .map(ViewProperty::getName).collect(Collectors.toSet());

        HashSet<String> expectedProperties = new HashSet<>(asList(
                qCat.birthdate.getMetadata().getName(),
                qCat.alive.getMetadata().getName(),
                qCat.breed.getMetadata().getName()));

        assertEquals(expectedProperties, actualProperties);
    }

    @Test
    public void add_complex_property_with_predefined_view() {
        View predefinedView = ViewBuilder.of(Employee.class)
                .addAll("id", "firstName").build();

        QCompany qCompany = QCompany.company;

        View companyView = new TypedViewBuilder<>()
                .view(qCompany, qCompany.name, qCompany.officialName)
                .property(qCompany.ceo, predefinedView).build();

        Set<String> actualProperties = companyView.getProperties().stream()
                .flatMap(p -> p.getView() == null ? Stream.of(p.getName())
                        : p.getView().getProperties().stream()
                        .map(ip -> p.getName() + "." + ip.getName()))
                .collect(Collectors.toSet());

        String ceoName = qCompany.ceo.getMetadata().getName();
        HashSet<String> expectedProperties = new HashSet<>(asList(
                qCompany.name.getMetadata().getName(),
                qCompany.officialName.getMetadata().getName(),
                ceoName + "." + qCompany.ceo.id.getMetadata().getName(),
                ceoName + "." + qCompany.ceo.firstName.getMetadata().getName()));

        assertEquals(expectedProperties, actualProperties);
    }
}
