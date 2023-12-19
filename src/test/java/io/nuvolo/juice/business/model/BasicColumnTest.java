package io.nuvolo.juice.business.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicColumnTest {
    @Mock
    private Table table;
    @Mock private Table.Cell cell;
    private int column = 0;
    private Class<Table.Cell> cellType = Table.Cell.class;

    private BasicColumn<Table.Cell> createTarget() {
        return new BasicColumn<>(column, table, cellType);
    }

    @Test
    void constructor_givenNegativeRow_throwsIllegalArgumentException() {
        // Arrange
        column = -1;

        // Act
        final var exception = assertThrows(IllegalArgumentException.class, this::createTarget);

        // Assert
        assertEquals("column < 0", exception.getMessage());
    }

    @Test
    void constructor_givenNullTable_throwsNullPointerException() {
        // Arrange
        table = null;

        // Act
        final var exception = assertThrows(NullPointerException.class, this::createTarget);

        // Assert
        assertEquals("table cannot be null", exception.getMessage());
    }

    @Test
    void constructor_givenNullCellType_throwsNullPointerException() {
        // Arrange
        cellType = null;

        // Act
        final var exception = assertThrows(NullPointerException.class, this::createTarget);

        // Assert
        assertEquals("cellType cannot be null", exception.getMessage());
    }

    @Test
    void getColumn_returnsColumn() {
        // Arrange
        final var target = createTarget();

        // Act
        final var result = target.getColumn();

        // Assert
        assertEquals(column, result);
    }

    @Test
    void size_returnsRowCount() {
        // Arrange
        final var rowCount = 1;
        final var target = createTarget();
        when(table.getRowCount()).thenReturn(rowCount);

        // Act
        final var result = target.size();

        // Assert
        assertEquals(rowCount, result);
        verify(table, times(1)).getRowCount();
    }

    @Test
    void get_retrievesCellFromTable() {
        // Arrange
        final var target = createTarget();
        final var index = 2;
        when(table.getCell(index, column, cellType)).thenReturn(cell);

        // Act
        var result = target.get(index);

        // Assert
        assertEquals(cell, result);
        verify(table, times(1)).getCell(index, column, cellType);
    }
}