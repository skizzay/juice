package io.nuvolo.juice.business.model;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class ValueValidatorTest {

    @Test
    void alwaysValid() {
        // Arrange
        final ValueValidator validator = ValueValidator.alwaysValid();

        // Act & Assert
        assertDoesNotThrow(() -> validator.validate("any value"));
    }

    @Test
    void all_allAreValid_thenDoesNotThrow() {
        // Arrange
        final ValueValidator validator = ValueValidator.all(mock -> {}, mock -> {}, mock -> {});

        // Act & Assert
        assertDoesNotThrow(() -> validator.validate("any value"));
    }

    @Test
    void all_whenInvalid_thenDoesNotThrow() {
        // Arrange
        final ValueValidator validator = ValueValidator.all(mock -> {}, mock -> {}, mock -> {throw new IllegalArgumentException();});

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> validator.validate("any value"));
    }

    @Test
    void any_allAreValid_thenDoesNotThrow() {
        // Arrange
        final ValueValidator validator = ValueValidator.any(mock -> {}, mock -> {}, mock -> {});

        // Act & Assert
        assertDoesNotThrow(() -> validator.validate("any value"));
    }

    @Test
    void any_oneIsValid_thenDoesNotThrow() {
        // Arrange
        final ValueValidator validator = ValueValidator.any(mock -> {throw new IllegalArgumentException();}, mock -> {});

        // Act & Assert
        assertDoesNotThrow(() -> validator.validate("any value"));
    }

    @Test
    void any_allAreInvalid_thenDoesNotThrow() {
        // Arrange
        final ValueValidator validator = ValueValidator.any(mock -> {throw new IllegalArgumentException();}, mock -> {throw new IllegalArgumentException();});

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> validator.validate("any value"));
    }

    @Test
    void notEmpty_givenEmptyString_throwsException() {
        // Arrange
        final ValueValidator target = ValueValidator.notEmpty();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> target.validate(""));
    }

    @Test
    void notEmpty_givenNull_throwsException() {
        // Arrange
        final ValueValidator target = ValueValidator.notEmpty();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> target.validate(null));
    }

    @Test
    void notEmpty_givenPopulatedString_doesNotThrowException() {
        // Arrange
        final ValueValidator target = ValueValidator.notEmpty();

        // Act & Assert
        assertDoesNotThrow(() -> target.validate("Hello, World!"));
    }

    @Test
    void notBlank_givenEmptyString_throwsException() {
        // Arrange
        final ValueValidator target = ValueValidator.notBlank();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> target.validate(""));
    }

    @Test
    void notBlank_givenNull_throwsException() {
        // Arrange
        final ValueValidator target = ValueValidator.notBlank();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> target.validate(null));
    }

    @Test
    void notBlank_givenPopulatedString_doesNotThrowException() {
        // Arrange
        final ValueValidator target = ValueValidator.notBlank();

        // Act & Assert
        assertDoesNotThrow(() -> target.validate("Hello, World!"));
    }

    @Test
    void notBlank_givenBunchOfWhitespaceString_throwsException() {
        // Arrange
        final ValueValidator target = ValueValidator.notBlank();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> target.validate("    "));
    }

    @Test
    void notNull_givenNull_throwsException() {
        // Arrange
        final ValueValidator target = ValueValidator.notNull();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> target.validate(null));
    }

    @Test
    void notNull_givenBlank_doesNotThrowException() {
        // Arrange
        final ValueValidator target = ValueValidator.notNull();

        // Act & Assert
        assertDoesNotThrow(() -> target.validate(""));
    }

    @Test
    void isNumeric_givenNumber_doesNotThrow() {
        // Arrange
        final ValueValidator target = ValueValidator.isNumeric();

        // Act & Assert
        assertDoesNotThrow(() -> target.validate("123"));
    }

    @Test
    void isNumeric_givenNonNumber_throwsException() {
        // Arrange
        final ValueValidator target = ValueValidator.isNumeric();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> target.validate("abc"));
    }

    @Test
    void isInteger_givenInteger_doesNotThrow() {
        // Arrange
        final ValueValidator target = ValueValidator.isInteger();

        // Act & Assert
        assertDoesNotThrow(() -> target.validate("123"));
    }

    @Test
    void isInteger_givenNonInteger_throwsException() {
        // Arrange
        final ValueValidator target = ValueValidator.isInteger();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> target.validate("123.45"));
    }

    @Test
    void isBoolean_givenBoolean_doesNotThrow() {
        // Arrange
        final ValueValidator target = ValueValidator.isBoolean();

        // Act & Assert
        assertDoesNotThrow(() -> target.validate("true"));
    }

    @Test
    void isBoolean_givenNonBoolean_throwsException() {
        // Arrange
        final ValueValidator target = ValueValidator.isBoolean();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> target.validate("123"));
    }

    @Test
    void equals_givenEqualStrings_doesNotThrow() {
        // Arrange
        final ValueValidator target = ValueValidator.equals("abc");

        // Act & Assert
        assertDoesNotThrow(() -> target.validate("abc"));
    }

    @Test
    void equals_givenNonEqualStrings_throwsException() {
        // Arrange
        final ValueValidator target = ValueValidator.equals("abc");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> target.validate("def"));
    }

    @Test
    void equalsIgnoreCase_givenEqualStrings_doesNotThrow() {
        // Arrange
        final ValueValidator target = ValueValidator.equalsIgnoreCase("abc");

        // Act & Assert
        assertDoesNotThrow(() -> target.validate("abc"));
    }

    @Test
    void equalsIgnoreCase_givenEqualStringsDifferentCase_doesNotThrow() {
        // Arrange
        final ValueValidator target = ValueValidator.equalsIgnoreCase("abc");

        // Act & Assert
        assertDoesNotThrow(() -> target.validate("ABC"));
    }

    @Test
    void equalsIgnoreCase_givenNonEqualStrings_throwsException() {
        // Arrange
        final ValueValidator target = ValueValidator.equalsIgnoreCase("abc");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> target.validate("def"));
    }

    @Test
    void notEquals_givenNonEqualStrings_doesNotThrow() {
        // Arrange
        final ValueValidator target = ValueValidator.notEquals("abc");

        // Act & Assert
        assertDoesNotThrow(() -> target.validate("def"));
    }

    @Test
    void notEquals_givenEqualStrings_throwsException() {
        // Arrange
        final ValueValidator target = ValueValidator.notEquals("abc");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> target.validate("abc"));
    }

    @Test
    void notEqualsIgnoreCase_givenNonEqualStrings_doesNotThrow() {
        // Arrange
        final ValueValidator target = ValueValidator.notEqualsIgnoreCase("abc");

        // Act & Assert
        assertDoesNotThrow(() -> target.validate("def"));
    }

    @Test
    void notEqualsIgnoreCase_givenEqualStringsDifferentCase_throwsException() {
        // Arrange
        final ValueValidator target = ValueValidator.notEqualsIgnoreCase("abc");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> target.validate("ABC"));
    }

    @Test
    void notEqualsIgnoreCase_givenEqualStrings_throwsException() {
        // Arrange
        final ValueValidator target = ValueValidator.notEqualsIgnoreCase("abc");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> target.validate("abc"));
    }

    @Test
    void matches_givenMatchingPattern_doesNotThrow() {
        // Arrange
        final ValueValidator target = ValueValidator.matches(Pattern.compile("abc"));

        // Act & Assert
        assertDoesNotThrow(() -> target.validate("abc"));
    }

    @Test
    void matches_givenNonMatchingPattern_throwsException() {
        // Arrange
        final ValueValidator target = ValueValidator.matches(Pattern.compile("abc"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> target.validate("def"));
    }

    @Test
    void matches_givenNull_throwsException() {
        // Arrange
        final ValueValidator target = ValueValidator.matches(Pattern.compile("abc"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> target.validate(null));
    }

    @Test
    void matches_givenEmptyString_throwsException() {
        // Arrange
        final ValueValidator target = ValueValidator.matches(Pattern.compile("abc"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> target.validate(""));
    }

    @Test
    void matches_givenEmptyPattern_throwsException() {
        // Arrange
        final ValueValidator target = ValueValidator.matches(Pattern.compile(""));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> target.validate("abc"));
    }

    @Test
    void matches_givenEmptyPatternAndEmptyString_doesNotThrow() {
        // Arrange
        final ValueValidator target = ValueValidator.matches(Pattern.compile(""));

        // Act & Assert
        assertDoesNotThrow(() -> target.validate(""));
    }
}