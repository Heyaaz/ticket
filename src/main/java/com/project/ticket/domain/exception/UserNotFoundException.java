package com.project.ticket.domain.exception;

import com.project.ticket.common.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
  public UserNotFoundException() {
    super("USER_NOT_FOUND", "사용자를 찾을 수 없습니다.");
  }
}

