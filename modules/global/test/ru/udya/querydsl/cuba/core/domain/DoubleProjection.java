package ru.udya.querydsl.cuba.core.domain;

import com.querydsl.core.annotations.QueryProjection;

public class DoubleProjection {

    public double val;

    @QueryProjection
    public DoubleProjection(double val) {
        this.val = val;
    }

}
