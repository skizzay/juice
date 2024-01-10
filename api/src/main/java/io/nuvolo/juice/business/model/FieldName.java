package io.nuvolo.juice.business.model;

import java.util.Objects;

public record FieldName(String name) {
    public FieldName {
        Objects.requireNonNull(name);
    }

    public static FieldName of(String name) {
        return new FieldName(name);
    }

    @Override
    public String toString() {
        return name;
    }
}