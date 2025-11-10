package com.keepgoing.delivery.deliveryperson.domain.service;

import com.keepgoing.delivery.deliveryperson.domain.entity.DeliverySeq;
import com.keepgoing.delivery.global.exception.BusinessException;
import com.keepgoing.delivery.global.exception.ErrorCode;
import org.springframework.stereotype.Component;

@Component
public class DeliveryPersonDomainService {

    private static final int MAX_PERSON_COUNT = 10;

    public DeliverySeq assignNextSeq(int currentCount, Integer maxSeqValue) {
        if (currentCount >= MAX_PERSON_COUNT) {
            throw new BusinessException(ErrorCode.HUB_DELIVERY_PERSON_LIMIT_EXCEEDED);
        }
        return new DeliverySeq(calculateNextSeq(maxSeqValue));
    }

    private int calculateNextSeq(Integer maxSeqValue) {
        return (maxSeqValue == null) ? 1 : maxSeqValue + 1;
    }
}
