package io.nuvolo.juice.business.model;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SizedTableTest {
    private static class TestDriver implements SizedTable.Driver {
        private final int numberOfRows;
        private final int numberOfColumns;
        private final ArrayList<String> values;

        public TestDriver(int numberOfRows, int numberOfColumns) {
            this.numberOfRows = numberOfRows;
            this.numberOfColumns = numberOfColumns;
            this.values = new ArrayList<>(Collections.nCopies(numberOfRows * numberOfColumns, ""));
        }

        @Override
        public int numberOfRows() {
            return numberOfRows;
        }

        @Override
        public int numberOfColumns() {
            return numberOfColumns;
        }

        @Override
        public Table.Cell getCell(int row, int column) {
            return new Table.Cell() {
                @Override
                public int getRow() {
                    return row;
                }

                @Override
                public int getColumn() {
                    return column;
                }

                @Override
                public String getValue() {
                    return values.get(row * numberOfColumns + column);
                }

                @Override
                public void setValue(String value) {
                    values.set(row * numberOfColumns + column, value);
                }
            };
        }
    }

    private final TestDriver driver = new TestDriver(6, 2);

    private SizedTable createTarget() {
        return new SizedTable(driver);
    }

    @Test
    void getCell_givenValidCoordinates_returnsCell() {
        // Arrange
        SizedTable target = createTarget();
        driver.getCell(0, 0).setValue("A1");

        // Act
        final Table.Cell result = target.getCell(0, 0);

        // Assert
        assertEquals(0, result.getRow());
        assertEquals(0, result.getColumn());
        assertEquals("A1", result.getValue());
    }

    @Test
    void getCell_givenInvalidRow_throwsException() {
        // Arrange
        SizedTable target = createTarget();

        // Act
        final IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> target.getCell(-1, 0));

        // Assert
        assertEquals("Row must be positive", result.getMessage());
    }

    @Test
    void getCell_givenInvalidColumn_throwsException() {
        // Arrange
        SizedTable target = createTarget();

        // Act
        final IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> target.getCell(0, -1));

        // Assert
        assertEquals("Column must be positive", result.getMessage());
    }

    @Test
    void getCell_givenInvalidRowAndColumn_throwsException() {
        // Arrange
        SizedTable target = createTarget();

        // Act
        final IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> target.getCell(driver.numberOfRows(), driver.numberOfColumns()));

        // Assert
        assertEquals("Row must be less than number of rows", result.getMessage());
    }

    @Test
    void getCell_givenColumnOutOfBounds_throwException() {
        // Arrange
        SizedTable target = createTarget();

        // Act
        final IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> target.getCell(0, driver.numberOfColumns()));

        // Assert
        assertEquals("Column must be less than number of columns", result.getMessage());
    }

    @Test
    void getRowHeaders_givenValidCoordinates_returnsRowHeaders() {
        // Arrange
        SizedTable target = createTarget();
        driver.getCell(0, 0).setValue("A1");
        driver.getCell(1,0).setValue("A2");
        driver.getCell(2,0).setValue("A3");
        driver.getCell(3,0).setValue("A4");
        driver.getCell(4,0).setValue("A5");
        driver.getCell(5,0).setValue("A6");

        // Act
        final Table.RowHeaders result = target.getRowHeaders();

        // Assert
        assertEquals(6, result.size());
        assertEquals(0, result.indexOf("A1"));
        assertEquals(1, result.indexOf("A2"));
        assertEquals(2, result.indexOf("A3"));
        assertEquals(3, result.indexOf("A4"));
        assertEquals(4, result.indexOf("A5"));
        assertEquals(5, result.indexOf("A6"));
    }

    @Test
    void getRowHeaders_streamsOverEachRowHeader() {
        // Arrange
        SizedTable target = createTarget();
        driver.getCell(0, 0).setValue("A1");
        driver.getCell(1,0).setValue("A2");
        driver.getCell(2,0).setValue("A3");
        driver.getCell(3,0).setValue("A4");
        driver.getCell(4,0).setValue("A5");
        driver.getCell(5,0).setValue("A6");

        // Act
        final int numberOfRows = target.getRowHeaders().stream().mapToInt(row -> 1).sum();
        final Table.Column column = target.getRowHeaders().asColumn();

        // Assert
        assertEquals(driver.numberOfRows(), numberOfRows);
        assertEquals(driver.numberOfRows(), column.stream().mapToInt(row -> 1).sum());
    }

    @Test
    void getRowHeaders_givenUnknownHeader_returnsMinusOne() {
        // Arrange
        SizedTable target = createTarget();
        driver.getCell(0, 0).setValue("A1");
        driver.getCell(0,1).setValue("B1");

        // Act
        final Table.RowHeaders result = target.getRowHeaders();
        final int index = result.indexOf("Unknown Header");

        // Assert
        assertEquals(-1, index);
    }

    @Test
    void getRowHeaders_getHeaderIndex_returnsHeaderIndex() {
        // Arrange
        SizedTable target = createTarget();
        driver.getCell(0, 0).setValue("A1");
        driver.getCell(0,1).setValue("B1");

        // Act
        final int index = target.getRowHeaders().get(1).getIndex();

        // Assert
        assertEquals(1, index);
    }

    @Test
    void getRowHeaders_getHeaderValue_returnsHeaderValue() {
        // Arrange
        SizedTable target = createTarget();
        driver.getCell(0, 0).setValue("A1");
        driver.getCell(1,0).setValue("A2");

        // Act
        final String value = target.getRowHeaders().get(1).getValue();

        // Assert
        assertEquals("A2", value);
    }

    @Test
    void getRowHeaders_asColumn_returnsColumnOfCellsInFirstColumn() {
        // Arrange
        SizedTable target = createTarget();
        driver.getCell(0, 0).setValue("A1");
        driver.getCell(0,1).setValue("B1");
        driver.getCell(1, 0).setValue("A2");
        driver.getCell(1,1).setValue("B2");
        driver.getCell(2, 0).setValue("A3");
        driver.getCell(2,1).setValue("B3");
        driver.getCell(3, 0).setValue("A4");
        driver.getCell(3,1).setValue("B4");
        driver.getCell(4, 0).setValue("A5");
        driver.getCell(4,1).setValue("B5");
        driver.getCell(5, 0).setValue("A6");
        driver.getCell(5,1).setValue("B6");

        // Act
        final Table.RowHeaders headers = target.getRowHeaders();
        final Table.Column column = headers.asColumn();

        // Assert
        assertEquals(6, column.size());
        assertEquals(0, column.getIndex());
        assertEquals("A1", column.get(0).getValue());
        assertEquals("A2", column.get(1).getValue());
        assertEquals("A3", column.get(2).getValue());
        assertEquals("A4", column.get(3).getValue());
        assertEquals("A5", column.get(4).getValue());
        assertEquals("A6", column.get(5).getValue());
        assertEquals(headers.get(0).getValue(), column.get(0).getValue());
        assertEquals(headers.get(1).getValue(), column.get(1).getValue());
        assertEquals(headers.get(2).getValue(), column.get(2).getValue());
        assertEquals(headers.get(3).getValue(), column.get(3).getValue());
        assertEquals(headers.get(4).getValue(), column.get(4).getValue());
        assertEquals(headers.get(5).getValue(), column.get(5).getValue());
    }

    @Test
    void getColumnHeaders_asRow_matchesColumnHeaders() {
        // Arrange
        SizedTable target = createTarget();
        driver.getCell(0, 0).setValue("A1");
        driver.getCell(1,0).setValue("A2");
        driver.getCell(2,0).setValue("A3");
        driver.getCell(3,0).setValue("A4");
        driver.getCell(4,0).setValue("A5");
        driver.getCell(5,0).setValue("A6");
        driver.getCell(0, 1).setValue("B1");
        driver.getCell(1,1).setValue("B2");
        driver.getCell(2,1).setValue("B3");
        driver.getCell(3,1).setValue("B4");
        driver.getCell(4,1).setValue("B5");
        driver.getCell(5,1).setValue("B6");

        // Act
        final Table.ColumnHeaders headers = target.getColumnHeaders();
        final Table.Row row = headers.asRow();

        // Assert
        assertEquals(2, row.size());;
        assertEquals(0, row.getIndex());
        assertEquals("A1", row.get(0).getValue());
        assertEquals("B1", row.get(1).getValue());
        assertEquals(headers.get(0).getValue(), row.get(0).getValue());
        assertEquals(headers.get(1).getValue(), row.get(1).getValue());
    }

    @Test
    void getColumnHeaders_streamsOverEachRowHeader() {
        // Arrange
        SizedTable target = createTarget();
        driver.getCell(0, 0).setValue("A1");
        driver.getCell(0,1).setValue("B1");

        // Act
        final int numberOfColumns = target.getColumnHeaders().stream().mapToInt(column -> 1).sum();
        final Table.Row row = target.getColumnHeaders().asRow();

        // Assert
        assertEquals(driver.numberOfColumns(), numberOfColumns);
        assertEquals(driver.numberOfColumns(), row.stream().mapToInt(column -> 1).sum());
    }

    @Test
    void getColumnHeaders_givenValidCoordinates_returnsColumnHeaders() {
        // Arrange
        SizedTable target = createTarget();
        driver.getCell(0, 0).setValue("A1");
        driver.getCell(0,1).setValue("B1");

        // Act
        final Table.ColumnHeaders result = target.getColumnHeaders();

        // Assert
        assertEquals(2, result.size());
        assertEquals(0, result.indexOf("A1"));
        assertEquals(1, result.indexOf("B1"));
    }

    @Test
    void getColumnHeaders_getHeaderIndex_returnsHeaderIndex() {
        // Arrange
        SizedTable target = createTarget();
        driver.getCell(0, 0).setValue("A1");
        driver.getCell(1,0).setValue("A2");

        // Act
        final int index = target.getColumnHeaders().get(1).getIndex();

        // Assert
        assertEquals(1, index);
    }

    @Test
    void getColumnHeaders_givenUnknownHeader_returnsMinusOne() {
        // Arrange
        SizedTable target = createTarget();
        driver.getCell(0, 0).setValue("A1");
        driver.getCell(1,0).setValue("A2");

        // Act
        final Table.ColumnHeaders result = target.getColumnHeaders();
        final int index = result.indexOf("Unknown Header");

        // Assert
        assertEquals(-1, index);
    }

    @Test
    void getColumnHeaders_returnsRowOfCellsInFirstRow() {
        // Arrange
        SizedTable target = createTarget();
        driver.getCell(0, 0).setValue("A1");
        driver.getCell(1,0).setValue("A2");
        driver.getCell(2,0).setValue("A3");
        driver.getCell(3,0).setValue("A4");
        driver.getCell(4,0).setValue("A5");
        driver.getCell(5,0).setValue("A6");
        driver.getCell(0, 1).setValue("B1");
        driver.getCell(1,1).setValue("B2");
        driver.getCell(2,1).setValue("B3");
        driver.getCell(3,1).setValue("B4");
        driver.getCell(4,1).setValue("B5");
        driver.getCell(5,1).setValue("B6");

        // Act
        final Table.ColumnHeaders headers = target.getColumnHeaders();
        final Table.Row row = target.getRow(0);

        // Assert
        assertEquals(2, row.size());
        assertEquals(0, row.getIndex());
        assertEquals("A1", row.get(0).getValue());
        assertEquals("B1", row.get(1).getValue());
        assertEquals(headers.get(0).getValue(), row.get(0).getValue());
        assertEquals(headers.get(1).getValue(), row.get(1).getValue());
    }

    @Test
    void columns_iteratorsOverEachColumn() {
        // Arrange
        final SizedTable target = createTarget();
        final Iterator<Table.Column> columns = target.columns().iterator();

        // Act
        final int numberOfColumns = StreamSupport.stream(Spliterators.spliteratorUnknownSize(columns, Spliterator.ORDERED), false).mapToInt(column -> 1).sum();

        // Assert
        assertEquals(driver.numberOfColumns(), numberOfColumns);
    }

    @Test
    void rows_iteratorsOverEachRow() {
        // Arrange
        final SizedTable target = createTarget();
        final Iterator<Table.Row> rows = target.rows().iterator();

        // Act
        final int numberOfRows = StreamSupport.stream(Spliterators.spliteratorUnknownSize(rows, Spliterator.ORDERED), false).mapToInt(row -> 1).sum();

        // Assert
        assertEquals(driver.numberOfRows(), numberOfRows);
    }

    @Test
    void getColumn_givenValidCoordinates_returnsColumn() {
        // Arrange
        SizedTable target = createTarget();
        driver.getCell(0, 0).setValue("A1");

        // Act
        final Table.Column result = target.getColumn(0);

        // Assert
        assertEquals(0, result.getIndex());
        assertEquals(6, result.size());
        assertEquals("A1", result.get(0).getValue());
    }

    @Test
    void getColumn_givenNegativeColumn_throwsException() {
        // Arrange
        SizedTable target = createTarget();

        // Act
        final IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> target.getColumn(-1));

        // Assert
        assertEquals("Column must be positive", result.getMessage());
    }

    @Test
    void getColumn_givenColumnOutOfBounds_throwsException() {
        // Arrange
        SizedTable target = createTarget();

        // Act
        final IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> target.getColumn(driver.numberOfColumns()));

        // Assert
        assertEquals("Column must be less than number of columns", result.getMessage());
    }

    @Test
    void getRow_givenValidCoordinates_returnsRow() {
        // Arrange
        SizedTable target = createTarget();
        driver.getCell(0, 0).setValue("A1");

        // Act
        final Table.Row result = target.getRow(0);

        // Assert
        assertEquals(0, result.getIndex());
        assertEquals(2, result.size());
        assertEquals("A1", result.get(0).getValue());
    }

    @Test
    void getRow_givenNegativeRow_throwsException() {
        // Arrange
        SizedTable target = createTarget();

        // Act
        final IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> target.getRow(-1));

        // Assert
        assertEquals("Row must be positive", result.getMessage());
    }

    @Test
    void getRow_givenRowOutOfBounds_throwsException() {
        // Arrange
        SizedTable target = createTarget();

        // Act
        final IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () -> target.getRow(driver.numberOfRows()));

        // Assert
        assertEquals("Row must be less than number of rows", result.getMessage());
    }
}