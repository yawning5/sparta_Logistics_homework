package com.keepgoing.hub.service;

import com.sparta.hub.application.command.CreateHubCommand;
import com.sparta.hub.application.service.HubService;
import com.sparta.hub.domain.entity.Hub;
import com.sparta.hub.domain.repository.HubRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class HubServiceTest {

    private HubRepository hubRepository;
    private HubService hubService;

    @BeforeEach
    void setUp() {
        hubRepository = Mockito.mock(HubRepository.class);
        hubService = new HubService(hubRepository, null);
    }

    @Test
    void 허브를_생성할_수_있다() {
        // given
        CreateHubCommand command = new CreateHubCommand("서울허브", "서울 송파구", 37.5, 127.0, "ACTIVE");
        Hub mockHub = Hub.create("서울허브", "서울 송파구", 37.5, 127.0, "ACTIVE");
        when(hubRepository.save(any(Hub.class))).thenReturn(mockHub);

        // when
        var response = hubService.createHub(command, 1L);

        // then
        assertThat(response.getName()).isEqualTo("서울허브");
        assertThat(response.getHubStatus()).isEqualTo("ACTIVE");
    }

    @Test
    void 존재하지_않는_허브_조회시_예외가_발생한다() {
        // given
        UUID id = UUID.randomUUID();
        when(hubRepository.findById(id)).thenReturn(Optional.empty());

        // when & then
        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () ->
                hubService.getHub(id)
        );
    }
}