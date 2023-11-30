Feature: Explore the various happy path scenarios for the framework

  Scenario: Setting field values as individual steps and check them as a table (Given/Then statements)
    Given Field "alpha" is set to "lorem ipsum"
    And Field "bravo" is set to "12.3"
    And Field "charlie" is set to 42
    And Field "delta" is set to -35.3
    And Field "echo" is set to true
    Then The following fields have been set:
      | alpha   | lorem ipsum |
      | bravo   | 12.3        |
      | charlie | 42          |
      | delta   | -35.3       |
      | echo    | true        |

  Scenario: Setting field values as a table and check them as individual steps (Given/Then statements)
    Given The following fields are set:
      | alpha   | lorem ipsum |
      | bravo   | 12.3        |
      | charlie | 42          |
      | delta   | -35.3       |
      | echo    | true        |
    Then Field "alpha" has been set to "lorem ipsum"
    And Field "bravo" has been set to "12.3"
    And Field "charlie" has been set to 42
    And Field "delta" has been set to "-35.3"
    And Field "echo" has been set to true

  Scenario: We can switch to a different screen
    Given I am on the "Screen 1" screen
    Then I should be on the "Screen 1" screen

  Scenario Outline: We can submit requests to the current screen
    When I submit a "<request type>" request

    Examples:
      | request type    |
      | New Order       |
      | Cancel-Replace  |
      | New Transaction |
      | Update Account  |
