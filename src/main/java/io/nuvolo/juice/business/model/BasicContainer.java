package io.nuvolo.juice.business.model;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class BasicContainer implements Container {
    private static final Pattern TABLE_CELL_PATTERN = Pattern.compile("([^\\[]+)\\[(\\d+),(\\d+)]");
    private final Map<FieldName, Field> fields;

    public BasicContainer(Map<FieldName, Field> fields) {
        this.fields = Objects.requireNonNull(fields, "Fields cannot be null");
    }

    @Override
    public Optional<Field> getField(FieldName name) {
        Objects.requireNonNull(name, "Field name cannot be null");
        final Matcher matcher = TABLE_CELL_PATTERN.matcher(name.name());
        if (matcher.matches()) {
            return getTable(FieldName.of(matcher.group(1)))
                    .map(table -> table.getCell(Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3))));
        } else {
            return Optional.ofNullable(fields.get(name));
        }
    }

    @Override
    public Map<FieldName, String> getState() {
        return Stream.concat(Stream.concat(streamFields(), streamContainers()), streamTables())
                .collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);
    }

    private Stream<Map.Entry<FieldName, String>> streamFields() {
        return streamFields(ReadableField.class)
                .sequential()
                .map(f -> Map.entry(f.getFieldName(), f.getValue()));
    }

    private record ContainerState(FieldName name, Map<FieldName, String> state) {
        Stream<Map.Entry<FieldName, String>> stream() {
            final String prefix = name + ".";
            return state.entrySet().stream()
                    .map(e -> Map.entry(FieldName.of(prefix + e.getKey()), e.getValue()));
        }
    }

    private Stream<Map.Entry<FieldName, String>> streamContainers() {
        return streamFields(ContainerField.class)
                .sequential()
                .map(containerField -> new ContainerState(containerField.getFieldName(), containerField.getState()))
                .flatMap(ContainerState::stream);
    }

    private Stream<Map.Entry<FieldName, String>> streamTables() {
        return streamFields(Table.class)
                .sequential()
                .map(Table::getState)
                .map(Map::entrySet)
                .flatMap(Collection::stream);
    }

    private <T extends Field> Stream<T> streamFields(Class<T> type) {
        return fields.values().stream().filter(type::isInstance).map(type::cast);
    }
}
