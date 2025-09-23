package com.project.ticket.common.exception;

public abstract class BadRequestException extends BusinessException {
  protected BadRequestException(String code, String message) {
    super(400, code, message);
  }
}

