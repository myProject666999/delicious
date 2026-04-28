package com.delicious.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.delicious.entity.Recipe;

import java.util.List;

public interface RecipeService extends IService<Recipe> {
    Page<Recipe> pageList(Integer pageNum, Integer pageSize, String title, Long categoryId, Integer status);
    Page<Recipe> frontPageList(Integer pageNum, Integer pageSize, String title, Long categoryId, Long userId);
    Recipe getDetail(Long id, Long userId);
    boolean viewCountIncrement(Long id);
    boolean toggleLike(Long userId, Long recipeId);
    List<Recipe> getRecommend(Integer limit);
}
