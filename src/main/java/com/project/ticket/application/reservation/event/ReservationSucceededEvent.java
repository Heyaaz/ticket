package com.project.ticket.application.reservation.event;

public record ReservationSucceededEvent(
    Long userId,
    Long reservationId,
    Long concertId,
    Long seatId
) {}

