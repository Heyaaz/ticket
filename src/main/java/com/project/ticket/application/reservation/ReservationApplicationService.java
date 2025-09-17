package com.project.ticket.application.reservation;

import com.project.ticket.application.reservation.dto.ReservationEnqueueRequest;
import com.project.ticket.application.reservation.dto.ReservationEnqueueResponse;
import com.project.ticket.application.reservation.dto.ReservationStatusResponse;
import com.project.ticket.domain.reservation.Reservation;
import com.project.ticket.domain.reservation.ReservationQueue;
import com.project.ticket.domain.seat.Seat;
import com.project.ticket.domain.status.QueueStatus;
import com.project.ticket.domain.user.User;
import com.project.ticket.infra.persistence.reservation.ReservationRepository;
import com.project.ticket.infra.persistence.reservationqueue.ReservationQueueRepository;
import com.project.ticket.infra.persistence.seat.SeatRepository;
import com.project.ticket.infra.persistence.user.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationApplicationService {

  private final ReservationQueueRepository reservationQueueRepository;
  private final ReservationRepository reservationRepository;
  private final SeatRepository seatRepository;
  private final UserRepository userRepository;

  @Transactional
  public ReservationEnqueueResponse requestReservation(ReservationEnqueueRequest request) {
    userRepository.findById(request.userId())
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

    seatRepository.findById(request.seatId())
        .orElseThrow(() -> new IllegalArgumentException("좌석을 찾을 수 없습니다."));

    ReservationQueue queue = ReservationQueue.create(request.userId(), request.seatId());

    ReservationQueue saved = reservationQueueRepository.save(queue);
    return ReservationEnqueueResponse.builder()
        .queueId(saved.getId())
        .build();
  }

  @Transactional(readOnly = true)
  public ReservationStatusResponse getStatus(Long queueId) {
    ReservationQueue queue = reservationQueueRepository.findById(queueId)
        .orElseThrow(() -> new IllegalArgumentException("대기열을 찾을 수 없습니다."));

    Long reservationId = null;
    if (queue.getStatus() == QueueStatus.SUCCESS) {
      Optional<Reservation> reservation = reservationRepository.findAll().stream()
          .filter(r -> r.getSeat() != null && r.getSeat().getId().equals(queue.getSeatId()))
          .findFirst();
      reservationId = reservation.map(Reservation::getId).orElse(null);
    }

    return ReservationStatusResponse.builder()
        .queueId(queue.getId())
        .status(queue.getStatus())
        .reservationId(reservationId)
        .build();
  }

  private void processSingleTask(ReservationQueue task) {
    try {
      task.markProcessing();

      Seat seat = seatRepository.findById(task.getSeatId())
          .orElseThrow(() -> new IllegalStateException("좌석 정보가 존재하지 않습니다."));

      seat.reserve();

      User user = userRepository.findById(task.getUserId())
          .orElseThrow(() -> new IllegalStateException("사용자 정보가 존재하지 않습니다."));

      Reservation reservation = Reservation.create(user, seat);
      reservationRepository.save(reservation);

      task.markSuccess();
    } catch (Exception e) {
      task.markFailed();
    }
  }
}
