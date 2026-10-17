CREATE TABLE card_tokens (
  token VARCHAR(80) PRIMARY KEY,
  encrypted_pan VARCHAR(512) NOT NULL,
  last_four CHAR(4) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL
);
CREATE TABLE payments (
  id UUID PRIMARY KEY,
  idempotency_key VARCHAR(100) NOT NULL UNIQUE,
  amount NUMERIC(12,2) NOT NULL CHECK (amount > 0),
  currency CHAR(3) NOT NULL,
  status VARCHAR(30) NOT NULL CHECK (status IN ('APPROVED','DECLINED')),
  decline_reason VARCHAR(80),
  authorization_code CHAR(6),
  card_token VARCHAR(80) NOT NULL REFERENCES card_tokens(token),
  created_at TIMESTAMPTZ NOT NULL
);
CREATE INDEX idx_payments_created_at ON payments(created_at DESC);
