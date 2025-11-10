package com.sparta.vendor.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.vendor.application.command.CreateVendorCommand;
import com.sparta.vendor.application.dto.VendorResult;
import com.sparta.vendor.application.service.VendorService;
import com.sparta.vendor.config.WithMockCustomUser;
import com.sparta.vendor.domain.vo.UserRole;
import com.sparta.vendor.domain.vo.VendorType;
import com.sparta.vendor.presentation.dto.VendorTypeDTO;
import com.sparta.vendor.presentation.dto.request.CreateVendorRequestDTO;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@WebMvcTest(controllers = ExternalVendorController.class)
@EnableMethodSecurity
@TestPropertySource(properties = {
    "spring.cloud.config.enabled=false",
    "eureka.client.enabled=false"
})
class ExternalVendorControllerTest {

    private static final UUID HUB_ID = UUID.randomUUID();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VendorService vendorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("헬스 체크 - OK 반환")
    @WithMockCustomUser
    void healthCheck() throws Exception {
        mockMvc.perform(get("/v1/vendors/health-check"))
            .andExpect(status().isOk())
            .andExpect(content().string("OK"));
    }

    @Test
    @DisplayName("벤더 생성 API - 성공")
    @WithMockCustomUser(id = 1, role = UserRole.MASTER)
    void createVendor() throws Exception {
        // given
        UUID vendorId = UUID.randomUUID();

        CreateVendorRequestDTO request = new CreateVendorRequestDTO(
            "테스트벤더",
            VendorTypeDTO.RECEIVER,
            "서울시 강남구",
            "도승로 28 9",
            "12345",
            HUB_ID
        );

        VendorResult vendorResult = VendorResult.builder()
            .id(vendorId)
            .vendorName("테스트벤더")
            .vendorType(VendorType.PRODUCER)
            .city("서울시 강남구")
            .street("도승로 28 9")
            .zipCode("12345")
            .hubId(HUB_ID)
            .build();

        given(vendorService.createVendor(any(CreateVendorCommand.class))).willReturn(vendorResult);

        // when & then
        mockMvc.perform(post("/v1/vendors")
                .with(csrf())

                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.vendorName").value("테스트벤더"));
    }

    @Test
    @DisplayName("벤더 생성 API - HUB 권한 성공")
    @WithMockCustomUser(id = 1, role = UserRole.HUB, affiliationId = "07aa9c86-1a5f-42ff-a236-7a68a980c778")
    void createVendorHub() throws Exception {
        // given
        UUID vendorId = UUID.randomUUID();

        CreateVendorRequestDTO request = new CreateVendorRequestDTO(
            "테스트벤더",
            VendorTypeDTO.RECEIVER,
            "서울시 강남구",
            "도승로 28 9",
            "12345",
            HUB_ID
        );

        VendorResult vendorResult = VendorResult.builder()
            .id(vendorId)
            .vendorName("테스트벤더")
            .vendorType(VendorType.PRODUCER)
            .city("서울시 강남구")
            .street("도승로 28 9")
            .zipCode("12345")
            .hubId(HUB_ID)
            .build();

        given(vendorService.createVendor(any(CreateVendorCommand.class))).willReturn(vendorResult);

        // when & then
        mockMvc.perform(post("/v1/vendors")
                .with(csrf())

                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.vendorName").value("테스트벤더"));
    }

    @Test
    @DisplayName("벤더 생성 API - COMPANY 권한 접근 시 거부")
    @WithMockCustomUser(
        id = 1, role = UserRole.COMPANY, affiliationId = "29190093-0213-4bae-bca9-1ce082da2129")
    void createVendorCompanyForbidden() throws Exception {
        CreateVendorRequestDTO request = new CreateVendorRequestDTO(
            "테스트벤더",
            VendorTypeDTO.RECEIVER,
            "서울시 강남구",
            "도승로 28 9",
            "12345",
            HUB_ID
        );

        mockMvc.perform(post("/v1/vendors")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("벤더 생성 API - DELIVERY 권한 접근 시 거부")
    @WithMockCustomUser(
        id = 1, role = UserRole.DELIVERY, affiliationId = "3fccbeba-86cc-4001-a8eb-5d5311c6bcdb")
    void createVendorDeliveryForbidden() throws Exception {
        CreateVendorRequestDTO request = new CreateVendorRequestDTO(
            "테스트벤더",
            VendorTypeDTO.RECEIVER,
            "서울시 강남구",
            "도승로 28 9",
            "12345",
            HUB_ID
        );

        mockMvc.perform(post("/v1/vendors")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andDo(print())
            .andExpect(status().isForbidden());
    }
}
