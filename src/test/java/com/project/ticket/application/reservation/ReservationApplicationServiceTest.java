package com.project.ticket.application.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.project.ticket.application.reservation.dto.ReservationCancelRequest;
import com.project.ticket.application.reservation.dto.ReservationCancelResponse;
import com.project.ticket.application.reservation.dto.ReservationEnqueueRequest;
import com.project.ticket.domain.concert.Concert;
import com.project.ticket.domain.reservation.Reservation;
import com.project.ticket.domain.reservation.ReservationQueue;
import com.project.ticket.domain.seat.Seat;
import com.project.ticket.domain.status.QueueStatus;
import com.project.ticket.domain.status.ReservationStatus;
import com.project.ticket.domain.status.SeatStatus;
import com.project.ticket.domain.user.User;
import com.project.ticket.infra.persistence.reservation.ReservationRepository;
import com.project.ticket.infra.persistence.reservationqueue.ReservationQueueRepository;
import com.project.ticket.infra.persistence.seat.SeatRepository;
import com.project.ticket.infra.persistence.user.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class ReservationApplicationServiceTest {

  @Mock private ReservationQueueRepository reservationQueueRepository;
  @Mock private ReservationRepository reservationRepository;
  @Mock private SeatRepository seatRepository;
  @Mock private UserRepository userRepository;

  @InjectMocks private ReservationApplicationService service;

  @Test
  void cancelReservation_changesStatus_andSeatAvailable() {
    // given
    User user = User.create("new", "pw");
    Concert concert = Concert.create("t", "v", java.time.LocalDateTime.now().plusDays(1));
    Seat seat = Seat.create(concert, "A1");
    seat.updateStatus(SeatStatus.RESERVED);
    Reservation reservation = Reservation.create(user, seat);
    when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

    // when
    ReservationCancelResponse res = service.cancelReservation(ReservationCancelRequest.builder()
        .userId(user.getId())
        .reservationId(1L)
        .build());

    // then
    assertThat(res.status()).isEqualTo(ReservationStatus.CANCELLED);
    assertThat(seat.getStatus()).isEqualTo(SeatStatus.AVAILABLE);
  }

  @Test
  void requestReservation_duplicate_throws() {
    // given
    when(reservationQueueRepository.existsByUserIdAndSeatIdAndStatusIn(1L, 1L, List.of(QueueStatus.PENDING, QueueStatus.PROCESSING)))
        .thenReturn(true);

    // expect
    assertThatThrownBy(() -> service.requestReservation(ReservationEnqueueRequest.builder().userId(1L).seatId(1L).build()))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("이미 예약 대기열에 등록");
  }

  @Test
  void processPendingReservations_reservesSeat_andCreatesReservation() {
    // given
    ReservationQueue task = ReservationQueue.create(1L, 1L);
    when(reservationQueueRepository.findByStatusOrderByCreatedAtAsc(QueueStatus.PENDING, PageRequest.of(0, 1)))
        .thenReturn(List.of(task));

    User user = User.create("new", "pw");
    Concert concert = Concert.create("t", "v", java.time.LocalDateTime.now().plusDays(1));
    Seat seat = Seat.create(concert, "A1");
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));
    when(reservationRepository.save(any(Reservation.class))).thenAnswer(inv -> inv.getArgument(0));

    // when
    service.processPendingReservations(1);

    // then
    assertThat(seat.getStatus()).isEqualTo(SeatStatus.RESERVED);
    assertThat(task.getStatus()).isEqualTo(QueueStatus.SUCCESS);
  }
}

