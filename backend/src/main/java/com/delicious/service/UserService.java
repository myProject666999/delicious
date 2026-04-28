package com.delicious.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.delicious.entity.User;

import java.util.Map;

public interface UserService extends IService<User> {
    Map<String, Object> login(String username, String password);
    User getByUsername(String username);
    boolean register(User user);
    Page<User> pageList(Integer pageNum, Integer pageSize, String username, Integer status);
}
