package com.project.ticket.infra.persistence.reservationqueue;

import com.project.ticket.domain.reservation.ReservationQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationQueueRepository extends JpaRepository<ReservationQueue, Long> {

}

