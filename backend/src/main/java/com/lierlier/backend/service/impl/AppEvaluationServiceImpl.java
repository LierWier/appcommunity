package com.lierlier.backend.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lierlier.backend.mapper.AppEvaluationMapper;
import com.lierlier.backend.mapper.UserMapper;
import com.lierlier.backend.pojo.AppEvaluation;
import com.lierlier.backend.pojo.User;
import com.lierlier.backend.service.AppEvaluationService;
import com.lierlier.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class AppEvaluationServiceImpl implements AppEvaluationService {
    @Autowired
    private AppEvaluationMapper appEvaluationMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, Object> getAppEvlList(Integer appId, Integer page, Integer pageSize) {
        Map<String, Object> resp = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        QueryWrapper<AppEvaluation> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("status", 0).eq("app_id", appId).orderByDesc("create_time");

        List<AppEvaluation> appEvls;
        if (page != null && pageSize != null) {
            IPage<AppEvaluation> iPage = new Page<>(page, pageSize);
            appEvls = appEvaluationMapper.selectPage(iPage, queryWrapper).getRecords();
            data.put("total", appEvaluationMapper.selectCount(queryWrapper));
        } else {
            appEvls = appEvaluationMapper.selectList(queryWrapper);
        }

        List<Map<String, Object>> newData = new LinkedList<>();
        for (AppEvaluation appEvl: appEvls) {
            JSONObject newAppEvl = (JSONObject) JSON.toJSON(appEvl);
            newAppEvl.put("username", userMapper.selectById(appEvl.getUserId()).getUsername());
            newData.add(JSON.parseObject(newAppEvl.toString()));
        }

        data.put("appEvls", newData);
        resp.put("msg", "success");
        resp.put("data", data);
        return resp;
    }

    @Override
    public Map<String, Object> getAppEvlByLoginUser(Integer appId) {
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
        QueryWrapper<AppEvaluation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_id", appId).eq("user_id", user.getId());
        AppEvaluation appEvl = appEvaluationMapper.selectOne(queryWrapper);
        if (appEvl == null) {
            resp.put("msg", "未评论");
            return resp;
        }
        appEvl.setUserName(user.getUsername());
        resp.put("msg", "success");
        resp.put("data", appEvl);
        return resp;
    }

    @Override
    public Map<String, Object> postAppEvl(AppEvaluation appEvl) {
        Map<String, Object> resp = new HashMap<>();

        appEvl.setContent(appEvl.getContent().trim());

        if (appEvl.getScore() <= 0) {
            resp.put("msg", "请点击星星评分");
            return resp;
        }
        if (StringUtils.isEmpty(appEvl.getContent())) {
            resp.put("msg", "请填写评语");
            return resp;
        }

        QueryWrapper<AppEvaluation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", appEvl.getUserId()).eq("app_id", appEvl.getAppId());
        Long count = appEvaluationMapper.selectCount(queryWrapper);
        if (count > 0) {
            resp.put("msg", "用户已评论");
            return resp;
        }
        appEvl.setCreateTime(new Date());
        appEvaluationMapper.insert(appEvl);
        resp.put("msg", "success");
        return resp;
    }

    @Override
    public Map<String, Object> deleteAppEvlById(Integer id) {
        Map<String, Object> resp = new HashMap<>();
        User user;
        try {
            UsernamePasswordAuthenticationToken authentication =
                    (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
            user = loginUser.getUser();
        } catch (Exception e) {
            resp.put("msg", "无权限");
            return resp;
        }
        AppEvaluation appEvl = appEvaluationMapper.selectById(id);
        if (appEvl == null) {
            resp.put("msg", "该评论不存在");
            return resp;
        }
        if (!Objects.equals(appEvl.getUserId(), user.getId())) {
            resp.put("msg", "无权限");
            return resp;
        }

        appEvaluationMapper.deleteById(id);
        resp.put("msg", "success");
        return resp;
    }
}
