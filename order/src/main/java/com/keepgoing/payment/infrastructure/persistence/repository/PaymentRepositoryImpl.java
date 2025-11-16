package com.keepgoing.payment.infrastructure.persistence.repository;


import com.keepgoing.payment.application.repo.PaymentRepository;
import com.keepgoing.payment.domain.Payment;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment findById(UUID paymentId) {
        return paymentJpaRepository.findById(paymentId)
            .orElseThrow(
                () -> new IllegalArgumentException("결제를 찾을 수 없습니다. ")
            );
    }

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(payment);
    }
}
