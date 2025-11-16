package com.keepgoing.payment.application.repo;

import com.keepgoing.payment.domain.Payment;
import java.util.UUID;

public interface PaymentRepository {

    Payment findById(UUID paymentId);

    Payment save(Payment payment);

}
