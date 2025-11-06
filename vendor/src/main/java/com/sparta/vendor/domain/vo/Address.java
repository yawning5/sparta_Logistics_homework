package com.sparta.vendor.domain.vo;


import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
public class Address {

    private String city;
    private String street;
    private String zipCode;


    public Address(String city, String street, String zipCode) {
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
    }
}
