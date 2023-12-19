package io.nuvolo.juice.business.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasicContainerTest {
    @Mock private ReadableField readableField;
    @Mock private WriteableField writeableField;
    @Mock private ContainerField container;
    @Mock private Table table;
    @Mock private Table.Cell cell;

    private static final class CustomField implements ReadWriteField {
        private final FieldName fieldName;
        private String value = "";

        public CustomField(final FieldName name) {
            this.fieldName = name;
        }

        @Override
        public FieldName getFieldName() {
            return fieldName;
        }

        @Override
        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    private BasicContainer createTarget() {
        return new BasicContainer(Map.of(
                FieldName.of("readableField"), readableField,
                FieldName.of("writeableField"), writeableField,
                FieldName.of("container"), container,
                FieldName.of("table"), table
        ));
    }

    @Test
    void constructor_givenNullFields_throwsException() {
        // Act
        final Throwable result = assertThrows(NullPointerException.class, () -> new BasicContainer(null));

        // Assert
        assertEquals("Fields cannot be null", result.getMessage());
    }

    @Test
    void getReadableField_givenKnownFieldName_returnsField() {
        // Arrange
        final BasicContainer target = createTarget();

        // Act
        final Optional<ReadableField> result = target.getReadableField(new FieldName("readableField"));

        // Assert
        assertTrue(result.isPresent());
        assertEquals(readableField, result.get());
    }

    @Test
    void getReadableField_givenUnknownFieldName_returnsEmpty() {
        // Arrange
        final BasicContainer target = createTarget();

        // Act
        final Optional<ReadableField> result = target.getReadableField(new FieldName("unknown"));

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getReadableField_givenNullFieldName_throwsException() {
        // Arrange
        final BasicContainer target = createTarget();

        // Act
        final Throwable result = assertThrows(NullPointerException.class, () -> target.getReadableField(null));

        // Assert
        assertEquals("Field name cannot be null", result.getMessage());
    }

    @Test
    void getReadableField_givenNonreadableFieldName_isEmpty() {
        // Arrange
        final BasicContainer target = createTarget();

        // Act
        final var result = target.getReadableField(new FieldName("writeableField"));

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getWriteableField_givenKnownFieldName_returnsField() {
        // Arrange
        final BasicContainer target = createTarget();

        // Act
        final Optional<WriteableField> result = target.getWriteableField(new FieldName("writeableField"));

        // Assert
        assertTrue(result.isPresent());
        assertEquals(writeableField, result.get());
    }

    @Test
    void getWriteableField_givenUnknownFieldName_returnsEmpty() {
        // Arrange
        final BasicContainer target = createTarget();

        // Act
        final Optional<WriteableField> result = target.getWriteableField(new FieldName("unknown"));

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getWriteableField_givenNullFieldName_throwsException() {
        // Arrange
        final BasicContainer target = createTarget();

        // Act
        final Throwable result = assertThrows(NullPointerException.class, () -> target.getWriteableField(null));

        // Assert
        assertEquals("Field name cannot be null", result.getMessage());
    }

    @Test
    void getWriteableField_givenNonwriteableFieldName_isEmpty() {
        // Arrange
        final BasicContainer target = createTarget();

        // Act
        final var result = target.getWriteableField(new FieldName("readableField"));

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getContainer_givenKnownFieldName_returnsField() {
        // Arrange
        final BasicContainer target = createTarget();

        // Act
        final Optional<ContainerField> result = target.getContainer(new FieldName("container"));

        // Assert
        assertTrue(result.isPresent());
        assertEquals(container, result.get());
    }

    @Test
    void getContainer_givenUnknownFieldName_returnsEmpty() {
        // Arrange
        final BasicContainer target = createTarget();

        // Act
        final Optional<ContainerField> result = target.getContainer(new FieldName("unknown"));

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getContainer_givenNullFieldName_throwsException() {
        // Arrange
        final BasicContainer target = createTarget();

        // Act
        final Throwable result = assertThrows(NullPointerException.class, () -> target.getContainer(null));

        // Assert
        assertEquals("Field name cannot be null", result.getMessage());
    }

    @Test
    void getReadableField_givenCustomField_returnsField() {
        // Arrange
        final FieldName fieldName = new FieldName("customField");
        final CustomField customField = new CustomField(fieldName);
        final BasicContainer target = new BasicContainer(Map.of(fieldName, customField));

        // Act
        final Optional<CustomField> result = target.getField(fieldName, CustomField.class);

        // Assert
        assertTrue(result.isPresent());
    }

    @Test
    void getTable_givenKnownFieldName_returnsField() {
        // Arrange
        final BasicContainer target = createTarget();

        // Act
        final Optional<Table> result = target.getTable(new FieldName("table"));

        // Assert
        assertTrue(result.isPresent());
        assertEquals(table, result.get());
    }

    @Test
    void getTable_givenUnknownFieldName_returnsEmpty() {
        // Arrange
        final BasicContainer target = createTarget();

        // Act
        final Optional<Table> result = target.getTable(new FieldName("unknown"));

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getTable_givenNullFieldName_throwsException() {
        // Arrange
        final BasicContainer target = createTarget();

        // Act
        final Throwable result = assertThrows(NullPointerException.class, () -> target.getTable(null));

        // Assert
        assertEquals("Field name cannot be null", result.getMessage());
    }

    @Test
    void getField_givenTableCellReference_returnsTableCell() {
        // Arrange
        final BasicContainer target = createTarget();
        final FieldName fieldName = FieldName.of("table[1,2]");
        when(table.getCell(1, 2)).thenReturn(cell);

        // Act
        final Optional<Field> result = target.getField(fieldName);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(cell, result.get());
    }
}