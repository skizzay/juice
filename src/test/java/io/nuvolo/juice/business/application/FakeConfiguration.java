package io.nuvolo.juice.business.application;

import io.nuvolo.juice.business.model.Action;
import io.nuvolo.juice.business.model.ActionName;
import io.nuvolo.juice.business.model.FieldName;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Collection;
import java.util.List;

@SpringBootConfiguration
@DirtiesContext
@Profile("fake")
public class FakeConfiguration {
    @Bean("startingScreenFields")
    public Collection<FakeField> startingScreenFields() {
        return List.of(
                new FakeField(FieldName.of("alpha")),
                new FakeField(FieldName.of("bravo")),
                new FakeField(FieldName.of("charlie")),
                new FakeField(FieldName.of("delta")),
                new FakeField(FieldName.of("echo"))
        );
    }

    @Bean("screen-1-navigation")
    public Action screen1Navigation() {
        return Action.noOp(ActionName.of("Screen 1 Navigation"));
    }
}
