package com.project.ticket.domain.exception;

import com.project.ticket.common.exception.NotFoundException;

public class ReservationQueueNotFoundException extends NotFoundException {
  public ReservationQueueNotFoundException() {
    super("QUEUE_NOT_FOUND", "대기열을 찾을 수 없습니다.");
  }
}

