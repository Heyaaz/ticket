package com.project.ticket.application.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationQueueService {

  private final ReservationApplicationService reservationApplicationService;

  @Value("${app.reservation.worker.batch-size:50}")
  private int batchSize;

  @Value("${app.reservation.worker.enabled:true}")
  private boolean enabled;

  // fixedDelay: 이전 수행 종료 시점 기준 지연 후 재실행
  @Scheduled(fixedDelayString = "${app.reservation.worker.delay-ms:250}")
  public void run() {
    if (!enabled) return;
    reservationApplicationService.processPendingReservations(batchSize);
  }
}

