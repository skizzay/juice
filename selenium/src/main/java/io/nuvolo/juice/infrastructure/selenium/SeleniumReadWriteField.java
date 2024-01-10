package io.nuvolo.juice.infrastructure.selenium;

import io.nuvolo.juice.business.model.FieldName;
import io.nuvolo.juice.business.model.ReadWriteField;
import io.nuvolo.juice.business.model.ValueValidator;

public class SeleniumReadWriteField implements ReadWriteField {
    private final FieldName name;
    private final SeleniumElementFinder elementFinder;
    private final SeleniumValueExtractor valueExtractor;
    private final ValueValidator valueValidator;

    public SeleniumReadWriteField(FieldName name, SeleniumElementFinder elementFinder, SeleniumValueExtractor valueExtractor, ValueValidator valueValidator) {
        this.name = name;
        this.elementFinder = elementFinder;
        this.valueExtractor = valueExtractor;
        this.valueValidator = valueValidator;
    }

    @Override
    public FieldName getFieldName() {
        return name;
    }

    @Override
    public String getValue() {
        return SeleniumFieldUtils.getValue(elementFinder, name, valueExtractor);
    }

    @Override
    public void setValue(String value) {
        SeleniumFieldUtils.setValue(elementFinder, name, valueValidator, value);
    }
}
