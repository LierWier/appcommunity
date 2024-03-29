package com.lierlier.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lierlier.backend.mapper.AppMapper;
import com.lierlier.backend.pojo.App;
import com.lierlier.backend.service.AppService;
import com.lierlier.backend.utils.FileUploadUtils;
import com.lierlier.backend.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AppServiceImpl implements AppService {
    private final AppMapper appMapper;

    @Override
    public Map<String, Object> getInfo(Integer id) {
        Map<String, Object> map = new HashMap<>();
        App app = appMapper.selectById(id);
        map.put("msg", "success");
        map.put("data", app);
        return map;
    }

    @Override
    public Map<String, Object> getAppList(Map<String, Object> queryApp) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        QueryWrapper<App> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("status", 0);  // 过滤已删除
        if (StringUtils.isNotEmpty((String) queryApp.get("appName"))) queryWrapper.like("app_name", queryApp.get("appName"));
        if (StringUtils.isNotEmpty((String) queryApp.get("author"))) queryWrapper.like("author", queryApp.get("author"));
        if (StringUtils.isNotEmpty((String) queryApp.get("category"))) queryWrapper.like("category", queryApp.get("category"));
        if (StringUtils.isNotEmpty((String) queryApp.get("status"))) queryWrapper.eq("status", queryApp.get("status"));

        if (StringUtils.isNotEmpty((String) queryApp.get("order"))) queryWrapper.orderByDesc((String) queryApp.get("order"));

        List<App> apps;

        if (queryApp.get("page") != null && queryApp.get("pageSize") != null) {
            String page = queryApp.get("page").toString();
            String pageSize = queryApp.get("pageSize").toString();
            IPage<App> appIPage = new Page<>(Integer.parseInt(page), Integer.parseInt(pageSize));
            apps = appMapper.selectPage(appIPage, queryWrapper).getRecords();
            data.put("total", appMapper.selectCount(queryWrapper));
        } else {
            apps = appMapper.selectList(queryWrapper);
        }
        data.put("apps", apps);
        map.put("msg", "success");
        map.put("data", data);
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

        if (StringUtils.isNotEmpty(app.getAppIcon())) {
            try {
                String newIcon = FileUploadUtils.saveFile(app.getAppIcon(), "image/app/icon/");
                app.setAppIcon(newIcon);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        map.put("msg", msg);
        if (!"success".equals(msg)) return map;
        appMapper.insert(app);

        return map;
    }

    @Override
    public Map<String, Object> deleteAppByList(Integer[] ids) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            for (App app : appMapper.selectBatchIds(Arrays.asList(ids))) {
                if ((StringUtils.isNotEmpty(app.getAppIcon()))) {
                    FileUtil.deleteFile(FileUtil.urlToPath(app.getAppIcon()));
                }
            }
            appMapper.deleteBatchIds(Arrays.asList(ids));
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "删除失败");
            return map;
        }
        map.put("msg", "success");
        return map;
    }

    @Override
    public Map<String, Object> updateApp(App app) {
        HashMap<String, Object> map = new HashMap<>();
        App oldApp = appMapper.selectById(app.getId());
        if (oldApp == null) {
            map.put("msg", "ID错误，找不到数据！");
            return map;
        }
        String msg = commonValid(app);
        map.put("msg", msg);
        if (!"success".equals(msg)) return map;
        if (StringUtils.isNotEmpty(app.getAppIcon())) {
            try {
                if (StringUtils.isNotEmpty(oldApp.getAppIcon())
                        && !FileUtil.deleteFile(FileUtil.urlToPath(oldApp.getAppIcon()))) {
                    map.put("msg", "图标更换失败");
                    return map;
                }
                String newIcon = FileUploadUtils.saveFile(app.getAppIcon(), "image/app/icon/");
                app.setAppIcon(newIcon);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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
        if (app.getPostTime() == null) return "发布时间不能为空！";
        if (app.getUpdatePostTime() == null) return "更新时间不能为空！";
        QueryWrapper<App> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_name", app.getAppName()).ne("id", app.getId());
        List<App> apps = appMapper.selectList(queryWrapper);
        if (!apps.isEmpty()) return "应用名称已存在！";
        return "success";
    }

    @Override
    public Map<String, Object> getRank() {
        Map<String, Object> resp = new HashMap<>();
        QueryWrapper<App> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1);
        List<App> apps = appMapper.selectList(wrapper);
        for (App app: apps) {
            app.setRankScore(app.getScore() * app.getDownloads() * 0.01f);
        }
        apps.sort((a, b) -> -Float.compare(a.getRankScore(), b.getRankScore()));
        if (apps.size() > 100) apps.subList(0, 100);
        resp.put("msg", "success");
        resp.put("data", apps);
        return resp;
    }
}
