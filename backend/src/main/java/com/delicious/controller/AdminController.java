package com.delicious.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.delicious.common.Result;
import com.delicious.entity.Admin;
import com.delicious.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        try {
            Map<String, Object> result = adminService.login(username, password);
            return Result.success("登录成功", result);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/info")
    public Result<Admin> getInfo(@RequestHeader("Authorization") String token) {
        Admin admin = adminService.getById(1L);
        if (admin != null) {
            admin.setPassword(null);
        }
        return Result.success(admin);
    }

    @GetMapping("/list")
    public Result<Page<Admin>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String username) {
        Page<Admin> page = adminService.pageList(pageNum, pageSize, username);
        return Result.success(page);
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody Admin admin) {
        adminService.saveOrUpdate(admin);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        adminService.removeById(id);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    public Result<Admin> detail(@PathVariable Long id) {
        Admin admin = adminService.getById(id);
        if (admin != null) {
            admin.setPassword(null);
        }
        return Result.success(admin);
    }
}
