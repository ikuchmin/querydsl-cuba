package ru.udya.querydsl.cuba.core.domain5;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class MyEmbeddedAttribute {
    @ManyToOne
    private MyOtherEntity attributeWithInitProblem;
}