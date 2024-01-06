package io.nuvolo.juice.business.model;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractTable implements Table {
    private final FieldName tableName;
    private final int maxRowsForStateCapture;
    private final int maxColumnsForStateCapture;

    protected AbstractTable(FieldName tableName, int maxRowsForStateCapture, int maxColumnsForStateCapture) {
        this.tableName = Objects.requireNonNull(tableName, "tableName cannot be null");
        this.maxRowsForStateCapture = maxRowsForStateCapture;
        this.maxColumnsForStateCapture = maxColumnsForStateCapture;
    }

    @Override
    public FieldName getFieldName() {
        return tableName;
    }

    @Override
    public <T extends Cell> Row<T> getRow(int index, Class<T> type) {
        return new BasicRow<>(index, this, type);
    }

    @Override
    public <T extends Cell> Column<T> getColumn(int index, Class<T> type) {
        return new BasicColumn<>(index, this, type);
    }

    @Override
    public Map<FieldName, String> getState() {
        return cellsForStateCapture().collect(Collectors.toUnmodifiableMap(Field::getFieldName, ReadableField::getValue));
    }

    private Stream<ReadableCell> cellsForStateCapture() {
        final var rowStream = maxRowsForStateCapture > 0
                ? readRows().limit(maxRowsForStateCapture)
                : readRows();
        return rowStream
                .flatMap(row -> maxColumnsForStateCapture > 0
                        ? row.stream().limit(maxColumnsForStateCapture)
                        : row.stream());
    }
}
