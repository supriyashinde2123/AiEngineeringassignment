Feature: Get User
  Background:
    * url 'https://jsonplaceholder.typicode.com'
    * def users = read('classpath:data/users.json')
  Scenario: Get a user positive
    Given path 'users', 1
    When method get
    Then status 200
    And match response.name == users.valid.name
    And match response.username == users.valid.username
  Scenario: Get a user negative
    Given path 'users', 1
    When method get
    Then status 200
    And match response.name == users.invalid.name
    And match response.username != users.invalid.username