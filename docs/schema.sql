-- Users
CREATE TABLE IF NOT EXISTS users (
  user_id       BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_name     VARCHAR(255) NOT NULL,
  user_password VARCHAR(255) NOT NULL,
  created_at    DATETIME(6)  NOT NULL,
  CONSTRAINT uk_users_name UNIQUE (user_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Concerts
CREATE TABLE IF NOT EXISTS concerts (
  concert_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
  concert_title VARCHAR(255) NOT NULL,
  concert_venue VARCHAR(255) NOT NULL,
  concert_date  DATETIME(6)  NOT NULL,
  created_at    DATETIME(6)  NOT NULL,
  updated_at    DATETIME(6)  NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Seats
CREATE TABLE IF NOT EXISTS seats (
  seat_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
  seat_number VARCHAR(255) NOT NULL,
  status      ENUM('AVAILABLE','RESERVED') NOT NULL,
  created_at  DATETIME(6)  NOT NULL,
  updated_at  DATETIME(6)  NULL,
  concert_id  BIGINT       NULL,
  CONSTRAINT fk_seat_concert FOREIGN KEY (concert_id)
    REFERENCES concerts(concert_id)
    ON DELETE CASCADE,
  CONSTRAINT uk_seat_concert_number UNIQUE (concert_id, seat_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Reservations
CREATE TABLE IF NOT EXISTS reservations (
  reservation_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id        BIGINT       NULL,
  concert_id     BIGINT       NULL,
  seat_id        BIGINT       NULL,
  status         ENUM('PENDING','CONFIRMED','CANCELLED'),
  created_at     DATETIME(6)  NOT NULL,
  updated_at     DATETIME(6)  NULL,
  CONSTRAINT fk_reservation_user FOREIGN KEY (user_id) REFERENCES users(user_id),
  CONSTRAINT fk_reservation_concert FOREIGN KEY (concert_id) REFERENCES concerts(concert_id),
  CONSTRAINT fk_reservation_seat FOREIGN KEY (seat_id) REFERENCES seats(seat_id),
  CONSTRAINT uk_reservation_seat UNIQUE (seat_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Reservation Queue
CREATE TABLE IF NOT EXISTS reservation_queue (
  id             BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id        BIGINT       NULL,
  seat_id        BIGINT       NULL,
  status         ENUM('PENDING','PROCESSING','SUCCESS','FAILED') NOT NULL,
  reservation_id BIGINT       NULL,
  created_at     DATETIME(6)  NULL,
  updated_at     DATETIME(6)  NULL,
  CONSTRAINT fk_queue_user FOREIGN KEY (user_id) REFERENCES users(user_id),
  CONSTRAINT fk_queue_seat FOREIGN KEY (seat_id) REFERENCES seats(seat_id),
  INDEX idx_queue_status_created_at (status, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;