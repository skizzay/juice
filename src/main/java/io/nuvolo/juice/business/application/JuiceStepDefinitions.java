package io.nuvolo.juice.business.application;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.nuvolo.juice.business.model.ActionName;
import io.nuvolo.juice.business.model.FieldName;
import io.nuvolo.juice.business.model.ScreenName;
import io.nuvolo.juice.business.model.UserInterface;

import java.math.BigDecimal;
import java.util.Objects;

public class JuiceStepDefinitions {
    private final UserInterface userInterface;

    public JuiceStepDefinitions(UserInterface userInterface) {
        this.userInterface = Objects.requireNonNull(userInterface);
    }

    @ParameterType(value="true|false|True|False|TRUE|FALSE", name="boolean")
    public boolean booleanType(String value) {
        return Boolean.parseBoolean(value);
    }

    @Given("Field {string} is set to {string}")
    public void setField(String fieldName, String value) {
        userInterface.getCurrentScreen()
                .getWriteableField(FieldName.of(fieldName))
                .orElseThrow(() -> new IllegalArgumentException("Field " + fieldName + " not found on the current screen"))
                .setValue(value);
    }

    @Given("Field {string} is set to {boolean}")
    public void setField(String fieldName, boolean value) {
        setField(fieldName, Boolean.toString(value));
    }

    @Given("Field {string} is set to {bigdecimal}")
    public void setField(String fieldName, BigDecimal value) {
        setField(fieldName, value.toString());
    }

    @Given("The following fields are set:")
    public void setFields(DataTable dataTable) {
        dataTable.asMap().forEach(this::setField);
    }

    @Given("I am on the {string} screen")
    public void navigateToScreen(String screenName) {
        userInterface.navigateTo(new ScreenName(screenName));
    }

    @When("I submit a(n) {string} request")
    public void submitRequest(String requestName) {
        userInterface.getCurrentScreen()
                .performAction(ActionName.of(requestName));
    }

    @Then("Field {string} has been set to {string}")
    public void checkField(String fieldName, String expectedValue) {
        final String actualValue = userInterface.getCurrentScreen()
                .getReadableField(new FieldName(fieldName))
                .orElseThrow(() -> new IllegalArgumentException("Field " + fieldName + " not found on the current screen"))
                .getValue();
        if (!expectedValue.equals(actualValue)) {
            throw new AssertionError("Field " + fieldName + " has not been set to " + expectedValue + " but to " + actualValue);
        }
    }

    @Then("Field {string} has been set to {boolean}")
    public void checkField(String fieldName, boolean expectedValue) {
        checkField(fieldName, Boolean.toString(expectedValue));
    }

    @Then("Field {string} has been set to {bigdecimal}")
    public void checkField(String fieldName, BigDecimal expectedValue) {
        checkField(fieldName, expectedValue.toString());
    }

    @Then("The following fields have been set:")
    public void checkFields(DataTable dataTable) {
        dataTable.asMap().forEach(this::checkField);
    }

    @Then("I should be on the {string} screen")
    public void checkScreen(String expectedScreenName) {
        final String actualScreenName = userInterface.getCurrentScreen().getName().toString();
        if (!expectedScreenName.equals(actualScreenName)) {
            throw new AssertionError("Screen name is " + actualScreenName + " but should be " + expectedScreenName);
        }
    }
}
