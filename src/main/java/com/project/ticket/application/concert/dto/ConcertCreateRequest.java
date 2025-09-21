package com.project.ticket.application.concert.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ConcertCreateRequest(
    @NotBlank String title,
    @NotBlank String venue,
    @NotNull @Future LocalDateTime concertDate
) {
}

