package com.keepgoing.delivery.delivery.application.dto.response;

import com.keepgoing.delivery.delivery.domain.vo.Address;

public record AddressResponse(
        String street,
        String city,
        String zipcode
) {
    public static AddressResponse from(Address address) {
        return new AddressResponse(
                address.street(),
                address.city(),
                address.zipcode()
        );
    }
}