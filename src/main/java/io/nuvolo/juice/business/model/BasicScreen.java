package io.nuvolo.juice.business.model;

import java.util.Map;
import java.util.Objects;

public class BasicScreen extends BasicContainer implements Screen {
    private final ScreenName name;
    private final Map<ActionName, Action> requests;

    public BasicScreen(ScreenName name, Map<ActionName, Action> requests, Map<FieldName, Field> fields,
                       Map<FieldName, Container> containers) {
        super(fields);
        this.name = Objects.requireNonNull(name, "Screen name cannot be null");
        this.requests = requests;
    }

    @Override
    public ScreenName getScreenName() {
        return name;
    }

    @Override
    public void performAction(ActionName name) {
        Objects.requireNonNull(name, "Request name cannot be null");
        if (!requests.containsKey(name)) {
            throw new IllegalArgumentException("Unknown request: " + name);
        } else {
            final Action action = requests.get(name);
            action.execute(this);
        }
    }
}
