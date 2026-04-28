package com.delicious.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.delicious.entity.FoodCategory;
import com.delicious.mapper.FoodCategoryMapper;
import com.delicious.service.FoodCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class FoodCategoryServiceImpl extends ServiceImpl<FoodCategoryMapper, FoodCategory> implements FoodCategoryService {

    @Override
    public List<FoodCategory> listAll() {
        LambdaQueryWrapper<FoodCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FoodCategory::getStatus, 1)
                .orderByAsc(FoodCategory::getSort)
                .orderByDesc(FoodCategory::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public Page<FoodCategory> pageList(Integer pageNum, Integer pageSize, String name) {
        Page<FoodCategory> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<FoodCategory> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(name)) {
            wrapper.like(FoodCategory::getName, name);
        }
        
        wrapper.orderByAsc(FoodCategory::getSort)
                .orderByDesc(FoodCategory::getCreateTime);
        
        return this.page(page, wrapper);
    }
}
