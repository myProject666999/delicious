package com.delicious.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.delicious.common.Result;
import com.delicious.entity.Banner;
import com.delicious.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/banner")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @GetMapping("/list")
    public Result<List<Banner>> list() {
        List<Banner> list = bannerService.listAll();
        return Result.success(list);
    }

    @GetMapping("/page")
    public Result<Page<Banner>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String title) {
        Page<Banner> page = bannerService.pageList(pageNum, pageSize, title);
        return Result.success(page);
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody Banner banner) {
        bannerService.saveOrUpdate(banner);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        bannerService.removeById(id);
        return Result.success();
    }
}
