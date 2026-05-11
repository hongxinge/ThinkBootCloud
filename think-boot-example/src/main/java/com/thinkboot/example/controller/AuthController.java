package com.thinkboot.example.controller;

import com.thinkboot.auth.context.UserContext;
import com.thinkboot.auth.util.JwtUtils;
import com.thinkboot.common.result.R;
import com.thinkboot.example.model.dto.LoginRequest;
import com.thinkboot.example.model.dto.LoginResponse;
import com.thinkboot.example.model.entity.User;
import com.thinkboot.example.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Authentication", description = "Login, register, token refresh")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final long tokenExpireSeconds;

    public AuthController(UserService userService, JwtUtils jwtUtils, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.tokenExpireSeconds = jwtUtils.getExpiration() / 1000;
    }

    @Operation(summary = "User login")
    @PostMapping("/login")
    public R<LoginResponse> login(@RequestBody LoginRequest request) {
        User user = userService.getByUsername(request.getUsername());
        if (user == null) {
            return R.error(401, "Invalid username or password");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return R.error(401, "Invalid username or password");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        
        String token = jwtUtils.generateToken(user.getId().toString(), claims);
        String refreshToken = jwtUtils.generateRefreshToken(user.getId().toString(), claims);

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setRefreshToken(refreshToken);
        response.setUserId(user.getId().toString());
        response.setExpireTime(tokenExpireSeconds);

        return R.success(response);
    }

    @Operation(summary = "User register")
    @PostMapping("/register")
    public R<Void> register(@RequestBody LoginRequest request) {
        User existingUser = userService.getByUsername(request.getUsername());
        if (existingUser != null) {
            return R.error(400, "Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getUsername());
        user.setStatus(1);
        
        userService.save(user);
        return R.success();
    }

    @Operation(summary = "Refresh token")
    @PostMapping("/refresh")
    public R<LoginResponse> refreshToken(@RequestBody LoginResponse request) {
        String refreshToken = request.getRefreshToken();
        
        if (!jwtUtils.validateToken(refreshToken)) {
            return R.error(401, "Invalid refresh token");
        }

        String userId = jwtUtils.getUserId(refreshToken);
        Map<String, Object> claims = new HashMap<>();
        
        String newToken = jwtUtils.generateToken(userId, claims);
        String newRefreshToken = jwtUtils.generateRefreshToken(userId, claims);

        LoginResponse response = new LoginResponse();
        response.setToken(newToken);
        response.setRefreshToken(newRefreshToken);
        response.setUserId(userId);
        response.setExpireTime(tokenExpireSeconds);

        return R.success(response);
    }

    @Operation(summary = "Get current user info")
    @GetMapping("/info")
    public R<String> getCurrentUser() {
        String userId = UserContext.getCurrentUserId();
        return R.success(userId);
    }
}
