package com.delicious.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.delicious.entity.Forum;

public interface ForumService extends IService<Forum> {
    Page<Forum> pageList(Integer pageNum, Integer pageSize, String title, Long userId, Integer status);
    Page<Forum> frontPageList(Integer pageNum, Integer pageSize, String title);
    Forum getDetail(Long id);
    boolean viewCountIncrement(Long id);
    boolean publish(Forum forum);
}
