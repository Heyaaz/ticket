package com.project.ticket.application.concert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.ticket.application.concert.dto.ConcertCreateRequest;
import com.project.ticket.application.concert.dto.ConcertResponse;
import com.project.ticket.domain.concert.Concert;
import com.project.ticket.domain.seat.Seat;
import com.project.ticket.infra.persistence.concert.ConcertRepository;
import com.project.ticket.infra.persistence.reservation.ReservationRepository;
import com.project.ticket.infra.persistence.seat.SeatRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConcertApplicationServiceTest {

  @Mock private ConcertRepository concertRepository;
  @Mock private SeatRepository seatRepository;
  @Mock private ReservationRepository reservationRepository;

  @InjectMocks private ConcertApplicationService service;

  @Test
  void createConcert_createsConcert_andGenerates100Seats() {
    // given
    ConcertCreateRequest request = ConcertCreateRequest.builder()
        .title("Test Concert")
        .venue("Test Venue")
        .concertDate(LocalDateTime.now().plusDays(1))
        .build();

    when(concertRepository.save(any(Concert.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // when
    ConcertResponse response = service.createConcert(request);

    // then
    // seatRepository.saveAll이 100개의 좌석(A1~A100)으로 호출되었는지 검증
    verify(seatRepository).saveAll(argThat(iterable -> {
      if (iterable == null) return false;
      int count = 0;
      String firstNum = null;
      String lastNum = null;
      for (Seat s : iterable) {
        count++;
        if (firstNum == null) firstNum = s.getSeatNumber();
        lastNum = s.getSeatNumber();
      }
      return count == 100 && "A1".equals(firstNum) && "A100".equals(lastNum);
    }));

    // 응답 필드 기본 검증
    assertThat(response.title()).isEqualTo("Test Concert");
    assertThat(response.venue()).isEqualTo("Test Venue");
  }
}
