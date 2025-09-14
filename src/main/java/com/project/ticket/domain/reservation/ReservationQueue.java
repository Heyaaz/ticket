package com.project.ticket.domain.reservation;

import com.project.ticket.domain.status.QueueStatus;
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
@Table
public class ReservationQueue {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Long userId; // 요청한 사용자 Id

  private Long seatId; // 예매하려는 좌석 Id

  @Enumerated(EnumType.STRING)
  private QueueStatus status; // 작업 상태

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
