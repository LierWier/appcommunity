package com.lierlier.backend.controller;

import com.lierlier.backend.pojo.AppEvaluation;
import com.lierlier.backend.service.AppEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/appevl")
public class AppEvaluationController {
    @Autowired
    private AppEvaluationService appEvaluationService;

    @GetMapping("/getlist")
    public Map<String, Object> getAppEvlList(Integer appId, Integer page, Integer pageSize) {
        return appEvaluationService.getAppEvlList(appId, page, pageSize);
    }

    @GetMapping("/getbyloginuser")
    public Map<String, Object> getAppEvlByLoginUser(Integer appId) {
        return appEvaluationService.getAppEvlByLoginUser(appId);
    }

    @PostMapping("/post")
    public Map<String, Object> postAppEvl(AppEvaluation appEvl) {
        return appEvaluationService.postAppEvl(appEvl);
    }

    @PostMapping("/delete")
    public Map<String, Object> deleteAppEvlById(Integer id) {
        return appEvaluationService.deleteAppEvlById(id);
    }
}
