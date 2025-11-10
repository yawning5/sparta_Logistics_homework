package com.keepgoing.delivery.delivery.application.service;

import com.keepgoing.delivery.delivery.application.facade.DeliveryPersonFacade;
import com.keepgoing.delivery.delivery.domain.entity.Delivery;
import com.keepgoing.delivery.delivery.domain.entity.DeliveryRoute;
import com.keepgoing.delivery.delivery.domain.entity.DeliveryStatus;
import com.keepgoing.delivery.delivery.domain.repository.DeliveryRepository;
import com.keepgoing.delivery.delivery.domain.repository.DeliveryRouteRepository;
import com.keepgoing.delivery.delivery.domain.service.DeliveryDomainService;
import com.keepgoing.delivery.delivery.domain.vo.*;
import com.keepgoing.delivery.deliveryperson.domain.entity.DeliveryPerson;
import com.keepgoing.delivery.global.exception.BusinessException;
import com.keepgoing.delivery.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryRouteRepository deliveryRouteRepository;
    private final DeliveryDomainService deliveryDomainService;
    private final DeliveryPersonFacade deliveryPersonFacade;  // Facade 사용

    // 배송 생성
    public Delivery createDelivery(
            UUID orderId,
            UUID departureHubId,
            UUID destinationHubId,
            Address address,
            Long recipientUserId,
            String recipientSlackId
    ) {
        // 중복 생성 방지
        if (deliveryRepository.existsByOrderId(orderId)) {
            throw new BusinessException(ErrorCode.DELIVERY_ALREADY_EXISTS);
        }

        // 배송 생성
        Delivery delivery = Delivery.create(
                orderId,
                departureHubId,
                destinationHubId,
                address,
                recipientUserId,
                recipientSlackId
        );

        // 배송 저장
        delivery = deliveryRepository.save(delivery);

        // 배송 경로 생성
        // Todo: 허브 컨텍스트에 경로 요청 api 호출
        List<DeliveryRoute> routes = deliveryDomainService.addRoutes(
                delivery.getId(),
                departureHubId,
                destinationHubId
        );

        // 첫 번째 경로에 허브 배송담당자 자동 배정
        // Todo: 경로 응답 결과에 따라 수정 필요, 후 도메인 서비스로 분리
        if (!routes.isEmpty()) {
            // Facade를 통해 배송담당자 목록 조회
            // Todo: p2p라면 하나의 경로로 업체 배송 담당자를 배정해 직접 배송, relay 등의 방식이라면 경로별 허브배송, 업체배송 지정
            List<DeliveryPerson> hubDeliveryPersons = deliveryPersonFacade.getHubDeliveryPersons();
            // List<DeliveryPerson> vendorDeliveryPersons = deliveryPersonFacade.getVendorDeliveryPersons();

            // 도메인 서비스를 통해 선택
            // Todo: 경로 하나면 이대로 진행, 여러개면 경로 리스트 도메인 서비스에 넘겨 로직 처리
            DeliveryPerson deliveryPerson = deliveryDomainService.selectDeliveryPerson(hubDeliveryPersons);

            routes.getFirst().assignDeliveryPerson(deliveryPerson.getId());
        }

        // 경로 저장
        // Todo: pojo 하기 때문에 cascade는 할 수 없고, 직접 다 저장해야 하는건가?
        deliveryRouteRepository.saveAll(routes);

        // Delivery에 경로 로딩
        delivery.loadRoutes(routes);

        return delivery;
    }

    // 허브 간 배송 시작
    public void startHubDelivery(UUID deliveryId) {
        Delivery delivery = findDeliveryByIdWithRoutes(deliveryId);

        delivery.startFromHub();

        if (!delivery.getRoutes().isEmpty()) {
            delivery.startRoute(1);
            deliveryRouteRepository.save(delivery.getRoutes().getFirst());
        }

        deliveryRepository.save(delivery);
    }

    // 허브 간 배송 완료
    public void completeHubRoute(
            UUID deliveryId,
            int routeSeqValue,
            Distance actualDistance,
            Duration actualTime
    ) {
        Delivery delivery = findDeliveryByIdWithRoutes(deliveryId);

        delivery.completeRoute(routeSeqValue, actualDistance, actualTime);

        // 완료된 경로 저장
        DeliveryRoute completedRoute = delivery.getRoutes().stream()
                .filter(r -> r.getRouteSeq().value() == routeSeqValue)
                .findFirst()
                .orElseThrow();
        deliveryRouteRepository.save(completedRoute);

        // 다음 경로 처리
        DeliveryRoute nextRoute = delivery.getRoutes().stream()
                .filter(r -> r.getRouteSeq().value() == routeSeqValue + 1)
                .findFirst()
                .orElse(null);

        if (nextRoute != null) {
            // Facade를 통해 배송담당자 목록 조회
            List<DeliveryPerson> hubDeliveryPersons = deliveryPersonFacade.getHubDeliveryPersons();

            // 도메인 서비스를 통해 선택
            DeliveryPerson assignedPerson = deliveryDomainService.selectDeliveryPerson(hubDeliveryPersons);

            nextRoute.assignDeliveryPerson(assignedPerson.getId());
            delivery.startRoute(routeSeqValue + 1);
            deliveryRouteRepository.save(nextRoute);
        } else {
            // 마지막 경로 완료
            delivery.arriveAtDestHub();

            // Facade를 통해 업체 배송담당자 목록 조회
            List<DeliveryPerson> vendorDeliveryPersons = deliveryPersonFacade
                    .getVendorDeliveryPersonsByHub(delivery.getDestinationHubId());

            // 도메인 서비스를 통해 선택
            DeliveryPerson vendorPerson = deliveryDomainService
                    .selectDeliveryPerson(vendorDeliveryPersons);

            delivery.assignVendorDeliveryPerson(vendorPerson.getId());
        }

        deliveryRepository.save(delivery);
    }

    // 업체 배송 시작
    public void startVendorDelivery(UUID deliveryId) {
        Delivery delivery = findDeliveryById(deliveryId);

        // 배송담당자 유효성 검증 (Facade 사용)
        if (delivery.getVendorDeliveryPersonId() != null) {
            deliveryPersonFacade.validateDeliveryPerson(
                    delivery.getVendorDeliveryPersonId(),
                    "VENDOR"
            );
        }

        delivery.startVendorDelivery();
        deliveryRepository.save(delivery);
    }

    // 업체 배송 완료
    public void completeDelivery(UUID deliveryId) {
        Delivery delivery = findDeliveryById(deliveryId);
        delivery.completeDelivery();
        deliveryRepository.save(delivery);

        // Todo: 배송 완료 시 주문에 api 호출해서 완료를 알려야 하는가??
    }

    // 배송 조회 (경로 포함)
    @Transactional(readOnly = true)
    public Delivery findDeliveryByIdWithRoutes(UUID deliveryId) {
        Delivery delivery = deliveryRepository.findByIdAndIsDeletedFalse(deliveryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DELIVERY_NOT_FOUND));

        List<DeliveryRoute> routes = deliveryRouteRepository
                .findByDeliveryIdOrderByRouteSeq(deliveryId);
        delivery.loadRoutes(routes);

        return delivery;
    }

    // 배송 조회 (경로 제외)
    @Transactional(readOnly = true)
    public Delivery findDeliveryById(UUID deliveryId) {
        return deliveryRepository.findByIdAndIsDeletedFalse(deliveryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DELIVERY_NOT_FOUND));
    }

    // 배송 조회 (주문 id)
    @Transactional(readOnly = true)
    public Delivery findDeliveryByOrderId(UUID orderId) {
        return deliveryRepository.findByOrderIdAndIsDeletedFalse(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DELIVERY_BY_ORDER_NOT_FOUND));
    }

    // 배송 상태 별 목록 조회
    @Transactional(readOnly = true)
    public List<Delivery> findDeliveriesByStatus(DeliveryStatus status) {
        return deliveryRepository.findByStatusAndIsDeletedFalse(status);
    }

    // 배송 담당자 별 목록 조회
    @Transactional(readOnly = true)
    public List<DeliveryRoute> findRoutesByDeliveryPerson(Long deliveryPersonId) {
        return deliveryRouteRepository.findByDeliveryPersonIdAndIsDeletedFalse(deliveryPersonId);
    }

    // 배송 취소(soft delete)
    public void cancelDelivery(UUID deliveryId, String deletedBy) {
        Delivery delivery = findDeliveryByIdWithRoutes(deliveryId);

        delivery.markDeleted(deletedBy);

        // 모든 경로도 삭제
        delivery.getRoutes().forEach(DeliveryRoute::markDeleted);
        deliveryRouteRepository.saveAll(delivery.getRoutes());

        deliveryRepository.save(delivery);
    }

    // 특정 배송의 경로 조회
    @Transactional(readOnly = true)
    public List<DeliveryRoute> findRoutesByDeliveryId(UUID deliveryId) {
        return deliveryRouteRepository.findByDeliveryIdOrderByRouteSeq(deliveryId);
    }
}
