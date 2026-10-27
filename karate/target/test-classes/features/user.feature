Feature: Users API

  @smoke
  Scenario: Get a user positive
    Given url 'https://jsonplaceholder.typicode.com/users/1'
    When method get
    Then status 200
    And match response.name == 'Leanne Graham'
    And match response.username == 'Bret'

  @regression
  Scenario: Get a user negative
    Given url 'https://jsonplaceholder.typicode.com/users/1'
    When method get
    Then status 200
    And match response.name == 'Leanne Graham'
    And match response.username == 'Brets'