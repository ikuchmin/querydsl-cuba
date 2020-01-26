package ru.udya.querydsl.cuba.core.domain;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;

@SuppressWarnings("DataModelLocalizedMessageMissing")
public enum CompanyRating  implements EnumClass<String> {

    A("A"), AA("AA"), AAA("AAA");

    private String id;

    CompanyRating(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static CompanyRating fromId(String id) {
        for (CompanyRating at : CompanyRating.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
