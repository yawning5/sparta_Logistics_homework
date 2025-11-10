package com.keepgoing.member.interfaces.dto;

import com.keepgoing.member.domain.enums.Role;
import com.keepgoing.member.domain.vo.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record SignUpRequestDto(
    @NotBlank String name,
    @NotBlank String password,
    @NotBlank String email,
    @NotBlank String slackId,
    @NotNull Type affiliation_Type,
    @NotNull UUID affiliation_Id,
    @NotBlank String affiliation_name,
    @NotNull Role role
) {

}
