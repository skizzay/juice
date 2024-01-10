package io.nuvolo.juice.infrastructure.selenium;

import io.nuvolo.juice.business.model.FieldName;
import io.nuvolo.juice.business.model.ReadableField;

import java.util.Objects;

public class SeleniumReadableField implements ReadableField {
    private final FieldName name;
    private final SeleniumElementFinder elementFinder;
    private final SeleniumValueExtractor valueExtractor;

    public SeleniumReadableField(FieldName name, SeleniumElementFinder elementFinder, SeleniumValueExtractor valueExtractor) {
        this.name = Objects.requireNonNull(name);
        this.elementFinder = Objects.requireNonNull(elementFinder);
        this.valueExtractor = Objects.requireNonNull(valueExtractor, "Value extractor cannot be null");
    }

    @Override
    public FieldName getFieldName() {
        return name;
    }

    @Override
    public String getValue() {
        return SeleniumFieldUtils.getValue(elementFinder, name, valueExtractor);
    }
}
