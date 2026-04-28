package com.delicious.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.delicious.entity.Notice;
import com.delicious.mapper.NoticeMapper;
import com.delicious.service.NoticeService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Override
    public Page<Notice> pageList(Integer pageNum, Integer pageSize, String title, Integer status) {
        Page<Notice> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(title)) {
            wrapper.like(Notice::getTitle, title);
        }
        if (status != null) {
            wrapper.eq(Notice::getStatus, status);
        }
        
        wrapper.orderByDesc(Notice::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public Page<Notice> frontPageList(Integer pageNum, Integer pageSize) {
        Page<Notice> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        
        wrapper.eq(Notice::getStatus, 1)
                .orderByDesc(Notice::getCreateTime);
        
        return this.page(page, wrapper);
    }

    @Override
    public Notice getDetail(Long id) {
        return this.getById(id);
    }

    @Override
    public List<Notice> getLatest(Integer limit) {
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notice::getStatus, 1)
                .orderByDesc(Notice::getCreateTime)
                .last("LIMIT " + limit);
        return this.list(wrapper);
    }
}
