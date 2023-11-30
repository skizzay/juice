package io.nuvolo.juice.business.model;

public interface UserInterface {
    Screen getCurrentScreen();
    void navigateTo(ScreenName screenName);
}
