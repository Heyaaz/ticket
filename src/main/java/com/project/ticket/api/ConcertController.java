package com.project.ticket.api;

import com.project.ticket.application.concert.ConcertApplicationService;
import com.project.ticket.application.concert.dto.ConcertCreateRequest;
import com.project.ticket.application.concert.dto.ConcertResponse;
import com.project.ticket.application.seat.SeatApplicationService;
import com.project.ticket.application.seat.dto.SeatResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/concerts")
public class ConcertController {

  private final ConcertApplicationService concertService;
  private final SeatApplicationService seatService;

  @PostMapping
  public ResponseEntity<ConcertResponse> create(
      @Valid @RequestBody ConcertCreateRequest request
  ) {
    ConcertResponse response = concertService.createConcert(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping
  public ResponseEntity<List<ConcertResponse>> list() {
    return ResponseEntity.ok(concertService.getConcerts());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ConcertResponse> get(@PathVariable Long id) {
    return ResponseEntity.ok(concertService.getConcert(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    concertService.deleteConcert(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}/seats")
  public ResponseEntity<List<SeatResponse>> getSeats(@PathVariable Long id) {
    return ResponseEntity.ok(seatService.getSeatsByConcert(id));
  }
}

