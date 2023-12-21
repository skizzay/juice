package io.nuvolo.juice.infrastructure.file;

import io.nuvolo.juice.business.model.*;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Collection;
import java.util.List;

@SpringBootConfiguration
@DirtiesContext
@Profile("text")
public class TextConfiguration {
    @Bean
    public TextStorage textStorage() {
        return new TextStorage(new BoxDimensions(80, 24));
    }

    @Bean
    TextTable textTable(TextStorage textStorage) {
        final TextTable table = new TextTable(FieldName.of("tango"), 4, 4, textStorage, new Point(0, 5), new BoxDimensions(20, 1));
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
    public Collection<Field> startingScreenFields(TextStorage textStorage, TextTable table) {
        return List.of(
                new TextBoxField(FieldName.of("alpha"), textStorage, new Point(0, 0), new BoxDimensions(40, 1)),
                new TextBoxField(FieldName.of("bravo"), textStorage, new Point(40, 0), new BoxDimensions(40, 1)),
                new TextBoxField(FieldName.of("charlie"), textStorage, new Point(20, 1), new BoxDimensions(20, 1)),
                new TextBoxField(FieldName.of("delta"), textStorage, new Point(60, 1), new BoxDimensions(20, 1)),
                new TextBoxField(FieldName.of("echo"), textStorage, new Point(0, 2), new BoxDimensions(80, 1)),
                table
            );
    }

    @Bean("screen-1-navigation")
    public Action screen1Navigation() {
        return Action.noOp(ActionName.of("Screen 1 Navigation"));
    }
}
