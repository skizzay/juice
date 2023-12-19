package io.nuvolo.juice.business.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class NavigationTable {
    private final Map<ScreenName, Map<ScreenName, Action>> table = new HashMap<>();
    private final Map<ScreenName, Action> allNavigations = new HashMap<>();

    private Stream<Navigation> getNavigations(ScreenName sourceName, Map<ScreenName, Action> targets) {
        return targets.entrySet().stream().map(entry -> {
            final ScreenName targetName = entry.getKey();
            final Action action = entry.getValue();
            return new Navigation(sourceName, targetName, action);
        });
    }

    private void addUnique(Map<ScreenName, Action> map, ScreenName source, ScreenName target, Action action) {
        if (map.containsKey(target)) {
            throw new IllegalArgumentException("Navigation already exists from '" + source + "' to '" + target + "'");
        }
        else {
            map.put(target, action);
        }
    }

    public void addNavigation(Navigation navigation) {
        Objects.requireNonNull(navigation, "navigation must not be null");
        if (navigation.isNavigableFromAll()) {
            addUnique(allNavigations, Navigation.GLOBAL_SOURCE, navigation.target(), navigation.action());
        } else {
            addUnique(table.computeIfAbsent(navigation.source(), k -> new HashMap<>()), navigation.source(), navigation.target(), navigation.action());
        }
    }

    public Optional<Action> getNavigationAction(ScreenName source, ScreenName target) {
        return Optional.ofNullable(table.getOrDefault(source, allNavigations).get(target));
    }

    public Stream<Navigation> getNavigations() {
        return Stream.concat(
                getNavigations(Navigation.GLOBAL_SOURCE, allNavigations),
                table.entrySet().stream().flatMap(entry -> getNavigations(entry.getKey(), entry.getValue()))
        );
    }

    public static NavigationTable from(Iterable<Navigation> navigations) {
        final NavigationTable table = new NavigationTable();
        navigations.forEach(table::addNavigation);
        return table;
    }
}
