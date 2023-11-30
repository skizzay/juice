package io.nuvolo.juice.business.application;

import io.cucumber.spring.CucumberContextConfiguration;
import io.nuvolo.juice.business.model.*;
import io.nuvolo.juice.infrastructure.selenium.SeleniumConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@CucumberContextConfiguration
@SpringBootTest(classes = {HappyPathTestConfiguration.class, FakeConfiguration.class, SeleniumConfiguration.class})
@DirtiesContext
public class HappyPathTestConfiguration {
    @Bean
    public Screen startingScreen(@Qualifier("startingScreenFields") Collection<? extends ReadWriteField> fields, @Qualifier("requests") Collection<Action> requests, Map<ScreenName, Action> navigations) {
        final BasicScreenBuilder builder = new BasicScreenBuilder(new ScreenName("Starting Screen"));
        fields.forEach(f -> builder.addReadWriteField(f.getFieldName(), f));
        requests.forEach(request -> builder.addRequest(request.getName(), request));
        navigations.forEach(builder::addNavigableScreen);
        return builder.build();
    }

    @Bean
    public ScreenNavigator screenNavigator(Screen... screens) {
        final GraphBasedScreenNavigator navigator = new GraphBasedScreenNavigator();
        for (final Screen screen : screens) {
            navigator.addScreen(screen);
        }
        return navigator;
    }
    
    @Bean
    public UserInterface userInterface(@Qualifier("startingScreen") Screen startingScreen, ScreenNavigator screenNavigator) {
        return new BasicUserInterface(startingScreen, screenNavigator);
    }

    @Bean
    public Screen screen1() {
        return new BasicScreen(
                new ScreenName("Screen 1"),
                Map.of(),
                Map.of(),
                Map.of(),
                Map.of(),
                Map.of()
        );
    }

    @Bean
    public Map<ScreenName, Action> navigations(@Qualifier("screen-1-navigation") Action screen1Navigation) {
        return Map.of(
                new ScreenName("Screen 1"), screen1Navigation
        );
    }

    @Bean("new-order")
    public Action newOrder() {
        return Action.noOp(ActionName.of("New Order"));
    }

    @Bean("cancel-replace")
    public Action cancelReplace() {
        return Action.noOp(ActionName.of("Cancel-Replace"));
    }

    @Bean("new-transaction")
    public Action newTransaction() {
        return Action.noOp(ActionName.of("New Transaction"));
    }

    @Bean("update-account")
    public Action updateAccount() {
        return Action.noOp(ActionName.of("Update Account"));
    }

    @Bean("requests")
    public Collection<Action> requests(@Qualifier("new-order") Action newOrder,
                                       @Qualifier("cancel-replace") Action cancelReplace,
                                       @Qualifier("new-transaction") Action newTransaction,
                                       @Qualifier("update-account") Action updateAccount) {
        return List.of(
                newOrder,
                cancelReplace,
                newTransaction,
                updateAccount
        );
    }
}
