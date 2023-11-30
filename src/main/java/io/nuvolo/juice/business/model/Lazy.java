package io.nuvolo.juice.business.model;

import java.util.Objects;
import java.util.function.Supplier;

public class Lazy<T> {
    private final Supplier<T> supplier;
    private T value;

    public Lazy(Supplier<T> supplier, T value) {
        this.supplier = supplier;
        this.value = Objects.requireNonNull(value, "Cannot initialize a lazy variable with a null value");
    }

    public Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
        this.value = null;
    }

    public void reset() {
        value = null;
    }

    public T get() {
        if (null == value) {
            value = Objects.requireNonNull(supplier.get(), "Lazy variable cannot be set to a null value");
        }
        return value;
    }
}
