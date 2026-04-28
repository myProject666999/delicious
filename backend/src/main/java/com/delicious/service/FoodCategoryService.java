package com.delicious.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.delicious.entity.FoodCategory;

import java.util.List;

public interface FoodCategoryService extends IService<FoodCategory> {
    List<FoodCategory> listAll();
    Page<FoodCategory> pageList(Integer pageNum, Integer pageSize, String name);
}
