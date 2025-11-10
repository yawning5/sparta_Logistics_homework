package com.keepgoing.member.domain.vo;

import static com.keepgoing.member.domain.support.ArgsValidator.requireAllNonNull;

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
