package com.project.ticket.api.exception;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record ErrorResponse(
    LocalDateTime timestamp,
    int status,
    String error,
    String code,
    String message,
    String path,
    List<FieldErrorDetail> fieldErrors
) {
  @Builder
  public record FieldErrorDetail(
      String field,
      Object rejectedValue,
      String reason
  ) {}
}

