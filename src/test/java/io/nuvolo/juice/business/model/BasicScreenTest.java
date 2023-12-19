package io.nuvolo.juice.business.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BasicScreenTest {
    private final ScreenName screenName = new ScreenName("screenName");
    private final HashMap<ActionName, Action> requests = new HashMap<>();
    private final HashMap<FieldName, Field> fields = new HashMap<>();
    private final HashMap<FieldName, Container> containers = new HashMap<>();

    private BasicScreen createBasicScreen() {
        return new BasicScreen(screenName, requests, fields, containers);
    }

    @Test
    public void getName_returnsScreenName() {
        // Arrange
        final BasicScreen basicScreen = createBasicScreen();

        // Act
        final var name = basicScreen.getScreenName();

        // Assert
        assertEquals(screenName, name);
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