package com.project.ticket.domain.exception;

import com.project.ticket.common.exception.ConflictException;

public class DuplicateSeatNumberException extends ConflictException {
  public DuplicateSeatNumberException() {
    super("SEAT_DUPLICATED", "이미 존재하는 좌석 번호입니다.");
  }
}

