package io.nuvolo.juice.business.model;

import java.util.Objects;

public record ScreenName(String name) {
    public ScreenName {
        Objects.requireNonNull(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
