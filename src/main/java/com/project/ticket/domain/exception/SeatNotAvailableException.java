package com.project.ticket.domain.exception;

import com.project.ticket.common.exception.ConflictException;

public class SeatNotAvailableException extends ConflictException {
  public SeatNotAvailableException() {
    super("SEAT_NOT_AVAILABLE", "예약이 불가능한 좌석입니다.");
  }
}

