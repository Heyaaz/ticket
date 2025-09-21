package com.project.ticket.application.seat.dto;

import com.project.ticket.domain.status.SeatStatus;
import lombok.Builder;

@Builder
public record SeatUpdateRequest(
    String seatNumber,
    SeatStatus status
) {
}

