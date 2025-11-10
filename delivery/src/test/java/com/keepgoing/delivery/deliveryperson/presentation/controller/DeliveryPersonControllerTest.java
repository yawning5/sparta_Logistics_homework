package com.keepgoing.delivery.deliveryperson.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keepgoing.delivery.deliveryperson.application.dto.request.ChangeHubRequest;
import com.keepgoing.delivery.deliveryperson.application.dto.request.ChangeTypeRequest;
import com.keepgoing.delivery.deliveryperson.application.dto.request.RegisterDeliveryPersonRequest;
import com.keepgoing.delivery.deliveryperson.application.dto.request.UpdateSlackIdRequest;
import com.keepgoing.delivery.deliveryperson.application.service.DeliveryPersonService;
import com.keepgoing.delivery.deliveryperson.domain.entity.DeliveryPerson;
import com.keepgoing.delivery.deliveryperson.domain.entity.DeliveryPersonType;
import com.keepgoing.delivery.deliveryperson.domain.entity.DeliverySeq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DeliveryPersonController.class)
class DeliveryPersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DeliveryPersonService deliveryPersonService;

    @Test
    @DisplayName("배송 담당자 등록 - 성공")
    void registerDeliveryPerson_Success() throws Exception {
        // Given
        Long userId = 1L;
        String slackId = "@user123";
        DeliveryPersonType type = DeliveryPersonType.HUB;

        RegisterDeliveryPersonRequest request = new RegisterDeliveryPersonRequest(
                userId, slackId, type, null
        );

        DeliveryPerson person = DeliveryPerson.createHubDeliveryPerson(userId, slackId, new DeliverySeq(1));
        given(deliveryPersonService.registerDeliveryPerson(userId, slackId, type, null))
                .willReturn(person);

        // When & Then
        mockMvc.perform(post("/api/v1/delivery-persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(userId))
                .andExpect(jsonPath("$.data.type").value("HUB"))
                .andExpect(jsonPath("$.message").value("배송 담당자가 등록되었습니다."));
    }

    @Test
    @DisplayName("배송 담당자 조회 - 성공")
    void getDeliveryPerson_Success() throws Exception {
        // Given
        Long userId = 1L;
        DeliveryPerson person = DeliveryPerson.createHubDeliveryPerson(
                userId, "@user123", new DeliverySeq(1)
        );

        given(deliveryPersonService.findDeliveryPerson(userId))
                .willReturn(person);

        // When & Then
        mockMvc.perform(get("/api/v1/delivery-persons/{id}", userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(userId))
                .andExpect(jsonPath("$.data.slackId").value("@user123"));
    }

    @Test
    @DisplayName("타입별 배송 담당자 조회 - 성공")
    void getDeliveryPersonsByType_Success() throws Exception {
        // Given
        DeliveryPerson person1 = DeliveryPerson.createHubDeliveryPerson(1L, "@user1", new DeliverySeq(1));
        DeliveryPerson person2 = DeliveryPerson.createHubDeliveryPerson(2L, "@user2", new DeliverySeq(2));

        given(deliveryPersonService.findDeliveryPersonsByType(DeliveryPersonType.HUB))
                .willReturn(List.of(person1, person2));

        // When & Then
        mockMvc.perform(get("/api/v1/delivery-persons")
                        .param("type", "HUB"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    @DisplayName("Slack ID 수정 - 성공")
    void updateSlackId_Success() throws Exception {
        // Given
        Long userId = 1L;
        UpdateSlackIdRequest request = new UpdateSlackIdRequest("@new_user");

        doNothing().when(deliveryPersonService).updateSlackId(userId, "@new_user");

        // When & Then
        mockMvc.perform(patch("/api/v1/delivery-persons/{id}/slack-id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Slack ID가 수정되었습니다."));
    }

    @Test
    @DisplayName("허브 변경 - 성공")
    void changeHub_Success() throws Exception {
        // Given
        Long userId = 1L;
        UUID newHubId = UUID.randomUUID();
        ChangeHubRequest request = new ChangeHubRequest(newHubId);

        doNothing().when(deliveryPersonService).changeHub(userId, newHubId);

        // When & Then
        mockMvc.perform(patch("/api/v1/delivery-persons/{id}/hub", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("소속 허브가 변경되었습니다."));
    }

    @Test
    @DisplayName("타입 변경 - 성공")
    void changeType_Success() throws Exception {
        // Given
        Long userId = 1L;
        UUID newHubId = UUID.randomUUID();
        ChangeTypeRequest request = new ChangeTypeRequest(DeliveryPersonType.VENDOR, newHubId);

        doNothing().when(deliveryPersonService).changeType(userId, DeliveryPersonType.VENDOR, newHubId);

        // When & Then
        mockMvc.perform(patch("/api/v1/delivery-persons/{id}/type", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("배송 담당자 타입이 변경되었습니다."));
    }

    @Test
    @DisplayName("배송 담당자 삭제 - 성공")
    void deleteDeliveryPerson_Success() throws Exception {
        // Given
        Long userId = 1L;
        String deletedBy = "admin";

        doNothing().when(deliveryPersonService).softDelete(userId, deletedBy);

        // When & Then
        mockMvc.perform(delete("/api/v1/delivery-persons/{id}", userId)
                        .param("deletedBy", deletedBy))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("배송 담당자가 삭제되었습니다."));
    }
}