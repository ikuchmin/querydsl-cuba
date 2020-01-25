package ru.udya.querydsl.cuba.core.domain;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.Collection;

@Entity
public class Human extends Mammal {

    @ElementCollection
    Collection<Integer> hairs;

}
