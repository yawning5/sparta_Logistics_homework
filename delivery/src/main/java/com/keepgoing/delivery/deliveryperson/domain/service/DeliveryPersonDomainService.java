package com.keepgoing.delivery.deliveryperson.domain.service;

import com.keepgoing.delivery.deliveryperson.domain.entity.DeliverySeq;
import org.springframework.stereotype.Component;

@Component
public class DeliveryPersonDomainService {

    private static final int MAX_PERSON_COUNT = 10;

    public DeliverySeq assignNextSeq(int currentCount, Integer maxSeqValue) {
        if (currentCount >= MAX_PERSON_COUNT) {
            throw new IllegalStateException("최대 인원(" + MAX_PERSON_COUNT + "명)을 초과할 수 없습니다.");
        }
        return new DeliverySeq(calculateNextSeq(maxSeqValue));
    }

    private int calculateNextSeq(Integer maxSeqValue) {
        return (maxSeqValue == null) ? 1 : maxSeqValue + 1;
    }
}
