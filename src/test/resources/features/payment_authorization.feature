Feature: Payment authorisation
  Scenario: Approve a valid payment
    Given a tokenized test card and sufficient simulated funds
    When a payment for GBP 150.00 is authorised with an idempotency key
    Then the response status is APPROVED
    And the response contains a six-digit authorization code
