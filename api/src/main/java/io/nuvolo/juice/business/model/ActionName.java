package io.nuvolo.juice.business.model;

import java.util.Objects;

public record ActionName(String value) {
    public ActionName {
        Objects.requireNonNull(value, "Action name cannot be null");
    }

    public static ActionName of(String value) {
        return new ActionName(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
