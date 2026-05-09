CREATE DATABASE IF NOT EXISTS `think_boot_example` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `think_boot_example`;

CREATE TABLE `sys_user`
(
    `id`          BIGINT       NOT NULL COMMENT 'Primary key ID',
    `username`    VARCHAR(50)  NOT NULL COMMENT 'Username',
    `password`    VARCHAR(100) NOT NULL COMMENT 'Password',
    `nickname`    VARCHAR(50)  NULL     DEFAULT NULL COMMENT 'Nickname',
    `email`       VARCHAR(100) NULL     DEFAULT NULL COMMENT 'Email',
    `phone`       VARCHAR(20)  NULL     DEFAULT NULL COMMENT 'Phone number',
    `avatar`      VARCHAR(255) NULL     DEFAULT NULL COMMENT 'Avatar URL',
    `status`      INT          NULL     DEFAULT 0 COMMENT 'Status: 0=disabled, 1=enabled',
    `create_by`   VARCHAR(50)  NULL     DEFAULT 'system' COMMENT 'Created by',
    `create_time` DATETIME     NULL     DEFAULT CURRENT_TIMESTAMP COMMENT 'Created time',
    `update_by`   VARCHAR(50)  NULL     DEFAULT 'system' COMMENT 'Updated by',
    `update_time` DATETIME     NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated time',
    `deleted`     INT          NULL     DEFAULT 0 COMMENT 'Logical delete: 0=active, 1=deleted',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_username` (`username`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = 'User table';

INSERT INTO `sys_user` (`id`, `username`, `password`, `nickname`, `email`, `phone`, `status`, `create_by`, `update_by`, `deleted`)
VALUES (1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Administrator', 'admin@thinkboot.com', '13800138000', 1, 'system', 'system', 0),
       (2, 'user', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Test User', 'user@thinkboot.com', '13800138001', 1, 'system', 'system', 0);
