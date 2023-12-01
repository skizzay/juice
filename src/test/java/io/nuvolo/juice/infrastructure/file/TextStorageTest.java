package io.nuvolo.juice.infrastructure.file;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextStorageTest {
    private final BoxDimensions dimensions = new BoxDimensions(80, 24);

    private TextStorage createTarget() {
        return new TextStorage(dimensions);
    }

    @Test
    void setText() {
        final TextStorage target = createTarget();
        final String text = "Hello, World!";
        final Point position = new Point(0, 0);
        final BoxDimensions dimensions = new BoxDimensions(13, 1);

        target.setText(text, position, dimensions);

        assertEquals(text, target.getText(position, dimensions));
    }
}