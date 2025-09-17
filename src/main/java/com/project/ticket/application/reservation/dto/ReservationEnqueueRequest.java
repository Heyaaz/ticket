package com.project.ticket.application.reservation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ReservationEnqueueRequest(
    @NotNull
    Long userId,

    @NotNull
    Long seatId
) {
}

