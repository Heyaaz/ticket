package com.project.ticket.application.concert;

import com.project.ticket.application.concert.dto.ConcertCreateRequest;
import com.project.ticket.application.concert.dto.ConcertResponse;
import com.project.ticket.domain.concert.Concert;
import com.project.ticket.domain.seat.Seat;
import com.project.ticket.domain.status.SeatStatus;
import com.project.ticket.infra.persistence.concert.ConcertRepository;
import com.project.ticket.infra.persistence.reservation.ReservationRepository;
import com.project.ticket.infra.persistence.seat.SeatRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConcertApplicationService {

  private final ConcertRepository concertRepository;
  private final SeatRepository seatRepository;
  private final ReservationRepository reservationRepository;

  @Transactional(readOnly = true)
  public List<ConcertResponse> getConcerts() {
    List<Concert> concerts = concertRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    return concerts.stream()
        .map(ConcertApplicationService::toResponse)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public ConcertResponse getConcert(Long concertId) {
    Concert concert = concertRepository.findById(concertId)
        .orElseThrow(() -> new IllegalArgumentException("콘서트를 찾을 수 없습니다."));
    return toResponse(concert);
  }

  @Transactional
  public ConcertResponse createConcert(ConcertCreateRequest request) {
    Concert concert = Concert.create(request.title(), request.venue(), request.concertDate());
    Concert savedConcert = concertRepository.save(concert);

    // 좌석 100개 자동 생성 (A1 ~ A100)
    List<Seat> seats = new ArrayList<>(100);
    for (int i = 1; i <= 100; i++) {
      seats.add(Seat.create(savedConcert, "A" + i));
    }
    seatRepository.saveAll(seats);

    return toResponse(savedConcert);
  }

  private static ConcertResponse toResponse(Concert c) {
    return ConcertResponse.builder()
        .id(c.getId())
        .title(c.getTitle())
        .venue(c.getVenue())
        .concertDate(c.getConcertDate())
        .build();
  }

  @Transactional
  public void deleteConcert(Long concertId) {
    if (!concertRepository.existsById(concertId)) {
      throw new IllegalArgumentException("콘서트를 찾을 수 없습니다.");
    }

    if (reservationRepository.existsByConcertId(concertId)) {
      throw new IllegalStateException("예매 이력이 있어 콘서트를 삭제할 수 없습니다.");
    }

    if (seatRepository.existsByConcertIdAndStatus(concertId, SeatStatus.RESERVED)) {
      throw new IllegalStateException("예약된 좌석이 있어 콘서트를 삭제할 수 없습니다.");
    }

    seatRepository.deleteByConcertId(concertId);
    concertRepository.deleteById(concertId);
  }
}
