package com.project.ticket.domain.exception;

import com.project.ticket.common.exception.ForbiddenException;

public class AuthorizationException extends ForbiddenException {
  public AuthorizationException() {
    super("FORBIDDEN", "권한이 없습니다.");
  }
}

