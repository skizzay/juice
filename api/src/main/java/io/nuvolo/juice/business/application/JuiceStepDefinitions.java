package io.nuvolo.juice.business.application;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.nuvolo.juice.business.model.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class JuiceStepDefinitions {
    private final UserInterface userInterface;

    public JuiceStepDefinitions(UserInterface userInterface) {
        this.userInterface = Objects.requireNonNull(userInterface, "user interface cannot be null");
    }

    @ParameterType(value="true|false|True|False|TRUE|FALSE", name="boolean")
    public boolean booleanType(String value) {
        return Boolean.parseBoolean(Character.toUpperCase(value.charAt(0)) + value.substring(1).toLowerCase());
    }

    @ParameterType(value="ordered|unordered|contains", name="tablematching")
    public TableMatching tableMatching(String value) {
        return TableMatching.Type.valueOf(value.toUpperCase());
    }

    @ParameterType(value="equal to|greater than|less than|greater than or equal to|less than or equal to|not equal to", name="comparison")
    public NumberComparison numberComparison(String value) {
        return NumberComparison.Type.valueOf(value.toUpperCase().replace(" ", "_"));
    }

    private static IllegalArgumentException notFound(String type, FieldName fieldName, ScreenName screenName) {
        return new IllegalArgumentException(type + " " + fieldName + " not found on the " + screenName + " screen");
    }

    private static IllegalArgumentException fieldNotFound(FieldName fieldName, ScreenName screenName) {
        return notFound("Field", fieldName, screenName);
    }

    private IllegalArgumentException fieldNotFound(FieldName fieldName) {
        return fieldNotFound(fieldName, userInterface.getCurrentScreen().getScreenName());
    }

    private IllegalArgumentException tableNotFound(FieldName tableName) {
        return notFound("Table", tableName, userInterface.getCurrentScreen().getScreenName());
    }

    @Given("field {string} is set to {string}")
    public void setField(String fieldName, String value) {
        userInterface.getCurrentScreen()
                .getWriteableField(FieldName.of(fieldName))
                .orElseThrow(() -> fieldNotFound(FieldName.of(fieldName)))
                .setValue(value);
    }

    @Given("field {string} is set to {boolean}")
    public void setField(String fieldName, boolean value) {
        setField(fieldName, Boolean.toString(value));
    }

    @Given("field {string} is set to {bigdecimal}")
    public void setField(String fieldName, BigDecimal value) {
        setField(fieldName, value.toString());
    }

    @Given("field {string} is set from screen {string} field {string} value")
    public void setFieldFromScreen(String fieldName, String screenName, String screenFieldName) {
        final String value =  userInterface.getPreviousState(ScreenName.of(screenName))
                .orElseThrow(() -> new IllegalArgumentException("Screen " + screenName + " not found"))
                .get(FieldName.of(screenFieldName));
        setField(
                fieldName,
                Optional.ofNullable(value)
                        .orElseThrow(() -> fieldNotFound(FieldName.of(screenFieldName), ScreenName.of(screenName))));
    }

    @Given("the following fields are set:")
    public void setFields(DataTable dataTable) {
        dataTable.asMap().forEach(this::setField);
    }

    @Given("I am on the {string} screen")
    public void navigateToScreen(String screenName) {
        userInterface.navigateTo(ScreenName.of(screenName));
    }

    @When("I submit a(n) {string} request")
    public void submitRequest(String requestName) {
        userInterface.performAction(ActionName.of(requestName));
    }

    public String getFieldValue(FieldName fieldName) {
        Objects.requireNonNull(fieldName, "fieldName must not be null");
        return userInterface.getCurrentScreen()
                .getReadableField(fieldName)
                .orElseThrow(() -> fieldNotFound(fieldName))
                .getValue();
    }

    @Then("field {string} is {comparison} {bigdecimal}")
    public void checkField(String fieldName, NumberComparison comparison, BigDecimal expectedValue) {
        final var actualValue = new BigDecimal(getFieldValue(FieldName.of(fieldName)));
        if (!comparison.compare(actualValue, expectedValue)) {
            comparison.fail(FieldName.of(fieldName), actualValue, expectedValue);
        }
    }

    @Then("field {string} is {comparison} field {string}")
    public void checkField(String fieldName1, NumberComparison comparison, String fieldName2) {
        final BigDecimal fieldValue1 = new BigDecimal(getFieldValue(FieldName.of(fieldName1)));
        final BigDecimal fieldValue2 = new BigDecimal(getFieldValue(FieldName.of(fieldName2)));
        if (!comparison.compare(fieldValue1, fieldValue2)) {
            comparison.fail(FieldName.of(fieldName1), fieldValue1, FieldName.of(fieldName2), fieldValue2);
        }
    }

    @Then("field {string} is equal to {string}")
    public void checkField(String fieldName, String expectedValue) {
        final String actualValue = getFieldValue(FieldName.of(fieldName));
        if (!expectedValue.equals(actualValue)) {
            throw new AssertionError(String.format("Field %s[%s] is not equal to %s", fieldName, actualValue, expectedValue));
        }
    }

    @Then("field {string} is equal to {boolean}")
    public void checkField(String fieldName, boolean expectedValue) {
        checkField(fieldName, Boolean.toString(expectedValue));
    }

    @Then("field {string} has been set to screen {string} field {string} value")
    public void checkField(String fieldName, String screenName, String screenFieldName) {
        final String expectedValue = userInterface.getPreviousState(ScreenName.of(screenName))
                .orElseThrow(() -> new IllegalArgumentException("Screen " + screenName + " not found"))
                .get(FieldName.of(screenFieldName));
        checkField(
                fieldName,
                Optional.ofNullable(expectedValue)
                        .orElseThrow(() -> fieldNotFound(FieldName.of(screenFieldName), ScreenName.of(screenName))));
    }

    @Then("field {string} matches {string}")
    public void checkFieldMatchesRegex(String fieldName, String regex) {
        final String actualValue = getFieldValue(FieldName.of(fieldName));
        if (!actualValue.matches(regex)) {
            throw new AssertionError(String.format("Field %s[%s] does not match regex:%s", fieldName, actualValue, regex));
        }
    }

    @Then("field {string} does not match {string}")
    public void checkFieldNotMatchesRegex(String fieldName, String regex) {
        final String actualValue = getFieldValue(FieldName.of(fieldName));
        if (actualValue.matches(regex)) {
            throw new AssertionError(String.format("Field %s[%s] matches regex:%s", fieldName, actualValue, regex));
        }
    }

    @Then("field {string} is empty")
    public void checkForBlankField(String fieldName) {
        final String actualValue = getFieldValue(FieldName.of(fieldName));
        if (!actualValue.isBlank()) {
            throw new AssertionError(String.format("Field %s[%s] is not blank", fieldName, actualValue));
        }
    }

    @Then("field {string} is not empty")
    public void checkForNotBlankField(String fieldName) {
        final String actualValue = getFieldValue(FieldName.of(fieldName));
        if (actualValue.isBlank()) {
            throw new AssertionError(String.format("Field %s is blank", fieldName));
        }
    }

    @Then("the following fields have been set:")
    public void checkFields(DataTable dataTable) {
        dataTable.asMap().forEach(this::checkField);
    }

    @Then("I should be on the {string} screen")
    public void checkScreen(String expectedScreenName) {
        final String actualScreenName = userInterface.getCurrentScreen().getScreenName().toString();
        if (!expectedScreenName.equals(actualScreenName)) {
            throw new AssertionError("Screen name is " + actualScreenName + " but should be " + expectedScreenName);
        }
    }

    @Then("the {string} table should match - {tablematching}")
    public void checkTable(String tableName, TableMatching tableMatching, DataTable expectedTable) {
        final List<List<String>> actualTable = userInterface.getCurrentScreen()
                .getTable(FieldName.of(tableName))
                .orElseThrow(() -> tableNotFound(FieldName.of(tableName)))
                .readRows()
                .map(TableUtilities::asCellValues)
                .toList();
        tableMatching.match(DataTable.create(actualTable), expectedTable);
    }
}
