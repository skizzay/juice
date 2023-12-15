package io.nuvolo.juice.business.application;

import io.cucumber.spring.CucumberContextConfiguration;
import io.nuvolo.juice.business.model.*;
import io.nuvolo.juice.infrastructure.selenium.SeleniumConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CucumberContextConfiguration
@SpringBootTest(classes = {HappyPathTestConfiguration.class, FakeConfiguration.class, SeleniumConfiguration.class})
@DirtiesContext
public class HappyPathTestConfiguration {
    @Bean
    public Screen startingScreen(@Qualifier("startingScreenFields") Collection<? extends ReadWriteField> fields, @Qualifier("requests") Collection<Action> requests) {
        final BasicScreenBuilder builder = new BasicScreenBuilder(new ScreenName("Starting Screen"));
        fields.forEach(f -> builder.addReadWriteField(f.getFieldName(), f));
        requests.forEach(request -> builder.addRequest(request.getName(), request));
        return builder.build();
    }

    @Bean
    public List<Navigation> navigationList() {
        return List.of(
                new Navigation(null, ScreenName.of("Screen 1"), Action.noOp(ActionName.of("Screen 1")))
        );
    }

    @Bean
    public Map<ScreenName, Screen> screensTable(Screen... screens) {
        final Map<ScreenName, Screen> screenMap = new HashMap<>();
        for (final Screen screen : screens) {
            screenMap.put(screen.getName(), screen);
        }
        return screenMap;
    }

    @Bean
    public ScreenNavigator navigationTable(Map<ScreenName, Screen> screens, Iterable<Navigation> navigations) {
        return new GraphBasedScreenNavigator(screens, NavigationTable.from(screens.keySet(), navigations));
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
                Map.of()
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
