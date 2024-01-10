package io.nuvolo.juice.business.model;

import java.util.Map;

public interface ReadableField extends Field {
    String getValue();

    default Map<FieldName, String> getState() {
        return Map.of(getFieldName(), getValue());
    }
}
