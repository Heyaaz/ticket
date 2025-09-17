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

  public ReservationQueue(Long userId, Long seatId) {
    this.userId = userId;
    this.seatId = seatId;
  }

  public static ReservationQueue create(Long userId, Long seatId) {
    ReservationQueue queue = new ReservationQueue(userId, seatId);
    queue.status = QueueStatus.PENDING;
    queue.createdAt = LocalDateTime.now();
    queue.updatedAt = null;
    return queue;
  }

  public void markProcessing() {
    this.status = QueueStatus.PROCESSING;
    this.updatedAt = LocalDateTime.now();
  }

  public void markSuccess() {
    this.status = QueueStatus.SUCCESS;
    this.updatedAt = LocalDateTime.now();
  }

  public void markFailed() {
    this.status = QueueStatus.FAILED;
    this.updatedAt = LocalDateTime.now();
  }
}
