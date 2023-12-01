package io.nuvolo.juice.business.model;

import java.util.Optional;

public interface Container {
    Optional<ReadableField> getReadableField(FieldName name);
    default <T extends ReadableField> Optional<T> getReadableField(FieldName name, Class<T> type) {
        return getReadableField(name).filter(type::isInstance).map(type::cast);
    }

    Optional<WriteableField> getWriteableField(FieldName name);
    default <T extends WriteableField> Optional<T> getWriteableField(FieldName name, Class<T> type) {
        return getWriteableField(name).filter(type::isInstance).map(type::cast);
    }

    Optional<Container> getContainer(FieldName name);
    default <T extends Container> Optional<T> getContainer(FieldName name, Class<T> type) {
        return getContainer(name).filter(type::isInstance).map(type::cast);
    }
}
