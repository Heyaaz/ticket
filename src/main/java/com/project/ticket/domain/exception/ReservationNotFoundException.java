package com.project.ticket.domain.exception;

import com.project.ticket.common.exception.NotFoundException;

public class ReservationNotFoundException extends NotFoundException {
  public ReservationNotFoundException() {
    super("RESERVATION_NOT_FOUND", "예약을 찾을 수 없습니다.");
  }
}

