package com.project.ticket.api;

import com.project.ticket.application.user.UserApplicationService;
import com.project.ticket.application.user.dto.UserCreateRequest;
import com.project.ticket.application.user.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserApplicationService userService;

  @PostMapping
  public ResponseEntity<UserResponse> create(
      @Valid @RequestBody UserCreateRequest request
  ) {
    UserResponse response = userService.createUser(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> get(@PathVariable Long id) {
    return ResponseEntity.ok(userService.getUser(id));
  }
}

