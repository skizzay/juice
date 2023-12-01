package io.nuvolo.juice.business.model;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class BasicScreen extends BasicContainer implements Screen {
    private final ScreenName name;
    private final Map<ScreenName, Action> navigation;
    private final Map<ActionName, Action> requests;

    public BasicScreen(ScreenName name, Map<ScreenName, Action> navigation, Map<ActionName, Action> requests, Map<FieldName, ReadableField> readableFields,
                       Map<FieldName, WriteableField> writeableFields,
                       Map<FieldName, Container> containers) {
        super(readableFields, writeableFields, containers);
        this.name = Objects.requireNonNull(name, "Screen name cannot be null");
        this.navigation = Objects.requireNonNull(navigation, "Navigation cannot be null");
        this.requests = requests;
    }

    @Override
    public ScreenName getName() {
        return name;
    }

    @Override
    public void navigateTo(ScreenName name) {
        if (!this.name.equals(name)) {
            if (!navigation.containsKey(name)) {
                throw new IllegalArgumentException("Unknown screen: " + name);
            } else {
                final Action action = navigation.get(name);
                action.execute(this);
            }
        }
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

    @Override
    public Collection<ScreenName> getDirectlyNavigableScreens() {
        return navigation.keySet();
    }
}
