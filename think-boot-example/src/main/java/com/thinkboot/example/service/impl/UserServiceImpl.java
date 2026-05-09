package com.thinkboot.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thinkboot.example.mapper.UserMapper;
import com.thinkboot.example.model.entity.User;
import com.thinkboot.example.service.UserService;
import com.thinkboot.mybatis.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getByUsername(String username) {
        return getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }
}
