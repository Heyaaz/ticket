package com.project.ticket.domain.exception;

import com.project.ticket.common.exception.ConflictException;

public class DuplicateNicknameException extends ConflictException {
  public DuplicateNicknameException() {
    super("NICKNAME_DUPLICATED", "이미 사용 중인 닉네임입니다.");
  }
}

