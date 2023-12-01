package io.nuvolo.juice.business.model;

public interface UserInterface {
    Screen getCurrentScreen();
    default <T extends Screen> T getCurrentScreen(ScreenName screenName, Class<T> screenClass) {
        return screenClass.cast(getCurrentScreen());
    }
    void navigateTo(ScreenName screenName);
}
