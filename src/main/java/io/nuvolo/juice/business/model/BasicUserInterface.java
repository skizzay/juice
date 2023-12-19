package io.nuvolo.juice.business.model;

import java.util.Objects;

public class BasicUserInterface implements UserInterface {
    private final Screen startingScreen;
    private Screen currentScreen;
    private final ScreenNavigator screenNavigator;

    public BasicUserInterface(Screen startingScreen, ScreenNavigator screenNavigator) {
        this.startingScreen = Objects.requireNonNull(startingScreen, "Starting screen cannot be null");
        this.screenNavigator = screenNavigator;
    }

    @Override
    public Screen getCurrentScreen() {
        return currentScreen;
    }

    @Override
    public void navigateTo(ScreenName screenName) {
        Objects.requireNonNull(screenName, "Screen name cannot be null");
        currentScreen = screenNavigator.navigate(getCurrentScreen(), screenName);
    }

    @Override
    public void navigateToStartingScreen() {
        currentScreen = startingScreen;
    }
}
