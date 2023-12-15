package io.nuvolo.juice.business.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BasicScreenBuilder {
    private final ScreenName name;
    private final Map<FieldName, ReadableField> readableFields = new HashMap<>();
    private final Map<FieldName, WriteableField> writeableFields = new HashMap<>();
    private final Map<ActionName, Action> requests = new HashMap<>();

    public BasicScreenBuilder(ScreenName name) {
        Objects.requireNonNull(name, "Screen name cannot be null");
        this.name = name;
    }

    public BasicScreenBuilder addReadableField(FieldName name, ReadableField field) {
        Objects.requireNonNull(name, "Field name cannot be null");
        Objects.requireNonNull(field, "Field cannot be null");
        readableFields.put(name, field);
        return this;
    }

    public BasicScreenBuilder addWriteableField(FieldName name, WriteableField field) {
        Objects.requireNonNull(name, "Field name cannot be null");
        Objects.requireNonNull(field, "Field cannot be null");
        writeableFields.put(name, field);
        return this;
    }

    public <T extends ReadableField & WriteableField> BasicScreenBuilder addReadWriteField(FieldName name, T field) {
        Objects.requireNonNull(name, "Field name cannot be null");
        Objects.requireNonNull(field, "Field cannot be null");
        readableFields.put(name, field);
        writeableFields.put(name, field);
        return this;
    }

    public BasicScreenBuilder addRequest(ActionName actionName, Action action) {
        Objects.requireNonNull(actionName, "Action name cannot be null");
        Objects.requireNonNull(action, "Action cannot be null");
        requests.put(actionName, action);
        return this;
    }

    public BasicScreen build() {
        return new BasicScreen(name, requests, readableFields, writeableFields, Map.of());
    }
}
