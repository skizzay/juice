package io.nuvolo.juice.business.application;

import io.nuvolo.juice.business.model.*;
import io.nuvolo.juice.infrastructure.memory.InMemoryTable;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Collection;
import java.util.List;

@SpringBootConfiguration
@DirtiesContext
public class FakeConfiguration {
    @Bean
    public Table table() {
        final Table table = new InMemoryTable(FieldName.of("tango"), 4, 4);
        Table.Row<Table.WriteableCell> row = table.writeRow(0);
        row.get(0).setValue("employee id");
        row.get(1).setValue("first name");
        row.get(2).setValue("last name");
        row.get(3).setValue("hire date");
        row = table.writeRow(1);
        row.get(0).setValue("1234");
        row.get(1).setValue("Jane");
        row.get(2).setValue("Smith");
        row.get(3).setValue("2023-12-20");
        row = table.writeRow(2);
        row.get(0).setValue("4321");
        row.get(1).setValue("John");
        row.get(2).setValue("Rambo");
        row.get(3).setValue("1947-12-05");
        row = table.writeRow(3);
        row.get(0).setValue("9876");
        row.get(1).setValue("John");
        row.get(2).setValue("McClane");
        row.get(3).setValue("1955-03-19");
        return table;
    }

    @Bean("startingScreenFields")
    public Collection<Field> startingScreenFields() {
        return List.of(
                new FakeField(FieldName.of("alpha")),
                new FakeField(FieldName.of("bravo")),
                new FakeField(FieldName.of("charlie")),
                new FakeField(FieldName.of("delta")),
                new FakeField(FieldName.of("echo")),
                table()
        );
    }

    @Bean("screen-1-navigation")
    public Action screen1Navigation() {
        return Action.noOp(ActionName.of("Screen 1 Navigation"));
    }
}
