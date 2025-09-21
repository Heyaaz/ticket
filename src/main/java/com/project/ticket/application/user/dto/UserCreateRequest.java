package com.project.ticket.application.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserCreateRequest(
    @NotBlank String nickName,
    @NotBlank String password
) {
}

