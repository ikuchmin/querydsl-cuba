package ru.udya.querydsl.cuba.core.domain;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "QUERYDSL_CUBA_ENTITY1")
@Entity(name = "querydslcuba_Entity1")
public class Entity1 extends StandardEntity {

    @Column(name = "INT_ID")
    protected Integer intId;

    @Column(name = "PROPERTY")
    public String property;

    public static Entity1 entity1() {
        Metadata metadata = AppBeans.get(Metadata.class);
        return metadata.create(Entity1.class);
    }

    public static Entity1 entity1(Integer intId) {
        Metadata metadata = AppBeans.get(Metadata.class);
        Entity1 entity = metadata.create(Entity1.class);

        entity.setIntId(intId);

        return entity;
    }

    public Integer getIntId() {
        return intId;
    }

    public void setIntId(Integer intId) {
        this.intId = intId;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }
}
