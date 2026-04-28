package com.delicious.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.delicious.entity.RecipeCategory;

import java.util.List;

public interface RecipeCategoryService extends IService<RecipeCategory> {
    List<RecipeCategory> listAll();
    Page<RecipeCategory> pageList(Integer pageNum, Integer pageSize, String name);
}
