package com.project.ticket.domain.exception;

import com.project.ticket.common.exception.BadRequestException;

public class InvalidSeatNumberException extends BadRequestException {
  public InvalidSeatNumberException() {
    super("INVALID_SEAT_NUMBER", "좌석 번호는 필수입니다.");
  }
}

