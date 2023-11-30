package io.nuvolo.juice.infrastructure.selenium;

import io.nuvolo.juice.business.model.FieldName;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class SeleniumElementFinder {
    private final WebDriver webDriver;
    private final Map<FieldName, Provider> providers;

    private interface Provider {
        WebElement getWebElement(WebDriver webDriver);
    }

    public static final class Builder {
        private final WebDriver webDriver;
        private final Map<FieldName, Provider> providers = new HashMap<>();

        private Builder(WebDriver webDriver) {
            this.webDriver = Objects.requireNonNull(webDriver);
        }

        private Builder(WebDriver webDriver, Map<FieldName, Provider> providers) {
            this.webDriver = Objects.requireNonNull(webDriver);
            this.providers.putAll(providers);
        }

        public Builder byId(FieldName fieldName, String id) {
            Objects.requireNonNull(fieldName);
            Objects.requireNonNull(id);
            final Builder builder = new Builder(webDriver, providers);
            builder.providers.put(fieldName, webDriver -> webDriver.findElement(By.id(id)));
            return builder;
        }

        public Builder byLinkText(FieldName fieldName, String linkText) {
            Objects.requireNonNull(fieldName);
            Objects.requireNonNull(linkText);
            final Builder builder = new Builder(webDriver, providers);
            builder.providers.put(fieldName, webDriver -> webDriver.findElement(By.linkText(linkText)));
            return builder;
        }

        public Builder byPartialLinkText(FieldName fieldName, String partialLinkText) {
            Objects.requireNonNull(fieldName);
            Objects.requireNonNull(partialLinkText);
            final Builder builder = new Builder(webDriver, providers);
            builder.providers.put(fieldName, webDriver -> webDriver.findElement(By.partialLinkText(partialLinkText)));
            return builder;
        }

        public Builder byName(FieldName fieldName, String name) {
            Objects.requireNonNull(fieldName);
            Objects.requireNonNull(fieldName);
            final Builder builder = new Builder(webDriver, providers);
            builder.providers.put(fieldName, webDriver -> webDriver.findElement(By.name(name)));
            return builder;
        }

        public Builder byTagName(FieldName fieldName, String tagName) {
            Objects.requireNonNull(fieldName);
            Objects.requireNonNull(tagName);
            final Builder builder = new Builder(webDriver, providers);
            builder.providers.put(fieldName, webDriver -> webDriver.findElement(By.tagName(tagName)));
            return builder;
        }

        public Builder byXPath(FieldName fieldName, String xPath) {
            Objects.requireNonNull(fieldName);
            Objects.requireNonNull(xPath);
            final Builder builder = new Builder(webDriver, providers);
            builder.providers.put(fieldName, webDriver -> webDriver.findElement(By.xpath(xPath)));
            return builder;
        }

        public Builder byClassName(FieldName fieldName, String className) {
            Objects.requireNonNull(fieldName);
            Objects.requireNonNull(className);
            final Builder builder = new Builder(webDriver, providers);
            builder.providers.put(fieldName, webDriver -> webDriver.findElement(By.className(className)));
            return builder;
        }

        public Builder byCssSelector(FieldName fieldName, String cssSelector) {
            Objects.requireNonNull(fieldName);
            Objects.requireNonNull(cssSelector);
            final Builder builder = new Builder(webDriver, providers);
            builder.providers.put(fieldName, webDriver -> webDriver.findElement(By.cssSelector(cssSelector)));
            return builder;
        }

        public SeleniumElementFinder build() {
            return new SeleniumElementFinder(webDriver, providers);
        }
    }

    private SeleniumElementFinder(WebDriver webDriver, Map<FieldName, Provider> providers) {
        this.webDriver = Objects.requireNonNull(webDriver);
        this.providers = Objects.requireNonNull(providers);
    }

    public static Builder builder(WebDriver webDriver) {
        return new Builder(webDriver);
    }

    public Optional<WebElement> find(FieldName name) {
        return Optional.ofNullable(providers.get(name))
                .map(provider -> provider.getWebElement(webDriver));
    }
}
