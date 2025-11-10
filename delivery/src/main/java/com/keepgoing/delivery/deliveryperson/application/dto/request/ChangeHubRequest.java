package com.keepgoing.delivery.deliveryperson.application.dto.request;

import java.util.UUID;

public record ChangeHubRequest(
        UUID hubId
) {
}