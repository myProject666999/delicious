package com.delicious.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.delicious.entity.UserLike;

public interface UserLikeService extends IService<UserLike> {
    boolean isLiked(Long userId, Long targetId, String targetType);
    boolean toggleLike(Long userId, Long targetId, String targetType);
}
