package com.thinkboot.example.model.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String refreshToken;
    private Long expireTime;
    private String userId;
}
