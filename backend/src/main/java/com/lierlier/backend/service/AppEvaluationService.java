package com.lierlier.backend.service;

import com.lierlier.backend.pojo.AppEvaluation;

import java.util.Map;

public interface AppEvaluationService {
    Map<String, Object> getAppEvlList(Integer appId, Integer page, Integer pageSize);
    Map<String, Object> getAppEvlByLoginUser(Integer appId);
    Map<String, Object> postAppEvl(AppEvaluation appEvl);
    Map<String, Object> deleteAppEvlById(Integer id);
}
