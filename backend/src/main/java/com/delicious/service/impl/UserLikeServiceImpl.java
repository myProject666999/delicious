package com.delicious.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.delicious.entity.Food;
import com.delicious.entity.Recipe;
import com.delicious.entity.UserLike;
import com.delicious.mapper.FoodMapper;
import com.delicious.mapper.RecipeMapper;
import com.delicious.mapper.UserLikeMapper;
import com.delicious.service.UserLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserLikeServiceImpl extends ServiceImpl<UserLikeMapper, UserLike> implements UserLikeService {

    @Autowired
    private RecipeMapper recipeMapper;
    
    @Autowired
    private FoodMapper foodMapper;

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
            LambdaUpdateWrapper<Recipe> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(Recipe::getId, targetId)
                    .setSql("like_count = ifnull(like_count, 0) + " + delta);
            recipeMapper.update(null, wrapper);
        } else if ("food".equals(targetType)) {
            LambdaUpdateWrapper<Food> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(Food::getId, targetId)
                    .setSql("like_count = ifnull(like_count, 0) + " + delta);
            foodMapper.update(null, wrapper);
        }
    }
}
