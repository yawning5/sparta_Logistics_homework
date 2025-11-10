package com.keepgoing.delivery.delivery.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddressJpa {

    @Column(name = "address_street", nullable = false)
    private String street;

    @Column(name = "address_city", nullable = false)
    private String city;

    @Column(name = "address_zipcode", nullable = false)
    private String zipcode;

    public AddressJpa(String street, String city, String zipcode) {
        this.street = street;
        this.city = city;
        this.zipcode = zipcode;
    }
}