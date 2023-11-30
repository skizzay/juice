package io.nuvolo.juice.business.model;

public interface WriteableField {
    FieldName getFieldName();
    void setValue(String value);
}
