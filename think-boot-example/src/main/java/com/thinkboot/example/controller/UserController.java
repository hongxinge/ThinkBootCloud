package com.thinkboot.example.controller;

import com.thinkboot.auth.annotation.NoLogin;
import com.thinkboot.common.result.R;
import com.thinkboot.example.model.dto.UserDTO;
import com.thinkboot.example.model.entity.User;
import com.thinkboot.example.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User Management", description = "User CRUD operations")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get all users")
    @GetMapping
    public R<List<User>> list() {
        return R.success(userService.list());
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public R<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return R.error(404, "User not found");
        }
        return R.success(user);
    }

    @Operation(summary = "Create user")
    @PostMapping
    public R<Void> create(@RequestBody UserDTO dto) {
        User user = new User();
        BeanUtils.copyProperties(dto, user);
        userService.save(user);
        return R.success();
    }

    @Operation(summary = "Update user")
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @RequestBody UserDTO dto) {
        User user = userService.getById(id);
        if (user == null) {
            return R.error(404, "User not found");
        }
        BeanUtils.copyProperties(dto, user, "password");
        user.setId(id);
        userService.updateById(user);
        return R.success();
    }

    @Operation(summary = "Delete user")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        userService.removeById(id);
        return R.success();
    }
}
