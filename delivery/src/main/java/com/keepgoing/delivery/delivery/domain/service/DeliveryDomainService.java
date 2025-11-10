package com.keepgoing.delivery.delivery.domain.service;

import com.keepgoing.delivery.delivery.domain.entity.DeliveryRoute;
import com.keepgoing.delivery.delivery.domain.vo.Distance;
import com.keepgoing.delivery.delivery.domain.vo.Duration;
import com.keepgoing.delivery.delivery.domain.vo.RouteSeq;
import com.keepgoing.delivery.deliveryperson.domain.entity.DeliveryPerson;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class DeliveryDomainService {

    // 허브 간 경로 추가
    public List<DeliveryRoute> addRoutes(
            UUID deliveryId,
            UUID departureHubId,
            UUID destinationHubId
    ) {

        List<DeliveryRoute> routes = new ArrayList<>();

        DeliveryRoute route = DeliveryRoute.create(
                deliveryId,
                departureHubId,
                destinationHubId,
                new Distance(100.0),
                new Duration(120),
                new RouteSeq(1)
        );

        routes.add(route);
        return routes;
    }

    // 배송 담당자 선택 (round-robin)
    public DeliveryPerson selectDeliveryPerson(List<DeliveryPerson> availablePersons) {
        if (availablePersons == null || availablePersons.isEmpty()) {
            throw new IllegalStateException("사용 가능한 허브 배송담당자가 없습니다.");
        }

        return availablePersons.stream()
                .min((p1, p2) -> Integer.compare(
                        p1.getDeliverySeq().value(),
                        p2.getDeliverySeq().value()
                ))
                .orElseThrow(() -> new IllegalStateException("배송담당자 배정 실패"));
    }
}