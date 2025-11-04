package com.sparta.member.doamin.vo;

import java.util.UUID;

public record Affiliation(
    Type type,
    UUID id,
    String name
) {

}
