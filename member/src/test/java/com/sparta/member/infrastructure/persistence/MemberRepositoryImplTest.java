package com.sparta.member.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sparta.member.config.TestContainerConfig;
import com.sparta.member.domain.model.Member;
import com.sparta.member.domain.vo.AccountInfo;
import com.sparta.member.domain.vo.Type;
import com.sparta.member.infrastructure.config.QuerydslConfig;
import com.sparta.member.infrastructure.persistence.jpa.MemberRepositoryImpl;
import com.sparta.member.infrastructure.persistence.jpa.querydsl.QueryDslMemberRepositoryImpl;
import com.sparta.member.infrastructure.persistence.mapper.MemberJpaMapper;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({
    MemberJpaMapper.class,
    MemberRepositoryImpl.class,
    QueryDslMemberRepositoryImpl.class,
    QuerydslConfig.class,
    TestContainerConfig.class
})
public class MemberRepositoryImplTest {

    @Autowired
    MemberRepositoryImpl memberRepository;

    Member testMember;

    @BeforeEach
    void setUp() {
        // 1. 모든 Member에 id=null, AccountInfo 직접 주입
        List<Member> members = Instancio.ofList(Member.class)
            .size(54)
            .set(Select.field(Member.class, "id"), null)
            .supply(Select.field(Member.class, "accountInfo"),
                () -> new AccountInfo(
                    "user-" + UUID.randomUUID(),
                    "pw",
                    "user" + UUID.randomUUID() + "@example.com",
                    "slack-" + UUID.randomUUID()
                )
            )
            .create();

        // 2. 테스트용 Member 명시 생성
        testMember = Instancio.of(Member.class)
            .set(Select.field("id"), null)
            .supply(Select.field("accountInfo"),
                () -> new AccountInfo(
                    "tester",
                    "pw",
                    "test@test.com",
                    "slack123"
                )
            )
            .create();

        // 3. 리스트에 추가 후 저장
        members.add(testMember);
        memberRepository.saveAll(members);
    }
    @AfterEach
    void tearDown() {

    }

    @Test
    public void test() {
    }


    @Nested
    @DisplayName("검색 조회 테스트")
    class Search {

        @Test
        @DisplayName("아무 조건 없다면 조건 없는 전체 조회")
        public void findBySearchOption() {
            // given
            Pageable pageable = PageRequest.of(0, 10);

            // when
            Page<Member> page = memberRepository.findBySearchOption(
                pageable,
                null,
                null,
                null,
                null,
                null,
                null
            );

            // then
            assertAll(
                () -> assertEquals(55, page.getTotalElements()),
                () -> assertEquals(0, page.getNumber()),
                () -> assertTrue(page.getTotalPages() >= 6),
                () -> assertThat(page.getContent()).allMatch(Objects::nonNull)
            );
        }

        @Test
        @DisplayName("이메일 검색 조건 test@test.com")
        public void searchOption_email() {
            // given
            Pageable pageable = PageRequest.of(0, 10);

            // when
            Page<Member> page = memberRepository.findBySearchOption(
                pageable,
                null,
                null,
                null,
                "test@test.com",
                null,
                null
            );

            // then
            boolean containsTestMember = page.getContent().stream()
            .anyMatch(member -> "test@test.com".equals(member.accountInfo().email()));
            assertAll(
                () -> assertTrue(containsTestMember)
            );
        }

        @Test
        @DisplayName("HUB 검색조건")
        public void searchOption_HUB() {
            // given
            Pageable pageable = PageRequest.of(0, 10);


            // when
            Page<Member> page = memberRepository.findBySearchOption(
                pageable,
                null,
                "HUB",
                null,
                null,
                null,
                null
            );

            // then
            assertAll(
                () -> assertTrue((page.getContent().stream()
                    .allMatch(member -> member.affiliation().isType(Type.HUB))))
            );

        }
    }
}
