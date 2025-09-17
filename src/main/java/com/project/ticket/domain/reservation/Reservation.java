package com.project.ticket.domain.reservation;

import com.project.ticket.domain.concert.Concert;
import com.project.ticket.domain.seat.Seat;
import com.project.ticket.domain.user.User;
import com.project.ticket.domain.status.ReservationStatus;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reservations")
public class Reservation {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "reservation_id", nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "concert_id")
  private Concert concert;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seat_id")
  private Seat seat;

  @Enumerated(EnumType.STRING)
  private ReservationStatus status;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime creatAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  public static Reservation create(User user, Seat seat) {
    Reservation reservation = new Reservation();
    reservation.user = user;
    reservation.seat = seat;
    reservation.status = ReservationStatus.CONFIRMED;
    reservation.creatAt = LocalDateTime.now();
    reservation.updatedAt = null;
    return reservation;
  }
}
