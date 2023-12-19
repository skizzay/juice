package io.nuvolo.juice.business.application;

public interface NumberComparison {
    boolean compare(Number a, Number b);

    enum Type implements NumberComparison {
        EQUAL_TO {
            @Override
            public boolean compare(Number a, Number b) {
                return a.equals(b);
            }
        },
        GREATER_THAN {
            @Override
            public boolean compare(Number a, Number b) {
                return a.doubleValue() > b.doubleValue();
            }
        },
        LESS_THAN {
            @Override
            public boolean compare(Number a, Number b) {
                return a.doubleValue() < b.doubleValue();
            }
        },
        GREATER_THAN_OR_EQUAL_TO {
            @Override
            public boolean compare(Number a, Number b) {
                return a.doubleValue() >= b.doubleValue();
            }
        },
        LESS_THAN_OR_EQUAL_TO {
            @Override
            public boolean compare(Number a, Number b) {
                return a.doubleValue() <= b.doubleValue();
            }
        },
        NOT_EQUAL_TO {
            @Override
            public boolean compare(Number a, Number b) {
                return !a.equals(b);
            }
        }
    }
}
