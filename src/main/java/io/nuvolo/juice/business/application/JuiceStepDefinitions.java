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

    private static IllegalArgumentException notFound(String type, FieldName fieldName) {
        return new IllegalArgumentException(type + " " + fieldName + " not found on the current screen");
    }

    private static IllegalArgumentException fieldNotFound(FieldName fieldName) {
        return notFound("Field", fieldName);
    }

    private static IllegalArgumentException tableNotFound(FieldName tableName) {
        return notFound("Table", tableName);
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
        final String value = userInterface.getPreviousState(ScreenName.of(screenName)).get(FieldName.of(screenFieldName));
        setField(fieldName, value);
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
        final String actualValue = getFieldValue(FieldName.of(fieldName));
        if (!comparison.compare(new BigDecimal(actualValue), expectedValue)) {
            throw new AssertionError("Field " + fieldName + " is not " + comparison + " " + expectedValue + " but " + actualValue);
        }
    }

    @Then("field {string} is {comparison} field {string}")
    public void checkField(String fieldName1, NumberComparison comparison, String fieldName2) {
        final String fieldValue1 = getFieldValue(FieldName.of(fieldName1));
        final String fieldValue2 = getFieldValue(FieldName.of(fieldName2));
        if (!comparison.compare(new BigDecimal(fieldValue1), new BigDecimal(fieldValue2))) {
            throw new AssertionError("Field " + fieldName1 + " is not " + comparison + " " + fieldName2 + " but " + fieldValue1);
        }
    }

    @Then("field {string} has been set to {string}")
    public void checkField(String fieldName, String expectedValue) {
        final String actualValue = getFieldValue(FieldName.of(fieldName));
        if (!expectedValue.equals(actualValue)) {
            throw new AssertionError("Field " + fieldName + " has not been set to " + expectedValue + " but to " + actualValue);
        }
    }

    @Then("field {string} has been set to {boolean}")
    public void checkField(String fieldName, boolean expectedValue) {
        checkField(fieldName, Boolean.toString(expectedValue));
    }

    @Then("field {string} has been set to {bigdecimal}")
    public void checkField(String fieldName, BigDecimal expectedValue) {
        checkField(fieldName, expectedValue.toString());
    }

    @Then("field {string} has been set to screen {string} field {string} value")
    public void checkField(String fieldName, String screenName, String screenFieldName) {
        final String expectedValue = userInterface.getPreviousState(ScreenName.of(screenName))
                .get(FieldName.of(screenFieldName));
        checkField(fieldName, expectedValue);
    }

    @Then("field {string} matches {string}")
    public void checkFieldMatchesRegex(String fieldName, String regex) {
        final String actualValue = getFieldValue(FieldName.of(fieldName));
        if (!actualValue.matches(regex)) {
            throw new AssertionError("Field " + fieldName + " does not match " + regex + " but is " + actualValue);
        }
    }

    @Then("field {string} does not match {string}")
    public void checkFieldNotMatchesRegex(String fieldName, String regex) {
        final String actualValue = getFieldValue(FieldName.of(fieldName));
        if (actualValue.matches(regex)) {
            throw new AssertionError("Field " + fieldName + " matches " + regex + " but should not");
        }
    }

    @Then("field {string} is empty")
    public void checkForBlankField(String fieldName) {
        final String actualValue = getFieldValue(FieldName.of(fieldName));
        if (!actualValue.isBlank()) {
            throw new AssertionError("Field " + fieldName + " is not blank but to " + actualValue);
        }
    }

    @Then("field {string} is not empty")
    public void checkForNotBlankField(String fieldName) {
        final String actualValue = getFieldValue(FieldName.of(fieldName));
        if (actualValue.isBlank()) {
            throw new AssertionError("Field " + fieldName + " is blank");
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
