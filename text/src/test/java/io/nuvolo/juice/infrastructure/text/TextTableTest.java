package io.nuvolo.juice.infrastructure.text;

import io.nuvolo.juice.business.model.FieldName;
import io.nuvolo.juice.business.model.Table;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TextTableTest {
    private int rows = 4;
    private int columns = 6;
    private final Point position = new Point(3, 8);
    private final BoxDimensions cellSize = new BoxDimensions(8, 2);
    private final TextStorage textStorage = new MemoryTextStorage(new BoxDimensions(80, 24));

    private TextTable createTarget() {
        return new TextTable(FieldName.of("table"), rows, columns, textStorage, position, cellSize);
    }

    @Test
    void constructor_givenNegativeRows_throws() {
        // Arrange
        rows = -1;

        // Act
        final var exception = assertThrows(IllegalArgumentException.class, this::createTarget);

        // Assert
        assertEquals("rows must be greater than 0", exception.getMessage());
    }

    @Test
    void constructor_givenZeroRows_throws() {
        // Arrange
        rows = 0;

        // Act
        final var exception = assertThrows(IllegalArgumentException.class, this::createTarget);

        // Assert
        assertEquals("rows must be greater than 0", exception.getMessage());
    }

    @Test
    void constructor_givenNegativeColumns_throws() {
        // Arrange
        columns = -1;

        // Act
        final var exception = assertThrows(IllegalArgumentException.class, this::createTarget);

        // Assert
        assertEquals("columns must be greater than 0", exception.getMessage());
    }

    @Test
    void constructor_givenZeroColumns_throws() {
        // Arrange
        columns = 0;

        // Act
        final var exception = assertThrows(IllegalArgumentException.class, this::createTarget);

        // Assert
        assertEquals("columns must be greater than 0", exception.getMessage());
    }

    @Test
    void cellTableName_returnsNameOfTable() {
        // Arrange
        final var target = createTarget();

        // Act
        final Table.Cell cell = target.getCell(0, 0);

        // Assert
        assertEquals(FieldName.of("table[0,0]"), cell.getFieldName());
    }

    @Test
    void rows_returnsAllRows() {
        // Arrange
        final var target = createTarget();

        // Act
        final var actual = target.readRows();

        // Assert
        assertEquals(rows, actual.count());
    }

    @Test
    void columns_returnsAllColumns() {
        // Arrange
        final var target = createTarget();

        // Act
        final var actual = target.readColumns();

        // Assert
        assertEquals(columns, actual.count());
    }
}