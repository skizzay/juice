package io.nuvolo.juice.business.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbstractTableTest {
    private static final class TestTable extends AbstractTable {
        private final class TestCell extends AbstractCell implements Table.ReadWriteCell {
            private final List<String> values;
            private final int numColumns;

            TestCell(int row, int column, List<String> values, int numColumns) {
                super(row, column);
                this.values = values;
                this.numColumns = numColumns;
            }

            @Override
            public String getValue() {
                return values.get(getRow() * numColumns + getColumn());
            }

            @Override
            public void setValue(String value) {
                values.set(getRow() * numColumns + getColumn(), value);
            }

            @Override
            public FieldName getTableName() {
                return TestTable.this.getFieldName();
            }
        }

        private final List<String> values;
        private final int rows;
        private final int columns;

        TestTable(FieldName tableName, int rows, int columns) {
            super(tableName);
            this.rows = rows;
            this.columns = columns;
            this.values = new ArrayList<>(Collections.nCopies(rows * columns, ""));
        }

        @Override
        public Cell getCell(int row, int column) {
            return new TestCell(row, column, values, getColumnCount());
        }

        @Override
        public int getRowCount() {
            return rows;
        }

        @Override
        public <T extends Cell> Stream<Row<T>> rows(Class<T> type) {
            return Stream.iterate(0, i -> i + 1)
                    .limit(getRowCount())
                    .map(i -> getRow(i, type));
        }

        @Override
        public int getColumnCount() {
            return columns;
        }

        @Override
        public <T extends Cell> Stream<Column<T>> columns(Class<T> type) {
            return Stream.iterate(0, i -> i + 1)
                    .limit(getColumnCount())
                    .map(i -> getColumn(i, type));
        }
    }

    private final FieldName fieldName = FieldName.of("test");
    private final int rows = 10;
    private final int columns = 10;

    private TestTable createTarget() {
        return new TestTable(fieldName, rows, columns);
    }

    @Test
    void getReadableCell_afterWritingToCell_reflectsCellValue() {
        // Arrange
        final var target = createTarget();
        final int row = 5;
        final int column = 5;
        final var writeableCell = target.getWriteableCell(row, column);
        writeableCell.setValue("test value");

        // Act
        final var readableCell = target.getReadableCell(row, column);

        // Assert
        assertEquals(readableCell.getRow(), writeableCell.getRow());
        assertEquals(readableCell.getColumn(), writeableCell.getColumn());
        assertEquals("test value", readableCell.getValue());
    }

    @Test
    void getFieldName_returnsTableName() {
        // Arrange
        final var target = createTarget();

        // Act
        final var result = target.getFieldName();

        // Assert
        assertEquals(fieldName, result);
    }

    @Test
    void getRow_returnsRow() {
        // Arrange
        final var target = createTarget();
        final int row = 5;

        // Act
        final var result = target.readRow(row);

        // Assert
        assertEquals(row, result.getRow());
        assertEquals(columns, result.size());
        for (int i = 0; i < columns; i++) {
            assertEquals("", result.get(i).getValue());
            assertEquals(row, result.get(i).getRow());
            assertEquals(i, result.get(i).getColumn());
        }
    }

    @Test
    void getHeaderRow_returnsHeaderRow() {
        // Arrange
        final var target = createTarget();
        target.writeRow(0).forEach(cell -> cell.setValue("test"));

        // Act
        final var result = target.getHeaderRow();

        // Assert
        assertEquals(0, result.getRow());
        assertEquals(columns, result.size());
        for (int i = 0; i < columns; i++) {
            assertEquals("test", result.get(i).getValue());
            assertEquals(0, result.get(i).getRow());
            assertEquals(i, result.get(i).getColumn());
        }
    }

    @Test
    void readRows_streamsAllRows() {
        // Arrange
        final var target = createTarget();

        // Act
        final var result = target.readRows();

        // Assert
        assertEquals(rows, result.count());
    }

    @Test
    void writeRows_streamsAllRows() {
        // Arrange
        final var target = createTarget();

        // Act
        final var result = target.writeRows();

        // Assert
        assertEquals(rows, result.count());
    }

    @Test
    void getColumn_returnsColumn() {
        // Arrange
        final var target = createTarget();
        final int column = 5;

        // Act
        final var result = target.readColumn(column);

        // Assert
        assertEquals(column, result.getColumn());
        assertEquals(rows, result.size());
        for (int i = 0; i < rows; i++) {
            assertEquals("", result.get(i).getValue());
            assertEquals(i, result.get(i).getRow());
            assertEquals(column, result.get(i).getColumn());
        }
    }

    @Test
    void getHeaderColumn_returnsHeaderColumn() {
        // Arrange
        final var target = createTarget();
        target.writeColumn(0).forEach(cell -> cell.setValue("test"));

        // Act
        final var result = target.getHeaderColumn();

        // Assert
        assertEquals(0, result.getColumn());
        assertEquals(rows, result.size());
        for (int i = 0; i < rows; i++) {
            assertEquals("test", result.get(i).getValue());
            assertEquals(i, result.get(i).getRow());
            assertEquals(0, result.get(i).getColumn());
        }
    }

    @Test
    void readColumns_streamsAllColumns() {
        // Arrange
        final var target = createTarget();

        // Act
        final var result = target.readColumns();

        // Assert
        assertEquals(columns, result.count());
    }

    @Test
    void writeColumns_streamsAllColumns() {
        // Arrange
        final var target = createTarget();

        // Act
        final var result = target.writeColumns();

        // Assert
        assertEquals(columns, result.count());
    }

    @Test
    void size_returnsCellCount() {
        // Arrange
        final var target = createTarget();

        // Act
        final var result = target.size();

        // Assert
        assertEquals(rows * columns, result);
    }

    @Test
    void asCellValues_convertsRowsToListOfStringValues() {
        // Arrange
        final var target = createTarget();
        target.writeRows().flatMap(List::stream).forEach(cell -> cell.setValue("test"));

        // Act
        final var result = target.readRows().map(TableUtilities::asCellValues).flatMap(List::stream).toList();

        // Assert
        assertEquals(target.size(), result.size());
        result.forEach(value -> assertEquals("test", value));
    }
}