DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS user_connection;
DROP TABLE IF EXISTS users;

--
-- Table structure for table `users`
--
CREATE TABLE `users` (
  `users_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(100) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`users_id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `uk_users_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `user_connection`
--
CREATE TABLE `user_connection` (
  `user_id` int NOT NULL,
  `connections_id` int NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`,`connections_id`),
  KEY `fk_uc_connection` (`connections_id`),
  CONSTRAINT `fk_uc_connection` FOREIGN KEY (`connections_id`) REFERENCES `users` (`users_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_uc_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`users_id`) ON DELETE CASCADE,
  CONSTRAINT `ck_uc_diff` CHECK ((`user_id` <> `connections_id`))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions` (
  `trans_id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(200) DEFAULT NULL,
  `amount` decimal(19,2) NOT NULL,
  `sender_id` int NOT NULL,
  `receiver_id` int NOT NULL,
  `fee` decimal(19,2) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`trans_id`),
  KEY `idx_tx_sender` (`sender_id`),
  KEY `idx_tx_receiver` (`receiver_id`),
  CONSTRAINT `fk_tx_receiver` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`users_id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_tx_sender` FOREIGN KEY (`sender_id`) REFERENCES `users` (`users_id`) ON DELETE RESTRICT,
  CONSTRAINT `ck_amount_positive` CHECK ((`amount` > 0)),
  CONSTRAINT `ck_fee_nonnegative` CHECK ((`fee` >= 0)),
  CONSTRAINT `ck_no_self_transfer` CHECK ((`sender_id` <> `receiver_id`))
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


