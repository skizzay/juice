package io.nuvolo.juice.business.application;

import io.cucumber.datatable.DataTable;
import io.nuvolo.juice.business.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JuiceStepDefinitionsTest {
    @Mock
    private UserInterface userInterface;
    @Mock
    private Screen screen;
    @Mock
    private ReadWriteField field;
    @Mock
    private DataTable dataTable;

    private JuiceStepDefinitions createTarget() {
        return new JuiceStepDefinitions(userInterface);
    }

    @Test
    void setField_givenKnownField_returnsField() {
        // Arrange
        final JuiceStepDefinitions target = createTarget();
        final FieldName fieldName = FieldName.of("fieldName");
        when(userInterface.getCurrentScreen()).thenReturn(screen);
        when(screen.getWriteableField(fieldName)).thenReturn(Optional.of(field));

        // Act
        target.setField(fieldName.name(), "value");

        // Assert
        verify(field, times(1)).setValue("value");
    }

    @Test
    void setField_givenUnknownField_throws() {
        // Arrange
        final JuiceStepDefinitions target = createTarget();
        final String fieldName = "fieldName";
        when(userInterface.getCurrentScreen()).thenReturn(screen);
        when(screen.getScreenName()).thenReturn(ScreenName.of("screenName"));

        // Act
        final var exception = assertThrows(IllegalArgumentException.class, () -> target.setField(fieldName, "value"));

        // Assert
        assertEquals("Field fieldName not found on the screenName screen", exception.getMessage());
    }

    @Test
    void setFieldFromScreen_givenKnownScreenAndKnownField_returnsField() {
        // Arrange
        final JuiceStepDefinitions target = createTarget();
        final String fieldName = "fieldName";
        final String screenFieldName = "screenFieldName";
        final String screenName = "screenName";
        when(userInterface.getCurrentScreen()).thenReturn(screen);
        when(userInterface.getPreviousState(ScreenName.of(screenName))).thenReturn(Optional.of(Map.of(FieldName.of(screenFieldName), "value")));
        when(screen.getWriteableField(FieldName.of(fieldName))).thenReturn(Optional.of(field));

        // Act
        target.setFieldFromScreen(fieldName, screenName, screenFieldName);

        // Assert
        verify(field, times(1)).setValue("value");
    }

    @Test
    void setFieldFromScreen_givenUnknownScreen_throws() {
        // Arrange
        final JuiceStepDefinitions target = createTarget();
        final String fieldName = "fieldName";
        final String screenFieldName = "screenFieldName";
        final String screenName = "screenName";
        when(userInterface.getPreviousState(ScreenName.of(screenName))).thenReturn(Optional.empty());

        // Act
        final var exception = assertThrows(IllegalArgumentException.class, () -> target.setFieldFromScreen(fieldName, screenName, screenFieldName));

        // Assert
        assertEquals("Screen screenName not found", exception.getMessage());
    }

    @Test
    void setFieldFromScreen_givenUnknownField_throws() {
        // Arrange
        final JuiceStepDefinitions target = createTarget();
        final String fieldName = "fieldName";
        final String screenFieldName = "screenFieldName";
        final String screenName = "screenName";
        when(userInterface.getCurrentScreen()).thenReturn(screen);
        when(screen.getScreenName()).thenReturn(ScreenName.of(screenName));
        when(userInterface.getPreviousState(ScreenName.of(screenName))).thenReturn(Optional.of(Map.of(FieldName.of(screenFieldName), "value")));

        // Act
        final var exception = assertThrows(IllegalArgumentException.class, () -> target.setFieldFromScreen(fieldName, screenName, screenFieldName));

        // Assert
        assertEquals("Field fieldName not found on the screenName screen", exception.getMessage());
    }

    @Test
    void setFieldFromScreen_givenUnknownScreenField_throws() {
        // Arrange
        final JuiceStepDefinitions target = createTarget();
        final String fieldName = "fieldName";
        final String screenFieldName = "screenFieldName";
        final String screenName = "previousScreenName";
        when(userInterface.getPreviousState(ScreenName.of(screenName))).thenReturn(Optional.of(Map.of()));

        // Act
        final var exception = assertThrows(IllegalArgumentException.class, () -> target.setFieldFromScreen(fieldName, screenName, screenFieldName));

        // Assert
        assertEquals("Field screenFieldName not found on the previousScreenName screen", exception.getMessage());
    }

    @Test
    void checkField_givenKnownFieldAndExpectedValue_doesNotThrow() {
        // Arrange
        final JuiceStepDefinitions target = createTarget();
        final String fieldName = "fieldName";
        final String actualValue = "1";
        final BigDecimal expectedValue = new BigDecimal("2");
        when(userInterface.getCurrentScreen()).thenReturn(screen);
        when(screen.getReadableField(FieldName.of(fieldName))).thenReturn(Optional.of(field));
        when(field.getValue()).thenReturn(actualValue);

        // Act & Assert
        assertDoesNotThrow(() -> target.checkField(fieldName, NumberComparison.Type.LESS_THAN, expectedValue));
    }

    @Test
    void checkField_givenKnownFieldAndUnexpectedValue_throws() {
        // Arrange
        final JuiceStepDefinitions target = createTarget();
        final String fieldName = "fieldName";
        final String actualValue = "1";
        final BigDecimal expectedValue = new BigDecimal("0");
        when(userInterface.getCurrentScreen()).thenReturn(screen);
        when(screen.getReadableField(FieldName.of(fieldName))).thenReturn(Optional.of(field));
        when(field.getValue()).thenReturn(actualValue);

        // Act
        final var exception = assertThrows(AssertionError.class, () -> target.checkField(fieldName, NumberComparison.Type.LESS_THAN, expectedValue));

        // Assert
        assertEquals("Field fieldName[1] is not less than 0", exception.getMessage());
    }

    @Test
    void checkField_givenKnownFieldAndExpectedFieldValue_doesNotThrow() {
        // Arrange
        final JuiceStepDefinitions target = createTarget();
        final String actualFieldName = "actualFieldName";
        final String expectedFieldName = "expectedFieldName";
        final String actualValue = "1";
        final String expectedValue = "2";
        final ReadableField expectedField = mock(ReadableField.class);
        when(userInterface.getCurrentScreen()).thenReturn(screen);
        when(screen.getReadableField(FieldName.of(expectedFieldName))).thenReturn(Optional.of(expectedField));
        when(screen.getReadableField(FieldName.of(actualFieldName))).thenReturn(Optional.of(field));
        when(expectedField.getValue()).thenReturn(expectedValue);
        when(field.getValue()).thenReturn(actualValue);

        // Act & Assert
        assertDoesNotThrow(() -> target.checkField(actualFieldName, NumberComparison.Type.LESS_THAN, expectedFieldName));
    }

    @Test
    void checkField_givenKnownFieldAndUnexpectedFieldValue_doesNotThrow() {
        // Arrange
        final JuiceStepDefinitions target = createTarget();
        final String actualFieldName = "actualFieldName";
        final String expectedFieldName = "expectedFieldName";
        final String actualValue = "1";
        final String expectedValue = "0";
        final ReadableField expectedField = mock(ReadableField.class);
        when(userInterface.getCurrentScreen()).thenReturn(screen);
        when(screen.getReadableField(FieldName.of(expectedFieldName))).thenReturn(Optional.of(expectedField));
        when(screen.getReadableField(FieldName.of(actualFieldName))).thenReturn(Optional.of(field));
        when(expectedField.getValue()).thenReturn(expectedValue);
        when(field.getValue()).thenReturn(actualValue);

        // Act
        final var exception = assertThrows(AssertionError.class, () -> target.checkField(actualFieldName, NumberComparison.Type.LESS_THAN, expectedFieldName));

        // Assert
        assertEquals("Field actualFieldName[1] is not less than field expectedFieldName[0]", exception.getMessage());
    }

    @Test
    void checkField_givenKnownFieldAndExpectedString_doesNotThrow() {
        // Arrange
        final JuiceStepDefinitions target = createTarget();
        final String fieldName = "fieldName";
        final String actualValue = "value";
        final String expectedValue = "value";
        when(userInterface.getCurrentScreen()).thenReturn(screen);
        when(screen.getReadableField(FieldName.of(fieldName))).thenReturn(Optional.of(field));
        when(field.getValue()).thenReturn(actualValue);

        // Act & Assert
        assertDoesNotThrow(() -> target.checkField(fieldName, expectedValue));
    }

    @Test
    void checkField_givenKnownFieldAndUnexpectedString_throws() {
        // Arrange
        final JuiceStepDefinitions target = createTarget();
        final String fieldName = "fieldName";
        final String actualValue = "value";
        final String expectedValue = "other value";
        when(userInterface.getCurrentScreen()).thenReturn(screen);
        when(screen.getReadableField(FieldName.of(fieldName))).thenReturn(Optional.of(field));
        when(field.getValue()).thenReturn(actualValue);

        // Act
        final var exception = assertThrows(AssertionError.class, () -> target.checkField(fieldName, expectedValue));

        // Assert
        assertEquals("Field fieldName[value] is not equal to other value", exception.getMessage());
    }

    @Test
    void checkField_givenKnownFieldAndExpectedBoolean_doesNotThrow() {
        // Arrange
        final JuiceStepDefinitions target = createTarget();
        final String fieldName = "fieldName";
        final String actualValue = "true";
        final boolean expectedValue = true;
        when(userInterface.getCurrentScreen()).thenReturn(screen);
        when(screen.getReadableField(FieldName.of(fieldName))).thenReturn(Optional.of(field));
        when(field.getValue()).thenReturn(actualValue);

        // Act & Assert
        assertDoesNotThrow(() -> target.checkField(fieldName, expectedValue));
    }

    @Test
    void checkField_givenKnownFieldAndUnexpectedBoolean_throws() {
        // Arrange
        final JuiceStepDefinitions target = createTarget();
        final String fieldName = "fieldName";
        final String actualValue = "true";
        final boolean expectedValue = false;
        when(userInterface.getCurrentScreen()).thenReturn(screen);
        when(screen.getReadableField(FieldName.of(fieldName))).thenReturn(Optional.of(field));
        when(field.getValue()).thenReturn(actualValue);

        // Act
        final var exception = assertThrows(AssertionError.class, () -> target.checkField(fieldName, expectedValue));

        // Assert
        assertEquals("Field fieldName[true] is not equal to false", exception.getMessage());
    }

    @Test
    void checkField_givenUnknownFieldName_throws() {
        // Arrange
        final JuiceStepDefinitions target = createTarget();
        final String fieldName = "fieldName";
        when(userInterface.getCurrentScreen()).thenReturn(screen);
        when(screen.getScreenName()).thenReturn(ScreenName.of("screenName"));
        when(screen.getReadableField(FieldName.of(fieldName))).thenReturn(Optional.empty());

        // Act
        final var exception = assertThrows(IllegalArgumentException.class, () -> target.checkField(fieldName, "value"));

        // Assert
        assertEquals("Field fieldName not found on the screenName screen", exception.getMessage());
    }

    @Test
    void checkField_givenUnknownScreenName_throws() {
        // Arrange
        final JuiceStepDefinitions target = createTarget();
        final String fieldName = "fieldName";
        final String screenName = "screenName";
        when(userInterface.getCurrentScreen()).thenReturn(screen);
        when(screen.getReadableField(FieldName.of(fieldName))).thenReturn(Optional.empty());
        when(screen.getScreenName()).thenReturn(ScreenName.of(screenName));

        // Act
        final var exception = assertThrows(IllegalArgumentException.class, () -> target.checkField(fieldName, "value"));

        // Assert
        assertEquals("Field fieldName not found on the screenName screen", exception.getMessage());
    }

    @Test
    void checkField_givenUnknownPreviousFieldName_throws() {
        // Arrange
        final JuiceStepDefinitions target = createTarget();
        final String fieldName = "fieldName";
        final String screenName = "screenName";
        final String previousScreenName = "previousScreenName";
        when(userInterface.getPreviousState(ScreenName.of(previousScreenName))).thenReturn(Optional.of(Map.of()));

        // Act
        final var exception = assertThrows(IllegalArgumentException.class, () -> target.checkField(fieldName, previousScreenName, "previousFieldName"));

        // Assert
        assertEquals("Field previousFieldName not found on the previousScreenName screen", exception.getMessage());
    }

    @Test
    void checkField_givenKnownFieldPreviousScreenWithFieldName_doesNotThrow() {
        // Arrange
        final JuiceStepDefinitions target = createTarget();
        final String fieldName = "fieldName";
        final String screenName = "screenName";
        final String previousScreenName = "previousScreenName";
        final String previousFieldName = "previousFieldName";
        final String actualValue = "value";
        final String expectedValue = "value";
        final ReadableField expectedField = mock(ReadableField.class);
        when(userInterface.getCurrentScreen()).thenReturn(screen);
        when(screen.getReadableField(FieldName.of(fieldName))).thenReturn(Optional.of(field));
        when(userInterface.getPreviousState(ScreenName.of(previousScreenName))).thenReturn(Optional.of(Map.of(FieldName.of(previousFieldName), expectedValue)));
        when(field.getValue()).thenReturn(actualValue);

        // Act & Assert
        assertDoesNotThrow(() -> target.checkField(fieldName, previousScreenName, previousFieldName));
    }

    @Test
    void checkFieldMatchesRegex_givenKnownFieldAndExpectedRegex_doesNotThrow() {
        // Arrange
        final JuiceStepDefinitions target = createTarget();
        final String fieldName = "fieldName";
        final String actualValue = "value";
        final String regex = ".*";
        when(userInterface.getCurrentScreen()).thenReturn(screen);
        when(screen.getReadableField(FieldName.of(fieldName))).thenReturn(Optional.of(field));
        when(field.getValue()).thenReturn(actualValue);

        // Act & Assert
        assertDoesNotThrow(() -> target.checkFieldMatchesRegex(fieldName, regex));
    }

    @Test
    void checkFieldMatchesRegex_givenKnownFieldAndUnexpectedRegex_throws() {
        // Arrange
        final JuiceStepDefinitions target = createTarget();
        final String fieldName = "fieldName";
        final String actualValue = "value";
        final String regex = "other value";
        when(userInterface.getCurrentScreen()).thenReturn(screen);
        when(screen.getReadableField(FieldName.of(fieldName))).thenReturn(Optional.of(field));
        when(field.getValue()).thenReturn(actualValue);

        // Act
        final var exception = assertThrows(AssertionError.class, () -> target.checkFieldMatchesRegex(fieldName, regex));

        // Assert
        assertEquals("Field fieldName[value] does not match regex:other value", exception.getMessage());
    }

    @Test
    void checkFieldNotMatchesRegex_givenKnownFieldAndExpectedRegex_doesNotThrow() {
        // Arrange
        final JuiceStepDefinitions target = createTarget();
        final String fieldName = "fieldName";
        final String actualValue = "value";
        final String regex = "other value";
        when(userInterface.getCurrentScreen()).thenReturn(screen);
        when(screen.getReadableField(FieldName.of(fieldName))).thenReturn(Optional.of(field));
        when(field.getValue()).thenReturn(actualValue);

        // Act & Assert
        assertDoesNotThrow(() -> target.checkFieldNotMatchesRegex(fieldName, regex));
    }

    @Test
    void checkFieldNotMatchesRegex_givenKnownFieldAndUnexpectedRegex_throws() {
        // Arrange
        final JuiceStepDefinitions target = createTarget();
        final String fieldName = "fieldName";
        final String actualValue = "value";
        final String regex = ".*";
        when(userInterface.getCurrentScreen()).thenReturn(screen);
        when(screen.getReadableField(FieldName.of(fieldName))).thenReturn(Optional.of(field));
        when(field.getValue()).thenReturn(actualValue);

        // Act
        final var exception = assertThrows(AssertionError.class, () -> target.checkFieldNotMatchesRegex(fieldName, regex));

        // Assert
        assertEquals("Field fieldName[value] matches regex:.*", exception.getMessage());
    }

    @Test
    void checkForBlankField_givenKnownFieldWithoutBlankValue_throws() {
        // Arrange
        final JuiceStepDefinitions target = createTarget();
        final String fieldName = "fieldName";
        final String actualValue = "value";
        when(userInterface.getCurrentScreen()).thenReturn(screen);
        when(screen.getReadableField(FieldName.of(fieldName))).thenReturn(Optional.of(field));
        when(field.getValue()).thenReturn(actualValue);

        // Act
        final var exception = assertThrows(AssertionError.class, () -> target.checkForBlankField(fieldName));

        // Assert
        assertEquals("Field fieldName[value] is not blank", exception.getMessage());
    }

    @Test
    void checkForBlankField_givenKnownFieldWithBlankValue_doesNotThrow() {
        // Arrange
        final JuiceStepDefinitions target = createTarget();
        final String fieldName = "fieldName";
        final String actualValue = "";
        when(userInterface.getCurrentScreen()).thenReturn(screen);
        when(screen.getReadableField(FieldName.of(fieldName))).thenReturn(Optional.of(field));
        when(field.getValue()).thenReturn(actualValue);

        // Act & Assert
        assertDoesNotThrow(() -> target.checkForBlankField(fieldName));
    }

    @Test
    void checkForNotBlankField_givenKnownFieldWithBlankValue_throws() {
        // Arrange
        final JuiceStepDefinitions target = createTarget();
        final String fieldName = "fieldName";
        final String actualValue = "";
        when(userInterface.getCurrentScreen()).thenReturn(screen);
        when(screen.getReadableField(FieldName.of(fieldName))).thenReturn(Optional.of(field));
        when(field.getValue()).thenReturn(actualValue);

        // Act
        final var exception = assertThrows(AssertionError.class, () -> target.checkForNotBlankField(fieldName));

        // Assert
        assertEquals("Field fieldName is blank", exception.getMessage());
    }

    @Test
    void checkForNotBlankField_givenKnownFieldWithoutBlankValue_doesNotThrow() {
        // Arrange
        final JuiceStepDefinitions target = createTarget();
        final String fieldName = "fieldName";
        final String actualValue = "value";
        when(userInterface.getCurrentScreen()).thenReturn(screen);
        when(screen.getReadableField(FieldName.of(fieldName))).thenReturn(Optional.of(field));
        when(field.getValue()).thenReturn(actualValue);

        // Act & Assert
        assertDoesNotThrow(() -> target.checkForNotBlankField(fieldName));
    }

    @Test
    void checkScreen_givenExpectedScreen_doesNotThrow() {
        // Arrange
        final JuiceStepDefinitions target = createTarget();
        final String screenName = "screenName";
        when(userInterface.getCurrentScreen()).thenReturn(screen);
        when(screen.getScreenName()).thenReturn(ScreenName.of(screenName));

        // Act & Assert
        assertDoesNotThrow(() -> target.checkScreen(screenName));
    }

    @Test
    void checkScreen_givenUnexpectedScreen_throws() {
        // Arrange
        final JuiceStepDefinitions target = createTarget();
        final String screenName = "screenName";
        when(userInterface.getCurrentScreen()).thenReturn(screen);
        when(screen.getScreenName()).thenReturn(ScreenName.of("otherScreenName"));

        // Act
        final var exception = assertThrows(AssertionError.class, () -> target.checkScreen(screenName));

        // Assert
        assertEquals("Screen name is otherScreenName but should be screenName", exception.getMessage());
    }

    @Test
    void checkTable_givenUnknownTable_throws() {
        // Arrange
        final JuiceStepDefinitions target = createTarget();
        final String tableName = "tableName";
        when(userInterface.getCurrentScreen()).thenReturn(screen);
        when(screen.getScreenName()).thenReturn(ScreenName.of("screenName"));

        // Act
        final var exception = assertThrows(IllegalArgumentException.class, () -> target.checkTable(tableName, TableMatching.Type.ORDERED, dataTable));

        // Assert
        assertEquals("Table tableName not found on the screenName screen", exception.getMessage());
    }
}