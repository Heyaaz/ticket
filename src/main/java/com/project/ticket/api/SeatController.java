package com.project.ticket.api;

import com.project.ticket.application.seat.SeatApplicationService;
import com.project.ticket.application.seat.dto.SeatCreateRequest;
import com.project.ticket.application.seat.dto.SeatResponse;
import com.project.ticket.application.seat.dto.SeatUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seats")
public class SeatController {

  private final SeatApplicationService seatService;

  @PostMapping
  public ResponseEntity<SeatResponse> create(
      @Valid @RequestBody SeatCreateRequest request
  ) {
    SeatResponse response = seatService.createSeat(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<SeatResponse> update(
      @PathVariable Long id,
      @RequestBody SeatUpdateRequest request
  ) {
    return ResponseEntity.ok(seatService.updateSeat(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    seatService.deleteSeat(id);
    return ResponseEntity.noContent().build();
  }
}

