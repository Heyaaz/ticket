package com.project.ticket.common.exception;

public abstract class BusinessException extends RuntimeException {
  private final int status;
  private final String code;

  protected BusinessException(int status, String code, String message) {
    super(message);
    this.status = status;
    this.code = code;
  }

  public int getStatus() {
    return status;
  }

  public String getCode() {
    return code;
  }
}

