package io.nuvolo.juice.business.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GraphBasedScreenNavigatorTest {
    private record Node(String value) {}

    private GraphBasedScreenNavigator createNavigator() {
        return new GraphBasedScreenNavigator();
    }

    @Test
    void getScreen_givenScreenExists_returnsScreen() {
        // Arrange
        final GraphBasedScreenNavigator navigator = createNavigator();
        final Screen screen = mock(Screen.class);
        when(screen.getName()).thenReturn(new ScreenName("screen"));
        navigator.addScreen(screen);

        // Act
        navigator.addScreen(screen);
        final Screen result = navigator.getScreen(new ScreenName("screen")).orElseThrow();

        // Assert
        assertEquals(screen, result);
    }

    @Test
    void getScreen_givenScreenDoesNotExist_returnsEmpty() {
        // Arrange
        final GraphBasedScreenNavigator navigator = createNavigator();
        final Screen screen = mock(Screen.class);
        when(screen.getName()).thenReturn(new ScreenName("screen"));
        navigator.addScreen(screen);

        // Act
        navigator.addScreen(screen);
        final var result = navigator.getScreen(new ScreenName("screen2"));

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void navigate_givenUnknownSourceScreen_throws() {
        // Arrange
        final GraphBasedScreenNavigator navigator = createNavigator();
        final Screen screen1 = mock(Screen.class);
        when(screen1.getName()).thenReturn(new ScreenName("screen1"));
        when(screen1.getDirectlyNavigableScreens()).thenReturn(List.of(new ScreenName("screen2")));
        final Screen screen2 = mock(Screen.class);
        when(screen2.getName()).thenReturn(new ScreenName("screen2"));
        when(screen2.getDirectlyNavigableScreens()).thenReturn(List.of());
        navigator.addScreen(screen2);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> navigator.navigate(screen1, new ScreenName("screen2")));
    }

    @Test
    void navigate_givenUnknownTargetScreenName_throws() {
        // Arrange
        final GraphBasedScreenNavigator navigator = createNavigator();
        final Screen screen1 = mock(Screen.class);
        when(screen1.getName()).thenReturn(new ScreenName("screen1"));
        when(screen1.getDirectlyNavigableScreens()).thenReturn(List.of(new ScreenName("screen2")));
        final Screen screen2 = mock(Screen.class);
        when(screen2.getName()).thenReturn(new ScreenName("screen2"));
        when(screen2.getDirectlyNavigableScreens()).thenReturn(List.of());
        navigator.addScreen(screen1);
        navigator.addScreen(screen2);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> navigator.navigate(screen1, new ScreenName("screen3")));
    }

    @Test
    void navigate_givenNoPathBetweenScreens_throws() {
        // Arrange
        final GraphBasedScreenNavigator navigator = createNavigator();
        final Screen screen1 = mock(Screen.class);
        when(screen1.getName()).thenReturn(new ScreenName("screen1"));
        when(screen1.getDirectlyNavigableScreens()).thenReturn(List.of());
        final Screen screen2 = mock(Screen.class);
        when(screen2.getName()).thenReturn(new ScreenName("screen2"));
        when(screen2.getDirectlyNavigableScreens()).thenReturn(List.of());
        navigator.addScreen(screen1);
        navigator.addScreen(screen2);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> navigator.navigate(screen1, new ScreenName("screen2")));
    }

    @Test
    void navigate_givenCircularPathBetweenScreens_returnsSelf() {
        // Arrange
        final GraphBasedScreenNavigator navigator = createNavigator();
        final Screen screen1 = mock(Screen.class);
        when(screen1.getName()).thenReturn(new ScreenName("screen1"));
        when(screen1.getDirectlyNavigableScreens()).thenReturn(List.of(new ScreenName("screen1")));
        navigator.addScreen(screen1);

        // Act & Assert
        final Screen result = navigator.navigate(screen1, new ScreenName("screen1"));

        // Assert
        assertEquals(screen1, result);
    }

    @Test
    void navigate_givenValidScreen_returnsScreen() {
        // Arrange
        final GraphBasedScreenNavigator navigator = createNavigator();
        final Screen screen1 = mock(Screen.class);
        when(screen1.getName()).thenReturn(new ScreenName("screen1"));
        when(screen1.getDirectlyNavigableScreens()).thenReturn(List.of(new ScreenName("screen2")));
        final Screen screen2 = mock(Screen.class);
        when(screen2.getName()).thenReturn(new ScreenName("screen2"));
        when(screen2.getDirectlyNavigableScreens()).thenReturn(List.of());
        navigator.addScreen(screen1);
        navigator.addScreen(screen2);

        // Act
        navigator.addScreen(screen1);
        navigator.addScreen(screen2);
        final Screen result = navigator.navigate(screen1, new ScreenName("screen2"));

        // Assert
        assertEquals(screen2, result);
    }
}