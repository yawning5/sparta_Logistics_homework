package com.sparta.vendor.application.service;

import com.sparta.vendor.application.exception.ErrorCode;
import com.sparta.vendor.application.exception.HubClientException;
import com.sparta.vendor.domain.vo.HubId;
import com.sparta.vendor.infrastructure.external.client.HubClient;
import com.sparta.vendor.infrastructure.external.dto.HubResponseDTO;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubClientService {

    private final HubClient hubClient;

    public void validationHub(HubId vendorId, String token) {
        try {
            String bearerToken = "Bearer " + token;
            HubResponseDTO response = hubClient.getHubById(bearerToken, vendorId.getId());

            if (response == null) {
                throw new HubClientException(ErrorCode.HUB_NOT_FOUND);
            }
        } catch (Exception e) {
            throw new HubClientException(ErrorCode.HUB_NOT_FOUND, e);
        }
    }

    public void validationHubId(UUID vendorId, String token) {
        validationHub(HubId.of(vendorId), token);
    }
}
