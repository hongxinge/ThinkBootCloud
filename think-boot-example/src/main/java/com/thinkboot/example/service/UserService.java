package com.thinkboot.example.service;

import com.thinkboot.example.model.entity.User;
import com.thinkboot.mybatis.base.BaseService;

public interface UserService extends BaseService<User> {

    User getByUsername(String username);
}
