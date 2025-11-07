package com.sparta.member.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sparta.member.domain.enums.Role;
import com.sparta.member.domain.model.Member;
import com.sparta.member.domain.vo.Type;
import com.sparta.member.infrastructure.persistence.jpa.MemberRepositoryImpl;
import com.sparta.member.infrastructure.persistence.mapper.MemberJpaMapper;
import java.util.List;
import java.util.Objects;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Import({
    MemberJpaMapper.class,
    MemberRepositoryImpl.class
})
public class MemberRepositoryImplTest {

    @Autowired
    MemberRepositoryImpl memberRepository;

    Member testMember;

    @BeforeEach
    void setUp() {
        List<Member> members = Instancio.ofList(Member.class)
            .size(153)
            .create();
        testMember = Instancio.of(Member.class)
            .set(Select.field("email"), "test@test.com")
            .create();
        members.add(testMember);
        memberRepository.saveAll(members);
    }
    @AfterEach
    void tearDown() {

    }


    @Nested
    @DisplayName("검색 조회 테스트")
    class Search {

        @Test
        @Transactional
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
                null
            );

            // then
            assertAll(
                () -> assertEquals(154, page.getTotalElements()),
                () -> assertEquals(0, page.getNumber()),
                () -> assertEquals(16, page.getTotalPages()),
                () -> assertTrue(page.getTotalPages() >= 16),
                () -> assertThat(page.getContent()).allMatch(Objects::nonNull)
            );
        }

        @Test
        @Transactional
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
                "test@test.com"
            );

            // then
            boolean containsTestMember = page.getContent().stream()
            .anyMatch(member -> "test@test.com".equals(member.accountInfo().email()));
            assertAll(
                () -> assertTrue(containsTestMember)
            );
        }

        @Test
        @Transactional
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
