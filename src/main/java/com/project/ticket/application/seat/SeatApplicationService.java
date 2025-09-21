package com.project.ticket.application.seat;

import com.project.ticket.application.seat.dto.SeatCreateRequest;
import com.project.ticket.application.seat.dto.SeatResponse;
import com.project.ticket.application.seat.dto.SeatUpdateRequest;
import com.project.ticket.domain.exception.ConcertNotFoundException;
import com.project.ticket.domain.exception.DuplicateSeatNumberException;
import com.project.ticket.domain.exception.SeatNotFoundException;
import com.project.ticket.domain.exception.SeatReservedDeletionNotAllowedException;
import com.project.ticket.domain.concert.Concert;
import com.project.ticket.domain.seat.Seat;
import com.project.ticket.domain.status.SeatStatus;
import com.project.ticket.infra.persistence.concert.ConcertRepository;
import com.project.ticket.infra.persistence.seat.SeatRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SeatApplicationService {

  private final SeatRepository seatRepository;
  private final ConcertRepository concertRepository;

  @Transactional(readOnly = true)
  public List<SeatResponse> getSeatsByConcert(Long concertId) {
    if (!concertRepository.existsById(concertId)) {
      throw new ConcertNotFoundException();
    }
    return seatRepository.findByConcertIdOrderBySeatNumberAsc(concertId)
        .stream()
        .map(SeatApplicationService::toResponse)
        .collect(Collectors.toList());
  }

  @Transactional
  public SeatResponse createSeat(SeatCreateRequest request) {
    Concert concert = concertRepository.findById(request.concertId())
        .orElseThrow(ConcertNotFoundException::new);

    if (seatRepository.existsByConcertIdAndSeatNumber(request.concertId(), request.seatNumber())) {
      throw new DuplicateSeatNumberException();
    }

    Seat seat = Seat.create(concert, request.seatNumber());
    Seat saved = seatRepository.save(seat);
    return toResponse(saved);
  }

  @Transactional
  public SeatResponse updateSeat(Long seatId, SeatUpdateRequest request) {
    Seat seat = seatRepository.findById(seatId)
        .orElseThrow(SeatNotFoundException::new);

    if (request.seatNumber() != null) {
      // 중복 좌석 번호 방지
      Long concertId = seat.getConcert().getId();
      if (seatRepository.existsByConcertIdAndSeatNumber(concertId, request.seatNumber())
          && !request.seatNumber().equals(seat.getSeatNumber())) {
        throw new DuplicateSeatNumberException();
      }
      seat.updateSeatNumber(request.seatNumber());
    }

    if (request.status() != null) {
      seat.updateStatus(request.status());
    }

    return toResponse(seat);
  }

  @Transactional
  public void deleteSeat(Long seatId) {
    Seat seat = seatRepository.findById(seatId)
        .orElseThrow(SeatNotFoundException::new);
    if (seat.getStatus() == SeatStatus.RESERVED) {
      throw new SeatReservedDeletionNotAllowedException();
    }
    seatRepository.delete(seat);
  }

  @Transactional
  public long deleteSeatsByConcert(Long concertId) {
    if (!concertRepository.existsById(concertId)) {
      throw new IllegalArgumentException("콘서트를 찾을 수 없습니다.");
    }
    if (seatRepository.existsByConcertIdAndStatus(concertId, SeatStatus.RESERVED)) {
      throw new IllegalStateException("예약된 좌석이 있어 일괄 삭제할 수 없습니다.");
    }
    return seatRepository.deleteByConcertId(concertId);
  }

  private static SeatResponse toResponse(Seat s) {
    return SeatResponse.builder()
        .id(s.getId())
        .seatNumber(s.getSeatNumber())
        .status(s.getStatus())
        .build();
  }
}
