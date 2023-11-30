package io.nuvolo.juice.business.model;

import java.util.Objects;
import java.util.function.Consumer;

public interface Action {
    ActionName getName();

    void execute(Screen currentScreen);

    static Action from(ActionName name, Consumer<Screen> action) {
        Objects.requireNonNull(name, "Action name cannot be null");
        Objects.requireNonNull(action, "Action cannot be null");
        return new Action() {
            @Override
            public ActionName getName() {
                return name;
            }

            @Override
            public void execute(Screen currentScreen) {
                action.accept(currentScreen);
            }
        };
    }

    static Action noOp(ActionName name) {
        return from(name, screen -> {
            // Do nothing
        });
    }

    static Action noOp() {
        return noOp(ActionName.of("No Op"));
    }

    static Action of(ActionName name, Action... actions) {
        if (actions.length == 0) {
            throw new IllegalArgumentException("At least one action must be provided");
        }
        else {
            return from(name, screen -> {
                for (final Action action : actions) {
                    action.execute(screen);
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
            name.append(action.getName());
            first = false;
        }
        name.append("]");
        return of(ActionName.of(name.toString()), actions);
    }
}
