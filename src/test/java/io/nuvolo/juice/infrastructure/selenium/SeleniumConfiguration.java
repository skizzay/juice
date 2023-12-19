package io.nuvolo.juice.infrastructure.selenium;

import io.nuvolo.juice.business.application.FakeField;
import io.nuvolo.juice.business.model.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Collection;
import java.util.List;

@SpringBootConfiguration
@DirtiesContext
@Profile("selenium")
public class SeleniumConfiguration {
    private final WebDriver webDriver = ChromeDriver.builder()
            .build();
    @Bean
    public WebDriver webDriver() {
        webDriver.get("https://duckduckgo.com");
        return webDriver;
    }

    @Bean("startingScreenFields")
    public Collection<Field> seleniumStartingScreenFields(SeleniumElementFinder elementFinder) {
        return List.of(
                new SeleniumReadWriteField(FieldName.of("alpha"), elementFinder, SeleniumValueExtractor.fromValueAttribute(), ValueValidator.alwaysValid()),
                new FakeField(FieldName.of("bravo")),
                new FakeField(FieldName.of("charlie")),
                new FakeField(FieldName.of("delta")),
                new FakeField(FieldName.of("echo"))
        );
    }

    @Bean
    public SeleniumElementFinder elementFinder(WebDriver webDriver) {
        return SeleniumElementFinder.builder(webDriver)
                .byName(FieldName.of("alpha"), "q")
                .byXPath(FieldName.of("submit button"), "//*[@id=\"searchbox_homepage\"]/div/div/button")
                .build();
    }

    @Bean("screen-1-navigation")
    public Action screen1Navigation(SeleniumElementFinder elementFinder) {
        return Action.from(ActionName.of("Screen 1 Navigation"), currentScreen -> SeleniumFieldUtils.click(elementFinder, FieldName.of("submit button")));
    }
}
