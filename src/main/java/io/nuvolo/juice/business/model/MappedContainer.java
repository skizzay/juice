package io.nuvolo.juice.business.model;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class MappedContainer implements Container {
    private final Map<FieldName, ReadableField> readableFields;
    private final Map<FieldName, WriteableField> writeableFields;
    private final Map<FieldName, Container> containers;

    public MappedContainer(Map<FieldName, ReadableField> readableFields,
                           Map<FieldName, WriteableField> writeableFields,
                           Map<FieldName, Container> containers) {
        this.readableFields = Objects.requireNonNull(readableFields, "Readable fields cannot be null");
        this.writeableFields = Objects.requireNonNull(writeableFields, "Writeable fields cannot be null");
        this.containers = Objects.requireNonNull(containers, "Containers cannot be null");
    }

    @Override
    public Optional<ReadableField> getReadableField(FieldName name) {
        Objects.requireNonNull(name, "Field name cannot be null");
        return Optional.ofNullable(readableFields.get(name));
    }

    @Override
    public Optional<WriteableField> getWriteableField(FieldName name) {
        Objects.requireNonNull(name, "Field name cannot be null");
        return Optional.ofNullable(writeableFields.get(name));
    }

    @Override
    public Optional<Container> getContainer(FieldName name) {
        Objects.requireNonNull(name, "Field name cannot be null");
        return Optional.ofNullable(containers.get(name));
    }
}
