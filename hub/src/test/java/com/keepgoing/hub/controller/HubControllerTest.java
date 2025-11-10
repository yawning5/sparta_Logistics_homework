//package com.keepgoing.hub.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.keepgoing.hub.application.command.CreateHubCommand;
//import com.keepgoing.hub.application.dto.HubResponse;
//import com.keepgoing.hub.application.service.HubService;
//import com.keepgoing.hub.presentation.HubController;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.UUID;
//
//import static org.mockito.Mockito.when;
//
//
//@ExtendWith(MockitoExtension.class)
//class HubControllerTest {
//
//    @InjectMocks
//    private HubController hubController;
//
//    @Mock
//    private HubService hubService;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Test
//    void 허브_생성_API_성공() throws Exception {
//        // given
//        CreateHubCommand command = new CreateHubCommand(
//                "서울허브", "서울 송파구", 37.5, 127.0, "ACTIVE"
//        );
//
//        HubResponse response = HubResponse.builder()
//                .id(UUID.randomUUID())
//                .name("서울허브")
//                .address("서울 송파구")
//                .hubStatus("ACTIVE")
//                .build();
//
//        when(hubService.createHub(command, 1L)).thenReturn(response);
//
//        // when
//        HubResponse result = hubController.createHub(command, 1L);
//
//        // then
//        assertEquals("서울허브", result.getName());
//    }
//}