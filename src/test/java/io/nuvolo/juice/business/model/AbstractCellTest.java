package io.nuvolo.juice.business.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbstractCellTest {

    private static final class TestCell extends AbstractCell {
        TestCell(int row, int column) {
            super(row, column);
        }

        @Override
        FieldName getTableName() {
            return FieldName.of("test");
        }
    }

    @Test
    void constructor_givenNegativeRow_throwsIllegalArgumentException() {
        // Arrange
        final int row = -1;
        final int column = 0;

        // Act
        final var exception = assertThrows(IllegalArgumentException.class, () -> new TestCell(row, column));

        // Assert
        assertEquals("row must be >= 0", exception.getMessage());
    }

    @Test
    void constructor_givenNegativeColumn_throwsIllegalArgumentException() {
        // Arrange
        final int row = 0;
        final int column = -1;

        // Act
        final var exception = assertThrows(IllegalArgumentException.class, () -> new TestCell(row, column));

        // Assert
        assertEquals("column must be >= 0", exception.getMessage());
    }

    @Test
    void getFieldName_returnsFieldName() {
        // Arrange
        final int row = 0;
        final int column = 0;
        final var cell = new TestCell(row, column);

        // Act
        final var fieldName = cell.getFieldName();

        // Assert
        assertEquals(FieldName.of("test[0,0]"), fieldName);
    }
}