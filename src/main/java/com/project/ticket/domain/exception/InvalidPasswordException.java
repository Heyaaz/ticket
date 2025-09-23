package com.project.ticket.domain.exception;

import com.project.ticket.common.exception.BadRequestException;

public class InvalidPasswordException extends BadRequestException {
  public InvalidPasswordException() {
    super("INVALID_PASSWORD", "패스워드는 필수입니다.");
  }
}

