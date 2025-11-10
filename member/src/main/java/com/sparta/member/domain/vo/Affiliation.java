package com.keepgoing.member.domain.vo;

import static com.keepgoing.member.domain.support.ArgsValidator.requireAllNonNull;

import com.keepgoing.member.domain.support.ArgsValidator;
import java.util.UUID;

public record Affiliation(
    Type type,
    UUID id,
    String name
) {

    public Affiliation {
        requireAllNonNull(
            "type", type,
            "id", id,
            "name", name
        );
    }

    public boolean isType(Type type) {
        return type == this.type;
    }
}
