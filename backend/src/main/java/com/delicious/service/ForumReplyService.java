package com.delicious.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.delicious.entity.ForumReply;

import java.util.List;

public interface ForumReplyService extends IService<ForumReply> {
    List<ForumReply> getByForumId(Long forumId);
    Page<ForumReply> pageList(Integer pageNum, Integer pageSize, Long forumId);
    boolean addReply(ForumReply reply);
}
