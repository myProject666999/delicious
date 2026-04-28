package com.delicious.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.delicious.common.Result;
import com.delicious.entity.Notice;
import com.delicious.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/list")
    public Result<Page<Notice>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer status) {
        Page<Notice> page = noticeService.pageList(pageNum, pageSize, title, status);
        return Result.success(page);
    }

    @GetMapping("/front/list")
    public Result<Page<Notice>> frontList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Notice> page = noticeService.frontPageList(pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/detail/{id}")
    public Result<Notice> detail(@PathVariable Long id) {
        Notice notice = noticeService.getDetail(id);
        return Result.success(notice);
    }

    @GetMapping("/latest")
    public Result<List<Notice>> latest(@RequestParam(defaultValue = "5") Integer limit) {
        List<Notice> list = noticeService.getLatest(limit);
        return Result.success(list);
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody Notice notice) {
        noticeService.saveOrUpdate(notice);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        noticeService.removeById(id);
        return Result.success();
    }
}
