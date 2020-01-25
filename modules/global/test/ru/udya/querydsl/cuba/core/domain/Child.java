package ru.udya.querydsl.cuba.core.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity(name = "Child2")
public class Child {

    @Id
    int id;

    @ManyToOne
    Parent parent;

}
