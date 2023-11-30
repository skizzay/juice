package io.nuvolo.juice.business.model;

import java.util.regex.Pattern;

public interface ValueValidator {
    void validate(String value);

    static ValueValidator alwaysValid() {
        return value -> {};
    }

    static ValueValidator all(ValueValidator... validators) {
        return value -> {
            for (final ValueValidator validator : validators) {
                validator.validate(value);
            }
        };
    }

    static ValueValidator any(ValueValidator... validators) {
        return value -> {
            IllegalArgumentException exception = null;
            for (final ValueValidator validator : validators) {
                try {
                    validator.validate(value);
                    return;
                } catch (IllegalArgumentException e) {
                    if (exception == null) {
                        exception = e;
                    }
                }
            }
            if (exception != null) {
                throw exception;
            }
        };
    }

    static ValueValidator notEmpty() {
        return value -> {
            if (null == value || value.isEmpty()) {
                throw new IllegalArgumentException("Value cannot be empty");
            }
        };
    }

    static ValueValidator notBlank() {
        return value -> {
            if (null == value || value.isBlank()) {
                throw new IllegalArgumentException("Value cannot be blank");
            }
        };
    }

    static ValueValidator notNull() {
        return value -> {
            if (value == null) {
                throw new IllegalArgumentException("Value cannot be null");
            }
        };
    }

    static ValueValidator isNumeric() {
        return value -> {
            try {
                Double.parseDouble(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Value must be numeric: " + value);
            }
        };
    }

    static ValueValidator isInteger() {
        return value -> {
            try {
                Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Value must be an integer: " + value);
            }
        };
    }

    static ValueValidator isBoolean() {
        return value -> {
            if (!"true".equalsIgnoreCase(value) && !"false".equalsIgnoreCase(value)) {
                throw new IllegalArgumentException("Value must be a boolean: " + value);
            }
        };
    }

    static ValueValidator matches(Pattern pattern) {
        return value -> {
            if (null == value || !pattern.matcher(value).matches()) {
                throw new IllegalArgumentException("Value must match pattern " + pattern + ", found: " + value);
            }
        };
    }

    static ValueValidator equals(String expected) {
        return value -> {
            if (!expected.equals(value)) {
                throw new IllegalArgumentException("Value must be equal to " + expected + ", found: " + value);
            }
        };
    }

    static ValueValidator equalsIgnoreCase(String expected) {
        return value -> {
            if (!expected.equalsIgnoreCase(value)) {
                throw new IllegalArgumentException("Value must be equal to " + expected + " (ignoring case), found: " + value);
            }
        };
    }

    static ValueValidator notEquals(String expected) {
        return value -> {
            if (expected.equals(value)) {
                throw new IllegalArgumentException("Value must not be equal to " + expected + ", found: " + value);
            }
        };
    }

    static ValueValidator notEqualsIgnoreCase(String expected) {
        return value -> {
            if (expected.equalsIgnoreCase(value)) {
                throw new IllegalArgumentException("Value must not be equal to " + expected + " (ignoring case), found: " + value);
            }
        };
    }
}
