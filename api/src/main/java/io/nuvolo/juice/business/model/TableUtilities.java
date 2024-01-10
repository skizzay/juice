package io.nuvolo.juice.business.model;

import java.util.List;
import java.util.stream.Stream;

public interface TableUtilities {
    static List<String> asCellValues(List<? extends Table.ReadableCell> cells) {
        return cells.stream().map(Table.ReadableCell::getValue).toList();
    }

    static <T extends Table.Cell> Stream<Table.Row<T>> streamRowsOfKnownSize(Table table, Class<T> type, int rows) {
        return Stream.iterate(
                table.getRow(0, type),
                row -> row.getRow() < rows,
                row -> table.getRow(row.getRow() + 1, type)
        ).limit(rows);
    }

    static <T extends Table.Cell> Stream<Table.Row<T>> streamRowsOfKnownSize(Table table, Class<T> type) {
        return streamRowsOfKnownSize(table, type, table.getRowCount());
    }

    static <T extends Table.Cell> Stream<Table.Column<T>> streamColumnsOfKnownSize(Table table, Class<T> type, int columns) {
        return Stream.iterate(
                table.getColumn(0, type),
                column -> column.getColumn() < columns,
                column -> table.getColumn(column.getColumn() + 1, type)
        ).limit(columns);
    }

    static <T extends Table.Cell> Stream<Table.Column<T>> streamColumnsOfKnownSize(Table table, Class<T> type) {
        return streamColumnsOfKnownSize(table, type, table.getColumnCount());
    }

    static FieldName cellFieldName(FieldName tableName, int row, int column) {
        return FieldName.of(tableName + "[" + row + "," + column + "]");
    }

    static int normalizeRowColumnIndex(int index, int count) {
        final int normalized = Math.abs(index) % count;
        return index < 0 ? count - normalized : normalized;
    }
}