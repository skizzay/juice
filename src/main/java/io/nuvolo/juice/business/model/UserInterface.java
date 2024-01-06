package io.nuvolo.juice.business.model;

import java.util.Map;

public interface UserInterface {
    Screen getCurrentScreen();
    default <T extends Screen> T getCurrentScreen(ScreenName screenName, Class<T> screenClass) {
        return screenClass.cast(getCurrentScreen());
    }
    void navigateTo(ScreenName screenName);
    void navigateToStartingScreen();
    void performAction(ActionName actionName);
    Map<FieldName, String> getPreviousState(ScreenName screenName);
}
