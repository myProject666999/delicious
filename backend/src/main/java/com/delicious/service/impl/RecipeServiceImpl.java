package com.delicious.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.delicious.entity.Recipe;
import com.delicious.entity.RecipeCategory;
import com.delicious.entity.User;
import com.delicious.entity.UserLike;
import com.delicious.mapper.RecipeMapper;
import com.delicious.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class RecipeServiceImpl extends ServiceImpl<RecipeMapper, Recipe> implements RecipeService {

    @Autowired
    private RecipeCategoryService recipeCategoryService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserLikeService userLikeService;

    @Override
    public Page<Recipe> pageList(Integer pageNum, Integer pageSize, String title, Long categoryId, Integer status) {
        Page<Recipe> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Recipe> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(title)) {
            wrapper.like(Recipe::getTitle, title);
        }
        if (categoryId != null) {
            wrapper.eq(Recipe::getCategoryId, categoryId);
        }
        if (status != null) {
            wrapper.eq(Recipe::getStatus, status);
        }
        
        wrapper.orderByDesc(Recipe::getCreateTime);
        Page<Recipe> result = this.page(page, wrapper);
        
        for (Recipe recipe : result.getRecords()) {
            fillRecipeInfo(recipe);
        }
        
        return result;
    }

    @Override
    public Page<Recipe> frontPageList(Integer pageNum, Integer pageSize, String title, Long categoryId, Long userId) {
        Page<Recipe> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Recipe> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(title)) {
            wrapper.like(Recipe::getTitle, title);
        }
        if (categoryId != null) {
            wrapper.eq(Recipe::getCategoryId, categoryId);
        }
        
        wrapper.eq(Recipe::getStatus, 1)
                .orderByDesc(Recipe::getCreateTime);
        
        Page<Recipe> result = this.page(page, wrapper);
        
        for (Recipe recipe : result.getRecords()) {
            fillRecipeInfo(recipe);
            if (userId != null) {
                recipe.setIsLiked(userLikeService.isLiked(userId, recipe.getId(), "recipe"));
            }
        }
        
        return result;
    }

    @Override
    public Recipe getDetail(Long id, Long userId) {
        Recipe recipe = this.getById(id);
        if (recipe == null) {
            return null;
        }
        
        fillRecipeInfo(recipe);
        
        if (userId != null) {
            recipe.setIsLiked(userLikeService.isLiked(userId, recipe.getId(), "recipe"));
        }
        
        return recipe;
    }

    @Override
    public boolean viewCountIncrement(Long id) {
        LambdaUpdateWrapper<Recipe> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Recipe::getId, id)
                .setSql("view_count = view_count + 1");
        return this.update(wrapper);
    }

    @Override
    public boolean toggleLike(Long userId, Long recipeId) {
        return userLikeService.toggleLike(userId, recipeId, "recipe");
    }

    @Override
    public List<Recipe> getRecommend(Integer limit) {
        LambdaQueryWrapper<Recipe> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Recipe::getStatus, 1)
                .orderByDesc(Recipe::getViewCount)
                .orderByDesc(Recipe::getLikeCount)
                .last("LIMIT " + limit);
        
        List<Recipe> list = this.list(wrapper);
        for (Recipe recipe : list) {
            fillRecipeInfo(recipe);
        }
        return list;
    }

    private void fillRecipeInfo(Recipe recipe) {
        if (recipe.getCategoryId() != null) {
            RecipeCategory category = recipeCategoryService.getById(recipe.getCategoryId());
            if (category != null) {
                recipe.setCategoryName(category.getName());
            }
        }
        
        if (recipe.getUserId() != null) {
            User user = userService.getById(recipe.getUserId());
            if (user != null) {
                recipe.setUserName(user.getNickname());
            }
        }
    }
}
