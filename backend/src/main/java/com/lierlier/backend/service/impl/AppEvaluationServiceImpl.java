package com.lierlier.backend.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lierlier.backend.mapper.AppEvaluationMapper;
import com.lierlier.backend.mapper.UserMapper;
import com.lierlier.backend.pojo.AppEvaluation;
import com.lierlier.backend.service.AppEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AppEvaluationServiceImpl implements AppEvaluationService {
    @Autowired
    private AppEvaluationMapper appEvaluationMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, Object> getAppEvl(Integer appId, Integer page, Integer pageSize) {
        Map<String, Object> resp = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        QueryWrapper<AppEvaluation> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("status", 0).eq("app_id", appId);

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
}
