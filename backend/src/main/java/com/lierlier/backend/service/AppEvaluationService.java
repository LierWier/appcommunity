package com.lierlier.backend.service;

import java.util.Map;

public interface AppEvaluationService {
    Map<String, Object> getAppEvl(Integer appId, Integer page, Integer pageSize);
}
