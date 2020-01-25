package ru.udya.querydsl.cuba.core.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity(name = "Parent2")
public class Parent {

    @Id
    int id;

    @OneToMany(mappedBy = "parent")
    Set<Child> children;

}
