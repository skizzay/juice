package io.nuvolo.juice.business.model;

import java.util.*;
import java.util.function.Consumer;

public class GraphBasedScreenNavigator implements ScreenNavigator {
    private final Lazy<Graph<ScreenName>> graph = new Lazy<>(this::buildGraph);
    private final Map<ScreenName, Screen> screens = new HashMap<>();

    @Override
    public void addScreen(Screen screen) {
        Objects.requireNonNull(screen);
        this.screens.put(screen.getName(), screen);
        graph.reset();
    }

    private Graph<ScreenName> buildGraph() {
        final List<Graph.Edge<ScreenName>> edges = new ArrayList<>();
        for (final Screen screen : screens.values()) {
            Objects.requireNonNull(screen);
            this.screens.put(screen.getName(), screen);
            final ScreenName name = screen.getName();
            for (final ScreenName neighbor : screen.getDirectlyNavigableScreens()) {
                edges.add(new Graph.Edge<>(name, neighbor));
            }
        }
        return new Graph<>(edges);
    }

    @Override
    public Screen navigate(Screen source, ScreenName target) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);
        if (!screens.containsKey(target)) {
            throw new IllegalArgumentException("Unknown screen: " + target);
        }
        else if (!screens.containsKey(source.getName())) {
            throw new IllegalArgumentException("Unknown screen: " + source.getName());
        }
        else {
            final List<ScreenName> path = graph.get().getShortestPath(source.getName(), target);
            if (path.isEmpty()) {
                throw new IllegalArgumentException("No path from " + source.getName() + " to " + target);
            }
            else if (1 == path.size()) {
                // Already on the target screen
                return source;
            }
            else {
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
                source.navigateTo(screenName);
                source = screens.get(screenName);
            }
        }
        final var traverse = new ScreenNameTraverse(source);
        path.stream().skip(1).forEach(traverse);
        return traverse.source;
    }

    @Override
    public Optional<Screen> getScreen(ScreenName name) {
        return Optional.ofNullable(screens.get(name));
    }
}
