package com.delicious.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.delicious.entity.ForumReply;
import com.delicious.entity.User;
import com.delicious.mapper.ForumReplyMapper;
import com.delicious.service.ForumReplyService;
import com.delicious.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ForumReplyServiceImpl extends ServiceImpl<ForumReplyMapper, ForumReply> implements ForumReplyService {

    @Autowired
    private UserService userService;

    @Override
    public List<ForumReply> getByForumId(Long forumId) {
        LambdaQueryWrapper<ForumReply> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ForumReply::getForumId, forumId)
                .eq(ForumReply::getStatus, 1)
                .orderByAsc(ForumReply::getCreateTime);
        
        List<ForumReply> list = this.list(wrapper);
        
        for (ForumReply reply : list) {
            fillReplyInfo(reply);
        }
        
        return buildTree(list);
    }

    @Override
    public Page<ForumReply> pageList(Integer pageNum, Integer pageSize, Long forumId) {
        Page<ForumReply> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<ForumReply> wrapper = new LambdaQueryWrapper<>();
        
        if (forumId != null) {
            wrapper.eq(ForumReply::getForumId, forumId);
        }
        
        wrapper.orderByDesc(ForumReply::getCreateTime);
        Page<ForumReply> result = this.page(page, wrapper);
        
        for (ForumReply reply : result.getRecords()) {
            fillReplyInfo(reply);
        }
        
        return result;
    }

    @Override
    public boolean addReply(ForumReply reply) {
        reply.setStatus(1);
        return this.save(reply);
    }

    private void fillReplyInfo(ForumReply reply) {
        if (reply.getUserId() != null) {
            User user = userService.getById(reply.getUserId());
            if (user != null) {
                reply.setUserName(user.getNickname());
                reply.setUserAvatar(user.getAvatar());
            }
        }
        
        if (reply.getReplyUserId() != null) {
            User replyUser = userService.getById(reply.getReplyUserId());
            if (replyUser != null) {
                reply.setReplyUserName(replyUser.getNickname());
            }
        }
    }

    private List<ForumReply> buildTree(List<ForumReply> list) {
        Map<Long, List<ForumReply>> groupMap = list.stream()
                .collect(Collectors.groupingBy(reply -> reply.getParentId() == null ? 0L : reply.getParentId()));
        
        List<ForumReply> rootList = groupMap.getOrDefault(0L, new ArrayList<>());
        
        for (ForumReply root : rootList) {
            root.setChildren(groupMap.getOrDefault(root.getId(), new ArrayList<>()));
        }
        
        return rootList;
    }
}
