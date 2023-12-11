package io.nuvolo.juice.business.model;

import java.util.List;

public interface Table {
    interface Cell {
        int getRow();
        int getColumn();
        String getValue();
        void setValue(String value);
    }

    interface Header {
        int getIndex();
        String getValue();
    }

    interface Row extends List<Cell> {
        int getIndex();
    }

    interface Column extends List<Cell> {
        int getIndex();
    }
    interface ColumnHeaders extends List<Header> {
        Row asRow();
        int indexOf(String headerName);
    }

    interface RowHeaders extends List<Header> {
        Column asColumn();
        int indexOf(String headerName);
    }

    ColumnHeaders getColumnHeaders();
    RowHeaders getRowHeaders();
    Row getRow(int index);
    Column getColumn(int index);
    Iterable<Row> rows();
    Iterable<Column> columns();
    int numberOfRows();
    int numberOfColumns();
    Cell getCell(int row, int column);
}