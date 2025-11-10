package com.keepgoing.vendor.domain.vo;


import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class Address {

    private String city;
    private String street;
    private String zipCode;


    public Address(String city, String street, String zipCode) {
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
    }

    public static Address of(String city, String street, String zipCode) {
        return new Address(city, street, zipCode);
    }
}
