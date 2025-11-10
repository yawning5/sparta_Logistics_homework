package com.sparta.member.domain.model;

import static com.sparta.member.domain.support.ArgsValidator.requireAllNonNull;
import static org.springframework.util.Assert.hasText;

import com.sparta.member.domain.enums.Role;
import com.sparta.member.domain.enums.Status;
import com.sparta.member.domain.vo.AccountInfo;
import com.sparta.member.domain.vo.Affiliation;
import com.sparta.member.domain.vo.Type;
import java.time.LocalDateTime;
import java.util.UUID;

public class Member {

    private final Long id;
    private final AccountInfo accountInfo;
    private final Affiliation affiliation;
    private Role role;
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
        Status status,
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
        this.status = status == null ? Status.PENDING :  status;
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
        return new Member(null, name, password, email, slackId, affiliation, role, null,LocalDateTime.now(), LocalDateTime.now(), null, null);
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
        Status status,
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

        return new Member(id, name, password, email, slackId, affiliation, role, status, createdAt, updatedAt, deletedAt, deleteBy);
    }

    public Member changeInfo(
        String name,
        String password,
        String email,
        String slackId,
        Type affiliationType,
        UUID affiliationId,
        String affiliationName,
        Role role,
        Status status
    ) {
        // 기존 값 유지
        String newName = hasText(name) ? name : this.accountInfo.name();
        String newPassword = hasText(password) ? password : this.accountInfo.password();
        String newEmail = hasText(email) ? email : this.accountInfo.email();
        String newSlackId = hasText(slackId) ? slackId : this.accountInfo.slackId();

        Type newType = affiliationType != null ? affiliationType : this.affiliation.type();
        UUID newAffiliationId = affiliationId != null ? affiliationId : this.affiliation.id();
        String newAffiliationName = hasText(affiliationName) ? affiliationName : this.affiliation.name();

        Role newRole = role != null ? role : this.role;
        Status newStatus = status != null ? status : this.status;

        // 새 VO 구성
        AccountInfo newAccountInfo = new AccountInfo(newName, newPassword, newEmail, newSlackId);
        Affiliation newAffiliation = new Affiliation(newType, newAffiliationId, newAffiliationName);

        // 새로운 Member 인스턴스 반환 (불변성 유지)
        return new Member(
            this.id,
            newAccountInfo.name(),
            newAccountInfo.password(),
            newAccountInfo.email(),
            newAccountInfo.slackId(),
            newAffiliation,
            newRole,
            newStatus,
            this.createdAt,
            LocalDateTime.now(), // updatedAt
            this.deletedAt,
            this.deleteBy
        );
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    public void changRole(Role role) {
        this.role = role;
    }
}
