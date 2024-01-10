package io.nuvolo.juice.business.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public interface Table extends Field {
    interface Cell extends Field {
        int getRow();
        int getColumn();
    }

    interface ReadableCell extends Cell, ReadableField {
    }

    interface WriteableCell extends Cell, WriteableField {
    }

    interface ReadWriteCell extends ReadableCell, WriteableCell {
    }

    interface Row<T extends Cell> extends List<T> {
        int getRow();
    }

    interface Column<T extends Cell> extends List<T> {
        int getColumn();
    }

    Cell getCell(int row, int column);
    default <T extends Cell> T getCell(int row, int column, Class<T> type) {
        return type.cast(getCell(row, column));
    }
    default ReadableCell getReadableCell(int row, int column) {
        return getCell(row, column, ReadableCell.class);
    }
    default WriteableCell getWriteableCell(int row, int column) {
        return getCell(row, column, WriteableCell.class);
    }

    <T extends Cell> Row<T> getRow(int index, Class<T> type);
    default Row<ReadableCell> readRow(int index) {
        return getRow(index, ReadableCell.class);
    }
    default Row<WriteableCell> writeRow(int index) {
        return getRow(index, WriteableCell.class);
    }
    default Row<ReadableCell> getHeaderRow() {
        return readRow(0);
    }
    int getRowCount();
    <T extends Cell> Stream<Row<T>> rows(Class<T> type);
    default Stream<Row<ReadableCell>> readRows() {
        return rows(ReadableCell.class);
    }
    default Stream<Row<WriteableCell>> writeRows() {
        return rows(WriteableCell.class);
    }

    <T extends Cell> Column<T> getColumn(int index, Class<T> type);
    default Column<ReadableCell> readColumn(int index) {
        return getColumn(index, ReadableCell.class);
    }
    default Column<WriteableCell> writeColumn(int index) {
        return getColumn(index, WriteableCell.class);
    }
    default Column<ReadableCell> getHeaderColumn() {
        return readColumn(0);
    }
    int getColumnCount();
    <T extends Cell> Stream<Column<T>> columns(Class<T> type);
    default Stream<Column<ReadableCell>> readColumns() {
        return columns(ReadableCell.class);
    }
    default Stream<Column<WriteableCell>> writeColumns() {
        return columns(WriteableCell.class);
    }

    default int size() {
        return Math.max(-1, getRowCount() * getColumnCount());
    }

    Map<FieldName, String> getState();
}
