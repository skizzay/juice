package io.nuvolo.juice.business.model;

import java.util.Optional;

public interface Container {
    Optional<Field> getField(FieldName name);
    default <T extends Field> Optional<T> getField(FieldName name, Class<T> type) {
        return getField(name).filter(type::isInstance).map(type::cast);
    }

    default Optional<ReadableField> getReadableField(FieldName name) {
        return getField(name, ReadableField.class);
    }

    default Optional<WriteableField> getWriteableField(FieldName name) {
        return getField(name, WriteableField.class);
    }

    default Optional<ContainerField> getContainer(FieldName name) {
        return getField(name, ContainerField.class);
    }

    default Optional<Table> getTable(FieldName name) {
        return getField(name, Table.class);
    }
}
