package io.nuvolo.juice.business.model;

public record Navigation(ScreenName source, ScreenName target, Action action) {
    public Navigation {
        if (null == target) {
            throw new IllegalArgumentException("target must not be null");
        }
        if (null == action) {
            throw new IllegalArgumentException("action must not be null");
        }
    }

    public boolean isNavigableFromAll() {
        return null == source;
    }
}
