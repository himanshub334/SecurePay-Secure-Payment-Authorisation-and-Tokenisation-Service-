package com.himanshu.securepay.entity;
import jakarta.persistence.*; import java.math.BigDecimal; import java.time.Instant;
@Entity @Table(name="payments", uniqueConstraints=@UniqueConstraint(name="uk_payment_idempotency", columnNames="idempotency_key"))
public class Payment {
 @Id @GeneratedValue(strategy=GenerationType.UUID) private java.util.UUID id;
 @Column(name="idempotency_key", nullable=false, length=100) private String idempotencyKey;
 @Column(nullable=false, precision=12, scale=2) private BigDecimal amount;
 @Column(nullable=false, length=3) private String currency;
 @Column(nullable=false, length=30) private String status;
 @Column(name="decline_reason", length=80) private String declineReason;
 @Column(name="authorization_code", length=6) private String authorizationCode;
 @Column(name="card_token", length=80) private String cardToken;
 @Column(name="created_at", nullable=false) private Instant createdAt=Instant.now();
 protected Payment(){}
 public Payment(String key, BigDecimal amount, String currency, String status, String reason, String code, String token){this.idempotencyKey=key;this.amount=amount;this.currency=currency;this.status=status;this.declineReason=reason;this.authorizationCode=code;this.cardToken=token;this.createdAt=Instant.now();}
 public java.util.UUID getId(){return id;} public String getIdempotencyKey(){return idempotencyKey;} public BigDecimal getAmount(){return amount;} public String getCurrency(){return currency;} public String getStatus(){return status;} public String getDeclineReason(){return declineReason;} public String getAuthorizationCode(){return authorizationCode;} public String getCardToken(){return cardToken;} public Instant getCreatedAt(){return createdAt;}
}
