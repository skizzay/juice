package io.nuvolo.juice.business.application;


import io.nuvolo.juice.business.model.FieldName;
import io.nuvolo.juice.business.model.ReadWriteField;

import java.util.Objects;

public class FakeField implements ReadWriteField {
    private final FieldName fieldName;
    private String value = "";

    public FakeField(FieldName fieldName) {
        this.fieldName = Objects.requireNonNull(fieldName, "fieldName must not be null");
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
        this.value = value;
    }
}
