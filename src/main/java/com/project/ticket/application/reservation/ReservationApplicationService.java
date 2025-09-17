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
import org.springframework.data.domain.PageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationApplicationService {

  private final ReservationQueueRepository reservationQueueRepository;
  private final ReservationRepository reservationRepository;
  private final SeatRepository seatRepository;
  private final UserRepository userRepository;

  @Transactional
  public ReservationEnqueueResponse requestReservation(ReservationEnqueueRequest request) {
    if (reservationQueueRepository.existsByUserIdAndSeatIdAndStatusIn(
        request.userId(), request.seatId(), List.of(QueueStatus.PENDING, QueueStatus.PROCESSING))) {
      throw new IllegalStateException("이미 예약 대기열에 등록된 요청입니다.");
    }

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

    return ReservationStatusResponse.builder()
        .queueId(queue.getId())
        .status(queue.getStatus())
        .reservationId(queue.getReservationId())
        .build();
  }

  @Transactional
  public void processPendingReservations(int size) {
    List<ReservationQueue> tasks = reservationQueueRepository
        .findByStatusOrderByCreatedAtAsc(QueueStatus.PENDING, PageRequest.of(0, size));

    for (ReservationQueue task : tasks) {
      processSingleTask(task);
    }
  }

  private void processSingleTask(ReservationQueue task) {
    try {
      task.markProcessing();

      User user = userRepository.findById(task.getUserId())
          .orElseThrow(() -> new IllegalStateException("사용자 정보가 존재하지 않습니다."));

      Seat seat = seatRepository.findById(task.getSeatId())
          .orElseThrow(() -> new IllegalStateException("좌석 정보가 존재하지 않습니다."));

      seat.reserve();

      Reservation reservation = Reservation.create(user, seat);
      reservationRepository.save(reservation);

      task.markSuccess(reservation.getId());
    } catch (Exception e) {
      task.markFailed();
    }
  }
}
