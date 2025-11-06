package com.sparta.member.infrastructure.persistence.jpa.entity;

import com.sparta.member.domain.enums.Role;
import com.sparta.member.domain.enums.Status;
import com.sparta.member.domain.vo.Type;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "p_member")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String password;
    private String email;
    private String slackId;

    @Enumerated(EnumType.STRING)
    private Type affiliationType;
    private UUID affiliationId;
    private String affiliationName;

    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public MemberJpa(
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
        this.name = name;
        this.password = password;
        this.email = email;
        this.slackId = slackId;
        this.affiliationType = affiliationType;
        this.affiliationId = affiliationId;
        this.affiliationName = affiliationName;
        this.role = role;
        this.status = status;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MemberJpa other)) return false;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
