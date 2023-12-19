package io.nuvolo.juice.business.model;

public record Navigation(ScreenName source, ScreenName target, Action action) {
    public static ScreenName GLOBAL_SOURCE = ScreenName.of("*");
    public Navigation {
        if (null == source) {
            throw new IllegalArgumentException("source must not be null");
        }
        if (null == target) {
            throw new IllegalArgumentException("target must not be null");
        }
        if (null == action) {
            throw new IllegalArgumentException("action must not be null");
        }
    }

    public boolean isNavigableFromAll() {
        return GLOBAL_SOURCE.equals(source);
    }
}
