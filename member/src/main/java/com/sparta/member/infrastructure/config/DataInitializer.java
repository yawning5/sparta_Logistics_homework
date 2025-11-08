package com.sparta.member.infrastructure.config;

import com.sparta.member.domain.enums.Role;
import com.sparta.member.domain.model.Member;
import com.sparta.member.domain.repository.MemberRepository;
import com.sparta.member.domain.vo.Affiliation;
import com.sparta.member.domain.vo.Type;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

// TODO: 공부 및 정리 필요

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Order(1)
    public CommandLineRunner initDatabase(MemberRepository memberRepository) {
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
            memberRepository.save(master);
        };
    }

}