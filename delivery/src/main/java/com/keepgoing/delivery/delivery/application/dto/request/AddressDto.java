package com.keepgoing.delivery.delivery.application.dto.request;

public record AddressDto(
        String street,
        String city,
        String zipcode
) {
}