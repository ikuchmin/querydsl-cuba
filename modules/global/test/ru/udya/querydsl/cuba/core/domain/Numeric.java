package ru.udya.querydsl.cuba.core.domain;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "QUERYDSL_CUBA_NUMERIC")
@Entity(name = "querydslcuba_Numeric")
public class Numeric extends StandardEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "LONG_ID")
    private Long longId;

    @Column(name = "VALUE")
    private BigDecimal value;

    public static Numeric numeric() {
        Metadata metadata = AppBeans.get(Metadata.class);
        return metadata.create(Numeric.class);
    }

    public Long getLongId() {
        return longId;
    }

    public void setLongId
            (Long longId) {
        this.longId = longId;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

}
