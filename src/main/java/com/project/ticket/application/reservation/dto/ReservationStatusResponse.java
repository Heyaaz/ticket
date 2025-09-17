package com.project.ticket.application.reservation.dto;

import com.project.ticket.domain.status.QueueStatus;
import lombok.Builder;

@Builder
public record ReservationStatusResponse(
    Long queueId,

    QueueStatus status,

    Long reservationId
) {
}

