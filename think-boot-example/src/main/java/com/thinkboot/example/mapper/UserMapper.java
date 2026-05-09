package com.thinkboot.example.mapper;

import com.thinkboot.example.model.entity.User;
import com.thinkboot.mybatis.base.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
