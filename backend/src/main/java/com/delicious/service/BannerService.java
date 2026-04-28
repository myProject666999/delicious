package com.delicious.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.delicious.entity.Banner;

import java.util.List;

public interface BannerService extends IService<Banner> {
    List<Banner> listAll();
    Page<Banner> pageList(Integer pageNum, Integer pageSize, String title);
}
