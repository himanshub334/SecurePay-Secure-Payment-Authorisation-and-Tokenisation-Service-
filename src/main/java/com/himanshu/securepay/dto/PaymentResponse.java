package com.himanshu.securepay.dto;
import java.math.BigDecimal; import java.time.Instant; import java.util.UUID;
public record PaymentResponse(UUID paymentId,String status,String authorizationCode,String declineReason,BigDecimal amount,String currency,String cardToken,Instant createdAt) {}
