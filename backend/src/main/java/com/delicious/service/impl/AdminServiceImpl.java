package com.delicious.service.impl;

import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.delicious.common.JwtUtils;
import com.delicious.entity.Admin;
import com.delicious.mapper.AdminMapper;
import com.delicious.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Map<String, Object> login(String username, String password) {
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getUsername, username);
        Admin admin = this.getOne(wrapper);
        
        if (admin == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        
        String md5Password = MD5.create().digestHex(password);
        if (!md5Password.equals(admin.getPassword())) {
            throw new IllegalArgumentException("密码错误");
        }
        
        String token = jwtUtils.generateToken(admin.getId(), admin.getUsername(), "admin");
        
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userInfo", admin);
        
        return result;
    }

    @Override
    public Admin getByUsername(String username) {
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getUsername, username);
        return this.getOne(wrapper);
    }

    @Override
    public Page<Admin> pageList(Integer pageNum, Integer pageSize, String username) {
        Page<Admin> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(username)) {
            wrapper.like(Admin::getUsername, username);
        }
        
        wrapper.orderByDesc(Admin::getCreateTime);
        return this.page(page, wrapper);
    }
}
