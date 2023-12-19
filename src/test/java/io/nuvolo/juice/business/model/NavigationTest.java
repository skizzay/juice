package io.nuvolo.juice.business.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NavigationTest {

    private ScreenName sourceName;
    private ScreenName targetName;
    private Action action;

    private Navigation createTarget() {
        return new Navigation(sourceName, targetName, action);
    }

    @Test
    void constructor_givenNullSource_throws() {
        // Arrange
        sourceName = null;
        targetName = ScreenName.of("target");
        action = Action.noOp();

        // Act
        final var exception = assertThrows(IllegalArgumentException.class, this::createTarget);

        // Assert
        assertEquals("source must not be null", exception.getMessage());
    }

    @Test
    void constructor_givenNullTarget_throws() {
        // Arrange
        sourceName = ScreenName.of("source");
        targetName = null;
        action = Action.noOp();

        // Act
        final var exception = assertThrows(IllegalArgumentException.class, this::createTarget);

        // Assert
        assertEquals("target must not be null", exception.getMessage());
    }

    @Test
    void constructor_givenNullAction_throws() {
        // Arrange
        sourceName = ScreenName.of("source");
        targetName = ScreenName.of("target");
        action = null;

        // Act
        final var exception = assertThrows(IllegalArgumentException.class, this::createTarget);

        // Assert
        assertEquals("action must not be null", exception.getMessage());
    }

    @Test
    void isNavigableFromAll_givenGlobalSource_returnsTrue() {
        // Arrange
        sourceName = Navigation.GLOBAL_SOURCE;
        targetName = ScreenName.of("target");
        action = Action.noOp();
        final Navigation target = createTarget();

        // Act
        final var actual = target.isNavigableFromAll();

        // Assert
        assertTrue(actual);
    }

    @Test
    void isNavigableFromAll_givenNonGlobalSource_returnsFalse() {
        // Arrange
        sourceName = ScreenName.of("source");
        targetName = ScreenName.of("target");
        action = Action.noOp();
        final Navigation target = createTarget();

        // Act
        final var actual = target.isNavigableFromAll();

        // Assert
        assertFalse(actual);
    }
}