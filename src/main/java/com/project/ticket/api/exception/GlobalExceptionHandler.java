package com.project.ticket.api.exception;

import com.project.ticket.common.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    List<ErrorResponse.FieldErrorDetail> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
        .map(fe -> ErrorResponse.FieldErrorDetail.builder()
            .field(fe.getField())
            .rejectedValue(fe.getRejectedValue())
            .reason(resolveMessage(fe))
            .build())
        .collect(Collectors.toList());

    ErrorResponse body = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .code("VALIDATION_ERROR")
        .message("요청 값이 올바르지 않습니다.")
        .path(null)
        .fieldErrors(fieldErrors)
        .build();
    return ResponseEntity.badRequest().body(body);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    ErrorResponse body = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .code("INVALID_JSON")
        .message("요청 본문을 읽을 수 없습니다.")
        .path(null)
        .build();
    return ResponseEntity.badRequest().body(body);
  }

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(
      NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    ErrorResponse body = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.NOT_FOUND.value())
        .error(HttpStatus.NOT_FOUND.getReasonPhrase())
        .code("NOT_FOUND")
        .message("요청한 리소스를 찾을 수 없습니다.")
        .path(ex.getRequestURL())
        .build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  protected ResponseEntity<ErrorResponse> handleConstraintViolation(
      ConstraintViolationException ex, HttpServletRequest req) {
    List<ErrorResponse.FieldErrorDetail> details = ex.getConstraintViolations().stream()
        .map(v -> ErrorResponse.FieldErrorDetail.builder()
            .field(v.getPropertyPath().toString())
            .rejectedValue(v.getInvalidValue())
            .reason(v.getMessage())
            .build())
        .collect(Collectors.toList());
    ErrorResponse body = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .code("VALIDATION_ERROR")
        .message("요청 값이 올바르지 않습니다.")
        .path(req.getRequestURI())
        .fieldErrors(details)
        .build();
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  protected ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest req) {
    ErrorResponse body = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .code("BAD_REQUEST")
        .message(ex.getMessage())
        .path(req.getRequestURI())
        .build();
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(IllegalStateException.class)
  protected ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException ex, HttpServletRequest req) {
    ErrorResponse body = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.CONFLICT.value())
        .error(HttpStatus.CONFLICT.getReasonPhrase())
        .code("CONFLICT")
        .message(ex.getMessage())
        .path(req.getRequestURI())
        .build();
    return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
  }

  @ExceptionHandler(BusinessException.class)
  protected ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex, HttpServletRequest req) {
    ErrorResponse body = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(ex.getStatus())
        .error(HttpStatus.valueOf(ex.getStatus()).getReasonPhrase())
        .code(ex.getCode())
        .message(ex.getMessage())
        .path(req.getRequestURI())
        .build();
    return ResponseEntity.status(ex.getStatus()).body(body);
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorResponse> handleAny(Exception ex, HttpServletRequest req) {
    ErrorResponse body = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
        .code("INTERNAL_ERROR")
        .message("예상치 못한 오류가 발생했습니다.")
        .path(req.getRequestURI())
        .build();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }

  private String resolveMessage(FieldError fe) {
    return fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "잘못된 값";
  }
}
