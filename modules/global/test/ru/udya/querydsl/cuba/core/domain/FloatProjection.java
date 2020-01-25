package ru.udya.querydsl.cuba.core.domain;

import com.querydsl.core.annotations.QueryProjection;

public class FloatProjection {

    public float val;

    @QueryProjection
    public FloatProjection(float val) {
        this.val = val;
    }

}
