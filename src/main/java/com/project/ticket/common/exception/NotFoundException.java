package com.project.ticket.common.exception;

public abstract class NotFoundException extends BusinessException {
  protected NotFoundException(String code, String message) {
    super(404, code, message);
  }
}

