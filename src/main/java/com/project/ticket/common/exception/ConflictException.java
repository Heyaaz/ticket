package com.project.ticket.common.exception;

public abstract class ConflictException extends BusinessException {
  protected ConflictException(String code, String message) {
    super(409, code, message);
  }
}

