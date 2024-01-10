package io.nuvolo.juice.business.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
                    result.addEdge(screen.getScreenName(), navigation.target());
                }
            } else {
                result.addEdge(navigation.source(), navigation.target());
            }
        });
        return result;
    }

    @Override
    public Optional<Screen> getScreen(ScreenName screenName) {
        Objects.requireNonNull(screenName, "screenName must not be null");
        return Optional.ofNullable(screens.get(screenName));
    }

    @Override
    public Screen navigate(Screen source, ScreenName target) {
        Objects.requireNonNull(source, "source must not be null");
        Objects.requireNonNull(target, "target must not be null");
        if (source.getScreenName().equals(target)) {
            return source;
        }
        else {
            final List<ScreenName> path = graph.get().getShortestPath(source.getScreenName(), target);
            if (path.isEmpty()) {
                throw new IllegalArgumentException("No path from " + source.getScreenName() + " to " + target);
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

            private void performNavigationAction(ScreenName screenName) {
                table.getNavigationAction(source.getScreenName(), screenName)
                        .orElseThrow(() -> new IllegalArgumentException("No navigation action from " + source.getScreenName() + " to " + screenName))
                        .accept(source);
            }

            private void updateCurrentScreenTo(ScreenName screenName) {
                source = screens.get(screenName);
            }

            @Override
            public void accept(ScreenName screenName) {
                source.rememberState();
                performNavigationAction(screenName);
                updateCurrentScreenTo(screenName);
            }
        }
        final var traverse = new ScreenNameTraverse(source);
        path.stream().skip(1).forEach(traverse);
        return traverse.source;
    }
}
