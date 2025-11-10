package com.keepgoing.delivery.delivery.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keepgoing.delivery.delivery.application.dto.request.CompleteRouteRequest;
import com.keepgoing.delivery.delivery.application.dto.request.CreateDeliveryRequest;
import com.keepgoing.delivery.delivery.application.dto.request.AddressDto;
import com.keepgoing.delivery.delivery.application.service.DeliveryService;
import com.keepgoing.delivery.delivery.domain.entity.Delivery;
import com.keepgoing.delivery.delivery.domain.entity.DeliveryStatus;
import com.keepgoing.delivery.delivery.domain.vo.Address;
import com.keepgoing.delivery.delivery.domain.vo.Distance;
import com.keepgoing.delivery.delivery.domain.vo.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DeliveryController.class)
class DeliveryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DeliveryService deliveryService;

    @Test
    @DisplayName("배송 생성 - 성공")
    void createDelivery_Success() throws Exception {
        // Given
        UUID orderId = UUID.randomUUID();
        UUID departureHubId = UUID.randomUUID();
        UUID destinationHubId = UUID.randomUUID();

        CreateDeliveryRequest request = new CreateDeliveryRequest(
                orderId,
                departureHubId,
                destinationHubId,
                new AddressDto("서울시 강남구", "테헤란로", "06234"),
                1L,
                "@user123"
        );

        Delivery delivery = Delivery.create(
                orderId, departureHubId, destinationHubId,
                new Address("서울시 강남구", "테헤란로", "06234"),
                1L, "@user123"
        );

        given(deliveryService.createDelivery(
                eq(orderId), eq(departureHubId), eq(destinationHubId),
                any(Address.class), eq(1L), eq("@user123")
        )).willReturn(delivery);

        // When & Then
        mockMvc.perform(post("/api/v1/deliveries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.orderId").value(orderId.toString()))
                .andExpect(jsonPath("$.data.status").value("HUB_WAITING"))
                .andExpect(jsonPath("$.message").value("배송이 생성되었습니다."));
    }

    @Test
    @DisplayName("배송 조회 - 성공")
    void getDelivery_Success() throws Exception {
        // Given
        UUID deliveryId = UUID.randomUUID();
        Delivery delivery = Delivery.create(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                new Address("서울", "강남", "12345"), 1L, "@user"
        );

        given(deliveryService.findDeliveryByIdWithRoutes(deliveryId))
                .willReturn(delivery);

        // When & Then
        mockMvc.perform(get("/api/v1/deliveries/{deliveryId}", deliveryId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("HUB_WAITING"));
    }

    @Test
    @DisplayName("상태별 배송 목록 조회 - 성공")
    void getDeliveriesByStatus_Success() throws Exception {
        // Given
        Delivery delivery1 = Delivery.create(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                new Address("서울", "강남", "12345"), 1L, "@user1"
        );
        Delivery delivery2 = Delivery.create(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                new Address("서울", "강남", "12345"), 2L, "@user2"
        );

        given(deliveryService.findDeliveriesByStatus(DeliveryStatus.HUB_WAITING))
                .willReturn(List.of(delivery1, delivery2));

        // When & Then
        mockMvc.perform(get("/api/v1/deliveries")
                        .param("status", "HUB_WAITING"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    @DisplayName("허브 간 배송 시작 - 성공")
    void startHubDelivery_Success() throws Exception {
        // Given
        UUID deliveryId = UUID.randomUUID();
        doNothing().when(deliveryService).startHubDelivery(deliveryId);

        // When & Then
        mockMvc.perform(post("/api/v1/deliveries/{deliveryId}/start-hub-delivery", deliveryId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("허브 간 배송이 시작되었습니다."));
    }

    @Test
    @DisplayName("경로 완료 - 성공")
    void completeHubRoute_Success() throws Exception {
        // Given
        UUID deliveryId = UUID.randomUUID();
        CompleteRouteRequest request = new CompleteRouteRequest(1, 105.5, 130);

        doNothing().when(deliveryService).completeHubRoute(
                eq(deliveryId), eq(1), any(Distance.class), any(Duration.class)
        );

        // When & Then
        mockMvc.perform(post("/api/v1/deliveries/{deliveryId}/complete-route", deliveryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("경로가 완료되었습니다."));
    }

    @Test
    @DisplayName("배송 완료 - 성공")
    void completeDelivery_Success() throws Exception {
        // Given
        UUID deliveryId = UUID.randomUUID();
        doNothing().when(deliveryService).completeDelivery(deliveryId);
        // When & Then
        mockMvc.perform(post("/api/v1/deliveries/{deliveryId}/complete", deliveryId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("배송이 완료되었습니다."));
    }

    @Test
    @DisplayName("배송 취소 - 성공")
    void cancelDelivery_Success() throws Exception {
        // Given
        UUID deliveryId = UUID.randomUUID();
        String deletedBy = "admin";

        doNothing().when(deliveryService).cancelDelivery(deliveryId, deletedBy);

        // When & Then
        mockMvc.perform(delete("/api/v1/deliveries/{deliveryId}", deliveryId)
                        .param("deletedBy", deletedBy))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("배송이 취소되었습니다."));
    }
}