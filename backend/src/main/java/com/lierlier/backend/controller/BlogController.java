package com.lierlier.backend.controller;

import com.lierlier.backend.pojo.Blog;
import com.lierlier.backend.pojo.BlogReply;
import com.lierlier.backend.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/blog")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;

    @GetMapping("/blog_list")
    public Map<String, Object> getList(Blog blog, Integer page, Integer pageSize) {
        return blogService.getList(blog, page, pageSize);
    }

    @GetMapping("/blog")
    public Map<String, Object> getBlog(Integer id) {
        return blogService.getBlog(id);
    }

    @GetMapping("/reply_list")
    public Map<String, Object> getReplyList(Integer blogId, Integer order) {
        return blogService.getReplyList(blogId, order);
    }

    @GetMapping("/reply_reply_list")
    public Map<String, Object> getReplyReplyList(Integer blogReplyId) {
        return blogService.getReplyReplyList(blogReplyId);
    }

    @PostMapping("/blog")
    public Map<String, Object> postBlog(Blog blog) {
        return blogService.postBlog(blog);
    }

    @DeleteMapping("/blog")
    public Map<String, Object> deleteBlog(Blog blog) {
        return blogService.deleteBlog(blog);
    }

    @PostMapping("/reply")
    public Map<String, Object> postReply(BlogReply blogReply) {
        return blogService.postReply(blogReply);
    }
}
