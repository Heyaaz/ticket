package com.project.ticket.domain.exception;

import com.project.ticket.common.exception.ConflictException;

public class SeatReservedDeletionNotAllowedException extends ConflictException {
  public SeatReservedDeletionNotAllowedException() {
    super("SEAT_DELETE_FORBIDDEN", "예약된 좌석은 삭제할 수 없습니다.");
  }
}

