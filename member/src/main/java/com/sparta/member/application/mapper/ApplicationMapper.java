package com.sparta.member.application.mapper;

import com.sparta.member.application.dto.SignUpRequestDto;
import com.sparta.member.domain.model.Member;

public interface ApplicationMapper {
    Member signUpRequestDtoToMember(SignUpRequestDto signUpRequestDto);
}
