package io.nuvolo.juice.business.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActionTest {
    @Mock private Action action1;
    @Mock private Action action2;
    @Mock private Screen screen;

    private Action createTarget() {
        return Action.of(action1, action2);
    }

    @Test
    void getName() {
        // Arrange
        when(action1.getName()).thenReturn(new ActionName("action1"));
        when(action2.getName()).thenReturn(new ActionName("action2"));
        final Action target = createTarget();

        // Act
        final ActionName result = target.getName();

        // Assert
        assertEquals(new ActionName("Action Sequence[action1,action2]"), result);
    }

    @Test
    void execute() {
        // Arrange
        final Action target = createTarget();

        // Act
        target.execute(screen);

        // Assert
        verify(action1, times(1)).execute(screen);
        verify(action2, times(1)).execute(screen);
    }

    @Test
    void of_givenNoActions_throwsIllegalArgumentException() {
        // Arrange
        // Act & Assert
        assertThrows(IllegalArgumentException.class, Action::of);
    }
}