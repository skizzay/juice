Feature: Explore the various happy path scenarios for the framework

  Scenario: Setting field values as individual steps and check them as a table (Given/Then statements)
    Given field "alpha" is set to "lorem ipsum"
    And field "bravo" is set to "12.3"
    And field "charlie" is set to 42
    And field "delta" is set to -35.3
    And field "echo" is set to true
    Then the following fields have been set:
      | alpha   | lorem ipsum |
      | bravo   | 12.3        |
      | charlie | 42          |
      | delta   | -35.3       |
      | echo    | true        |

  Scenario: Setting field values as a table and check them as individual steps (Given/Then statements)
    Given the following fields are set:
      | alpha   | lorem ipsum |
      | bravo   | 12.3        |
      | charlie | 42          |
      | delta   | -35.3       |
      | echo    | true        |
    Then field "alpha" has been set to "lorem ipsum"
    And field "bravo" has been set to "12.3"
    And field "charlie" has been set to 42
    And field "delta" has been set to "-35.3"
    And field "echo" has been set to true

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

  Scenario: We can verify comparisons
    Given the following fields are set:
      | alpha   | 42 |
      | bravo   | 12 |
      | charlie | 25 |
    Then field "alpha" is greater than field "bravo"
    And field "charlie" is equal to 25

  Scenario: We can match against a regular expression
    Given field "alpha" is set to "lorem ipsum"
    And field "bravo" is set to "ipsum lorem"
    Then field "alpha" matches "lorem \w+"
    And field "bravo" does not match "lorem \w+"

  Scenario: We can verify that a field is empty
    Given field "alpha" is set to "lorem ipsum"
    And field "bravo" is set to ""
    Then field "alpha" is not empty
    And field "bravo" is empty

  Scenario: We can make perform table reconciliation
#    Using assumption that table is populated from backend system. This uses fictitious fields for testing the capability.
    Then the "tango" table should match - ordered
      | employee id | first name | last name | hire date  |
      | 1234        | Jane       | Smith     | 2023-12-20 |
      | 4321        | John       | Rambo     | 1947-12-05 |
      | 9876        | John       | McClane   | 1955-03-19 |
    And the "tango" table should match - unordered
      | employee id | first name | last name | hire date  |
      | 1234        | Jane       | Smith     | 2023-12-20 |
      | 9876        | John       | McClane   | 1955-03-19 |
      | 4321        | John       | Rambo     | 1947-12-05 |
    And the "tango" table should match - contains
      | employee id | first name | last name | hire date  |
      | 4321        | John       | Rambo     | 1947-12-05 |