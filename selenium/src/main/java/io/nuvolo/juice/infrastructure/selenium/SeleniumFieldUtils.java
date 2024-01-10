package io.nuvolo.juice.infrastructure.selenium;

import io.nuvolo.juice.business.model.FieldName;
import io.nuvolo.juice.business.model.ValueValidator;
import org.openqa.selenium.WebElement;

class SeleniumFieldUtils {
    private SeleniumFieldUtils() {
    }
    public static String getValue(SeleniumElementFinder elementFinder, FieldName name, SeleniumValueExtractor seleniumValueExtractor) {
        final WebElement webElement = elementFinder.find(name)
                .orElseThrow(() -> new ElementNotFound(name));
        return seleniumValueExtractor.extractValue(webElement);
    }

    public static void setValue(SeleniumElementFinder elementFinder, FieldName name, ValueValidator valueValidator, String value) {
        valueValidator.validate(value);
        final WebElement webElement = elementFinder.find(name)
                .orElseThrow(() -> new ElementNotFound(name));
        webElement.sendKeys(value);
    }

    public static void click(SeleniumElementFinder elementFinder, FieldName name) {
        final WebElement webElement = elementFinder.find(name)
                .orElseThrow(() -> new ElementNotFound(name));
        webElement.click();
    }
}
