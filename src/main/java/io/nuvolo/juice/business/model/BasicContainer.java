package io.nuvolo.juice.business.model;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasicContainer implements Container {
    private static final Pattern TABLE_CELL_PATTERN = Pattern.compile("([^]]+)\\[(\\d+),(\\d+)]");
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
}
