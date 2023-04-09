package com.lierlier.backend.service;

import com.lierlier.backend.pojo.Blog;
import com.lierlier.backend.pojo.BlogReply;

import java.util.Map;

public interface BlogService {
    Map<String, Object> getList(Blog blog, Integer page, Integer pageSize);
    Map<String, Object> getBlog(Integer id);
    Map<String, Object> getReplyList(Integer blogId, Integer order);
    Map<String, Object> getReplyReplyList(Integer blogReplyId);
    Map<String, Object> postBlog(Blog blog);
    Map<String, Object> deleteBlog(Blog blog);
    Map<String, Object> postReply(BlogReply blogReply);
}
