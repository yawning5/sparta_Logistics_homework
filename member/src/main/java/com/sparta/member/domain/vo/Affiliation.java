package com.sparta.member.domain.vo;

import static com.sparta.member.domain.support.ArgsValidator.requireAllNonNull;

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
        requireAllNonNull(
            "type", type,
            "id", id,
            "name", name);
        return new Affiliation(type, id, name);
    }

    public boolean isType(Type type) {
        return type == this.type;
    }
}
