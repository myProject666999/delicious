package com.delicious.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.delicious.entity.Food;
import com.delicious.entity.FoodCategory;
import com.delicious.entity.User;
import com.delicious.mapper.FoodMapper;
import com.delicious.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class FoodServiceImpl extends ServiceImpl<FoodMapper, Food> implements FoodService {

    @Autowired
    private FoodCategoryService foodCategoryService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserLikeService userLikeService;

    @Override
    public Page<Food> pageList(Integer pageNum, Integer pageSize, String name, Long categoryId, Integer status) {
        Page<Food> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Food> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(name)) {
            wrapper.like(Food::getName, name);
        }
        if (categoryId != null) {
            wrapper.eq(Food::getCategoryId, categoryId);
        }
        if (status != null) {
            wrapper.eq(Food::getStatus, status);
        }
        
        wrapper.orderByDesc(Food::getCreateTime);
        Page<Food> result = this.page(page, wrapper);
        
        for (Food food : result.getRecords()) {
            fillFoodInfo(food);
        }
        
        return result;
    }

    @Override
    public Page<Food> frontPageList(Integer pageNum, Integer pageSize, String name, Long categoryId, Long userId) {
        Page<Food> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Food> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(name)) {
            wrapper.like(Food::getName, name);
        }
        if (categoryId != null) {
            wrapper.eq(Food::getCategoryId, categoryId);
        }
        
        wrapper.eq(Food::getStatus, 1)
                .orderByDesc(Food::getCreateTime);
        
        Page<Food> result = this.page(page, wrapper);
        
        for (Food food : result.getRecords()) {
            fillFoodInfo(food);
            if (userId != null) {
                food.setIsLiked(userLikeService.isLiked(userId, food.getId(), "food"));
            }
        }
        
        return result;
    }

    @Override
    public Food getDetail(Long id, Long userId) {
        Food food = this.getById(id);
        if (food == null) {
            return null;
        }
        
        fillFoodInfo(food);
        
        if (userId != null) {
            food.setIsLiked(userLikeService.isLiked(userId, food.getId(), "food"));
        }
        
        return food;
    }

    @Override
    public boolean viewCountIncrement(Long id) {
        LambdaUpdateWrapper<Food> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Food::getId, id)
                .setSql("view_count = view_count + 1");
        return this.update(wrapper);
    }

    @Override
    public boolean toggleLike(Long userId, Long foodId) {
        return userLikeService.toggleLike(userId, foodId, "food");
    }

    @Override
    public List<Food> getRecommend(Integer limit) {
        LambdaQueryWrapper<Food> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Food::getStatus, 1)
                .orderByDesc(Food::getViewCount)
                .orderByDesc(Food::getLikeCount)
                .last("LIMIT " + limit);
        
        List<Food> list = this.list(wrapper);
        for (Food food : list) {
            fillFoodInfo(food);
        }
        return list;
    }

    private void fillFoodInfo(Food food) {
        if (food.getCategoryId() != null) {
            FoodCategory category = foodCategoryService.getById(food.getCategoryId());
            if (category != null) {
                food.setCategoryName(category.getName());
            }
        }
        
        if (food.getUserId() != null) {
            User user = userService.getById(food.getUserId());
            if (user != null) {
                food.setUserName(user.getNickname());
            }
        }
    }
}
