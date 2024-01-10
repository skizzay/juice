package io.nuvolo.juice.infrastructure.text;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TextStorageTest {
    private final BoxDimensions dimensions = new BoxDimensions(80, 24);

    private TextStorage createTarget() {
        return new MemoryTextStorage(dimensions);
    }

    @Test
    void setText_textCanBeRetrieved() {
        // Arrange
        final TextStorage target = createTarget();
        final String text = "Hello, World!";
        final Point position = new Point(0, 0);
        final BoxDimensions dimensions = new BoxDimensions(13, 1);

        // Act
        target.setText(text, position, dimensions);

        // Assert
        assertEquals(text, target.getText(position, dimensions));
    }

    @Test
    void setText_textSizeTooBig_throwsException() {
        // Arrange
        final var target = createTarget();
        final var text = "Text too big";
        final var position = new Point(0, 0);
        final var dimensions = new BoxDimensions(1, 1);

        // Act
        final var exception = assertThrows(IllegalArgumentException.class, () -> target.setText(text, position, dimensions));

        // Assert
        assertEquals("Value is too large for table cell", exception.getMessage());
    }
}