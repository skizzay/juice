package io.nuvolo.juice.business.application;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumberComparisonTest {
    @Test
    void testCompare() {
        assertTrue(NumberComparison.Type.EQUAL_TO.compare(1, 1));
        assertFalse(NumberComparison.Type.EQUAL_TO.compare(1, 2));
        assertTrue(NumberComparison.Type.GREATER_THAN.compare(2, 1));
        assertFalse(NumberComparison.Type.GREATER_THAN.compare(1, 2));
        assertTrue(NumberComparison.Type.LESS_THAN.compare(1, 2));
        assertFalse(NumberComparison.Type.LESS_THAN.compare(2, 1));
        assertTrue(NumberComparison.Type.GREATER_THAN_OR_EQUAL_TO.compare(2, 1));
        assertTrue(NumberComparison.Type.GREATER_THAN_OR_EQUAL_TO.compare(1, 1));
        assertFalse(NumberComparison.Type.GREATER_THAN_OR_EQUAL_TO.compare(1, 2));
        assertTrue(NumberComparison.Type.LESS_THAN_OR_EQUAL_TO.compare(1, 2));
        assertTrue(NumberComparison.Type.LESS_THAN_OR_EQUAL_TO.compare(1, 1));
        assertFalse(NumberComparison.Type.LESS_THAN_OR_EQUAL_TO.compare(2, 1));
        assertTrue(NumberComparison.Type.NOT_EQUAL_TO.compare(1, 2));
        assertFalse(NumberComparison.Type.NOT_EQUAL_TO.compare(1, 1));
    }
}