package io.nuvolo.juice.business.model;

import java.util.Map;

public interface Field {
    FieldName getFieldName();
    Map<FieldName, String> getState();
}
