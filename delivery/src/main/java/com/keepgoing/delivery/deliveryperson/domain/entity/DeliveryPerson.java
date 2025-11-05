package com.keepgoing.delivery.deliveryperson.domain.entity;

import java.util.UUID;

public class DeliveryPerson {
    private UUID id;
    private Long userId;
    private UUID hubId;
    private String slackId;
    private DeliveryPersonType type;
    private DeliverySeq deliverySeq;
}
