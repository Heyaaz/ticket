package com.project.ticket.application.seat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.ticket.application.seat.dto.SeatCreateRequest;
import com.project.ticket.application.seat.dto.SeatResponse;
import com.project.ticket.domain.concert.Concert;
import com.project.ticket.domain.seat.Seat;
import com.project.ticket.domain.status.SeatStatus;
import com.project.ticket.infra.persistence.concert.ConcertRepository;
import com.project.ticket.infra.persistence.seat.SeatRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SeatApplicationServiceTest {

  @Mock private SeatRepository seatRepository;
  @Mock private ConcertRepository concertRepository;
  @InjectMocks private SeatApplicationService service;

  @Test
  void deleteSeat_reserved_throws() {
    // given
    Concert concert = Concert.create("t", "v", java.time.LocalDateTime.now().plusDays(1));
    Seat seat = Seat.create(concert, "A1");
    seat.updateStatus(SeatStatus.RESERVED);
    when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));

    // expect
    assertThatThrownBy(() -> service.deleteSeat(1L))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("삭제할 수 없습니다");
  }

  @Test
  void getSeatsByConcert_returnsMappedList() {
    // given
    Concert concert = Concert.create("t", "v", java.time.LocalDateTime.now().plusDays(1));
    when(concertRepository.existsById(10L)).thenReturn(true);
    when(seatRepository.findByConcertIdOrderBySeatNumberAsc(10L))
        .thenReturn(List.of(Seat.create(concert, "A1"), Seat.create(concert, "A2")));

    // when
    List<SeatResponse> res = service.getSeatsByConcert(10L);

    // then
    assertThat(res).hasSize(2);
    assertThat(res.get(0).seatNumber()).isEqualTo("A1");
  }

  @Test
  void createSeat_duplicate_throws() {
    // given
    when(concertRepository.findById(9L)).thenReturn(Optional.of(Concert.create("t", "v", java.time.LocalDateTime.now().plusDays(1))));
    when(seatRepository.existsByConcertIdAndSeatNumber(eq(9L), eq("A1"))).thenReturn(true);

    // expect
    assertThatThrownBy(() -> service.createSeat(SeatCreateRequest.builder().concertId(9L).seatNumber("A1").build()))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("이미 존재하는 좌석 번호");
  }
}

