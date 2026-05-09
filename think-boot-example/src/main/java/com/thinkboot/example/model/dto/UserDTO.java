package com.thinkboot.example.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {

    private Long id;

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 2, max = 20, message = "Username length must be between 2 and 20 characters")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, max = 20, message = "Password length must be between 6 and 20 characters")
    private String password;

    private String nickname;

    @Email(message = "Invalid email format")
    private String email;

    private String phone;

    private String avatar;
}
