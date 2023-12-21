package io.nuvolo.juice.business.model;

public abstract class AbstractCell implements Table.Cell {
    private final int row;
    private final int column;

    protected AbstractCell(int row, int column) {
        if (row < 0) {
            throw new IllegalArgumentException("row must be >= 0");
        }
        else if (column < 0) {
            throw new IllegalArgumentException("column must be >= 0");
        }
        else {
            this.row = row;
            this.column = column;
        }
    }

    protected abstract FieldName getTableName();

    @Override
    public FieldName getFieldName() {
        return FieldName.of(getTableName() +
                "[" + row + "," + column + "]");
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return column;
    }
}
