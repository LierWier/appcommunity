package com.lierlier.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lierlier.backend.mapper.BlogMapper;
import com.lierlier.backend.mapper.BlogReplyMapper;
import com.lierlier.backend.mapper.BlogReplyReplyMapper;
import com.lierlier.backend.mapper.UserMapper;
import com.lierlier.backend.pojo.Blog;
import com.lierlier.backend.pojo.BlogReply;
import com.lierlier.backend.pojo.BlogReplyReply;
import com.lierlier.backend.pojo.User;
import com.lierlier.backend.service.BlogService;
import com.lierlier.backend.service.impl.utils.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {
    private final UserMapper userMapper;
    private final BlogMapper blogMapper;
    private final BlogReplyMapper blogReplyMapper;
    private final BlogReplyReplyMapper blogReplyReplyMapper;

    @Override
    public Map<String, Object> getList(Blog blog, Integer page, Integer pageSize) {
        Map<String, Object> resp = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("status", 0).orderByDesc("update_time");

        if (blog.getAuthorId() != null) queryWrapper.eq("author_id", blog.getAuthorId());

        if (StringUtils.isNotEmpty(blog.getAuthorName())) {
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.like("username", blog.getAuthorName()).select("id");
            List<Object> authorIds = userMapper.selectObjs(userQueryWrapper);
            queryWrapper.in("author_id", authorIds);
        }

        if (StringUtils.isNotEmpty(blog.getTitle())) queryWrapper.like("title", blog.getTitle());

        if (StringUtils.isNotEmpty(blog.getTag())) {
            String[] tags = blog.getTag().split(",");
            for (String tag: tags) queryWrapper.like("tag", tag);
        }

        List<Blog> blogs;
        if (page != null && pageSize != null) {
            IPage<Blog> iPage = new Page<>(page, pageSize);
            blogs = blogMapper.selectPage(iPage, queryWrapper).getRecords();
            data.put("total", blogMapper.selectCount(queryWrapper));
        } else {
            blogs = blogMapper.selectList(queryWrapper);
        }

        for (Blog b : blogs) b.setAuthorName(userMapper.selectById(b.getAuthorId()).getUsername());

        data.put("blogs", blogs);
        resp.put("msg", "success");
        resp.put("data", data);
        return resp;
    }

    @Override
    public Map<String, Object> getBlog(Integer id) {
        Map<String, Object> resp = new HashMap<>();
        Blog blog = blogMapper.selectById(id);
        User author = userMapper.selectById(blog.getAuthorId());
        blog.setAuthorName(author.getUsername());
        resp.put("msg", "success");
        resp.put("data", blog);
        return resp;
    }

    @Override
    public Map<String, Object> getReplyList(Integer blogId, Integer order) {
        Map<String, Object> resp = new HashMap<>();
        QueryWrapper<BlogReply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("blog_id", blogId);
        if (order == 1) queryWrapper.orderByDesc("create_time");
        List<BlogReply> replies = blogReplyMapper.selectList(queryWrapper);
        for (BlogReply reply: replies)
            reply.setUserName(userMapper.selectById(reply.getUserId()).getUsername());
        resp.put("msg", "success");
        resp.put("data", replies);
        return resp;
    }

    @Override
    public Map<String, Object> getReplyReplyList(Integer blogReplyId) {
        Map<String, Object> resp = new HashMap<>();
        QueryWrapper<BlogReplyReply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("blog_reply_id", blogReplyId);
        List<BlogReplyReply> replies = blogReplyReplyMapper.selectList(queryWrapper);
        for (BlogReplyReply reply: replies)
            reply.setUserName(userMapper.selectById(reply.getUserId()).getUsername());
        resp.put("msg", "success");
        resp.put("data", replies);
        return resp;
    }

    @Override
    public Map<String, Object> postBlog(Blog blog) {
        Map<String, Object> resp = new HashMap<>();
        User user;
        try {
            UsernamePasswordAuthenticationToken authentication =
                    (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
            user = loginUser.getUser();
        } catch (Exception e) {
            resp.put("msg", "未登录");
            return resp;
        }

        if (StringUtils.isEmpty(blog.getTitle())) {
            resp.put("msg", "标题不能为空");
            return resp;
        }
        if (StringUtils.isEmpty(blog.getContent())) {
            resp.put("msg", "内容不能为空");
            return resp;
        }
        blog.setAuthorId(user.getId());
        Date date = new Date();
        blog.setCreateTime(date);
        blog.setUpdateTime(date);
        blogMapper.insert(blog);
        resp.put("msg", "success");
        return resp;
    }

    @Override
    public Map<String, Object> deleteBlog(Blog blog) {
        Map<String, Object> resp = new HashMap<>();
        try {
            UsernamePasswordAuthenticationToken authentication =
                    (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
            User user = loginUser.getUser();
            if (!user.getId().equals(blog.getAuthorId()) && user.getIsManager() < 1) {
                resp.put("msg", "无权限");
                return resp;
            }
        } catch (Exception e) {
            resp.put("msg", "未登录");
            return resp;
        }
        blog.setStatus(-1);
        blogMapper.updateById(blog);
        deleteReplyWithBlog(blog.getId());
        resp.put("msg", "success");
        return resp;
    }

    private void deleteReplyWithBlog(Integer blogId) {
        QueryWrapper<BlogReply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("blog_id", blogId);
        BlogReply blogReply = new BlogReply();
        blogReply.setStatus(-1);
        blogReplyMapper.update(blogReply, queryWrapper);
    }

    @Override
    public Map<String, Object> postReply(BlogReply blogReply) {
        Map<String, Object> resp = new HashMap<>();
        User user;
        try {
            UsernamePasswordAuthenticationToken authentication =
                    (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
            user = loginUser.getUser();
        } catch (Exception e) {
            resp.put("msg", "未登录");
            return resp;
        }
        if (StringUtils.isEmpty(blogReply.getContent())) {
            resp.put("msg", "内容不能为空");
            return resp;
        }
        blogReply.setUserId(user.getId());
        blogReply.setCreateTime(new Date());
        blogReplyMapper.insert(blogReply);
        updateBlogReply(blogReply.getBlogId());
        updateBlog(blogReply.getBlogId());
        resp.put("msg", "success");
        return resp;
    }

    private void updateBlogReply(Integer id) {
        QueryWrapper<BlogReply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("blog_id", id);
        Blog blog = blogMapper.selectById(id);
        blog.setReply(Math.toIntExact(blogReplyMapper.selectCount(queryWrapper)));
        blogMapper.updateById(blog);
    }

    private void updateBlog(Blog blog) {
        blog.setUpdateTime(new Date());
        blogMapper.updateById(blog);
    }

    private void updateBlog(Integer id) {
        Blog blog = new Blog();
        blog.setId(id);
        blog.setUpdateTime(new Date());
        blogMapper.updateById(blog);
    }
}
