package com.delicious.controller;

import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.delicious.common.Result;
import com.delicious.entity.User;
import com.delicious.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        try {
            Map<String, Object> result = userService.login(username, password);
            return Result.success("登录成功", result);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/register")
    public Result<Void> register(@RequestBody User user) {
        try {
            userService.register(user);
            return Result.success("注册成功");
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/info")
    public Result<User> getInfo(@RequestHeader(value = "Authorization", required = false) String token,
                                 @RequestParam(required = false) Long userId) {
        User user = null;
        if (userId != null) {
            user = userService.getById(userId);
        }
        if (user == null) {
            user = userService.getById(1L);
        }
        if (user != null) {
            user.setPassword(null);
        }
        return Result.success(user);
    }

    @GetMapping("/list")
    public Result<Page<User>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer status) {
        Page<User> page = userService.pageList(pageNum, pageSize, username, status);
        return Result.success(page);
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody User user) {
        if (user.getId() == null && StringUtils.hasText(user.getPassword())) {
            user.setPassword(MD5.create().digestHex(user.getPassword()));
        }
        userService.saveOrUpdate(user);
        return Result.success();
    }

    @PostMapping("/update")
    public Result<Void> update(@RequestBody User user) {
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(MD5.create().digestHex(user.getPassword()));
        }
        userService.updateById(user);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.removeById(id);
        return Result.success();
    }

    @PutMapping("/status/{id}")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody User user) {
        User updateUser = new User();
        updateUser.setId(id);
        updateUser.setStatus(user.getStatus());
        userService.updateById(updateUser);
        return Result.success();
    }

    @GetMapping("/detail/{id}")
    public Result<User> detail(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user != null) {
            user.setPassword(null);
        }
        return Result.success(user);
    }
}
