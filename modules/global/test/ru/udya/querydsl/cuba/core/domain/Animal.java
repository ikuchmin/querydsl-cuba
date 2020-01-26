/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.udya.querydsl.cuba.core.domain;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * The Class Animal.
 */
@SuppressWarnings("DataModelLocalizedMessageMissing")
@Table(name = "QUERYDSL_CUBA_ANIMAL")
@Entity(name = "querydslcuba_Animal")
public class Animal extends StandardEntity {

    private static final long serialVersionUID = - 2805944819260156782L;

    @Column(name = "INT_ID", unique = true)
    protected Integer intId;

    @Column(name = "ALIVE")
    protected Boolean alive;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "BIRTHDATE")
    protected Date birthdate;

    @Column(name = "WEIGHT")
    protected Integer weight;

    @Column(name = "TOES")
    protected Integer toes;

    // needed for JPA tests
    @Column(name = "BODY_WEIGHT")
    protected Double bodyWeight;

    @Column(name = "DOUBLE_PROPERTY")
    private Double doubleProperty;

    @Column(name = "COLOR")
    private String color;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_FIELD")
    private Date dateField;

    @Column(name = "NAME")
    private String name;

    public Integer getIntId() {
        return intId;
    }

    public void setIntId(Integer intId) {
        this.intId = intId;
    }


    public static Animal animal(Integer id) {
        Metadata metadata = AppBeans.get(Metadata.class);
        Animal animal = metadata.create(Animal.class);

        animal.setIntId(id);

        return animal;
    }

    public void setColor(Color color) {
        this.color = color == null ? null : color.getId();
    }

    public Color getColor() {
        return color == null ? null : Color.fromId(color);
    }

    public Boolean getAlive() {
        return alive;
    }

    public void setAlive(Boolean alive) {
        this.alive = alive;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getToes() {
        return toes;
    }

    public void setToes(Integer toes) {
        this.toes = toes;
    }

    public Double getBodyWeight() {
        return bodyWeight;
    }

    public void setBodyWeight(Double bodyWeight) {
        this.bodyWeight = bodyWeight;
    }

    public Double getDoubleProperty() {
        return doubleProperty;
    }

    public void setDoubleProperty(Double doubleProperty) {
        this.doubleProperty = doubleProperty;
    }

    public Date getDateField() {
        return dateField;
    }

    public void setDateField(Date dateField) {
        this.dateField = dateField;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
