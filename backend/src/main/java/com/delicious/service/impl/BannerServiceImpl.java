package com.delicious.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.delicious.entity.Banner;
import com.delicious.mapper.BannerMapper;
import com.delicious.service.BannerService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {

    @Override
    public List<Banner> listAll() {
        LambdaQueryWrapper<Banner> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Banner::getStatus, 1)
                .orderByAsc(Banner::getSort)
                .orderByDesc(Banner::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public Page<Banner> pageList(Integer pageNum, Integer pageSize, String title) {
        Page<Banner> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Banner> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(title)) {
            wrapper.like(Banner::getTitle, title);
        }
        
        wrapper.orderByAsc(Banner::getSort)
                .orderByDesc(Banner::getCreateTime);
        
        return this.page(page, wrapper);
    }
}
