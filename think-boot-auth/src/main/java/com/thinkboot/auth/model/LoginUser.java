package com.thinkboot.auth.model;

import lombok.Data;

@Data
public class LoginUser {

    private String userId;

    private String token;

    private Long expireTime;
}
