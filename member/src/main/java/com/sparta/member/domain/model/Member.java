package com.sparta.member.domain.model;

import static com.sparta.member.domain.support.ArgsValidator.requireAllNonNull;

import com.sparta.member.domain.enums.Role;
import com.sparta.member.domain.enums.Status;
import com.sparta.member.domain.vo.Affiliation;

public class Member {

    private final String name;
    private final String password;
    private final String email;
    private final String slackId;
    private final Affiliation affiliation;
    private Status status;
    private final Role role;

    private Member(
        String name,
        String password,
        String email,
        String slackId,
        Affiliation affiliation,
        Role role
    ) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.slackId = slackId;
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
            "password", password,
            "email", email,
            "slackId", slackId,
            "affiliation", affiliation,
            "role", role);
        return new Member(name, password, email, slackId, affiliation, role);
    }

    public void approveMember(
    ) {
        this.status = Status.APPROVED;
    }

    public void rejectMember() {
        this.status = Status.REJECTED;
    }

    /**
     * Spring Security에서 로그인 ID(username)로 사용하는 이메일을 반환합니다.
     * @return 이메일 주소 (Member.email) (String)
     */
    public String getUsername() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }
}
