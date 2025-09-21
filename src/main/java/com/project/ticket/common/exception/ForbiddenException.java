package com.project.ticket.common.exception;

public abstract class ForbiddenException extends BusinessException {
  protected ForbiddenException(String code, String message) {
    super(403, code, message);
  }
}

