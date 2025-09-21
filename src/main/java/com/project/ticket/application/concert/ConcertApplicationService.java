package com.project.ticket.application.concert;

import com.project.ticket.application.concert.dto.ConcertResponse;
import com.project.ticket.domain.concert.Concert;
import com.project.ticket.infra.persistence.concert.ConcertRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConcertApplicationService {

  private final ConcertRepository concertRepository;

  public List<ConcertResponse> getConcerts() {
    List<Concert> concerts = concertRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    return concerts.stream()
        .map(ConcertApplicationService::toResponse)
        .collect(Collectors.toList());
  }

  public ConcertResponse getConcert(Long concertId) {
    Concert concert = concertRepository.findById(concertId)
        .orElseThrow(() -> new IllegalArgumentException("콘서트를 찾을 수 없습니다."));
    return toResponse(concert);
  }

  private static ConcertResponse toResponse(Concert c) {
    return ConcertResponse.builder()
        .id(c.getId())
        .title(c.getTitle())
        .venue(c.getVenue())
        .concertDate(c.getConcertDate())
        .build();
  }
}

