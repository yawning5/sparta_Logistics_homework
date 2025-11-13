package com.keepgoing.delivery.delivery.application.service;

import com.keepgoing.delivery.delivery.application.facade.DeliveryPersonFacade;
import com.keepgoing.delivery.delivery.domain.entity.Delivery;
import com.keepgoing.delivery.delivery.domain.entity.DeliveryRoute;
import com.keepgoing.delivery.delivery.domain.entity.DeliveryStatus;
import com.keepgoing.delivery.delivery.domain.repository.DeliveryRepository;
import com.keepgoing.delivery.delivery.domain.repository.DeliveryRouteRepository;
import com.keepgoing.delivery.delivery.domain.service.DeliveryDomainService;
import com.keepgoing.delivery.delivery.domain.vo.*;
import com.keepgoing.delivery.delivery.infrastructure.api.client.HubRouteService;
import com.keepgoing.delivery.delivery.infrastructure.api.dto.HubRouteResponse;
import com.keepgoing.delivery.deliveryperson.domain.entity.DeliveryPerson;
import com.keepgoing.delivery.deliveryperson.domain.entity.DeliverySeq;
import com.keepgoing.delivery.global.exception.BusinessException;
import com.keepgoing.delivery.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    @Mock
    private HubRouteService hubRouteService;

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private DeliveryRouteRepository deliveryRouteRepository;

    @Mock
    private DeliveryDomainService deliveryDomainService;

    @Mock
    private DeliveryPersonFacade deliveryPersonFacade;

    @InjectMocks
    private DeliveryService deliveryService;

    @Test
    @DisplayName("배송 생성 - 성공")
    void createDelivery_Success() {
        // Given
        UUID orderId = UUID.randomUUID();
        UUID departureHubId = UUID.randomUUID();
        UUID destinationHubId = UUID.randomUUID();
        Address address = new Address("서울시 강남구", "테헤란로", "06234");
        Long recipientUserId = 1L;
        String recipientSlackId = "@user123";
        String token = "abcd";

        HubRouteResponse hubRoute = new HubRouteResponse(
                UUID.randomUUID(),
                departureHubId,
                destinationHubId,
                10.0,
                20
        );

        given(deliveryRepository.existsByOrderId(orderId)).willReturn(false);

        Delivery savedDelivery = Delivery.create(
                orderId, departureHubId, destinationHubId, address, recipientUserId, recipientSlackId
        );
        given(deliveryRepository.save(any(Delivery.class))).willReturn(savedDelivery);

        DeliveryRoute route = DeliveryRoute.create(
                savedDelivery.getId(), departureHubId, destinationHubId,
                new Distance(10.0), new Duration(20), new RouteSeq(1)
        );

        given(deliveryDomainService.addRoutes(
                any(UUID.class),
                any(UUID.class),
                any(UUID.class),
                any(Distance.class),
                any(Duration.class)
        )).willReturn(List.of(route));

        DeliveryPerson hubPerson = DeliveryPerson.createHubDeliveryPerson(100L, "@hub_person", new DeliverySeq(1));
        given(deliveryPersonFacade.getVendorDeliveryPersonsByHub(any(), any(), any())).willReturn(List.of(hubPerson));
        given(deliveryDomainService.selectDeliveryPerson(anyList())).willReturn(hubPerson);
        given(hubRouteService.getHubRoute(any(UUID.class), any(String.class))).willReturn(hubRoute);

        // When
        Delivery result = deliveryService.createDelivery(
                orderId, departureHubId, destinationHubId, address, recipientUserId, recipientSlackId, token
        );

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getOrderId()).isEqualTo(orderId);
        assertThat(result.getStatus()).isEqualTo(DeliveryStatus.HUB_WAITING);
        verify(deliveryRepository, atLeastOnce()).save(any(Delivery.class));
        verify(deliveryRouteRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("배송 생성 - 중복 주문 실패")
    void createDelivery_DuplicateOrder_Fail() {
        // Given
        UUID orderId = UUID.randomUUID();
        String token = "asdf";
        given(deliveryRepository.existsByOrderId(orderId)).willReturn(true);

        // When & Then
        assertThatThrownBy(() -> deliveryService.createDelivery(
                orderId, UUID.randomUUID(), UUID.randomUUID(),
                new Address("서울", "강남", "12345"), 1L, "@user", token
        ))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.DELIVERY_ALREADY_EXISTS.getMessage());

        verify(deliveryRepository, never()).save(any());
    }

    @Test
    @DisplayName("배송 조회 - 성공")
    void findDeliveryById_Success() {
        // Given
        UUID deliveryId = UUID.randomUUID();
        Delivery delivery = Delivery.create(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                new Address("서울", "강남", "12345"), 1L, "@user"
        );

        given(deliveryRepository.findByIdAndIsDeletedFalse(deliveryId))
                .willReturn(Optional.of(delivery));

        // When
        Delivery result = deliveryService.findDeliveryById(deliveryId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(delivery);
    }

    @Test
    @DisplayName("배송 조회 - 존재하지 않음")
    void findDeliveryById_NotFound_Fail() {
        // Given
        UUID deliveryId = UUID.randomUUID();
        given(deliveryRepository.findByIdAndIsDeletedFalse(deliveryId))
                .willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> deliveryService.findDeliveryById(deliveryId))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.DELIVERY_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("허브 간 배송 시작 - 성공")
    void startHubDelivery_Success() {
        // Given
        UUID deliveryId = UUID.randomUUID();
        Delivery delivery = Delivery.create(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                new Address("서울", "강남", "12345"), 1L, "@user"
        );

        DeliveryRoute route = DeliveryRoute.create(
                delivery.getId(), UUID.randomUUID(), UUID.randomUUID(),
                new Distance(100.0), new Duration(120), new RouteSeq(1)
        );
        route.assignDeliveryPerson(100L);
        delivery.addRoute(route);

        given(deliveryRepository.findByIdAndIsDeletedFalse(deliveryId))
                .willReturn(Optional.of(delivery));
        given(deliveryRouteRepository.findByDeliveryIdOrderByRouteSeq(deliveryId))
                .willReturn(List.of(route));

        // When
        deliveryService.startHubDelivery(deliveryId);

        // Then
        assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.HUB_IN_PROGRESS);
        assertThat(route.getStatus().name()).containsIgnoringCase("MOVING");
        verify(deliveryRepository, atLeastOnce()).save(any(Delivery.class));
        verify(deliveryRouteRepository).save(route);
    }

    @Test
    @DisplayName("허브 경로 완료 - 성공")
    void completeHubRoute_Success() {
        // Given
        UUID orderId = UUID.randomUUID();
        UUID hubId = UUID.randomUUID();

        Delivery delivery = Delivery.create(
                orderId,
                hubId, UUID.randomUUID(),
                new Address("서울", "강남", "12345"), 1L, "@user"
        );

        DeliveryRoute route = DeliveryRoute.create(
                delivery.getId(), hubId, UUID.randomUUID(),
                new Distance(10.0), new Duration(20), new RouteSeq(1)
        );
        route.assignDeliveryPerson(100L);
        delivery.addRoute(route);

        // Mock repository & facade
        given(deliveryRepository.findByIdAndIsDeletedFalse(delivery.getId()))
                .willReturn(Optional.of(delivery));
        given(deliveryRouteRepository.findByDeliveryIdOrderByRouteSeq(delivery.getId()))
                .willReturn(List.of(route));
        given(deliveryPersonFacade.getVendorDeliveryPersonsByHub(any(), any(), any()))
                .willReturn(List.of(DeliveryPerson.createVendorDeliveryPerson(1L, hubId, "@vendor", new DeliverySeq(1))));
        given(deliveryDomainService.selectDeliveryPerson(anyList()))
                .willReturn(DeliveryPerson.createVendorDeliveryPerson(1L, hubId,"@vendor", new DeliverySeq(1)));

        // When
        deliveryService.startHubDelivery(delivery.getId()); // 경로 시작 (WAITING → MOVING)
        deliveryService.completeHubRoute(delivery.getId(), 1, new Distance(12.0), new Duration(25), "MASTER", hubId); // 완료 (MOVING → ARRIVED → DELIVERED)

        // Then
        assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.DELIVERED);

        // save는 startHubDelivery와 completeHubRoute 두 곳에서 호출되므로 atLeastOnce() 사용
        verify(deliveryRouteRepository, atLeastOnce()).save(route);
        verify(deliveryRepository, atLeastOnce()).save(any(Delivery.class));
    }



    @Test
    @DisplayName("배송 완료 - 성공")
    void completeDelivery_Success() {
        // Given
        UUID deliveryId = UUID.randomUUID();
        Delivery delivery = Delivery.create(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                new Address("서울", "강남", "12345"), 1L, "@user"
        );

        try {
            var statusField = Delivery.class.getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(delivery, DeliveryStatus.VENDOR_IN_PROGRESS);
        } catch (Exception e) {
            fail("상태 변경 실패");
        }

        given(deliveryRepository.findByIdAndIsDeletedFalse(deliveryId))
                .willReturn(Optional.of(delivery));

        // When
        deliveryService.completeDelivery(deliveryId);

        // Then
        assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.DELIVERED);
        verify(deliveryRepository, atLeastOnce()).save(any(Delivery.class));
    }

    @Test
    @DisplayName("배송 취소 - 성공")
    void cancelDelivery_Success() {
        // Given
        UUID deliveryId = UUID.randomUUID();
        Delivery delivery = Delivery.create(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                new Address("서울", "강남", "12345"), 1L, "@user"
        );
        String deletedBy = "admin";

        given(deliveryRepository.findByIdAndIsDeletedFalse(deliveryId))
                .willReturn(Optional.of(delivery));
        given(deliveryRouteRepository.findByDeliveryIdOrderByRouteSeq(deliveryId))
                .willReturn(List.of());

        // When
        deliveryService.cancelDelivery(deliveryId, deletedBy);

        // Then
        assertThat(delivery.isDeleted()).isTrue();
        assertThat(delivery.getDeletedBy()).isEqualTo(deletedBy);
        verify(deliveryRepository, atLeastOnce()).save(any(Delivery.class));
    }
}
