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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * The Class Company.
 */
@Entity
@Table(name = "company_")
public class Company {

    public enum Rating { A, AA, AAA }

    @Enumerated
    public Rating ratingOrdinal;

    @Enumerated(EnumType.STRING)
    public Rating ratingString;

    @ManyToOne
    public Employee ceo;

    @OneToMany
    public List<Department> departments;

    @Id
    public int id;

    public String name;

    @Column(name = "official_name")
    public String officialName;
}
