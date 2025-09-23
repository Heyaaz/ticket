package com.project.ticket.application.reservation;

import com.project.ticket.application.reservation.dto.ReservationEnqueueRequest;
import com.project.ticket.application.reservation.dto.ReservationEnqueueResponse;
import com.project.ticket.application.reservation.dto.ReservationStatusResponse;
import com.project.ticket.application.reservation.dto.ReservationCancelRequest;
import com.project.ticket.application.reservation.dto.ReservationCancelResponse;
import com.project.ticket.domain.exception.AuthorizationException;
import com.project.ticket.domain.exception.DuplicateQueueRequestException;
import com.project.ticket.domain.exception.ReservationNotFoundException;
import com.project.ticket.domain.exception.ReservationQueueNotFoundException;
import com.project.ticket.domain.exception.SeatNotFoundException;
import com.project.ticket.domain.exception.UserNotFoundException;
import com.project.ticket.domain.reservation.Reservation;
import com.project.ticket.domain.reservation.ReservationQueue;
import com.project.ticket.domain.seat.Seat;
import com.project.ticket.domain.status.SeatStatus;
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
      throw new DuplicateQueueRequestException();
    }

    userRepository.findById(request.userId())
        .orElseThrow(UserNotFoundException::new);

    seatRepository.findById(request.seatId())
        .orElseThrow(SeatNotFoundException::new);

    ReservationQueue queue = ReservationQueue.create(request.userId(), request.seatId());

    ReservationQueue saved = reservationQueueRepository.save(queue);
    return ReservationEnqueueResponse.builder()
        .queueId(saved.getId())
        .build();
  }

  @Transactional(readOnly = true)
  public ReservationStatusResponse getStatus(Long queueId) {
    ReservationQueue queue = reservationQueueRepository.findById(queueId)
        .orElseThrow(ReservationQueueNotFoundException::new);

    return ReservationStatusResponse.builder()
        .queueId(queue.getId())
        .status(queue.getStatus())
        .reservationId(queue.getReservationId())
        .build();
  }

  @Transactional
  public void processPendingReservations(int size) {
    List<ReservationQueue> tasks = reservationQueueRepository
        .findAndLockPendingForUpdateSkipLocked(QueueStatus.PENDING.name(), size);

    for (ReservationQueue task : tasks) {
      processSingleTask(task);
    }
  }

  private void processSingleTask(ReservationQueue task) {
    try {
      task.markProcessing();

      User user = userRepository.findById(task.getUserId())
          .orElseThrow(UserNotFoundException::new);

      Seat seat = seatRepository.findById(task.getSeatId())
          .orElseThrow(SeatNotFoundException::new);

      seat.reserve();

      Reservation reservation = Reservation.create(user, seat);
      reservationRepository.save(reservation);

      task.markSuccess(reservation.getId());
    } catch (Exception e) {
      task.markFailed();
    }
  }

  @Transactional
  public ReservationCancelResponse cancelReservation(ReservationCancelRequest request) {
    Reservation reservation = reservationRepository.findById(request.reservationId())
        .orElseThrow(ReservationNotFoundException::new);

    if (!reservation.getUser().getId().equals(request.userId())) {
      throw new AuthorizationException();
    }

    // 좌석 되돌리고 예약 상태 취소
    Seat seat = reservation.getSeat();
    reservation.cancel();
    seat.updateStatus(SeatStatus.AVAILABLE);

    return ReservationCancelResponse.builder()
        .reservationId(reservation.getId())
        .status(reservation.getStatus())
        .build();
  }
}
