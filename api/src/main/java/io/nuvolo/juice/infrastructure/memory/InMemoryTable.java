package io.nuvolo.juice.infrastructure.memory;

import io.nuvolo.juice.business.model.*;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;

public class InMemoryTable extends AbstractTable {
    private class CellImpl extends AbstractCell implements ReadWriteCell {
        private CellImpl(int row, int column) {
            super(row, column);
        }

        @Override
        protected FieldName getTableName() {
            return InMemoryTable.this.getFieldName();
        }

        @Override
        public String getValue() {
            return values.get(getRow() * getColumnCount() + getColumn());
        }

        @Override
        public void setValue(String value) {
            Objects.requireNonNull(value, "value cannot be null");
            values.set(getRow() * getColumnCount() + getColumn(), value);
        }
    }

    private final ArrayList<String> values = new ArrayList<>();

    public InMemoryTable(FieldName tableName, int maxRowsForStateCapture, int maxColumnsForStateCapture) {
        super(tableName, maxRowsForStateCapture, maxColumnsForStateCapture);
        for (int i = 0; i < size(); i++) {
            values.add("");
        }
    }

    @Override
    protected Table.Cell safeGetCell(int row, int column) {
        return new CellImpl(row, column);
    }

    @Override
    public int getRowCount() {
        return maxRowsForStateCapture;
    }

    @Override
    public <T extends Table.Cell> Stream<Row<T>> rows(Class<T> type) {
        return TableUtilities.streamRowsOfKnownSize(this, type);
    }

    @Override
    public int getColumnCount() {
        return maxColumnsForStateCapture;
    }

    @Override
    public <T extends Table.Cell> Stream<Column<T>> columns(Class<T> type) {
        return TableUtilities.streamColumnsOfKnownSize(this, type);
    }
}
