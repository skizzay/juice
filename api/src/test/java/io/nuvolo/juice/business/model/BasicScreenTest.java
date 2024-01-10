package io.nuvolo.juice.business.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicScreenTest {
    private final ActionName actionName = new ActionName("actionName");
    private final FieldName readableFieldName = new FieldName("readableFieldName");
    private final FieldName containerFieldName = new FieldName("containerFieldName");
    private final FieldName tableFieldName = new FieldName("tableFieldName");
    private final ScreenName screenName = new ScreenName("screenName");
    private final HashMap<ActionName, Action> actions = new HashMap<>();
    private final HashMap<FieldName, Field> fields = new HashMap<>();
    @Mock
    private UserInterface userInterface;
    @Mock
    private Action action;
    @Mock
    private Field field;
    @Mock
    private ReadableField readableField;
    @Mock
    private ContainerField containerField;
    @Mock
    private Table table;

    private BasicScreen createBasicScreen() {
        return new BasicScreen(screenName, actions, fields);
    }

    @BeforeEach
    void setUp() {
        actions.clear();
        actions.put(actionName, action);
        fields.clear();
        fields.put(new FieldName("fieldName"), field);
        fields.put(readableFieldName, readableField);
        fields.put(containerFieldName, containerField);
        fields.put(tableFieldName, table);
    }

    @Test
    void getName_returnsScreenName() {
        // Arrange
        final BasicScreen basicScreen = createBasicScreen();

        // Act
        final var name = basicScreen.getScreenName();

        // Assert
        assertEquals(screenName, name);
    }

    @Test
    void performAction_givenUnknownActionName_throws() {
        // Arrange
        actions.remove(actionName); // remove actionName from actions
        final BasicScreen target = createBasicScreen();

        // Act
        final var exception = assertThrows(IllegalArgumentException.class, () -> target.performAction(mock(UserInterface.class), actionName));

        // Assert
        assertEquals("Unknown request: " + actionName, exception.getMessage());
    }

    @Test
    void performAction_givenNullActionName_throws() {
        // Arrange
        final BasicScreen target = createBasicScreen();

        // Act
        final var exception = assertThrows(NullPointerException.class, () -> target.performAction(userInterface, null));

        // Assert
        assertEquals("Action name cannot be null", exception.getMessage());
    }

    @Test
    void performAction_givenNullUserInterface_throws() {
        // Arrange
        final BasicScreen target = createBasicScreen();

        // Act
        final var exception = assertThrows(NullPointerException.class, () -> target.performAction(null, actionName));

        // Assert
        assertEquals("User interface cannot be null", exception.getMessage());
    }

    @Test
    void performAction_givenActionName_executesAction() {
        // Arrange
        final BasicScreen target = createBasicScreen();

        // Act
        target.performAction(userInterface, actionName);

        // Assert
        verify(action, times(1)).execute(userInterface, target);
    }

    @Test
    void rememberState_givenFieldsWithReadableFields_remembersState() {
        // Arrange
        final BasicScreen target = createBasicScreen();
        when(readableField.getFieldName()).thenReturn(readableFieldName);
        when(readableField.getValue()).thenReturn("readable field value");
        final FieldName cellName = FieldName.of(tableFieldName + "[0,0]");
        when(table.getState()).thenReturn(Map.of(cellName, "cell value"));
        final FieldName subFieldName = FieldName.of(containerFieldName + ".subfield");
        when(containerField.getFieldName()).thenReturn(containerFieldName);
        when(containerField.getState()).thenReturn(Map.of(FieldName.of("subfield"), "container field value"));

        // Act
        target.rememberState();

        // Assert
        assertEquals(3, target.getLastState().size());
        assertEquals("readable field value", target.getLastState().get(readableFieldName));
        assertEquals("container field value", target.getLastState().get(subFieldName));
        assertEquals("cell value", target.getLastState().get(cellName));
    }
}