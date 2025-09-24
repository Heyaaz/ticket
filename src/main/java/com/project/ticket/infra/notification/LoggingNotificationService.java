package com.project.ticket.infra.notification;

import com.project.ticket.application.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoggingNotificationService implements NotificationService {
  @Override
  public void notifyReservationSuccess(Long userId, Long reservationId, Long concertId, Long seatId) {
    log.info("[NOTIFY] reservation success - userId={}, reservationId={}, concertId={}, seatId={}",
        userId, reservationId, concertId, seatId);
  }
}

