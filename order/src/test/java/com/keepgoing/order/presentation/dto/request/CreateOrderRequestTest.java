package com.keepgoing.order.presentation.dto.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keepgoing.order.application.service.order.OrderService;
import com.keepgoing.order.presentation.api.OrderControllerV1;
import com.keepgoing.order.presentation.dto.response.CreateOrderResponse;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = OrderControllerV1.class, properties = {
    "spring.config.import=",
    "spring.cloud.config.enabled=false",
    "spring.cloud.discovery.enabled=false",
    "eureka.client.enabled=false",
    "eureka.client.register-with-eureka=false",
    "eureka.client.fetch-registry=false"
})
@ActiveProfiles("test")
class CreateOrderRequestTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    OrderService orderService;

    @DisplayName("주문 생성시 전달되는 공급 업체의 식별자가 null인 경우 BadRequest 응답")
    @Test
    void CreateOrderRequestParameterValidationException1() throws Exception {

        // given
        LocalDateTime deliveryDueAt = LocalDateTime.of(2025, 11, 28, 12, 0, 0);

        CreateOrderRequest request = CreateOrderRequest.builder()
            .supplierId(null)
            .supplierName(null)
            .receiverId(UUID.fromString("f3cb7a76-0ad3-4c2d-b269-2e8c9c92a4e7"))
            .receiverName("무한상사")
            .productId(UUID.fromString("7b8df265-6f41-4ad7-942a-7fdf81b5f1c3"))
            .productName("무한동력기")
            .quantity(10)
            .price(250000)
            .deliveryDueAt(deliveryDueAt)
            .deliveryRequestNote("무한 동력기가 고장나지 않도록 안전하게 배송해주세요.")
            .build();

        // when // then
        mockMvc.perform(
                MockMvcRequestBuilders
                    .post("/v1/orders")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @DisplayName("주문 생성시 전달되는 공급 업체의 이름이 null인 경우 BadRequest")
    @Test
    void CreateOrderRequestParameterValidationException2() throws Exception {

        // given
        LocalDateTime deliveryDueAt = LocalDateTime.of(2025, 11, 28, 12, 0, 0);

        CreateOrderRequest request = CreateOrderRequest.builder()
            .supplierId(UUID.fromString("31e1c7c4-d7c7-4a39-9c68-1c3a9ae4c849"))
            .supplierName(null)
            .receiverId(UUID.fromString("f3cb7a76-0ad3-4c2d-b269-2e8c9c92a4e7"))
            .receiverName("무한상사")
            .productId(UUID.fromString("7b8df265-6f41-4ad7-942a-7fdf81b5f1c3"))
            .productName("무한동력기")
            .quantity(10)
            .price(250000)
            .deliveryDueAt(deliveryDueAt)
            .deliveryRequestNote("무한 동력기가 고장나지 않도록 안전하게 배송해주세요.")
            .build();

        // when // then
        mockMvc.perform(
                MockMvcRequestBuilders
                    .post("/v1/orders")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @DisplayName("주문 생성시 전달되는 수령 업체의 식별자가 null인 경우 BadRequest")
    @Test
    void CreateOrderRequestParameterValidationException3() throws Exception {

        // given
        LocalDateTime deliveryDueAt = LocalDateTime.of(2025, 11, 28, 12, 0, 0);

        CreateOrderRequest request = CreateOrderRequest.builder()
            .supplierId(UUID.fromString("31e1c7c4-d7c7-4a39-9c68-1c3a9ae4c849"))
            .supplierName("무한판매")
            .receiverId(null)
            .receiverName("무한상사")
            .productId(UUID.fromString("7b8df265-6f41-4ad7-942a-7fdf81b5f1c3"))
            .productName("무한동력기")
            .quantity(10)
            .price(250000)
            .deliveryDueAt(deliveryDueAt)
            .deliveryRequestNote("무한 동력기가 고장나지 않도록 안전하게 배송해주세요.")
            .build();

        // when // then
        mockMvc.perform(
                MockMvcRequestBuilders
                    .post("/v1/orders")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @DisplayName("주문 생성시 전달되는 수령 업체의 이름이 null인 경우 BadRequest")
    @Test
    void CreateOrderRequestParameterValidationException4() throws Exception {

        // given
        LocalDateTime deliveryDueAt = LocalDateTime.of(2025, 11, 28, 12, 0, 0);

        CreateOrderRequest request = CreateOrderRequest.builder()
            .supplierId(UUID.fromString("31e1c7c4-d7c7-4a39-9c68-1c3a9ae4c849"))
            .supplierName("무한판메")
            .receiverId(UUID.fromString("f3cb7a76-0ad3-4c2d-b269-2e8c9c92a4e7"))
            .receiverName(null)
            .productId(UUID.fromString("7b8df265-6f41-4ad7-942a-7fdf81b5f1c3"))
            .productName("무한동력기")
            .quantity(10)
            .price(250000)
            .deliveryDueAt(deliveryDueAt)
            .deliveryRequestNote("무한 동력기가 고장나지 않도록 안전하게 배송해주세요.")
            .build();

        // when // then
        mockMvc.perform(
                MockMvcRequestBuilders
                    .post("/v1/orders")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @DisplayName("주문 생성시 전달되는 판매 상품의 식별자가 null인 경우 BadRequest")
    @Test
    void CreateOrderRequestParameterValidationException5() throws Exception {

        // given
        LocalDateTime deliveryDueAt = LocalDateTime.of(2025, 11, 28, 12, 0, 0);

        CreateOrderRequest request = CreateOrderRequest.builder()
            .supplierId(UUID.fromString("31e1c7c4-d7c7-4a39-9c68-1c3a9ae4c849"))
            .supplierName("무한판메")
            .receiverId(UUID.fromString("f3cb7a76-0ad3-4c2d-b269-2e8c9c92a4e7"))
            .receiverName("무한상사")
            .productId(null)
            .productName("무한동력기")
            .quantity(10)
            .price(250000)
            .deliveryDueAt(deliveryDueAt)
            .deliveryRequestNote("무한 동력기가 고장나지 않도록 안전하게 배송해주세요.")
            .build();

        // when // then
        mockMvc.perform(
                MockMvcRequestBuilders
                    .post("/v1/orders")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @DisplayName("주문 생성시 전달되는 판매 상품의 이름이 null인 경우 BadRequest")
    @Test
    void CreateOrderRequestParameterValidationException6() throws Exception {

        // given
        LocalDateTime deliveryDueAt = LocalDateTime.of(2025, 11, 28, 12, 0, 0);

        CreateOrderRequest request = CreateOrderRequest.builder()
            .supplierId(UUID.fromString("31e1c7c4-d7c7-4a39-9c68-1c3a9ae4c849"))
            .supplierName("무한판메")
            .receiverId(UUID.fromString("f3cb7a76-0ad3-4c2d-b269-2e8c9c92a4e7"))
            .receiverName("무한상사")
            .productId(UUID.fromString("7b8df265-6f41-4ad7-942a-7fdf81b5f1c3"))
            .productName(null)
            .quantity(10)
            .price(250000)
            .deliveryDueAt(deliveryDueAt)
            .deliveryRequestNote("무한 동력기가 고장나지 않도록 안전하게 배송해주세요.")
            .build();

        // when // then
        mockMvc.perform(
                MockMvcRequestBuilders
                    .post("/v1/orders")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @DisplayName("주문 생성시 전달되는 상품 수량이 0인 경우 BadRequest")
    @Test
    void CreateOrderRequestParameterValidationException7() throws Exception {

        // given
        LocalDateTime deliveryDueAt = LocalDateTime.of(2025, 11, 28, 12, 0, 0);

        CreateOrderRequest request = CreateOrderRequest.builder()
            .supplierId(UUID.fromString("31e1c7c4-d7c7-4a39-9c68-1c3a9ae4c849"))
            .supplierName("무한판메")
            .receiverId(UUID.fromString("f3cb7a76-0ad3-4c2d-b269-2e8c9c92a4e7"))
            .receiverName("무한상사")
            .productId(UUID.fromString("7b8df265-6f41-4ad7-942a-7fdf81b5f1c3"))
            .productName("무한동력기")
            .quantity(0)
            .price(250000)
            .deliveryDueAt(deliveryDueAt)
            .deliveryRequestNote("무한 동력기가 고장나지 않도록 안전하게 배송해주세요.")
            .build();

        // when // then
        mockMvc.perform(
                MockMvcRequestBuilders
                    .post("/v1/orders")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @DisplayName("주문 생성시 전달되는 상품 수량이 1001인 경우 BadRequest")
    @Test
    void CreateOrderRequestParameterValidationException8() throws Exception {

        // given
        LocalDateTime deliveryDueAt = LocalDateTime.of(2025, 11, 28, 12, 0, 0);

        CreateOrderRequest request = CreateOrderRequest.builder()
            .supplierId(UUID.fromString("31e1c7c4-d7c7-4a39-9c68-1c3a9ae4c849"))
            .supplierName("무한판메")
            .receiverId(UUID.fromString("f3cb7a76-0ad3-4c2d-b269-2e8c9c92a4e7"))
            .receiverName("무한상사")
            .productId(UUID.fromString("7b8df265-6f41-4ad7-942a-7fdf81b5f1c3"))
            .productName("무한동력기")
            .quantity(1001)
            .price(250000)
            .deliveryDueAt(deliveryDueAt)
            .deliveryRequestNote("무한 동력기가 고장나지 않도록 안전하게 배송해주세요.")
            .build();

        // when // then
        mockMvc.perform(
                MockMvcRequestBuilders
                    .post("/v1/orders")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @DisplayName("주문 생성시 전달되는 상품 수량이 1000인 경우 OK")
    @Test
    void CreateOrderRequestParameterValidationException9() throws Exception {

        // given
        LocalDateTime deliveryDueAt = LocalDateTime.of(2025, 11, 28, 12, 0, 0);

        CreateOrderRequest request = CreateOrderRequest.builder()
            .supplierId(UUID.fromString("31e1c7c4-d7c7-4a39-9c68-1c3a9ae4c849"))
            .supplierName("무한판메")
            .receiverId(UUID.fromString("f3cb7a76-0ad3-4c2d-b269-2e8c9c92a4e7"))
            .receiverName("무한상사")
            .productId(UUID.fromString("7b8df265-6f41-4ad7-942a-7fdf81b5f1c3"))
            .productName("무한동력기")
            .quantity(1000)
            .price(250000)
            .deliveryDueAt(deliveryDueAt)
            .deliveryRequestNote("무한 동력기가 고장나지 않도록 안전하게 배송해주세요.")
            .build();

        LocalDateTime orderedAt = LocalDateTime.of(2025, 11, 8, 16, 43, 05);

        Mockito.when(orderService.create(Mockito.any()))
            .thenReturn(
                CreateOrderResponse.builder()
                    .orderId("a7c6cb94-9c51-4e68-a6d3-1f0ea1c8f6b2")
                    .orderState("PENDING_VALIDATION")
                    .orderedAt(orderedAt.toString())
                    .build()
            );


        // when // then
        mockMvc.perform(
                MockMvcRequestBuilders
                    .post("/v1/orders")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("주문 생성시 전달되는 가격이 음의 정수인 경우 BadRequest")
    @Test
    void CreateOrderRequestParameterValidationException10() throws Exception {

        // given
        LocalDateTime deliveryDueAt = LocalDateTime.of(2025, 11, 28, 12, 0, 0);

        CreateOrderRequest request = CreateOrderRequest.builder()
            .supplierId(UUID.fromString("31e1c7c4-d7c7-4a39-9c68-1c3a9ae4c849"))
            .supplierName("무한판메")
            .receiverId(UUID.fromString("f3cb7a76-0ad3-4c2d-b269-2e8c9c92a4e7"))
            .receiverName("무한상사")
            .productId(UUID.fromString("7b8df265-6f41-4ad7-942a-7fdf81b5f1c3"))
            .productName("무한동력기")
            .quantity(1000)
            .price(-1)
            .deliveryDueAt(deliveryDueAt)
            .deliveryRequestNote("무한 동력기가 고장나지 않도록 안전하게 배송해주세요.")
            .build();

        // when // then
        mockMvc.perform(
                MockMvcRequestBuilders
                    .post("/v1/orders")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @DisplayName("주문 생성시 전달되는 납품 기한이 null인 경우 BadRequest")
    @Test
    void CreateOrderRequestParameterValidationException11() throws Exception {

        // given
        CreateOrderRequest request = CreateOrderRequest.builder()
            .supplierId(UUID.fromString("31e1c7c4-d7c7-4a39-9c68-1c3a9ae4c849"))
            .supplierName("무한판메")
            .receiverId(UUID.fromString("f3cb7a76-0ad3-4c2d-b269-2e8c9c92a4e7"))
            .receiverName("무한상사")
            .productId(UUID.fromString("7b8df265-6f41-4ad7-942a-7fdf81b5f1c3"))
            .productName("무한동력기")
            .quantity(1000)
            .price(-1)
            .deliveryDueAt(null)
            .deliveryRequestNote("무한 동력기가 고장나지 않도록 안전하게 배송해주세요.")
            .build();

        // when // then
        mockMvc.perform(
                MockMvcRequestBuilders
                    .post("/v1/orders")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}