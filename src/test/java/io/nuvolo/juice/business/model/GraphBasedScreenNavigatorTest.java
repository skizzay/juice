package io.nuvolo.juice.business.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

class GraphBasedScreenNavigatorTest {
    private final NavigationTable navigationTable = new NavigationTable();
    private final Map<ScreenName, Screen> screens = mockScreens(
            ScreenName.of("screen1"),
            ScreenName.of("screen2"),
            ScreenName.of("screen3")
    );

    private static Screen mockScreen(ScreenName name) {
        final Screen screen = mock(Screen.class);
        when(screen.getScreenName()).thenReturn(name);
        return screen;
    }

    private static Map<ScreenName, Screen> mockScreens(ScreenName... names) {
        final Map<ScreenName, Screen> screens = new HashMap<>();
        for (final ScreenName name : names) {
            screens.put(name, mockScreen(name));
        }
        return screens;
    }

    private GraphBasedScreenNavigator createNavigator() {
        return new GraphBasedScreenNavigator(screens, navigationTable);
    }

    @Test
    void navigate_givenUnknownSourceScreen_throws() {
        // Arrange
        final GraphBasedScreenNavigator navigator = createNavigator();
        final Screen source = mock(Screen.class);
        when(source.getScreenName()).thenReturn(new ScreenName("unknown source"));
        final ScreenName targetName = ScreenName.of("screen2");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> navigator.navigate(source, targetName));
    }

    @Test
    void navigate_givenUnknownTargetScreenName_throws() {
        // Arrange
        final GraphBasedScreenNavigator navigator = createNavigator();
        final Screen source = screens.get(ScreenName.of("screen1"));
        final ScreenName targetName = ScreenName.of("unknown target");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> navigator.navigate(source, targetName));
    }

    @Test
    void navigate_givenNoPathBetweenScreens_throws() {
        // Arrange
        final GraphBasedScreenNavigator navigator = createNavigator();
        final Screen source = screens.get(ScreenName.of("screen1"));
        final ScreenName targetName = ScreenName.of("screen2");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> navigator.navigate(source, targetName));
    }

    @Test
    void navigate_givenCircularPathBetweenScreens_returnsSelf() {
        // Arrange
        final GraphBasedScreenNavigator navigator = createNavigator();
        final Screen source = screens.get(ScreenName.of("screen1"));
        final ScreenName targetName = source.getScreenName();

        // Act & Assert
        final Screen result = navigator.navigate(source, targetName);

        // Assert
        assertEquals(source, result);
    }

    @Test
    void navigate_givenValidScreen_returnsScreen() {
        // Arrange
        final GraphBasedScreenNavigator navigator = createNavigator();
        final Screen source = screens.get(ScreenName.of("screen1"));
        final ScreenName targetName = ScreenName.of("screen2");
        navigationTable.addNavigation(new Navigation(source.getScreenName(), targetName, Action.noOp()));

        // Act
        final Screen result = navigator.navigate(source, targetName);

        // Assert
        assertEquals(screens.get(targetName), result);
    }

    @Test
    void navigate_givenSourceIsValidFromAll_returnsScreen() {
        // Arrange
        final GraphBasedScreenNavigator navigator = createNavigator();
        final Screen source = screens.get(ScreenName.of("screen1"));
        final ScreenName targetName = ScreenName.of("screen2");
        navigationTable.addNavigation(new Navigation(ScreenName.of("*"), targetName, Action.noOp()));

        // Act
        final Screen result = navigator.navigate(source, targetName);

        // Assert
        assertEquals(screens.get(targetName), result);
    }
}