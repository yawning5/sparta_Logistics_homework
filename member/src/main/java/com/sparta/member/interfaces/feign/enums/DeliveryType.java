package com.sparta.member.interfaces.feign.enums;

import com.sparta.member.domain.vo.Type;

public enum DeliveryType {
    HUB,
    VENDOR;

    public static DeliveryType fromValue(Type affiliationType){
        if (affiliationType == Type.HUB){
            return VENDOR;
        }
        return HUB;
    }
}
