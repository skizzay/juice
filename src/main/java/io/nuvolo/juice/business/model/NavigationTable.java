package io.nuvolo.juice.business.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class NavigationTable {
    private final Map<ScreenName, Map<ScreenName, Action>> table = new HashMap<>();

    public void addNavigation(ScreenName source, ScreenName target, Action action) {
        final var map = table.computeIfAbsent(source, k -> new HashMap<>());
        if (map.containsKey(target)) {
            throw new IllegalArgumentException("Navigation already exists from '" + source + "' to '" + target + "'");
        }
        else {
            map.put(target, action);
        }
    }

    public Optional<Action> getNavigationAction(ScreenName source, ScreenName target) {
        return Optional.ofNullable(table.getOrDefault(source, Map.of()).get(target));
    }

    public Stream<Navigation> getNavigations() {
        return table.entrySet().stream().flatMap(sourceEntry -> {
            final ScreenName source = sourceEntry.getKey();
            return sourceEntry.getValue().entrySet().stream().map(targetEntry -> {
                final ScreenName target = targetEntry.getKey();
                final Action action = targetEntry.getValue();
                return new Navigation(source, target, action);
            });
        });
    }

    public static NavigationTable from(Iterable<ScreenName> screenNames, Iterable<Navigation> navigations) {
        final NavigationTable table = new NavigationTable();
        for (final Navigation navigation : navigations) {
            if (navigation.isNavigableFromAll()) {
                for (final ScreenName screenName : screenNames) {
                    table.addNavigation(screenName, navigation.target(), navigation.action());
                }
            } else {
                table.addNavigation(navigation.source(), navigation.target(), navigation.action());
            }
        }
        return table;
    }
}
