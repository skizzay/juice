package io.nuvolo.juice.business.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MappedContainerTest {
    @Mock private ReadableField readableField;
    @Mock private WriteableField writeableField;
    @Mock private Container container;

    private MappedContainer createTarget() {
        return new MappedContainer(Map.of(
                new FieldName("readableField"), readableField
        ), Map.of(
                new FieldName("writeableField"), writeableField
        ), Map.of(
                new FieldName("container"), container
        ));
    }

    @Test
    void constructor_givenNullReadableFields_throwsException() {
        // Act
        final Throwable result = assertThrows(NullPointerException.class, () -> new MappedContainer(null, Map.of(), Map.of()));

        // Assert
        assertEquals("Readable fields cannot be null", result.getMessage());
    }

    @Test
    void constructor_givenNullWriteableFields_throwsException() {
        // Act
        final Throwable result = assertThrows(NullPointerException.class, () -> new MappedContainer(Map.of(), null, Map.of()));

        // Assert
        assertEquals("Writeable fields cannot be null", result.getMessage());
    }

    @Test
    void constructor_givenNullContainers_throwsException() {
        // Act
        final Throwable result = assertThrows(NullPointerException.class, () -> new MappedContainer(Map.of(), Map.of(), null));

        // Assert
        assertEquals("Containers cannot be null", result.getMessage());
    }

    @Test
    void getReadableField_givenKnownFieldName_returnsField() {
        // Arrange
        final MappedContainer target = createTarget();

        // Act
        final Optional<ReadableField> result = target.getReadableField(new FieldName("readableField"));

        // Assert
        assertTrue(result.isPresent());
        assertEquals(readableField, result.get());
    }

    @Test
    void getReadableField_givenUnknownFieldName_returnsEmpty() {
        // Arrange
        final MappedContainer target = createTarget();

        // Act
        final Optional<ReadableField> result = target.getReadableField(new FieldName("unknown"));

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getReadableField_givenNullFieldName_throwsException() {
        // Arrange
        final MappedContainer target = createTarget();

        // Act
        final Throwable result = assertThrows(NullPointerException.class, () -> target.getReadableField(null));

        // Assert
        assertEquals("Field name cannot be null", result.getMessage());
    }

    @Test
    void getWriteableField_givenKnownFieldName_returnsField() {
        // Arrange
        final MappedContainer target = createTarget();

        // Act
        final Optional<WriteableField> result = target.getWriteableField(new FieldName("writeableField"));

        // Assert
        assertTrue(result.isPresent());
        assertEquals(writeableField, result.get());
    }

    @Test
    void getWriteableField_givenUnknownFieldName_returnsEmpty() {
        // Arrange
        final MappedContainer target = createTarget();

        // Act
        final Optional<WriteableField> result = target.getWriteableField(new FieldName("unknown"));

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getWriteableField_givenNullFieldName_throwsException() {
        // Arrange
        final MappedContainer target = createTarget();

        // Act
        final Throwable result = assertThrows(NullPointerException.class, () -> target.getWriteableField(null));

        // Assert
        assertEquals("Field name cannot be null", result.getMessage());
    }

    @Test
    void getContainer_givenKnownFieldName_returnsField() {
        // Arrange
        final MappedContainer target = createTarget();

        // Act
        final Optional<Container> result = target.getContainer(new FieldName("container"));

        // Assert
        assertTrue(result.isPresent());
        assertEquals(container, result.get());
    }

    @Test
    void getContainer_givenUnknownFieldName_returnsEmpty() {
        // Arrange
        final MappedContainer target = createTarget();

        // Act
        final Optional<Container> result = target.getContainer(new FieldName("unknown"));

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getContainer_givenNullFieldName_throwsException() {
        // Arrange
        final MappedContainer target = createTarget();

        // Act
        final Throwable result = assertThrows(NullPointerException.class, () -> target.getContainer(null));

        // Assert
        assertEquals("Field name cannot be null", result.getMessage());
    }
}