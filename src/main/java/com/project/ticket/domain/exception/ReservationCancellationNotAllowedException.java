package com.project.ticket.domain.exception;

import com.project.ticket.common.exception.ConflictException;

public class ReservationCancellationNotAllowedException extends ConflictException {
  public ReservationCancellationNotAllowedException() {
    super("RESERVATION_CANCEL_FORBIDDEN", "취소할 수 없는 예약 상태입니다.");
  }
}

