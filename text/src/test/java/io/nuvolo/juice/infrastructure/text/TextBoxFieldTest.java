package io.nuvolo.juice.infrastructure.text;

import io.nuvolo.juice.business.model.FieldName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TextBoxFieldTest {
    private final FieldName fieldName = new FieldName("fieldName");
    private final TextStorage storage = new MemoryTextStorage(new BoxDimensions(62, 160));
    private final Point position = new Point(0, 0);
    private final BoxDimensions dimensions = new BoxDimensions(5, 1);

    private TextBoxField createTarget() {
        return new TextBoxField(fieldName, storage, position, dimensions);
    }

    @Test
    void getFieldName() {
        final TextBoxField target = createTarget();
        assertEquals(fieldName, target.getFieldName());
    }

    @Test
    void getValue_isEmptyAfterConstruction() {
        // Arrange
        final TextBoxField target = createTarget();

        // Act
        final String actualValue = target.getValue();

        // Assert
        assertTrue(actualValue.isEmpty());
    }

    @Test
    void getValue_afterSettingValue_returnsSetValue() {
        // Arrange
        final TextBoxField target = createTarget();
        final String expectedValue = "value";
        target.setValue(expectedValue);

        // Act
        final String actualValue = target.getValue();

        // Assert
        assertEquals(expectedValue, actualValue);
    }
}