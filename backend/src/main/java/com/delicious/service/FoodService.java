package com.delicious.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.delicious.entity.Food;

import java.util.List;

public interface FoodService extends IService<Food> {
    Page<Food> pageList(Integer pageNum, Integer pageSize, String name, Long categoryId, Integer status);
    Page<Food> frontPageList(Integer pageNum, Integer pageSize, String name, Long categoryId, Long userId);
    Food getDetail(Long id, Long userId);
    boolean viewCountIncrement(Long id);
    boolean toggleLike(Long userId, Long foodId);
    List<Food> getRecommend(Integer limit);
}
