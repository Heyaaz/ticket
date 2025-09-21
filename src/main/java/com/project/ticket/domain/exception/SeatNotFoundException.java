package com.project.ticket.domain.exception;

import com.project.ticket.common.exception.NotFoundException;

public class SeatNotFoundException extends NotFoundException {
  public SeatNotFoundException() {
    super("SEAT_NOT_FOUND", "좌석을 찾을 수 없습니다.");
  }
}

