package com.project.ticket.domain.exception;

import com.project.ticket.common.exception.ConflictException;

public class DuplicateQueueRequestException extends ConflictException {
  public DuplicateQueueRequestException() {
    super("QUEUE_DUPLICATED", "이미 예약 대기열에 등록된 요청입니다.");
  }
}

