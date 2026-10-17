# SecurePay — Secure Payment Authorisation & Tokenisation Service

A portfolio-grade Spring Boot and PostgreSQL reference service demonstrating payment authorisation flows, card tokenisation, idempotency, schema migrations, API documentation, automated tests, and containerised development.

> **Safety and compliance:** This is an educational/demo project, not a production payment processor and not certified by Mastercard, a card network, or a PCI assessor. Do not submit real cardholder data. Production payment systems require a PCI DSS assessment, key management via a KMS/HSM, a payment-network/acquirer integration, TLS termination and certificate management, threat modelling, operational controls, and independent security review.

## Included

- Java 21, Spring Boot 3, Spring Data JPA, PostgreSQL
- Flyway-managed initial schema and constraints
- REST endpoints for PAN tokenisation and payment authorisation
- AES-256-GCM encryption of PAN before database persistence; random opaque token returned to callers
- Luhn checksum validation and last-four-only response metadata
- Idempotency-key handling for retries
- Configurable amount threshold fraud rule and simulated insufficient-funds decision
- HTTP Basic authentication for local demo; BCrypt password hashing
- Bean Validation, generic validation errors, Swagger/OpenAPI, Actuator health and metrics
- JUnit 5 / Mockito tests, JaCoCo report, GitHub Actions, Docker Compose
- A Gherkin acceptance-scenario sketch in `src/test/resources-placeholder.txt`

## Prerequisites

- Java 21 and Maven 3.9+ for local runs, or Docker Compose
- Generate a unique encryption key: `openssl rand -base64 32`

## Run with Docker

```bash
cp .env.example .env
# Edit .env: set unique SECUREPAY_ENCRYPTION_KEY, SECUREPAY_PASSWORD, and DATABASE_PASSWORD
docker compose up --build
```

API docs: <http://localhost:8080/swagger-ui.html>  
Health: <http://localhost:8080/actuator/health>

The local demo uses HTTP Basic authentication. Set `SECUREPAY_USER` and `SECUREPAY_PASSWORD` in `.env`. Do not use the example credentials in a shared environment.

## Run locally

Set environment variables `DATABASE_URL`, `DATABASE_USER`, `DATABASE_PASSWORD`, and `SECUREPAY_ENCRYPTION_KEY`, ensure PostgreSQL is running, then:

```bash
mvn spring-boot:run
```

Run tests and create a coverage report:

```bash
mvn verify
```

Report path: `target/site/jacoco/index.html`.

## API examples

All API routes except health, Swagger, and OpenAPI require HTTP Basic authentication.

### Tokenise a test PAN

Use only a published test card number such as `4242424242424242` in this local demo:

```bash
curl -u demo:change-me-now -X POST http://localhost:8080/api/v1/tokens \
  -H 'Content-Type: application/json' \
  -d '{"pan":"4242424242424242"}'
```

Response includes an opaque `tok_...` token and last four digits; the PAN is not returned.

### Authorise a payment

Replace `tok_...` with the token from the prior response:

```bash
curl -u demo:change-me-now -X POST http://localhost:8080/api/v1/payments/authorize \
  -H 'Content-Type: application/json' \
  -H 'Idempotency-Key: demo-payment-001' \
  -d '{"cardToken":"tok_REPLACE_ME","amount":150.00,"currency":"GBP"}'
```

Retrying with the same idempotency key returns the original payment result. Amounts above `FRAUD_THRESHOLD` are declined by the sample fraud rule. Amounts above GBP 1,000 and below the fraud threshold are declined by the **simulated** insufficient-funds rule; this is not a real balance check.

## Security design notes and limitations

- PAN is not logged by the application code; avoid logging request bodies in any added middleware.
- PAN ciphertext uses AES/GCM/NoPadding with a random 96-bit IV per encryption and a 128-bit authentication tag. The 32-byte key must be supplied through the environment for this demo. Use a managed KMS/HSM and rotation policy in production.
- Database access uses JPA parameter binding; Flyway owns schema changes.
- HTTP Basic is used to keep the demo small. It is not OAuth 2.0/OIDC. The local Docker setup is HTTP and must not be exposed publicly; terminate TLS at a properly configured gateway in a real deployment.
- CSRF is disabled because this API is stateless and uses an Authorization header, not cookie sessions. Re-evaluate this if browser-cookie authentication is introduced.
- Idempotency is implemented with a unique database constraint, but concurrent duplicate submissions may still need explicit conflict handling and retry semantics in a production-grade implementation.
- The project includes Testcontainers dependencies, but a complete Testcontainers integration suite and executable Cucumber runner are future extensions. The current CI runs the available JUnit/Mockito tests and produces a coverage report; no 87% coverage guarantee is claimed.
- The project does not connect to Mastercard or another processor, does not move real money, and should never be described as meeting Mastercard engineering standards or PCI DSS certification without independent verification.

## Suggested next improvements

1. Add transaction-safe idempotency conflict handling for simultaneous retries.
2. Add Testcontainers PostgreSQL integration tests for Flyway and repository constraints.
3. Wire the Gherkin scenario into a Cucumber JUnit Platform runner.
4. Add a real OAuth 2.0/OIDC provider, scopes, audit trail, rate limits, and TLS ingress.
5. Replace simulated funds/fraud decisions with provider-backed adapters and explicit timeout/retry/circuit-breaker policies.
