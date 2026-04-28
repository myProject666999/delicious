package com.delicious.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.delicious.common.Result;
import com.delicious.entity.Forum;
import com.delicious.entity.ForumReply;
import com.delicious.service.ForumReplyService;
import com.delicious.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/forum")
public class ForumController {

    @Autowired
    private ForumService forumService;
    
    @Autowired
    private ForumReplyService forumReplyService;

    @GetMapping("/list")
    public Result<Page<Forum>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer status) {
        Page<Forum> page = forumService.pageList(pageNum, pageSize, title, userId, status);
        return Result.success(page);
    }

    @GetMapping("/front/list")
    public Result<Page<Forum>> frontList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String title) {
        Page<Forum> page = forumService.frontPageList(pageNum, pageSize, title);
        return Result.success(page);
    }

    @GetMapping("/detail/{id}")
    public Result<Forum> detail(@PathVariable Long id) {
        forumService.viewCountIncrement(id);
        Forum forum = forumService.getDetail(id);
        return Result.success(forum);
    }

    @PostMapping("/publish")
    public Result<String> publish(@RequestBody Forum forum) {
        forumService.publish(forum);
        return Result.success("发布成功");
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody Forum forum) {
        forumService.saveOrUpdate(forum);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        forumService.removeById(id);
        return Result.success();
    }

    @PutMapping("/top/{id}")
    public Result<Void> updateTop(@PathVariable Long id, @RequestParam Integer isTop) {
        Forum forum = new Forum();
        forum.setId(id);
        forum.setIsTop(isTop);
        forumService.updateById(forum);
        return Result.success();
    }

    @PostMapping("/reply/save")
    public Result<Void> reply(@RequestBody ForumReply reply) {
        forumReplyService.addReply(reply);
        return Result.success();
    }

    @GetMapping("/reply/list")
    public Result<Page<ForumReply>> replyList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long forumId) {
        Page<ForumReply> page = forumReplyService.pageList(pageNum, pageSize, forumId);
        return Result.success(page);
    }

    @DeleteMapping("/reply/delete/{id}")
    public Result<Void> deleteReply(@PathVariable Long id) {
        forumReplyService.removeById(id);
        return Result.success();
    }
}
