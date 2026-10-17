package com.himanshu.securepay.repository;
import com.himanshu.securepay.entity.Payment; import org.springframework.data.jpa.repository.JpaRepository; import java.util.Optional; import java.util.UUID;
public interface PaymentRepository extends JpaRepository<Payment,UUID>{ Optional<Payment> findByIdempotencyKey(String key); }
