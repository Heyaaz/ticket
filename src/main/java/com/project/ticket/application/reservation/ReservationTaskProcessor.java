package com.project.ticket.application.reservation;

import com.project.ticket.domain.exception.SeatNotFoundException;
import com.project.ticket.domain.exception.UserNotFoundException;
import com.project.ticket.domain.reservation.Reservation;
import com.project.ticket.domain.reservation.ReservationQueue;
import com.project.ticket.domain.seat.Seat;
import com.project.ticket.domain.user.User;
import com.project.ticket.infra.persistence.reservation.ReservationRepository;
import com.project.ticket.infra.persistence.reservationqueue.ReservationQueueRepository;
import com.project.ticket.infra.persistence.seat.SeatRepository;
import com.project.ticket.infra.persistence.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationTaskProcessor {

  private final ReservationQueueRepository reservationQueueRepository;
  private final ReservationRepository reservationRepository;
  private final SeatRepository seatRepository;
  private final UserRepository userRepository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void processSingleTask(Long taskId) {
    ReservationQueue task = reservationQueueRepository.findById(taskId)
        .orElse(null);
    if (task == null) return;
    try {
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
}

