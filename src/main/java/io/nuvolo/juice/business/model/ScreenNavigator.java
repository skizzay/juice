package io.nuvolo.juice.business.model;

import java.util.Optional;

public interface ScreenNavigator {
    Optional<Screen> getScreen(ScreenName screenName);
    Screen navigate(UserInterface userInterface, Screen source, ScreenName target);
}
