package io.nuvolo.juice.infrastructure.memory;

import io.nuvolo.juice.business.model.FieldName;
import io.nuvolo.juice.business.model.ReadWriteField;

import java.util.Objects;

public class InMemoryField implements ReadWriteField {
    private String value;
    private final FieldName fieldName;

    public InMemoryField(FieldName fieldName) {
        this(fieldName, "");
    }

    public InMemoryField(FieldName fieldName, String value) {
        this.fieldName = Objects.requireNonNull(fieldName, "fieldName must not be null");
        this.value = Objects.requireNonNull(value, "value must not be null");
    }

    @Override
    public FieldName getFieldName() {
        return fieldName;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = Objects.requireNonNull(value, "value must not be null");
    }
}
