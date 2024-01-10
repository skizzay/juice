package io.nuvolo.juice.infrastructure.selenium;

import io.nuvolo.juice.business.model.FieldName;
import io.nuvolo.juice.business.model.ValueValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SeleniumFieldUtilsTest {
    private final WebDriver webDriver = ChromeDriver.builder()
            .build();

    @BeforeEach
    void setUp() {
        webDriver.get("https://duckduckgo.com");
    }

    @Test
    void value_canBeSetAndRetrieved() {
        // Arrange
        final var expectedValue = "Hello, World!";
        final var target = SeleniumElementFinder.builder(webDriver)
                .byName(FieldName.of("search"), "q")
                .build();

        // Act
        SeleniumFieldUtils.setValue(target, FieldName.of("search"), ValueValidator.alwaysValid(), expectedValue);
        final var actualValue = SeleniumFieldUtils.getValue(target, FieldName.of("search"), webElement -> webElement.getAttribute("value"));

        // Assert
        assertEquals(expectedValue, actualValue);
    }
}