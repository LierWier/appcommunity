package com.lierlier.backend.controller;

import com.lierlier.backend.pojo.App;
import com.lierlier.backend.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/app")
public class AppController {
    @Autowired
    private AppService appService;

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
}
