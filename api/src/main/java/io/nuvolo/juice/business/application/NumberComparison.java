package io.nuvolo.juice.business.application;

import io.nuvolo.juice.business.model.FieldName;

public interface NumberComparison {
    boolean compare(Number actual, Number expected);

    default void fail(FieldName fieldName1, Number fieldValue1, FieldName fieldName2, Number fieldValue2) {
        final var comparison = toString().replace("_", " ").toLowerCase();
        throw new AssertionError(String.format("Field %s[%s] is not %s field %s[%s]", fieldName1, fieldValue1, comparison, fieldName2, fieldValue2));
    }

    default void fail(FieldName fieldName, Number actual, Number expected) {
        final var comparison = toString().replace("_", " ").toLowerCase();
        throw new AssertionError(String.format("Field %s[%s] is not %s %s", fieldName, actual, comparison, expected));
    }

    enum Type implements NumberComparison {
        EQUAL_TO {
            @Override
            public boolean compare(Number actual, Number expected) {
                return actual.equals(expected);
            }
        },
        GREATER_THAN {
            @Override
            public boolean compare(Number actual, Number expected) {
                return actual.doubleValue() > expected.doubleValue();
            }
        },
        LESS_THAN {
            @Override
            public boolean compare(Number actual, Number expected) {
                return actual.doubleValue() < expected.doubleValue();
            }
        },
        GREATER_THAN_OR_EQUAL_TO {
            @Override
            public boolean compare(Number actual, Number expected) {
                return actual.doubleValue() >= expected.doubleValue();
            }
        },
        LESS_THAN_OR_EQUAL_TO {
            @Override
            public boolean compare(Number actual, Number expected) {
                return actual.doubleValue() <= expected.doubleValue();
            }
        },
        NOT_EQUAL_TO {
            @Override
            public boolean compare(Number actual, Number expected) {
                return !actual.equals(expected);
            }

            @Override
            public void fail(FieldName fieldName1, Number fieldValue1, FieldName fieldName2, Number fieldValue2) {
                throw new AssertionError(String.format("Field %s[%s] is equal to field %s[%s]", fieldName1, fieldValue1, fieldName2, fieldValue2));
            }

            @Override
            public void fail(FieldName fieldName, Number actual, Number expected) {
                throw new AssertionError(String.format("Field %s[%s] is equal to %s", fieldName, actual, expected));
            }
        }
    }
}
