package io.nuvolo.juice.infrastructure.text;

import io.nuvolo.juice.business.model.FieldName;
import io.nuvolo.juice.business.model.ReadWriteField;

public class TextBoxField implements ReadWriteField {
    private final FieldName fieldName;
    private final TextStorage storage;
    private final Point position;
    private final BoxDimensions dimensions;

    public TextBoxField(FieldName fieldName, TextStorage storage, Point position, BoxDimensions dimensions) {
        this.fieldName = fieldName;
        this.storage = storage;
        this.position = position;
        this.dimensions = dimensions;
    }

    @Override
    public FieldName getFieldName() {
        return fieldName;
    }

    @Override
    public void setValue(String value) {
        storage.setText(value, position, dimensions);
    }

    @Override
    public String getValue() {
        return storage.getText(position, dimensions);
    }
}
