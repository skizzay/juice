package io.nuvolo.juice.business.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NavigationTableTest {
    private NavigationTable createTarget() {
        return new NavigationTable();
    }


    @Test
    void getNavigations_withoutNavigations_isEmpty() {
        // Arrange
        final NavigationTable target = createTarget();

        // Act
        final var actual = target.getNavigations();

        // Assert
        assertEquals(0, actual.count());
    }

    @Test
    void getNavigations_withNavigations_returnsNavigations() {
        // Arrange
        final NavigationTable target = createTarget();
        target.addNavigation(new Navigation(ScreenName.of("source1"), ScreenName.of("target1"), Action.noOp()));
        target.addNavigation(new Navigation(ScreenName.of("source2"), ScreenName.of("target2"), Action.noOp()));

        // Act
        final var actual = target.getNavigations().toList();

        // Assert
        assertEquals(2, actual.size());
        assertEquals(ScreenName.of("source1"), actual.get(0).source());
        assertEquals(ScreenName.of("target1"), actual.get(0).target());
        assertEquals(ScreenName.of("source2"), actual.get(1).source());
        assertEquals(ScreenName.of("target2"), actual.get(1).target());
    }

    @Test
    void addNavigation_givenDuplicate_throws() {
        // Arrange
        final NavigationTable target = createTarget();
        target.addNavigation(new Navigation(ScreenName.of("source1"), ScreenName.of("target"), Action.noOp()));

        // Act
        final var exception = assertThrows(IllegalArgumentException.class, () -> target.addNavigation(new Navigation(ScreenName.of("source1"), ScreenName.of("target"), Action.noOp())));

        // Assert
        assertEquals("Navigation already exists from 'source1' to 'target'", exception.getMessage());
    }

    @Test
    void getNavigationAction_withoutNavigations_isEmpty() {
        // Arrange
        final NavigationTable target = createTarget();

        // Act
        final var actual = target.getNavigationAction(ScreenName.of("source"), ScreenName.of("target"));

        // Assert
        assertTrue(actual.isEmpty());
    }

    @Test
    void getNavigationAction_givenUnknownSource_isEmpty() {
        // Arrange
        final NavigationTable target = createTarget();
        target.addNavigation(new Navigation(ScreenName.of("source1"), ScreenName.of("target"), Action.noOp()));

        // Act
        final var actual = target.getNavigationAction(ScreenName.of("unknown"), ScreenName.of("target"));

        // Assert
        assertTrue(actual.isEmpty());
    }

    @Test
    void getNavigationAction_givenUnknownTarget_isEmpty() {
        // Arrange
        final NavigationTable target = createTarget();
        target.addNavigation(new Navigation(ScreenName.of("source1"), ScreenName.of("target"), Action.noOp()));

        // Act
        final var actual = target.getNavigationAction(ScreenName.of("source1"), ScreenName.of("unknown"));

        // Assert
        assertTrue(actual.isEmpty());
    }

    @Test
    void getNavigationAction_givenKnownValidation_isPresent() {
        // Arrange
        final NavigationTable target = createTarget();
        target.addNavigation(new Navigation(ScreenName.of("source1"), ScreenName.of("target"), Action.noOp()));

        // Act
        final var actual = target.getNavigationAction(ScreenName.of("source1"), ScreenName.of("target"));

        // Assert
        assertTrue(actual.isPresent());
    }
}