package io.nuvolo.juice.business.model;

import java.util.AbstractList;
import java.util.Objects;

public class BasicColumn<T extends Table.Cell> extends AbstractList<T> implements Table.Column<T> {
    private final int column;
    private final Table table;
    private final Class<T> cellType;

    public BasicColumn(int column, Table table, Class<T> cellType) {
        if (column < 0) {
            throw new IllegalArgumentException("column < 0");
        }
        this.column = column;
        this.table = Objects.requireNonNull(table, "table cannot be null");
        this.cellType = Objects.requireNonNull(cellType, "cellType cannot be null");
    }

    @Override
    public int getColumn() {
        return column;
    }

    @Override
    public T get(int index) {
        return table.getCell(index, column, cellType);
    }

    @Override
    public int size() {
        return table.getRowCount();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BasicColumn<?> that = (BasicColumn<?>) o;
        return getColumn() == that.getColumn() && Objects.equals(table, that.table) && Objects.equals(cellType, that.cellType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getColumn(), table, cellType);
    }
}
