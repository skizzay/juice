package io.nuvolo.juice.infrastructure.selenium;

import io.nuvolo.juice.business.model.FieldName;

public class ElementNotFound extends RuntimeException {
    private final FieldName fieldName;
    public ElementNotFound(FieldName fieldName) {
        super("Element not found: " + fieldName);
        this.fieldName = fieldName;
    }

    public FieldName getFieldName() {
        return fieldName;
    }
}
