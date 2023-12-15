package io.nuvolo.juice.business.model;

import java.util.Objects;

public record ScreenName(String name) {
    public ScreenName {
        Objects.requireNonNull(name);
    }

    public static ScreenName of(String value) {
        return new ScreenName(value);
    }

    @Override
    public String toString() {
        return name;
    }
}
