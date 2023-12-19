package io.nuvolo.juice.infrastructure.selenium;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public interface SeleniumValueExtractor {
    String extractValue(WebElement webElement);

    static SeleniumValueExtractor fromAttribute(String attribute) {
        return webElement -> webElement.getAttribute(attribute);
    }

    static SeleniumValueExtractor fromValueAttribute() {
        return fromAttribute("value");
    }

    static SeleniumValueExtractor fromCheckedAttribute() {
        return fromAttribute("checked");
    }

    static SeleniumValueExtractor fromText() {
        return WebElement::getText;
    }

    static SeleniumValueExtractor fromSelectedOption() {
        return webElement -> new Select(webElement).getFirstSelectedOption().getText();
    }
}
