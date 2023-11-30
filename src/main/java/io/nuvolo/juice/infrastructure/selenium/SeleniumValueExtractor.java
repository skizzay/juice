package io.nuvolo.juice.infrastructure.selenium;

import org.openqa.selenium.WebElement;

public interface SeleniumValueExtractor {
    String extractValue(WebElement webElement);

    static SeleniumValueExtractor fromAttribute(String attribute) {
        return webElement -> webElement.getAttribute(attribute);
    }

    static SeleniumValueExtractor fromValueAttribute() {
        return fromAttribute("value");
    }

    static SeleniumValueExtractor fromText() {
        return WebElement::getText;
    }
}
