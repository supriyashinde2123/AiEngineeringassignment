Feature: Get User
  Background:
    * url 'https://jsonplaceholder.typicode.com'

  @smoke
  Scenario Outline: Get user <id> - username <username>, match=<shouldMatch
    Given path 'users', id
    When method get
    Then status 200
    And match response.name == name
    * def result = karate.match('response.username == username')
    * assert result.pass == (shouldMatch == 'TRUE')
    Examples:
      | read('classpath:data/users.csv') |
