package com.delicious.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.delicious.entity.Notice;

import java.util.List;

public interface NoticeService extends IService<Notice> {
    Page<Notice> pageList(Integer pageNum, Integer pageSize, String title, Integer status);
    Page<Notice> frontPageList(Integer pageNum, Integer pageSize);
    Notice getDetail(Long id);
    List<Notice> getLatest(Integer limit);
}
