package io.nuvolo.juice.infrastructure.text;

import io.nuvolo.juice.business.model.AbstractCell;
import io.nuvolo.juice.business.model.AbstractTable;
import io.nuvolo.juice.business.model.FieldName;
import io.nuvolo.juice.business.model.TableUtilities;

import java.util.Objects;
import java.util.stream.Stream;

public class TextTable extends AbstractTable {
    private final Point position;
    private final BoxDimensions cellSize;
    private final TextStorage textStorage;

    public TextTable(FieldName tableName, int rows, int columns, TextStorage textStorage, Point position, BoxDimensions cellSize) {
        super(tableName, rows, columns);
        if (rows <= 0) {
            throw new IllegalArgumentException("rows must be greater than 0");
        }
        else if (columns <= 0) {
            throw new IllegalArgumentException("columns must be greater than 0");
        }
        else {
            this.position = Objects.requireNonNull(position, "position cannot be null");
            this.cellSize = Objects.requireNonNull(cellSize, "cell size cannot be null");
            this.textStorage = Objects.requireNonNull(textStorage, "text storage cannot be null");
        }
    }

    @Override
    protected Cell safeGetCell(int row, int column) {
        return new TextCell(row, column);
    }

    @Override
    public int getRowCount() {
        return maxRowsForStateCapture;
    }

    @Override
    public <T extends Cell> Stream<Row<T>> rows(Class<T> type) {
        return TableUtilities.streamRowsOfKnownSize(this, type);
    }

    @Override
    public int getColumnCount() {
        return maxColumnsForStateCapture;
    }

    @Override
    public <T extends Cell> Stream<Column<T>> columns(Class<T> type) {
        return TableUtilities.streamColumnsOfKnownSize(this, type);
    }

    private class TextCell extends AbstractCell implements ReadWriteCell {
        private TextCell(int row, int column) {
            super(row, column);
        }

        @Override
        public FieldName getTableName() {
            return TextTable.this.getFieldName();
        }

        @Override
        public String getValue() {
            return textStorage.getText(getPosition(), cellSize);
        }

        @Override
        public void setValue(String value) {
            textStorage.setText(value, getPosition(), cellSize);
        }

        private Point getPosition() {
            return new Point(getColumn() * cellSize.width() + position.x(), getRow() * cellSize.height() + position.y());
        }
    }
}
