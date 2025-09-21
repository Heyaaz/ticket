package com.project.ticket.application.seat.dto;

import com.project.ticket.domain.status.SeatStatus;
import lombok.Builder;

@Builder
public record SeatResponse(
    Long id,
    String seatNumber,
    SeatStatus status
) {
}

