package io.nuvolo.juice.business.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BasicScreenTest {
    private final ScreenName screenName = new ScreenName("screenName");
    private final HashMap<ScreenName, Action> navigation = new HashMap<>();
    private final HashMap<ActionName, Action> requests = new HashMap<>();
    private final HashMap<FieldName, ReadableField> readableFields = new HashMap<>();
    private final HashMap<FieldName, WriteableField> writeableFields = new HashMap<>();
    private final HashMap<FieldName, Container> containers = new HashMap<>();

    private BasicScreen createBasicScreen() {
        return new BasicScreen(screenName, navigation, requests, readableFields, writeableFields, containers);
    }

    @Test
    public void getName_returnsScreenName() {
        // Arrange
        final BasicScreen basicScreen = createBasicScreen();

        // Act
        final var name = basicScreen.getName();

        // Assert
        assertEquals(screenName, name);
    }

    @Test
    public void navigate_givenSameScreenName_doesNothing() {
        // Arrange
        final BasicScreen target = createBasicScreen();
        final ScreenName screenName = new ScreenName("screenName");

        // Act & Assert
        assertDoesNotThrow(() -> target.navigateTo(screenName));
    }

    @Test
    public void navigate_givenDifferentScreenName_attemptsNavigation() {
        // Arrange
        final Action intendedAction = mock(Action.class);
        final ScreenName screenName = new ScreenName("OtherScreenName");
        navigation.put(screenName, intendedAction);
        final BasicScreen target = createBasicScreen();

        // Act
        target.navigateTo(screenName);

        // Assert
        verify(intendedAction, times(1)).execute(target);
    }

    @Test
    public void navigate_givenUnknownScreenName_throws() {
        // Arrange
        final ScreenName screenName = new ScreenName("OtherScreenName");
        final BasicScreen target = createBasicScreen();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> target.navigateTo(screenName));
    }

    @Test
    public void getDirectlyNavigableScreens_returnsDirectlyNavigableScreens() {
        // Arrange
        final Action action1 = mock(Action.class);
        final Action action2 = mock(Action.class);
        final Action action3 = mock(Action.class);
        navigation.put(new ScreenName("screenName"), action1);
        navigation.put(new ScreenName("otherScreenName"), action2);
        navigation.put(new ScreenName("yetAnotherScreenName"), action3);
        final BasicScreen target = createBasicScreen();
        // NOTE: We're using a hash set because we're not guaranteed to get the screens back in the same order
        final HashSet<ScreenName> expected = new HashSet<>();
        expected.add(new ScreenName("screenName"));
        expected.add(new ScreenName("otherScreenName"));
        expected.add(new ScreenName("yetAnotherScreenName"));

        // Act
        final var actual = target.getDirectlyNavigableScreens();

        // Assert
        assertEquals(expected, new HashSet<>(actual));
    }

    @Test
    public void performAction_givenUnknownActionName_throws() {
        // Arrange
        final ActionName actionName = new ActionName("actionName");
        final BasicScreen target = createBasicScreen();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> target.performAction(actionName));
    }
}