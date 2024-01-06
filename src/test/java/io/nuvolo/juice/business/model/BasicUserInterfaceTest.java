package io.nuvolo.juice.business.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasicUserInterfaceTest {
    @Mock private Screen startingScreen;
    @Mock private ScreenNavigator screenNavigator;
    @Mock private Map<FieldName, String> previousState;

    private BasicUserInterface createTarget() {
        return new BasicUserInterface(startingScreen, screenNavigator);
    }

    @Test
    void getCurrentScreen_beforeNavigatingToStartingScreen_returnsNull() {
        // Arrange
        final BasicUserInterface target = createTarget();

        // Act
        final Screen actual = target.getCurrentScreen();

        // Assert
        assertNull(actual);
    }

    @Test
    void getCurrentScreen_afterNavigatingToStartingScreen_returnsStartingScreen() {
        // Arrange
        final BasicUserInterface target = createTarget();
        target.navigateToStartingScreen();

        // Act
        final Screen actual = target.getCurrentScreen();

        // Assert
        assertEquals(startingScreen, actual);
    }

    @Test
    void getCurrentScreen_afterNavigation_returnsNewScreen() {
        // Arrange
        final BasicUserInterface target = createTarget();
        target.navigateToStartingScreen();
        final ScreenName screenName = new ScreenName("screen name");
        final Screen newScreen = mock(Screen.class);
        when(screenNavigator.navigate(target, startingScreen, screenName))
                .thenReturn(newScreen);

        // Act
        target.navigateTo(screenName);
        final Screen actual = target.getCurrentScreen();

        // Assert
        assertEquals(newScreen, actual);
    }

    @Test
    void getPreviousState_givenNullScreenName_throws() {
        // Arrange
        final BasicUserInterface target = createTarget();
        final ScreenName screenName = null;

        // Act
        final var exception = assertThrows(NullPointerException.class,
                () -> target.getPreviousState(screenName));

        // Assert
        assertEquals("Screen name cannot be null", exception.getMessage());
    }

    @Test
    void getPreviousState_givenUnknownScreenName_throws() {
        // Arrange
        final BasicUserInterface target = createTarget();
        final ScreenName screenName = new ScreenName("unknown screen name");
        when(screenNavigator.getScreen(screenName))
                .thenReturn(java.util.Optional.empty());

        // Act
        final var exception = assertThrows(IllegalArgumentException.class,
                () -> target.getPreviousState(screenName));

        // Assert
        assertEquals("Screen not found", exception.getMessage());
    }

    @Test
    void getPreviousState_givenScreenName_returnsPreviousState() {
        // Arrange
        final BasicUserInterface target = createTarget();
        final ScreenName screenName = new ScreenName("screen name");
        when(startingScreen.getLastState()).thenReturn(previousState);
        when(screenNavigator.getScreen(screenName))
                .thenReturn(java.util.Optional.of(startingScreen));

        // Act
        final var actual = target.getPreviousState(screenName);

        // Assert
        assertEquals(previousState, actual);
    }
}