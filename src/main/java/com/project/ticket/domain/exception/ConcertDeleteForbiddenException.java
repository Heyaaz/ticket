package com.project.ticket.domain.exception;

import com.project.ticket.common.exception.ConflictException;

public class ConcertDeleteForbiddenException extends ConflictException {
  public ConcertDeleteForbiddenException(String message) {
    super("CONCERT_DELETE_FORBIDDEN", message);
  }
}

