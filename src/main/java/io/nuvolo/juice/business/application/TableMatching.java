package io.nuvolo.juice.business.application;

import io.cucumber.datatable.DataTable;

import java.util.HashSet;

public interface TableMatching {
    void match(DataTable actual, DataTable expected);

    enum Type implements TableMatching {
        ORDERED {
            @Override
            public void match(DataTable actual, DataTable expected) {
                expected.diff(actual);
            }
        },
        UNORDERED {
            @Override
            public void match(DataTable actual, DataTable expected) {
                expected.unorderedDiff(actual);
            }
        },
        CONTAINS {
            @Override
            public void match(DataTable actual, DataTable expected) {
                final var actualRows = actual.cells();
                if (!new HashSet<>(actualRows).containsAll(expected.cells())) {
                    throw new AssertionError("Expected rows [" + expected + "] were not found in actual rows [" + actualRows + "]");
                }
            }
        }
    }
}
