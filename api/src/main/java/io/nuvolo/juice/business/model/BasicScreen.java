package io.nuvolo.juice.business.model;

import java.util.*;

public class BasicScreen extends BasicContainer implements Screen {
    private final ScreenName name;
    private final Map<ActionName, Action> actions;
    private final Map<FieldName, String> lastState = new HashMap<>();

    public BasicScreen(ScreenName name, Map<ActionName, Action> actions, Map<FieldName, Field> fields) {
        super(fields);
        this.name = Objects.requireNonNull(name, "Screen name cannot be null");
        this.actions = Objects.requireNonNull(actions, "Actions cannot be null");
    }

    @Override
    public ScreenName getScreenName() {
        return name;
    }

    @Override
    public void performAction(UserInterface userInterface, ActionName name) {
        Objects.requireNonNull(userInterface, "User interface cannot be null");
        Objects.requireNonNull(name, "Action name cannot be null");
        if (!actions.containsKey(name)) {
            throw new IllegalArgumentException("Unknown request: " + name);
        } else {
            final Action action = actions.get(name);
            action.execute(userInterface, this);
        }
    }

    @Override
    public void rememberState() {
        lastState.clear();
        getFields()
                .map(Field::getState)
                .forEachOrdered(lastState::putAll);
    }

    @Override
    public Map<FieldName, String> getLastState() {
        return Collections.unmodifiableMap(lastState);
    }
}
