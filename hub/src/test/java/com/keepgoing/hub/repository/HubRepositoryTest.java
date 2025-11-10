//package com.keepgoing.hub.repository;

//import com.keepgoing.hub.domain.entity.Hub;
//import com.keepgoing.hub.infrastructure.repository.JpaHubRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//@ActiveProfiles("test")
//class HubRepositoryTest {
//
//    @Autowired
//    private JpaHubRepository jpaHubRepository;
//
//    @Test
//    @DisplayName("허브를 저장하고 조회할 수 있다")
//    void 허브_저장_조회_테스트() {
//        // given
//        Hub hub = Hub.create("서울허브", "서울 송파구", 37.5, 127.0, "ACTIVE");
//
//        // when
//        Hub savedHub = jpaHubRepository.save(hub);
//        Optional<Hub> foundHub = jpaHubRepository.findById(savedHub.getId());
//
//        // then
//        assertThat(foundHub).isPresent();
//        assertThat(foundHub.get().getName()).isEqualTo("서울허브");
//        assertThat(foundHub.get().getHubStatus()).isEqualTo("ACTIVE");
//    }
//
//    @Test
//    @DisplayName("허브를 논리적으로 삭제하면 deletedAt이 설정된다")
//    void 허브_논리삭제_테스트() {
//        // given
//        Hub hub = Hub.create("부산허브", "부산 동구", 35.2, 129.1, "ACTIVE");
//        Hub savedHub = jpaHubRepository.save(hub);
//
//        // when
//        savedHub.delete(1L);
//        jpaHubRepository.save(savedHub);
//
//        // then
//        Hub deletedHub = jpaHubRepository.findById(savedHub.getId()).orElseThrow();
//        assertThat(deletedHub.getDeletedAt()).isNotNull();
//        assertThat(deletedHub.getDeletedBy()).isEqualTo(1L);
//    }
//
//    @Test
//    @DisplayName("논리삭제된 허브는 검색 대상에서 제외할 수 있다")
//    void 허브_검색시_논리삭제_제외_테스트() {
//        // given
//        Hub activeHub = Hub.create("광주허브", "광주 서구", 35.0, 126.8, "ACTIVE");
//        Hub deletedHub = Hub.create("대구허브", "대구 북구", 36.0, 128.6, "ACTIVE");
//
//        jpaHubRepository.save(activeHub);
//        deletedHub.delete(1L);
//        jpaHubRepository.save(deletedHub);
//
//        // when
//        var hubs = jpaHubRepository.findAll();
//
//        // then
//        assertThat(hubs)
//                .extracting("name")
//                .contains("광주허브")
//                .doesNotContain("대구허브");
//    }
//}