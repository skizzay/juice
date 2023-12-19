package io.nuvolo.juice.business.model;

import java.util.AbstractList;
import java.util.Objects;

public class BasicRow<T extends Table.Cell> extends AbstractList<T> implements Table.Row<T> {
    private final int row;
    private final Table table;
    private final Class<T> cellType;

    public BasicRow(int row, Table table, Class<T> cellType) {
        if (row < 0) {
            throw new IllegalArgumentException("row < 0");
        }
        this.row = row;
        this.table = Objects.requireNonNull(table, "table cannot be null");
        this.cellType = Objects.requireNonNull(cellType, "cellType cannot be null");
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public T get(int index) {
        return table.getCell(row, index, cellType);
    }

    @Override
    public int size() {
        return table.getColumnCount();
    }
}
