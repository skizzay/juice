package io.nuvolo.juice.business.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicRowTest {
    @Mock private Table table;
    @Mock private Table.Cell cell;
    private int row = 0;
    private Class<Table.Cell> cellType = Table.Cell.class;

    private BasicRow<Table.Cell> createTarget() {
        return new BasicRow<>(row, table, cellType);
    }

    @Test
    void constructor_givenNegativeRow_throwsIllegalArgumentException() {
        // Arrange
        row = -1;

        // Act
        final var exception = assertThrows(IllegalArgumentException.class, this::createTarget);

        // Assert
        assertEquals("row < 0", exception.getMessage());
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
    void getRow_returnsRow() {
        // Arrange
        final var target = createTarget();

        // Act
        final var result = target.getRow();

        // Assert
        assertEquals(row, result);
    }

    @Test
    void size_returnsColumnCount() {
        // Arrange
        final var columnCount = 1;
        final var target = createTarget();
        when(table.getColumnCount()).thenReturn(columnCount);

        // Act
        final var result = target.size();

        // Assert
        assertEquals(columnCount, result);
        verify(table, times(1)).getColumnCount();
    }

    @Test
    void get_retrievesCellFromTable() {
        // Arrange
        final var target = createTarget();
        final var index = 2;
        when(table.getCell(row, index, cellType)).thenReturn(cell);

        // Act
        var result = target.get(index);

        // Assert
        assertEquals(cell, result);
        verify(table, times(1)).getCell(row, index, cellType);
    }
}