package com.sparta.member.domain.model;

import static com.sparta.member.domain.support.ArgsValidator.requireAllNonNull;

import com.sparta.member.domain.enums.Role;
import com.sparta.member.domain.enums.Status;
import com.sparta.member.domain.vo.AccountInfo;
import com.sparta.member.domain.vo.Affiliation;
import java.time.LocalDateTime;

public class Member {

    private final Long id;
    private final AccountInfo accountInfo;
    private final Affiliation affiliation;
    private final Role role;
    private Status status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private Long deleteBy;

    private Member(
        Long Id,
        String name,
        String password,
        String email,
        String slackId,
        Affiliation affiliation,
        Role role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt,
        Long deleteBy
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
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.deleteBy = deleteBy;
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
        return new Member(null, name, password, email, slackId, affiliation, role, LocalDateTime.now(), LocalDateTime.now(), null, null);
    }

    public void approve() {
        this.status = Status.APPROVED;
        touch();
    }

    public void reject() {
        this.status = Status.REJECTED;
        touch();
    }

    public void delete(Long id) {
        this.status = Status.DELETED;
        this.deleteBy = id;
        this.deletedAt = LocalDateTime.now();
        touch();
    }

    private void touch() {
        this.updatedAt = LocalDateTime.now();
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
    public LocalDateTime createdAt() {
        return createdAt;
    }
    public LocalDateTime updatedAt() {
        return updatedAt;
    }
    public LocalDateTime deletedAt() {
        return deletedAt;
    }
    public Long deleteBy() {
        return deleteBy;
    }
    public static Member from(
        Long id,
        String name,
        String password,
        String email,
        String slackId,
        Affiliation affiliation,
        Role role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt,
        Long deleteBy
    ) {
        requireAllNonNull(
            "id", id,
            "name", name,
            "password", password,
            "email", email,
            "slackId", slackId,
            "affiliation", affiliation,
            "role", role,
            "createdAt", createdAt,
            "updatedAt", updatedAt
        );

        return new Member(id, name, password, email, slackId, affiliation, role, createdAt, updatedAt, deletedAt, deleteBy);
    }
}
