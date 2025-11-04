package com.sparta.member.doamin.model;

import com.sparta.member.doamin.enums.Role;
import com.sparta.member.doamin.enums.Status;
import com.sparta.member.doamin.vo.Affiliation;

public class Member {

    private String name;
    private String password;
    private String email;
    private String slackId;
    private Affiliation affiliation;
    private Status status;
    private Role role;

    public Member(
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

}
