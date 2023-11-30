package io.nuvolo.juice.business.model;

import java.util.Optional;

public interface ScreenNavigator {
    void addScreen(Screen screen);
    Screen navigate(Screen source, ScreenName target);
    Optional<Screen> getScreen(ScreenName name);
}
