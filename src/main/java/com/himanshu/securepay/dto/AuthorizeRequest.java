package com.himanshu.securepay.dto;
import jakarta.validation.constraints.*; import java.math.BigDecimal;
public record AuthorizeRequest(@NotBlank String cardToken,@NotNull @DecimalMin(value="0.01") @Digits(integer=10,fraction=2) BigDecimal amount,@Pattern(regexp="[A-Z]{3}") String currency) {}
