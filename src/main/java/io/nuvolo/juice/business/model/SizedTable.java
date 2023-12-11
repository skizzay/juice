package io.nuvolo.juice.business.model;

import java.util.*;

public class SizedTable implements Table {
    public interface Driver {
        int numberOfRows();
        int numberOfColumns();
        Cell getCell(int row, int column);
    }

    private final Driver driver;

    public SizedTable(Driver driver) {
        this.driver = Objects.requireNonNull(driver, "Driver cannot be null");
    }

    private final class ColumnImpl extends AbstractList<Cell> implements Column {
        private final int index;

        public ColumnImpl(int index) {
            this.index = index;
        }

        @Override
        public int getIndex() {
            return index;
        }

        @Override
        public Spliterator<Cell> spliterator() {
            return Spliterators.spliterator(this, Spliterator.ORDERED | Spliterator.SIZED);
        }

        @Override
        public Cell get(int index) {
            return SizedTable.this.getCell(index, this.index);
        }

        @Override
        public int size() {
            return SizedTable.this.numberOfRows();
        }
    }

    private final class RowImpl extends AbstractList<Cell> implements Row {
        private final int index;

        public RowImpl(int index) {
            this.index = index;
        }

        @Override
        public int getIndex() {
            return index;
        }

        @Override
        public Spliterator<Cell> spliterator() {
            return Spliterators.spliterator(this, Spliterator.ORDERED | Spliterator.SIZED);
        }

        @Override
        public Cell get(int index) {
            return SizedTable.this.getCell(this.index, index);
        }

        @Override
        public int size() {
            return SizedTable.this.numberOfColumns();
        }
    }

    private final class ColumnHeadersImpl extends AbstractList<Header> implements ColumnHeaders {
        @Override
        public Header get(int index) {
            return new Header() {
                @Override
                public int getIndex() {
                    return index;
                }

                @Override
                public String getValue() {
                    return SizedTable.this.getCell(0, index).getValue();
                }
            };
        }

        @Override
        public int size() {
            return SizedTable.this.numberOfColumns();
        }

        @Override
        public Row asRow() {
            return SizedTable.this.getRow(0);
        }

        @Override
        public Spliterator<Header> spliterator() {
            return Spliterators.spliterator(this, Spliterator.ORDERED | Spliterator.SIZED);
        }

        @Override
        public int indexOf(String headerName) {
            for (int i = 0; i < size(); i++) {
                if (get(i).getValue().equals(headerName)) {
                    return i;
                }
            }
            return -1;
        }
    }

    private final class RowHeadersImpl extends AbstractList<Header> implements RowHeaders {
        @Override
        public Header get(int index) {
            return new Header() {
                @Override
                public int getIndex() {
                    return index;
                }

                @Override
                public String getValue() {
                    return SizedTable.this.getCell(index, 0).getValue();
                }
            };
        }

        @Override
        public int size() {
            return SizedTable.this.numberOfRows();
        }

        @Override
        public Column asColumn() {
            return SizedTable.this.getColumn(0);
        }

        @Override
        public Spliterator<Header> spliterator() {
            return Spliterators.spliterator(this, Spliterator.ORDERED | Spliterator.SIZED);
        }

        @Override
        public int indexOf(String headerName) {
            for (int i = 0; i < size(); i++) {
                if (get(i).getValue().equals(headerName)) {
                    return i;
                }
            }
            return -1;
        }
    }

    @Override
    public ColumnHeaders getColumnHeaders() {
        return new ColumnHeadersImpl();
    }

    @Override
    public RowHeaders getRowHeaders() {
        return new RowHeadersImpl();
    }

    @Override
    public Row getRow(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Row must be positive");
        }
        else if (index >= numberOfRows()) {
            throw new IllegalArgumentException("Row must be less than number of rows");
        }
        else {
            return new RowImpl(index);
        }
    }

    @Override
    public Column getColumn(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Column must be positive");
        }
        else if (index >= numberOfColumns()) {
            throw new IllegalArgumentException("Column must be less than number of columns");
        }
        else {
            return new ColumnImpl(index);
        }
    }

    @Override
    public Iterable<Row> rows() {
        return () -> new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < numberOfRows();
            }

            @Override
            public Row next() {
                return getRow(index++);
            }
        };
    }

    @Override
    public Iterable<Column> columns() {
        return () -> new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < numberOfColumns();
            }

            @Override
            public Column next() {
                return getColumn(index++);
            }
        };
    }

    @Override
    public int numberOfRows() {
        return driver.numberOfRows();
    }

    @Override
    public int numberOfColumns() {
        return driver.numberOfColumns();
    }

    @Override
    public Cell getCell(int row, int column) {
        if (row < 0) {
            throw new IllegalArgumentException("Row must be positive");
        }
        else if (column < 0) {
            throw new IllegalArgumentException("Column must be positive");
        }
        else if (row >= numberOfRows()) {
            throw new IllegalArgumentException("Row must be less than number of rows");
        }
        else if (column >= numberOfColumns()) {
            throw new IllegalArgumentException("Column must be less than number of columns");
        }
        else {
            return driver.getCell(row, column);
        }
    }
}
