package com.keepgoing.member.fixture;

<<<<<<< HEAD
import com.keepgoing.member.domain.enums.Role;
import com.keepgoing.member.domain.model.Member;
import com.keepgoing.member.domain.vo.Affiliation;
import com.keepgoing.member.domain.vo.Type;
=======
import com.sparta.member.domain.enums.Role;
import com.sparta.member.domain.enums.Status;
import com.sparta.member.domain.model.Member;
import com.sparta.member.domain.vo.Affiliation;
import com.sparta.member.domain.vo.Type;
>>>>>>> 84b265aace245f24c5ee8f8823dd3a33829a6688
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 테스트 전용 Member 픽스처.
 * 프로덕션 코드에 영향을 주지 않음.
 */
public final class MemberFixture {

    private MemberFixture() {
    }

    // === 공통 기본값 ===
    public static final Long ID = 1L;
    public static final String NAME = "홍길동";
    public static final String PASSWORD = "1234";
    public static final String EMAIL = "hong@test.com";
    public static final String SLACK_ID = "hong_slack";
    public static final Role ROLE = Role.MASTER;

    // === Affiliation 생성 ===
    public static Affiliation affiliation() {
        return new Affiliation(
            Type.HUB,
            UUID.randomUUID(),
            "스파르타"
        );
    }

    // === Member 생성 ===

    /**
     * 신규 가입 요청 (id = null, Status.PENDING)
     */
    public static Member member(String name, String password) {
        return Member.requestSignUp(
            name == null ? NAME : name,
            password == null ? PASSWORD : password,
            EMAIL,
            SLACK_ID,
            affiliation(),
            ROLE
        );
    }

    /**
     * ID가 포함된 Member (Status.PENDING)
     */
    public static Member memberWithId(String name, String password) {
        return Member.from(
            ID,
            name == null ? NAME : name,
            password == null ? PASSWORD : password,
            EMAIL,
            SLACK_ID,
            affiliation(),
            ROLE,
            null,
            LocalDateTime.now(),
            LocalDateTime.now(),
            null,
            null
        );
    }

    /**
     * 승인된 Member
     */
    public static Member approvedMember() {
        Member member = memberWithId(null, null);
        member.approve();
        return member;
    }

    /**
     * 거절된 Member
     */
    public static Member rejectedMember() {
        Member member = memberWithId(null, null);
        member.reject();
        return member;
    }
}