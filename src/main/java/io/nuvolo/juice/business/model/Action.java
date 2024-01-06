package io.nuvolo.juice.business.model;

import java.util.Objects;
import java.util.function.BiConsumer;

public interface Action {
    ActionName getActionName();

    void execute(UserInterface userInterface, Screen currentScreen);

    static Action from(ActionName name, BiConsumer<UserInterface, Screen> action) {
        Objects.requireNonNull(name, "Action name cannot be null");
        Objects.requireNonNull(action, "Action cannot be null");
        return new Action() {
            @Override
            public ActionName getActionName() {
                return name;
            }

            @Override
            public void execute(UserInterface userInterface, Screen currentScreen) {
                action.accept(userInterface, currentScreen);
            }
        };
    }

    static Action noOp(ActionName name) {
        return from(name, (userInterface, screen) -> {
            // Do nothing
        });
    }

    static Action noOp() {
        return noOp(ActionName.of("No Op"));
    }

    static Action navigateTo(ScreenName screenName) {
        Objects.requireNonNull(screenName, "Screen name cannot be null");
        return from(ActionName.of("Navigate to " + screenName), (userInterface, screen) -> userInterface.navigateTo(screenName));
    }

    static Action of(ActionName name, Action... actions) {
        if (actions.length == 0) {
            throw new IllegalArgumentException("At least one action must be provided");
        }
        else {
            return from(name, (userInterface, screen) -> {
                for (final Action action : actions) {
                    action.execute(userInterface, screen);
                }
            });
        }
    }

    static Action of(Action... actions) {
        boolean first = true;
        final StringBuilder name = new StringBuilder();
        name.append("Action Sequence[");
        for (final Action action : actions) {
            if (!first) {
                name.append(",");
            }
            name.append(action.getActionName());
            first = false;
        }
        name.append("]");
        return of(ActionName.of(name.toString()), actions);
    }
}
