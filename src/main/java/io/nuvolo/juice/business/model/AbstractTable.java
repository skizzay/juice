package io.nuvolo.juice.business.model;

import java.util.Objects;

public abstract class AbstractTable implements Table {
    private final FieldName tableName;

    protected AbstractTable(FieldName tableName) {
        this.tableName = Objects.requireNonNull(tableName, "tableName cannot be null");
    }

    @Override
    public FieldName getFieldName() {
        return tableName;
    }

    @Override
    public <T extends Cell> Row<T> getRow(int index, Class<T> type) {
        return new BasicRow<>(index, this, type);
    }

    @Override
    public <T extends Cell> Column<T> getColumn(int index, Class<T> type) {
        return new BasicColumn<>(index, this, type);
    }
}
