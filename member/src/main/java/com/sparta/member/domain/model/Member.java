package com.sparta.member.domain.model;

import static com.sparta.member.domain.support.ArgsValidator.requireAllNonNull;

import com.sparta.member.domain.enums.Role;
import com.sparta.member.domain.enums.Status;
import com.sparta.member.domain.vo.AccountInfo;
import com.sparta.member.domain.vo.Affiliation;

public class Member {

    private final Long id;
    private final AccountInfo accountInfo;
    private final Affiliation affiliation;
    private final Role role;
    private Status status;

    private Member(
        Long Id,
        String name,
        String password,
        String email,
        String slackId,
        Affiliation affiliation,
        Role role
    ) {
        this.id = Id;
        this.accountInfo = new AccountInfo(
            name,
            password,
            email,
            slackId
        );
        this.affiliation = affiliation;
        this.role = role;
        this.status = Status.PENDING;
    }

    public static Member requestSignUp(
        String name,
        String password,
        String email,
        String slackId,
        Affiliation affiliation,
        Role role
    ) {
        requireAllNonNull(
            "name", name,
            "accountInfo", new AccountInfo(
                name, password, email, slackId
            ),
            "affiliation", affiliation,
            "role", role
        );
        return new Member(null, name, password, email, slackId, affiliation, role);
    }

    public static Member from(
        Long id,
        String name,
        String password,
        String email,
        String slackId,
        Affiliation affiliation,
        Role role
    ) {
        requireAllNonNull(
            "id", id,
            "name", name,
            "password", password,
            "email", email,
            "slackId", slackId,
            "affiliation", affiliation,
            "role", role
        );

        return new Member(id, name, password, email, slackId, affiliation, role);
    }

    public void approveMember() {
        this.status = Status.APPROVED;
    }

    public void rejectMember() {
        this.status = Status.REJECTED;
    }

    public boolean isSameMember(Member other) {
        return this.id != null && this.id.equals(other.id);
    }

    public Long id() {
        return id;
    }
    public AccountInfo accountInfo() {
        return accountInfo;
    }
    public Affiliation affiliation() {
        return affiliation;
    }
    public Role role() {
        return role;
    }
    public Status status() {
        return status;
    }
}
