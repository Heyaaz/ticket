package com.project.ticket.infra.persistence.reservationqueue;

import com.project.ticket.domain.reservation.ReservationQueue;
import com.project.ticket.domain.status.QueueStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationQueueRepository extends JpaRepository<ReservationQueue, Long> {

  List<ReservationQueue> findByStatusOrderByCreatedAtAsc(QueueStatus queueStatus, PageRequest of);

  boolean existsByUserIdAndSeatIdAndStatusIn(Long userId, Long seatId, List<QueueStatus> statuses);

  @Query(value = "select * from reservation_queue where status = :status order by created_at asc limit :size for update skip locked", nativeQuery = true)
  List<ReservationQueue> findAndLockPendingForUpdateSkipLocked(@Param("status") String status, @Param("size") int size);
}
