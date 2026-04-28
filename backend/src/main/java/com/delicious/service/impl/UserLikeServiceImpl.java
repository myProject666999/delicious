package com.delicious.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.delicious.entity.Food;
import com.delicious.entity.Recipe;
import com.delicious.entity.UserLike;
import com.delicious.mapper.UserLikeMapper;
import com.delicious.service.FoodService;
import com.delicious.service.RecipeService;
import com.delicious.service.UserLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserLikeServiceImpl extends ServiceImpl<UserLikeMapper, UserLike> implements UserLikeService {

    @Autowired
    private RecipeService recipeService;
    
    @Autowired
    private FoodService foodService;

    @Override
    public boolean isLiked(Long userId, Long targetId, String targetType) {
        if (userId == null) {
            return false;
        }
        
        LambdaQueryWrapper<UserLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserLike::getUserId, userId)
                .eq(UserLike::getTargetId, targetId)
                .eq(UserLike::getTargetType, targetType);
        
        return this.count(wrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean toggleLike(Long userId, Long targetId, String targetType) {
        LambdaQueryWrapper<UserLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserLike::getUserId, userId)
                .eq(UserLike::getTargetId, targetId)
                .eq(UserLike::getTargetType, targetType);
        
        UserLike exist = this.getOne(wrapper);
        
        if (exist != null) {
            this.remove(wrapper);
            updateLikeCount(targetId, targetType, -1);
            return false;
        } else {
            UserLike userLike = new UserLike();
            userLike.setUserId(userId);
            userLike.setTargetId(targetId);
            userLike.setTargetType(targetType);
            this.save(userLike);
            updateLikeCount(targetId, targetType, 1);
            return true;
        }
    }

    private void updateLikeCount(Long targetId, String targetType, int delta) {
        if ("recipe".equals(targetType)) {
            Recipe recipe = recipeService.getById(targetId);
            if (recipe != null) {
                int newCount = (recipe.getLikeCount() == null ? 0 : recipe.getLikeCount()) + delta;
                recipe.setLikeCount(newCount);
                recipeService.updateById(recipe);
            }
        } else if ("food".equals(targetType)) {
            Food food = foodService.getById(targetId);
            if (food != null) {
                int newCount = (food.getLikeCount() == null ? 0 : food.getLikeCount()) + delta;
                food.setLikeCount(newCount);
                foodService.updateById(food);
            }
        }
    }
}
