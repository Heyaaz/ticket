package com.project.ticket.application.user;

import com.project.ticket.application.user.dto.UserCreateRequest;
import com.project.ticket.application.user.dto.UserResponse;
import com.project.ticket.domain.user.User;
import com.project.ticket.infra.persistence.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserApplicationService {

  private final UserRepository userRepository;

  @Transactional
  public UserResponse createUser(UserCreateRequest request) {
    if (userRepository.existsByNickName(request.nickName())) {
      throw new IllegalStateException("이미 사용 중인 닉네임입니다.");
    }
    User user = User.create(request.nickName(), request.password());
    User saved = userRepository.save(user);
    return toResponse(saved);
  }

  @Transactional(readOnly = true)
  public UserResponse getUser(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    return toResponse(user);
  }

  private static UserResponse toResponse(User u) {
    return UserResponse.builder()
        .id(u.getId())
        .nickName(u.getNickName())
        .createdAt(u.getCreatedAt())
        .build();
  }
}
