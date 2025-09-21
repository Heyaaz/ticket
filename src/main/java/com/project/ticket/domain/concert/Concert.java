package com.project.ticket.domain.concert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "concerts")
public class Concert {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "concert_id", nullable = false)
  private Long id;

  @Column(name = "concert_title", nullable = false)
  private String title;

  @Column(name = "concert_venue", nullable = false)
  private String venue; // 콘서트 장소

  @Column(name = "concert_date", nullable = false)
  private LocalDateTime concertDate; // 콘서트 시간

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  public static Concert create(String title, String venue, LocalDateTime concertDate) {
    Concert c = new Concert();
    c.title = title;
    c.venue = venue;
    c.concertDate = concertDate;
    c.createdAt = LocalDateTime.now();
    c.updatedAt = null;
    return c;
  }
}
