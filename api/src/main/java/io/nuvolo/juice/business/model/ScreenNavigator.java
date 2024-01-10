package io.nuvolo.juice.business.model;

import java.util.Optional;

public interface ScreenNavigator {
    Optional<Screen> getScreen(ScreenName screenName);
    Screen navigate(Screen source, ScreenName target);
}
