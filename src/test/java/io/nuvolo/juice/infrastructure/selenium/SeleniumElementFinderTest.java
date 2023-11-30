package io.nuvolo.juice.infrastructure.selenium;

import io.nuvolo.juice.business.model.FieldName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SeleniumElementFinderTest {
    private WebDriver webDriver;

    @BeforeEach
    void setUp() {
        webDriver = ChromeDriver.builder()
                .build();
        webDriver.get("https://duckduckgo.com");
    }

    private SeleniumElementFinder.Builder createTargetBuilder() {
        return SeleniumElementFinder.builder(webDriver);
    }

    @Test
    void find_byNameLocator_returnsWebElement() {
        // Arrange
        final var target = createTargetBuilder()
                .byName(FieldName.of("search"), "q")
                .build();

        // Act
        final var result = target.find(FieldName.of("search"));

        // Assert
        assertTrue(result.isPresent());
    }
}