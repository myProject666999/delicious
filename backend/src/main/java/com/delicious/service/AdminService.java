package com.delicious.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.delicious.entity.Admin;

import java.util.Map;

public interface AdminService extends IService<Admin> {
    Map<String, Object> login(String username, String password);
    Admin getByUsername(String username);
    Page<Admin> pageList(Integer pageNum, Integer pageSize, String username);
}
