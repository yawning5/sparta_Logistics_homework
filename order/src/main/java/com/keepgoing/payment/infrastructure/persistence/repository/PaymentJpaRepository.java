package com.keepgoing.payment.infrastructure.persistence.repository;

import com.keepgoing.payment.domain.Payment;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, UUID> {

}
