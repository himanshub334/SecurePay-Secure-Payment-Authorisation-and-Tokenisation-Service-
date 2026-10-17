package com.himanshu.securepay.controller;
import com.himanshu.securepay.dto.*; import com.himanshu.securepay.service.*; import jakarta.validation.Valid; import org.springframework.http.HttpStatus; import org.springframework.web.bind.annotation.*; import java.util.Map; import java.util.UUID;
@RestController @RequestMapping("/api/v1")
public class PaymentController {
 private final TokenizationService tokenization; private final PaymentService payments;
 public PaymentController(TokenizationService tokenization,PaymentService payments){this.tokenization=tokenization;this.payments=payments;}
 @PostMapping("/tokens") @ResponseStatus(HttpStatus.CREATED) public TokenizeResponse tokenize(@Valid @RequestBody TokenizeRequest request){return tokenization.tokenize(request.pan());}
 @PostMapping("/payments/authorize") public PaymentResponse authorize(@RequestHeader("Idempotency-Key") String key,@Valid @RequestBody AuthorizeRequest request){return payments.authorize(key,request);}
 @GetMapping("/payments/{id}") public PaymentResponse paymentLookup(@PathVariable UUID id){return payments.findById(id);}
 @GetMapping("/security") public Map<String,String> securityNotes(){return Map.of("panLogging","PAN values must never be logged","tokenization","Downstream calls should use card tokens","environment","Development demo; not certified for real payment-card data");}
}
