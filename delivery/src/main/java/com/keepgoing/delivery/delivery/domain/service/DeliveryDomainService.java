package com.keepgoing.delivery.delivery.domain.service;

import com.keepgoing.delivery.delivery.domain.entity.DeliveryRoute;
import com.keepgoing.delivery.delivery.domain.vo.Distance;
import com.keepgoing.delivery.delivery.domain.vo.Duration;
import com.keepgoing.delivery.delivery.domain.vo.RouteSeq;
import com.keepgoing.delivery.deliveryperson.domain.entity.DeliveryPerson;
import com.keepgoing.delivery.global.exception.BusinessException;
import com.keepgoing.delivery.global.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class DeliveryDomainService {

    private int lastAssignedSeq = -1; // 마지막으로 배정된 담당자의 Seq (초기값)

    // 허브 간 경로 추가
    public List<DeliveryRoute> addRoutes(
            UUID deliveryId,
            UUID departureHubId,
            UUID destinationHubId,
            Distance distance,
            Duration duration
    ) {
        List<DeliveryRoute> routes = new ArrayList<>();

        DeliveryRoute route = DeliveryRoute.create(
                deliveryId,
                departureHubId,
                destinationHubId,
                distance,
                duration,
                new RouteSeq(1)
        );

        routes.add(route);
        return routes;
    }

    // 배송 담당자 선택 (Round-Robin 방식)
    public DeliveryPerson selectDeliveryPerson(List<DeliveryPerson> availablePersons) {
        if (availablePersons == null || availablePersons.isEmpty()) {
            throw new BusinessException(ErrorCode.HUB_DELIVERY_PERSON_NOT_AVAILABLE);
        }

        // seq 기준 정렬
        List<DeliveryPerson> sorted = availablePersons.stream()
                .sorted(Comparator.comparingInt(p -> p.getDeliverySeq().value()))
                .toList();

        // lastAssignedSeq보다 큰 seq 중 첫 번째를 선택
        Optional<DeliveryPerson> nextPerson = sorted.stream()
                .filter(p -> p.getDeliverySeq().value() > lastAssignedSeq)
                .findFirst();

        // 없으면 처음(가장 작은 seq)으로 돌아감
        DeliveryPerson selected = nextPerson.orElse(sorted.getFirst());

        // 마지막 배정 seq 갱신
        lastAssignedSeq = selected.getDeliverySeq().value();

        return selected;
    }
}
