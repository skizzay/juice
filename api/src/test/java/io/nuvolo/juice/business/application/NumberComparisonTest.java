package io.nuvolo.juice.business.application;

import io.nuvolo.juice.business.model.FieldName;
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

    @Test
    void notEqualTo_fail() {
        final var fieldName1 = new FieldName("fieldName1");
        final var actual = 1;
        final var expected = actual;
        final var message = "Field fieldName1[1] is equal to 1";
        final var target = NumberComparison.Type.NOT_EQUAL_TO;

        final var exception = assertThrows(AssertionError.class, () -> target.fail(fieldName1, actual, expected));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void equalTo_fail() {
        final var fieldName1 = new FieldName("fieldName1");
        final var actual = 1;
        final var expected = 2;
        final var message = "Field fieldName1[1] is not equal to 2";
        final var target = NumberComparison.Type.EQUAL_TO;

        final var exception = assertThrows(AssertionError.class, () -> target.fail(fieldName1, actual, expected));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void greaterThan_fail() {
        final var fieldName1 = new FieldName("fieldName1");
        final var actual = 1;
        final var expected = 1;
        final var message = "Field fieldName1[1] is not greater than 1";
        final var target = NumberComparison.Type.GREATER_THAN;

        final var exception = assertThrows(AssertionError.class, () -> target.fail(fieldName1, actual, expected));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void lessThan_fail() {
        final var fieldName1 = new FieldName("fieldName1");
        final var actual = 1;
        final var expected = 1;
        final var message = "Field fieldName1[1] is not less than 1";
        final var target = NumberComparison.Type.LESS_THAN;

        final var exception = assertThrows(AssertionError.class, () -> target.fail(fieldName1, actual, expected));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void greaterThanOrEqualTo_fail() {
        final var fieldName1 = new FieldName("fieldName1");
        final var actual = 1;
        final var expected = 1;
        final var message = "Field fieldName1[1] is not greater than or equal to 1";
        final var target = NumberComparison.Type.GREATER_THAN_OR_EQUAL_TO;

        final var exception = assertThrows(AssertionError.class, () -> target.fail(fieldName1, actual, expected));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void lessThanOrEqualTo_fail() {
        final var fieldName1 = new FieldName("fieldName1");
        final var actual = 1;
        final var expected = 1;
        final var message = "Field fieldName1[1] is not less than or equal to 1";
        final var target = NumberComparison.Type.LESS_THAN_OR_EQUAL_TO;

        final var exception = assertThrows(AssertionError.class, () -> target.fail(fieldName1, actual, expected));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void notEqualTo_fail2() {
        final var fieldName1 = new FieldName("fieldName1");
        final var fieldName2 = new FieldName("fieldName2");
        final var actual = 1;
        final var expected = actual;
        final var message = "Field fieldName1[1] is equal to field fieldName2[1]";
        final var target = NumberComparison.Type.NOT_EQUAL_TO;

        final var exception = assertThrows(AssertionError.class, () -> target.fail(fieldName1, actual, fieldName2, expected));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void equalTo_fail2() {
        final var fieldName1 = new FieldName("fieldName1");
        final var fieldName2 = new FieldName("fieldName2");
        final var actual = 1;
        final var expected = 2;
        final var message = "Field fieldName1[1] is not equal to field fieldName2[2]";
        final var target = NumberComparison.Type.EQUAL_TO;

        final var exception = assertThrows(AssertionError.class, () -> target.fail(fieldName1, actual, fieldName2, expected));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void greaterThan_fail2() {
        final var fieldName1 = new FieldName("fieldName1");
        final var fieldName2 = new FieldName("fieldName2");
        final var actual = 1;
        final var expected = 1;
        final var message = "Field fieldName1[1] is not greater than field fieldName2[1]";
        final var target = NumberComparison.Type.GREATER_THAN;

        final var exception = assertThrows(AssertionError.class, () -> target.fail(fieldName1, actual, fieldName2, expected));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void lessThan_fail2() {
        final var fieldName1 = new FieldName("fieldName1");
        final var fieldName2 = new FieldName("fieldName2");
        final var actual = 1;
        final var expected = 1;
        final var message = "Field fieldName1[1] is not less than field fieldName2[1]";
        final var target = NumberComparison.Type.LESS_THAN;

        final var exception = assertThrows(AssertionError.class, () -> target.fail(fieldName1, actual, fieldName2, expected));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void greaterThanOrEqualTo_fail2() {
        final var fieldName1 = new FieldName("fieldName1");
        final var fieldName2 = new FieldName("fieldName2");
        final var actual = 1;
        final var expected = 1;
        final var message = "Field fieldName1[1] is not greater than or equal to field fieldName2[1]";
        final var target = NumberComparison.Type.GREATER_THAN_OR_EQUAL_TO;

        final var exception = assertThrows(AssertionError.class, () -> target.fail(fieldName1, actual, fieldName2, expected));

        assertEquals(message, exception.getMessage());
    }

    @Test
    void lessThanOrEqualTo_fail2() {
        final var fieldName1 = new FieldName("fieldName1");
        final var fieldName2 = new FieldName("fieldName2");
        final var actual = 1;
        final var expected = 1;
        final var message = "Field fieldName1[1] is not less than or equal to field fieldName2[1]";
        final var target = NumberComparison.Type.LESS_THAN_OR_EQUAL_TO;

        final var exception = assertThrows(AssertionError.class, () -> target.fail(fieldName1, actual, fieldName2, expected));

        assertEquals(message, exception.getMessage());
    }
}