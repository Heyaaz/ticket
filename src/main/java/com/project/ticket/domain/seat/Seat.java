package com.project.ticket.domain.seat;

import com.project.ticket.status.SeatStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

  @Enumerated(EnumType.STRING)
  private SeatStatus status;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
