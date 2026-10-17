package com.himanshu.securepay.service;
import com.himanshu.securepay.dto.AuthorizeRequest; import com.himanshu.securepay.entity.Payment; import com.himanshu.securepay.repository.*; import org.junit.jupiter.api.*; import org.junit.jupiter.api.extension.ExtendWith; import org.mockito.*; import org.mockito.junit.jupiter.MockitoExtension; import java.math.BigDecimal; import java.util.Optional; import static org.junit.jupiter.api.Assertions.*; import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class) class PaymentServiceTest {
 @Mock PaymentRepository payments; @Mock CardTokenRepository tokens; PaymentService service;
 @BeforeEach void setup(){service=new PaymentService(payments,tokens,new BigDecimal("2500.00"));}
 @Test void idempotentRetryReturnsOriginalPayment(){var p=new Payment("idem-1",new BigDecimal("25.00"),"GBP","APPROVED",null,"123456","tok_demo");when(payments.findByIdempotencyKey("idem-1")).thenReturn(Optional.of(p));var result=service.authorize("idem-1",new AuthorizeRequest("tok_demo",new BigDecimal("25.00"),"GBP"));assertEquals("APPROVED",result.status());verify(payments,never()).save(any());}
 @Test void unknownTokenIsRejected(){when(payments.findByIdempotencyKey("idem-2")).thenReturn(Optional.empty());when(tokens.existsById("bad-token")).thenReturn(false);assertThrows(IllegalArgumentException.class,()->service.authorize("idem-2",new AuthorizeRequest("bad-token",new BigDecimal("20.00"),"GBP")));}
}
