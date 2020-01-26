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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The Class Show.
 */
@Table(name = "QUERYDSL_CUBA_SHOW")
@Entity(name = "querydslcuba_Show")
public class Show extends StandardEntity {

    private static final long serialVersionUID = - 1107270705122358675L;

    @Column(name = "INT_ID", unique = true)
    protected Long lngId;

    @JoinColumn
    @ManyToOne
    public Show parent;

    public static Show show() {
        Metadata metadata = AppBeans.get(Metadata.class);
        return metadata.create(Show.class);
    }

    public static Show show(long id) {
        Metadata metadata = AppBeans.get(Metadata.class);
        Show show = metadata.create(Show.class);

        show.setLngId(id);

        return show;
    }

    public Long getLngId() {
        return lngId;
    }

    public void setLngId(Long lngId) {
        this.lngId = lngId;
    }

    public Show getParent() {
        return parent;
    }

    public void setParent(Show parent) {
        this.parent = parent;
    }
}
