package com.project.ticket.infra.persistence.user;

import com.project.ticket.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByNickName(String nickName);
}
