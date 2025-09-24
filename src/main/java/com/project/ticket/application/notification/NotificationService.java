package com.project.ticket.application.notification;

public interface NotificationService {
  void notifyReservationSuccess(Long userId, Long reservationId, Long concertId, Long seatId);
}

