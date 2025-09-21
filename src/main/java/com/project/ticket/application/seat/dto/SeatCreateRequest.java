package com.project.ticket.application.seat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SeatCreateRequest(
    @NotNull Long concertId,
    @NotBlank String seatNumber
) {
}

