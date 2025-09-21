package com.project.ticket.infra.persistence.seat;

import com.project.ticket.domain.seat.Seat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByConcertIdOrderBySeatNumberAsc(Long concertId);

    boolean existsByConcertIdAndSeatNumber(Long concertId, String seatNumber);
}
