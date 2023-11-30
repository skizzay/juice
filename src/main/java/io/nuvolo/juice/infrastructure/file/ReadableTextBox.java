package io.nuvolo.juice.infrastructure.file;

import io.nuvolo.juice.business.model.FieldName;
import io.nuvolo.juice.business.model.ReadableField;

import java.util.Objects;

public class ReadableTextBox implements ReadableField {
    private final FieldName fieldName;
    private final TextStorage storage;
    private final Point position;
    private final BoxDimensions dimensions;

    public ReadableTextBox(FieldName fieldName, TextStorage storage, Point position, BoxDimensions dimensions) {
        this.fieldName = fieldName;
        this.storage = Objects.requireNonNull(storage);
        this.position = Objects.requireNonNull(position);
        this.dimensions = Objects.requireNonNull(dimensions);
    }

    @Override
    public FieldName getFieldName() {
        return fieldName;
    }

    @Override
    public String getValue() {
        return storage.getText(position, dimensions);
    }
}
