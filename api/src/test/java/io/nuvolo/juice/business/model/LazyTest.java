package io.nuvolo.juice.business.model;

import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LazyTest {
    private Supplier<String> supplier;
    private String value;

    private Lazy<String> createTarget() {
        if (null == value) {
            return new Lazy<>(supplier);
        }
        else {
            return new Lazy<>(supplier, value);
        }
    }

    @Test
    void get_usesSupplier() {
        supplier = () -> "Hello, World!";
        final Lazy<String> target = createTarget();
        assertEquals("Hello, World!", target.get());
    }

    @Test
    void get_givenSupplierReturnsNull_throwsException() {
        supplier = () -> null;
        final Lazy<String> target = createTarget();
        assertThrows(NullPointerException.class, target::get);
    }

    @Test
    void get_afterReset_usesSupplier() {
        supplier = new Supplier<>() {
            private boolean firstCall = true;

            @Override
            public String get() {
                if (firstCall) {
                    firstCall = false;
                    return "Hello, World!";
                } else {
                    return "Goodbye, World!";
                }
            }
        };
        final Lazy<String> target = createTarget();
        target.get(); // Ignore the first call
        target.reset();
        assertEquals("Goodbye, World!", target.get());
    }

    @Test
    void get_givenValue_returnsValue() {
        value = "Hello, World!";
        final Lazy<String> target = createTarget();
        assertEquals("Hello, World!", target.get());
    }
}