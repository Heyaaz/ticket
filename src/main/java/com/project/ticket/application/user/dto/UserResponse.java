package com.project.ticket.application.user.dto;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record UserResponse(
    Long id,
    String nickName,
    LocalDateTime createdAt
) {
}

