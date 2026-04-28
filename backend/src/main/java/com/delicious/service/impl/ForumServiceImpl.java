package com.delicious.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.delicious.entity.Forum;
import com.delicious.entity.User;
import com.delicious.mapper.ForumMapper;
import com.delicious.service.ForumReplyService;
import com.delicious.service.ForumService;
import com.delicious.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ForumServiceImpl extends ServiceImpl<ForumMapper, Forum> implements ForumService {

    @Autowired
    private UserService userService;
    
    @Autowired
    private ForumReplyService forumReplyService;

    @Override
    public Page<Forum> pageList(Integer pageNum, Integer pageSize, String title, Long userId, Integer status) {
        Page<Forum> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Forum> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(title)) {
            wrapper.like(Forum::getTitle, title);
        }
        if (userId != null) {
            wrapper.eq(Forum::getUserId, userId);
        }
        if (status != null) {
            wrapper.eq(Forum::getStatus, status);
        }
        
        wrapper.orderByDesc(Forum::getIsTop)
                .orderByDesc(Forum::getCreateTime);
        
        Page<Forum> result = this.page(page, wrapper);
        
        for (Forum forum : result.getRecords()) {
            fillForumInfo(forum);
        }
        
        return result;
    }

    @Override
    public Page<Forum> frontPageList(Integer pageNum, Integer pageSize, String title) {
        Page<Forum> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Forum> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(title)) {
            wrapper.like(Forum::getTitle, title);
        }
        
        wrapper.eq(Forum::getStatus, 1)
                .orderByDesc(Forum::getIsTop)
                .orderByDesc(Forum::getCreateTime);
        
        Page<Forum> result = this.page(page, wrapper);
        
        for (Forum forum : result.getRecords()) {
            fillForumInfo(forum);
        }
        
        return result;
    }

    @Override
    public Forum getDetail(Long id) {
        Forum forum = this.getById(id);
        if (forum == null) {
            return null;
        }
        
        fillForumInfo(forum);
        forum.setReplyList(forumReplyService.getByForumId(id));
        
        return forum;
    }

    @Override
    public boolean viewCountIncrement(Long id) {
        LambdaUpdateWrapper<Forum> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Forum::getId, id)
                .setSql("view_count = view_count + 1");
        return this.update(wrapper);
    }

    @Override
    public boolean publish(Forum forum) {
        forum.setViewCount(0);
        forum.setReplyCount(0);
        forum.setIsTop(0);
        forum.setStatus(1);
        return this.save(forum);
    }

    private void fillForumInfo(Forum forum) {
        if (forum.getUserId() != null) {
            User user = userService.getById(forum.getUserId());
            if (user != null) {
                forum.setUserName(user.getNickname());
                forum.setUserAvatar(user.getAvatar());
            }
        }
    }
}
