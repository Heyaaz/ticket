package com.project.ticket.application.reservation.dto;

import com.project.ticket.domain.status.ReservationStatus;
import lombok.Builder;

@Builder
public record ReservationCancelResponse(
    Long reservationId,
    ReservationStatus status
) {
}

