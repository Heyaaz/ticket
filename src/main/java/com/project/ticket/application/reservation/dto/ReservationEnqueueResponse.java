package com.project.ticket.application.reservation.dto;

import lombok.Builder;

@Builder
public record ReservationEnqueueResponse(
    Long queueId
) {
}

