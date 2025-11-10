package com.sparta.member.domain.vo;

import static com.sparta.member.domain.support.ArgsValidator.requireAllNonNull;

public record AccountInfo(
    String name,
    String password,
    String email,
    String slackId
) {
    public AccountInfo {
        requireAllNonNull(
            "name", name,
            "password", password,
            "email", email,
            "slackId", slackId
        );
    }
}
