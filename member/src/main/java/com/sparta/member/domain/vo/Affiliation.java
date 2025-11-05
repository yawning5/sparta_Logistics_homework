package com.sparta.member.domain.vo;

import com.sparta.member.domain.support.ArgsValidator;
import java.util.UUID;

public record Affiliation(
    Type type,
    UUID id,
    String name
) {
    public static Affiliation of(
        Type type,
        UUID id,
        String name
    ) {
        ArgsValidator.requireAllNonNull(type, id, name);
        return new Affiliation(type, id, name);
    }

    public boolean isType(Type type) {
        return type == this.type;
    }
}
