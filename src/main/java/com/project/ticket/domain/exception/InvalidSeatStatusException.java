package com.project.ticket.domain.exception;

import com.project.ticket.common.exception.BadRequestException;

public class InvalidSeatStatusException extends BadRequestException {
  public InvalidSeatStatusException() {
    super("INVALID_SEAT_STATUS", "좌석 상태는 필수입니다.");
  }
}

