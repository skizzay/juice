package io.nuvolo.juice.business.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BasicScreenBuilder {
    private final ScreenName name;
    private final Map<FieldName, Field> fields = new HashMap<>();
    private final Map<ActionName, Action> requests = new HashMap<>();

    public BasicScreenBuilder(ScreenName name) {
        Objects.requireNonNull(name, "Screen name cannot be null");
        this.name = name;
    }

    public BasicScreenBuilder addField(Field field) {
        Objects.requireNonNull(field, "Field cannot be null");
        Objects.requireNonNull(field.getFieldName(), "Field name cannot be null");
        fields.put(field.getFieldName(), field);
        return this;
    }

    public BasicScreenBuilder addRequest(Action action) {
        Objects.requireNonNull(action, "Action cannot be null");
        Objects.requireNonNull(action.getActionName(), "Action name cannot be null");
        requests.put(action.getActionName(), action);
        return this;
    }

    public BasicScreen build() {
        return new BasicScreen(name, requests, fields);
    }
}
