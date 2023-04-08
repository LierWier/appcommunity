package com.lierlier.backend.controller;

import com.lierlier.backend.pojo.App;
import com.lierlier.backend.service.AppService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/app")
@RequiredArgsConstructor
public class AppController {
    private final AppService appService;

    @GetMapping("/getinfo")
    public Map<String, Object> getInfo(Integer id) {
        return appService.getInfo(id);
    }

    @GetMapping("/getapplist")
    public Map<String, Object> getAppList(@RequestParam Map<String, Object> queryApp) {
        return appService.getAppList(queryApp);
    }

    @GetMapping("/getappcategory")
    public Map<String, Object> geAppCategory() {
        return appService.getAppCategory();
    }

    @PostMapping("/add")
    public Map<String, Object> addApp(App app) {
        return appService.addApp(app);
    }

    @PostMapping("/deletebylist")
    public Map<String, Object> deleteAppByList(@RequestParam("ids[]") Integer[] ids) {
        return appService.deleteAppByList(ids);
    }

    @PostMapping("/update")
    public Map<String, Object> updateApp(App app) {
        return appService.updateApp(app);
    }

    @PostMapping("/updatestatusbylist")
    public Map<String, Object> updateAppStatusByList(@RequestParam(value = "ids[]") Integer[] ids, Integer status) {
        return appService.updateAppStatusByList(ids, status);
    }

    @GetMapping("/tag_list")
    public Map<String, Object> getTagList() {
        Map<String, Object> resp = new HashMap<>();
        String[] tags = {"游戏", "工具", "购物", "教育", "美食", "社交", "摄影", "生活", "体育", "娱乐", "影音", "新闻", "商务", "儿童", "财务", "图书", "医疗", "旅游"};
        resp.put("msg", "success");
        resp.put("data", tags);
        return resp;
    }
}
