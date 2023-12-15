package io.nuvolo.juice.business.model;

import java.util.*;
import java.util.function.Consumer;

public class GraphBasedScreenNavigator implements ScreenNavigator {
    private final Lazy<Graph<ScreenName>> graph = new Lazy<>(this::buildGraph);
    private final Map<ScreenName, Screen> screens;
    private final NavigationTable table;

    public GraphBasedScreenNavigator(Map<ScreenName, Screen> screens, NavigationTable table) {
        this.screens = Objects.requireNonNull(screens, "screens must not be null");
        this.table = Objects.requireNonNull(table, "table must not be null");
    }

    private Graph<ScreenName> buildGraph() {
        final Graph<ScreenName> result = new Graph<>();
        table.getNavigations().forEach(navigation -> {
            if (navigation.isNavigableFromAll()) {
                for (final Screen screen : screens.values()) {
                    result.addEdge(screen.getName(), navigation.target());
                }
            } else {
                result.addEdge(navigation.source(), navigation.target());
            }
        });
        return result;
    }

    @Override
    public Screen navigate(Screen source, ScreenName target) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);
        if (source.getName().equals(target)) {
            return source;
        }
        else {
            final List<ScreenName> path = graph.get().getShortestPath(source.getName(), target);
            if (path.isEmpty()) {
                throw new IllegalArgumentException("No path from " + source.getName() + " to " + target);
            } else {
                return navigate(source, path);
            }
        }
    }

    private Screen navigate(Screen source, List<ScreenName> path) {
        class ScreenNameTraverse implements Consumer<ScreenName> {
            private Screen source;

            ScreenNameTraverse(Screen source) {
                this.source = source;
            }

            @Override
            public void accept(ScreenName screenName) {
                table.getNavigationAction(source.getName(), screenName).ifPresentOrElse(
                        action -> {
                            action.execute(source);
                            source = screens.get(screenName);
                        },
                        () -> {
                            throw new IllegalArgumentException("No navigation action found for screen " + screenName);
                        }
                );
            }
        }
        final var traverse = new ScreenNameTraverse(source);
        path.stream().skip(1).forEach(traverse);
        return traverse.source;
    }
}
