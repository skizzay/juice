package io.nuvolo.juice.infrastructure.file;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoxDimensionsTest {
    @Test
    void testConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new BoxDimensions(0, 1));
        assertThrows(IllegalArgumentException.class, () -> new BoxDimensions(1, 0));
        assertThrows(IllegalArgumentException.class, () -> new BoxDimensions(0, 0));
    }

    @Test
    void testSize() {
        assertEquals(1, new BoxDimensions(1, 1).size());
        assertEquals(2, new BoxDimensions(1, 2).size());
        assertEquals(2, new BoxDimensions(2, 1).size());
        assertEquals(4, new BoxDimensions(2, 2).size());
    }
}