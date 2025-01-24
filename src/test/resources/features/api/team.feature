Feature: Add new team API and DB validation
  @team @db
  Scenario: Post new team and verify in database
    Given I logged Bookit api as a "teacher"
    When Users sends POST request to "/api/teams/team" with following info:
      | campus-location | VA      |
      | batch-number    | 26      |
      | team-name       | BesTeam |
    Then status code should be 201
    And Database should persist same team info
    And User deletes previously created team