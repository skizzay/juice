package io.nuvolo.juice.business.model;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
        validateScreenName(screenName);
        currentScreen = screenNavigator.navigate(getCurrentScreen(), screenName);
    }

    @Override
    public void navigateToStartingScreen() {
        currentScreen = startingScreen;
    }

    @Override
    public void performAction(ActionName actionName) {
        validateActionName(actionName);
        getCurrentScreen().performAction(this, actionName);
    }

    @Override
    public Optional<Map<FieldName, String>> getPreviousState(ScreenName screenName) {
        validateScreenName(screenName);
        return screenNavigator.getScreen(screenName)
                .map(Screen::getLastState);
    }

    private static void validateScreenName(ScreenName screenName) {
        Objects.requireNonNull(screenName, "Screen name cannot be null");
    }

    private static void validateActionName(ActionName actionName) {
        Objects.requireNonNull(actionName, "Action name cannot be null");}
}