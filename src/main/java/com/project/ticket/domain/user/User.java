package com.project.ticket.domain.user;

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
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "user_id", nullable = false)
  private Long id;

  @Column(name = "user_name", nullable = false, unique = true)
  private String nickName;

  @Column(name = "user_password", nullable = false)
  private String password;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  public static User create(String nickName, String password) {
    if (nickName == null || nickName.isBlank()) {
      throw new IllegalArgumentException("닉네임은 필수입니다.");
    }
    if (password == null || password.isBlank()) {
      throw new IllegalArgumentException("패스워드는 필수입니다.");
    }
    User u = new User();
    u.nickName = nickName;
    u.password = password;
    u.createdAt = LocalDateTime.now();
    return u;
  }
}
