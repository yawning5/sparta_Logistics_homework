package com.keepgoing.member.infrastructure.config;

import com.keepgoing.member.domain.enums.Role;
import com.keepgoing.member.domain.model.Member;
import com.keepgoing.member.domain.repository.MemberRepository;
import com.keepgoing.member.domain.vo.Affiliation;
import com.keepgoing.member.domain.vo.Type;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

// TODO: 공부 및 정리 필요

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Order(1)
    public CommandLineRunner initDatabase() {
        log.info("init database start MASTER account");
        return args -> {
            String masterEmail = "master@master.com";
            if (memberRepository.existsByEmail(masterEmail)) {
                return;
            }
            Member master = Member.requestSignUp(
                "마스터",
                passwordEncoder.encode("123"),
                "master@master.com",
                "master",
                new Affiliation(
                    Type.COMPANY,
                    UUID.randomUUID(),
                    "sparta"
                ),
                Role.MASTER
            );
            master.approve();
            log.info("init database finish MASTER account" + master.status());
            memberRepository.save(master);
        };
    }

}