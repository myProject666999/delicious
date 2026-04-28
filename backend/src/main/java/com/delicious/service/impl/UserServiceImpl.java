package com.delicious.service.impl;

import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.delicious.common.JwtUtils;
import com.delicious.entity.User;
import com.delicious.mapper.UserMapper;
import com.delicious.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Map<String, Object> login(String username, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = this.getOne(wrapper);
        
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        
        if (user.getStatus() == 0) {
            throw new IllegalArgumentException("账号已被禁用");
        }
        
        String md5Password = MD5.create().digestHex(password);
        if (!md5Password.equals(user.getPassword())) {
            throw new IllegalArgumentException("密码错误");
        }
        
        String token = jwtUtils.generateToken(user.getId(), user.getUsername(), "user");
        
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userInfo", user);
        
        return result;
    }

    @Override
    public User getByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return this.getOne(wrapper);
    }

    @Override
    public boolean register(User user) {
        User existUser = this.getByUsername(user.getUsername());
        if (existUser != null) {
            throw new IllegalArgumentException("用户名已存在");
        }
        
        user.setPassword(MD5.create().digestHex(user.getPassword()));
        user.setStatus(1);
        
        return this.save(user);
    }

    @Override
    public Page<User> pageList(Integer pageNum, Integer pageSize, String username, Integer status) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(username)) {
            wrapper.like(User::getUsername, username);
        }
        if (status != null) {
            wrapper.eq(User::getStatus, status);
        }
        
        wrapper.orderByDesc(User::getCreateTime);
        return this.page(page, wrapper);
    }
}
