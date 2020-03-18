package ru.udya.querydsl.cuba.core;

import com.google.common.collect.ImmutableMap;
import com.querydsl.core.types.Ops;
import com.querydsl.jpa.DefaultQueryHandler;
import com.querydsl.jpa.JPQLOps;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.QueryHandler;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

/**
 * {@code CubaTemplates} extends {@link JPQLTemplates} with CUBA.platform and EclipseLink specific extensions
 *
 * Based on {@link com.querydsl.jpa.EclipseLinkTemplates}
 */
public class CubaTemplates extends JPQLTemplates {

    private static final QueryHandler QUERY_HANDLER;

    static {
        QueryHandler instance;
        try {
            instance = (QueryHandler) Class.forName("com.company.checkcompositionoffragments.querydsl.jpa.cuba.CubaHandler").newInstance();
        } catch (NoClassDefFoundError e) {
            instance = DefaultQueryHandler.DEFAULT;
        } catch (Exception e) {
            instance = DefaultQueryHandler.DEFAULT;
        }
        QUERY_HANDLER = instance;
    }


    public static final CubaTemplates DEFAULT = new CubaTemplates();

    private final Map<Class<?>, String> typeNames;

    public CubaTemplates() {
        this(DEFAULT_ESCAPE);
    }

    public CubaTemplates(char escape) {
        super(escape, QUERY_HANDLER);

        ImmutableMap.Builder<Class<?>, String> builder = ImmutableMap.builder();
        builder.put(Short.class, "short");
        builder.put(Integer.class, "integer");
        builder.put(Long.class, "bigint");
        builder.put(BigInteger.class, "bigint");
        builder.put(Float.class, "float");
        builder.put(Double.class, "double");
        builder.put(BigDecimal.class, "double");
        typeNames = builder.build();

        add(Ops.CHAR_AT, "substring({0},{1+'1'},1)");
        add(JPQLOps.CAST, "cast({0} {1s})");
        add(Ops.STRING_CAST, "trim(cast({0} char(128)))");
        add(Ops.NUMCAST, "cast({0} {1s})");

        // datetime
        add(Ops.DateTimeOps.MILLISECOND, "extract(millisecond from {0})");
        add(Ops.DateTimeOps.SECOND, "extract(second from {0})");
        add(Ops.DateTimeOps.MINUTE, "extract(minute from {0})");
        add(Ops.DateTimeOps.HOUR, "extract(hour from {0})");
        add(Ops.DateTimeOps.DAY_OF_WEEK, "extract(day_of_week from {0})");
        add(Ops.DateTimeOps.DAY_OF_MONTH, "extract(day from {0})");
        add(Ops.DateTimeOps.DAY_OF_YEAR, "extract(day_of_year from {0})");
        add(Ops.DateTimeOps.WEEK, "extract(week from {0})");
        add(Ops.DateTimeOps.MONTH, "extract(month from {0})");
        add(Ops.DateTimeOps.YEAR, "extract(year from {0})");

        add(Ops.DateTimeOps.YEAR_MONTH, "extract(year from {0}) * 100 + extract(month from {0})");
        add(Ops.DateTimeOps.YEAR_WEEK, "extract(year from {0}) * 100 + extract(week from {0})");

    }

    @Override
    public String getTypeForCast(Class<?> cl) {
        return typeNames.get(cl);
    }

    @Override
    public boolean isPathInEntitiesSupported() {
        return false;
    }
}
