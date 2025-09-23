package com.project.ticket.application.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.project.ticket.application.user.dto.UserCreateRequest;
import com.project.ticket.application.user.dto.UserResponse;
import com.project.ticket.domain.user.User;
import com.project.ticket.infra.persistence.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserApplicationServiceTest {

  @Mock private UserRepository userRepository;
  @InjectMocks private UserApplicationService service;

  @Test
  void createUser_success() {
    // given
    UserCreateRequest req = UserCreateRequest.builder()
        .nickName("neo")
        .password("pw")
        .build();
    when(userRepository.existsByNickName("new")).thenReturn(false);
    when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

    // when
    UserResponse res = service.createUser(req);

    // then
    assertThat(res.nickName()).isEqualTo("neo");
  }

  @Test
  void createUser_duplicateNickname_throws() {
    // given
    UserCreateRequest req = UserCreateRequest.builder()
        .nickName("dup")
        .password("pw")
        .build();
    when(userRepository.existsByNickName("dup")).thenReturn(true);

    // expect
    assertThatThrownBy(() -> service.createUser(req))
        .isInstanceOf(com.project.ticket.domain.exception.DuplicateNicknameException.class)
        .hasMessageContaining("이미 사용 중인 닉네임");
  }
}
