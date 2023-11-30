package io.nuvolo.juice.infrastructure.file;

import io.nuvolo.juice.business.model.FieldName;
import io.nuvolo.juice.business.model.WriteableField;

public class WriteableTextBox implements WriteableField {
    private final FieldName fieldName;
    private final TextStorage storage;
    private final Point position;
    private final BoxDimensions dimensions;

    public WriteableTextBox(FieldName fieldName, TextStorage storage, Point position, BoxDimensions dimensions) {
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
        if (value.length() > dimensions.size()) {
            throw new IllegalArgumentException("Value is too long");
        }
        storage.setText(value, position, dimensions);
    }
}
