package ru.udya.querydsl.cuba.core.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class World {

    @Id
    Long id;

    @OneToMany
    Set<Mammal> mammals;

}
