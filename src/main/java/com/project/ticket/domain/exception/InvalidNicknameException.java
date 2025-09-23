package com.project.ticket.domain.exception;

import com.project.ticket.common.exception.BadRequestException;

public class InvalidNicknameException extends BadRequestException {
  public InvalidNicknameException() {
    super("INVALID_NICKNAME", "닉네임은 필수입니다.");
  }
}

