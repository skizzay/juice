package io.nuvolo.juice.business.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public interface ContainerField extends Container, Field {
    @Override
    default Map<FieldName, String> getState() {
        return getFields()
                .map(Field::getState)
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .collect(
                        HashMap::new,
                        (map, entry) -> map.put(FieldName.of(getFieldName() + "." + entry.getKey()), entry.getValue()),
                        Map::putAll);
    }
}
