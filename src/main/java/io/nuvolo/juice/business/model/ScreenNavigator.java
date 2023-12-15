package io.nuvolo.juice.business.model;

public interface ScreenNavigator {
    Screen navigate(Screen source, ScreenName target);
}
