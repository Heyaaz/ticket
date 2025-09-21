package com.project.ticket.api;

import com.project.ticket.application.reservation.ReservationApplicationService;
import com.project.ticket.application.reservation.dto.ReservationCancelRequest;
import com.project.ticket.application.reservation.dto.ReservationCancelResponse;
import com.project.ticket.application.reservation.dto.ReservationEnqueueRequest;
import com.project.ticket.application.reservation.dto.ReservationEnqueueResponse;
import com.project.ticket.application.reservation.dto.ReservationStatusResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {

  private final ReservationApplicationService reservationService;

  @PostMapping
  public ResponseEntity<ReservationEnqueueResponse> requestReservation(
      @Valid @RequestBody ReservationEnqueueRequest request
  ) {
    ReservationEnqueueResponse response = reservationService.requestReservation(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/status/{queueId}")
  public ResponseEntity<ReservationStatusResponse> getStatus(
      @PathVariable Long queueId
  ) {
    return ResponseEntity.ok(reservationService.getStatus(queueId));
  }

  @PostMapping("/cancel")
  public ResponseEntity<ReservationCancelResponse> cancel(
      @Valid @RequestBody ReservationCancelRequest request
  ) {
    return ResponseEntity.ok(reservationService.cancelReservation(request));
  }
}

