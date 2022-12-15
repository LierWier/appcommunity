package com.lierlier.backend.controller;

import com.lierlier.backend.service.AppEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/appevl")
public class AppEvaluationController {
    @Autowired
    private AppEvaluationService appEvaluationService;

    @GetMapping("/getlist")
    public Map<String, Object> getAppEvl(Integer appId, Integer page, Integer pageSize) {
        return appEvaluationService.getAppEvl(appId, page, pageSize);
    }
}
