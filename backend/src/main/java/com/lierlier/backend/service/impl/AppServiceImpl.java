package com.lierlier.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.lierlier.backend.mapper.AppMapper;
import com.lierlier.backend.pojo.App;
import com.lierlier.backend.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Service
public class AppServiceImpl implements AppService {
    @Autowired
    private AppMapper appMapper;

    @Override
    public Map<String, Object> getAppList(Map<String, Object> queryApp) {
        HashMap<String, Object> map = new HashMap<>();

        QueryWrapper<App> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("status", 0);  // 过滤已删除
        if (StringUtils.isNotEmpty((String) queryApp.get("appName"))) queryWrapper.like("app_name", queryApp.get("appName"));
        if (StringUtils.isNotEmpty((String) queryApp.get("author"))) queryWrapper.like("author", queryApp.get("author"));
        if (StringUtils.isNotEmpty((String) queryApp.get("category"))) queryWrapper.like("category", queryApp.get("category"));
        if (StringUtils.isNotEmpty((String) queryApp.get("status"))) queryWrapper.eq("status", queryApp.get("status"));
        List<App> apps = appMapper.selectList(queryWrapper);

        map.put("msg", "success");
        map.put("data", apps);
        return map;
    }

    @Override
    public Map<String, Object> getAppCategory() {
        HashMap<String, Object> map = new HashMap<>();

        QueryWrapper<App> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("category").ne("category", "").select("distinct category");
        List<Object> category = appMapper.selectObjs(queryWrapper);

        map.put("msg", "success");
        map.put("data", category);
        return map;
    }

    @Override
    public Map<String, Object> addApp(App app) {
        HashMap<String, Object> map = new HashMap<>();
        String msg = commonValid(app);
        map.put("msg", msg);
        if (!"success".equals(msg)) return map;
        appMapper.insert(app);
        return map;
    }

    @Override
    public Map<String, Object> deleteAppByList(List<Integer> ids) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            appMapper.deleteBatchIds(ids);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        map.put("msg", "success");
        return map;
    }

    @Override
    public Map<String, Object> updateApp(App app) {
        HashMap<String, Object> map = new HashMap<>();
        String msg = commonValid(app);
        map.put("msg", msg);
        if (!"success".equals(msg)) return map;
        try {
            appMapper.updateById(app);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "更新失败！");
            return map;
        }
        return map;
    }

    @Override
    public Map<String, Object> updateAppStatusByList(Integer[] ids, Integer status) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            UpdateWrapper<App> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id", Arrays.asList(ids)).set("status", status);
            appMapper.update(null, updateWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "更新失败！");
            return map;
        }
        map.put("msg", "success");
        return map;
    }

    private String commonValid(App app) {
        if (StringUtils.isEmpty(app.getAppName())) return "应用名称不能为空！";
        QueryWrapper<App> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_name", app.getAppName());
        List<App> apps = appMapper.selectList(queryWrapper);
        if (!apps.isEmpty() && !Objects.equals(app.getId(), apps.get(0).getId())) return "应用名称已存在！";
        return "success";
    }
}
