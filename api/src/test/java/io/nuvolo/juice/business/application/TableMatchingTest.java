package io.nuvolo.juice.business.application;

import io.cucumber.datatable.DataTable;
import io.cucumber.datatable.TableDiffException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.*;

class TableMatchingTest {
    private final DataTable abcd = DataTable.create(List.of(
            List.of("a", "b"),
            List.of("c", "d")
    ));
    private final DataTable cdab = DataTable.create(List.of(
            List.of("c", "d"),
            List.of("a", "b")
    ));
    private final DataTable efgh = DataTable.create(List.of(
            List.of("e", "f"),
            List.of("g", "h")
    ));
    private final DataTable ab = DataTable.create(List.of(
            List.of("a", "b")
    ));

    @Test
    void ordered_givenSameTable_matches() {
        assertDoesNotThrow(() -> TableMatching.Type.ORDERED.match(abcd, abcd));
    }

    @Test
    void ordered_givenSameTableInDifferentOrder_throws() {
        // Arrange
        final var message = new StringJoiner(System.lineSeparator(), "", System.lineSeparator())
                .add("tables were different:")
                .add("    - | c | d |")
                .add("      | a | b |")
                .add("    + | c | d |")
                .toString();

        // Act
        final var exception = assertThrows(TableDiffException.class, () -> TableMatching.Type.ORDERED.match(abcd, cdab));

        // Assert
        assertEquals(message, exception.getMessage());
    }

    @Test
    void ordered_givenDifferentTable_throws() {
        // Arrange
        final var message = new StringJoiner(System.lineSeparator(), "", System.lineSeparator())
                .add("tables were different:")
                .add("    - | e | f |")
                .add("    - | g | h |")
                .add("    + | a | b |")
                .add("    + | c | d |")
                .toString();

        // Act
        final var exception = assertThrows(TableDiffException.class, () -> TableMatching.Type.ORDERED.match(abcd, efgh));

        // Assert
        assertEquals(message, exception.getMessage());
    }

    @Test
    void unordered_givenSameTable_matches() {
        assertDoesNotThrow(() -> TableMatching.Type.UNORDERED.match(abcd, abcd));
    }

    @Test
    void unordered_givenSameTableInDifferentOrder_matches() {
        assertDoesNotThrow(() -> TableMatching.Type.UNORDERED.match(abcd, cdab));
    }

    @Test
    void unordered_givenDifferentTable_throws() {
        // Arrange
        final var message = new StringJoiner(System.lineSeparator(), "", System.lineSeparator())
                .add("tables were different:")
                .add("    - | e | f |")
                .add("    - | g | h |")
                .add("    + | a | b |")
                .add("    + | c | d |")
                .toString();

        // Act
        final var exception = assertThrows(TableDiffException.class, () -> TableMatching.Type.UNORDERED.match(abcd, efgh));

        // Assert
        assertEquals(message, exception.getMessage());
    }

    @Test
    void contains_givenSameTable_matches() {
        assertDoesNotThrow(() -> TableMatching.Type.CONTAINS.match(abcd, abcd));
    }

    @Test
    void contains_givenSameTableInDifferentOrder_matches() {
        assertDoesNotThrow(() -> TableMatching.Type.CONTAINS.match(abcd, cdab));
    }

    @Test
    void contains_givenDifferentTable_throws() {
        // Arrange
        final var message = new StringJoiner(System.lineSeparator(), "", System.lineSeparator())
                .add("Expected rows | e | f |")
                .add("| g | h |")
                .add(" were not found in actual rows | a | b |")
                .add("| c | d |")
                .toString();

        // Act
        final var exception = assertThrows(AssertionError.class, () -> TableMatching.Type.CONTAINS.match(abcd, efgh));

        // Assert
        assertEquals(message, exception.getMessage());
    }

    @Test
    void contains_givenTableWithExtraRows_matches() {
        assertDoesNotThrow(() -> TableMatching.Type.CONTAINS.match(abcd, ab));
    }
}