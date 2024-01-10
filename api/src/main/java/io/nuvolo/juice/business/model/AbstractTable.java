package io.nuvolo.juice.business.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class AbstractTable implements Table {
    private final FieldName tableName;
    protected final int maxRowsForStateCapture;
    protected final int maxColumnsForStateCapture;

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
        return new BasicRow<>(TableUtilities.normalizeRowColumnIndex(index, getRowCount()), this, type);
    }

    @Override
    public <T extends Cell> Column<T> getColumn(int index, Class<T> type) {
        return new BasicColumn<>(TableUtilities.normalizeRowColumnIndex(index, getColumnCount()), this, type);
    }

    @Override
    public final Table.Cell getCell(int row, int column) {
        return safeGetCell(TableUtilities.normalizeRowColumnIndex(row, getRowCount()),
                TableUtilities.normalizeRowColumnIndex(column, getColumnCount()));
    }

    protected abstract Table.Cell safeGetCell(int row, int column);

    @Override
    public Map<FieldName, String> getState() {
        return cellsForStateCapture().collect(
                HashMap::new,
                (m, c) -> m.putAll(c.getState()),
                Map::putAll);
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
