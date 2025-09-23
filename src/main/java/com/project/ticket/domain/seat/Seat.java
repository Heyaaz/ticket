package com.project.ticket.domain.seat;

import com.project.ticket.domain.concert.Concert;
import com.project.ticket.domain.exception.SeatNotAvailableException;
import com.project.ticket.domain.status.SeatStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.project.ticket.domain.exception.InvalidSeatNumberException;
import com.project.ticket.domain.exception.InvalidSeatStatusException;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "seats")
public class Seat {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "seat_id", nullable = false)
  private Long id;

  @Column(name = "seat_number", nullable = false)
  private String seatNumber; // 좌석 번호 ex) A10 ...

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "concert_id")
  private Concert concert;

  @Enumerated(EnumType.STRING)
  private SeatStatus status;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  public void reserve() {
    if (this.status != SeatStatus.AVAILABLE) {
      throw new SeatNotAvailableException();
    }
    this.status = SeatStatus.RESERVED;
    this.updatedAt = LocalDateTime.now();
  }

  public static Seat create(Concert concert, String seatNumber) {
    Seat seat = new Seat();
    seat.concert = concert;
    seat.seatNumber = seatNumber;
    seat.status = SeatStatus.AVAILABLE;
    seat.createdAt = LocalDateTime.now();
    seat.updatedAt = null;
    return seat;
  }

  public void updateSeatNumber(String newSeatNumber) {
    if (newSeatNumber == null || newSeatNumber.isBlank()) {
      throw new InvalidSeatNumberException();
    }
    this.seatNumber = newSeatNumber;
    this.updatedAt = LocalDateTime.now();
  }

  public void updateStatus(SeatStatus newStatus) {
    if (newStatus == null) {
      throw new InvalidSeatStatusException();
    }
    this.status = newStatus;
    this.updatedAt = LocalDateTime.now();
  }
}
