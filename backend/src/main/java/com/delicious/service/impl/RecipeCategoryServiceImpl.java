package com.delicious.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.delicious.entity.RecipeCategory;
import com.delicious.mapper.RecipeCategoryMapper;
import com.delicious.service.RecipeCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class RecipeCategoryServiceImpl extends ServiceImpl<RecipeCategoryMapper, RecipeCategory> implements RecipeCategoryService {

    @Override
    public List<RecipeCategory> listAll() {
        LambdaQueryWrapper<RecipeCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RecipeCategory::getStatus, 1)
                .orderByAsc(RecipeCategory::getSort)
                .orderByDesc(RecipeCategory::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public Page<RecipeCategory> pageList(Integer pageNum, Integer pageSize, String name) {
        Page<RecipeCategory> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<RecipeCategory> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(name)) {
            wrapper.like(RecipeCategory::getName, name);
        }
        
        wrapper.orderByAsc(RecipeCategory::getSort)
                .orderByDesc(RecipeCategory::getCreateTime);
        
        return this.page(page, wrapper);
    }
}
