package com.project.ticket.application.reservation;

import com.project.ticket.application.notification.NotificationService;
import com.project.ticket.application.reservation.event.ReservationSucceededEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationEventListener {

  private final NotificationService notificationService;

  @Value("${app.notifications.enabled:true}")
  private boolean notificationsEnabled;

  @EventListener
  public void onReservationSucceeded(ReservationSucceededEvent event) {
    if (!notificationsEnabled) return;
    notificationService.notifyReservationSuccess(
        event.userId(), event.reservationId(), event.concertId(), event.seatId());
  }
}

