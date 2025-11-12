package com.sparta.vendor.infrastructure.external.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record HubResponseDTO(
    UUID id,
    String name,
    String address,
    Double latitude,
    Double longitude,
    String hubStatus,
    LocalDateTime createAt,
    LocalDateTime updateAt,
    LocalDateTime deleteAt
) {

}
