package com.project.ticket.application.concert.dto;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ConcertResponse(
    Long id,
    String title,
    String venue,
    LocalDateTime concertDate
) {
}

