package com.lierlier.backend.service;

import com.lierlier.backend.pojo.App;

import java.util.Map;

public interface AppService {
    Map<String, Object> getInfo(Integer id);
    Map<String, Object> getAppList(Map<String, Object> queryApp);
    Map<String, Object> getAppCategory();
    Map<String, Object> addApp(App app);
    Map<String, Object> deleteAppByList(Integer[] ids);
    Map<String, Object> updateApp(App app);
    Map<String, Object> updateAppStatusByList(Integer[] ids, Integer status);
}
