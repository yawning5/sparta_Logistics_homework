package com.keepgoing.delivery.delivery.infrastructure.api.client;

import com.keepgoing.delivery.delivery.infrastructure.api.dto.HubRouteResponse;
import com.keepgoing.delivery.global.exception.BusinessException;
import com.keepgoing.delivery.global.exception.ErrorCode;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubRouteService {

    private final HubRouteClient hubRouteClient;

    public HubRouteResponse getHubRoute(UUID routeId, String token) {
        try {
            return hubRouteClient.getHubRoute(routeId, token);

        } catch (FeignException.NotFound e) {
            throw new BusinessException(ErrorCode.HUB_ROUTE_NOT_FOUND);

        } catch (FeignException e) {
            throw new BusinessException(ErrorCode.HUB_SERVICE_UNAVAILABLE);

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}