package io.nuvolo.juice.infrastructure.selenium;

import io.nuvolo.juice.business.model.FieldName;
import io.nuvolo.juice.business.model.ValueValidator;
import io.nuvolo.juice.business.model.WriteableField;

import java.util.Objects;

public class SeleniumWriteableField implements WriteableField {
    private final FieldName name;
    private final ValueValidator valueValidator;
    private final SeleniumElementFinder elementFinder;

    public SeleniumWriteableField(FieldName name, ValueValidator valueValidator, SeleniumElementFinder elementFinder) {
        this.name = Objects.requireNonNull(name);
        this.valueValidator = Objects.requireNonNull(valueValidator);
        this.elementFinder = Objects.requireNonNull(elementFinder);
    }

    @Override
    public FieldName getFieldName() {
        return name;
    }

    @Override
    public void setValue(String value) {
        SeleniumFieldUtils.setValue(elementFinder, name, valueValidator, value);
    }
}
