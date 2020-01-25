package ru.udya.querydsl.cuba.core.qcore.targets;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
public @interface NoEclipseLink {

    ru.udya.querydsl.cuba.core.qcore.targets.Target[] value() default {};
}
