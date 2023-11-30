package io.nuvolo.juice.business.model;

import java.util.Optional;

public interface Container {
    Optional<ReadableField> getReadableField(FieldName name);
    Optional<WriteableField> getWriteableField(FieldName name);

    Optional<Container> getContainer(FieldName name);
}
