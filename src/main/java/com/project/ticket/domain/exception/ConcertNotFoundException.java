package com.project.ticket.domain.exception;

import com.project.ticket.common.exception.NotFoundException;

public class ConcertNotFoundException extends NotFoundException {
  public ConcertNotFoundException() {
    super("CONCERT_NOT_FOUND", "콘서트를 찾을 수 없습니다.");
  }
}

