package com.lierlier.backend.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lierlier.backend.mapper.AppEvaluationLikeRecordMapper;
import com.lierlier.backend.mapper.AppEvaluationMapper;
import com.lierlier.backend.mapper.AppMapper;
import com.lierlier.backend.mapper.UserMapper;
import com.lierlier.backend.pojo.App;
import com.lierlier.backend.pojo.AppEvaluation;
import com.lierlier.backend.pojo.AppEvaluationLikeRecord;
import com.lierlier.backend.pojo.User;
import com.lierlier.backend.service.AppEvaluationService;
import com.lierlier.backend.service.impl.utils.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


@Service
@RequiredArgsConstructor
public class AppEvaluationServiceImpl implements AppEvaluationService {
    private final AppEvaluationMapper appEvaluationMapper;
    private final UserMapper userMapper;
    private final AppMapper appMapper;
    private final AppEvaluationLikeRecordMapper appEvaluationLikeRecordMapper;

    @Override
    public Map<String, Object> getAppEvlList(Integer appId, Integer page, Integer pageSize) {
        Map<String, Object> resp = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        User user;
        try {
            UsernamePasswordAuthenticationToken authentication =
                    (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
            user = loginUser.getUser();
        } catch (Exception e) {
            user = null;
        }
        QueryWrapper<AppEvaluation> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("status", 0).eq("app_id", appId).orderByDesc("create_time");

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
            if (user != null) {
                QueryWrapper<AppEvaluationLikeRecord> wrapper = new QueryWrapper<>();
                wrapper.eq("app_evaluation_id", appEvl.getId()).eq("user_id", user.getId());
                if (appEvaluationLikeRecordMapper.selectCount(wrapper) > 0) newAppEvl.put("isLiked", 1);
                else newAppEvl.put("isLiked", 0);
            } else newAppEvl.put("isLiked", 0);
            newData.add(JSON.parseObject(newAppEvl.toString()));
        }

        data.put("appEvls", newData);
        resp.put("msg", "success");
        resp.put("data", data);
        return resp;
    }

    @Override
    public Map<String, Object> getAppEvlByLoginUser(Integer appId) {
        Map<String, Object> resp = new HashMap<>();
        User user;
        try {
            UsernamePasswordAuthenticationToken authentication =
                    (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
            user = loginUser.getUser();
        } catch (Exception e) {
            resp.put("msg", "未登录");
            return resp;
        }
        QueryWrapper<AppEvaluation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_id", appId).eq("user_id", user.getId());
        AppEvaluation appEvl = appEvaluationMapper.selectOne(queryWrapper);
        if (appEvl == null) {
            resp.put("msg", "未评论");
            return resp;
        }
        appEvl.setUserName(user.getUsername());
        resp.put("msg", "success");
        resp.put("data", appEvl);
        return resp;
    }

    @Override
    public Map<String, Object> postAppEvl(AppEvaluation appEvl) {
        Map<String, Object> resp = new HashMap<>();

        appEvl.setContent(appEvl.getContent().trim());

        if (appEvl.getScore() <= 0) {
            resp.put("msg", "请点击星星评分");
            return resp;
        }
        if (StringUtils.isEmpty(appEvl.getContent())) {
            resp.put("msg", "请填写评语");
            return resp;
        }

        QueryWrapper<AppEvaluation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", appEvl.getUserId()).eq("app_id", appEvl.getAppId());
        Long count = appEvaluationMapper.selectCount(queryWrapper);
        if (count > 0) {
            resp.put("msg", "用户已评论");
            return resp;
        }
        appEvl.setCreateTime(new Date());
        appEvaluationMapper.insert(appEvl);
        resp.put("msg", "success");

        updateAppScore(appEvl.getAppId());

        return resp;
    }

    private void updateAppScore(Integer appId) {
        QueryWrapper<AppEvaluation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_id", appId).select("avg(score) as avg");
        List<Map<String, Object>> maps = appEvaluationMapper.selectMaps(queryWrapper);
        App app = new App();
        app.setId(appId);
        if (maps.get(0) == null) {
            app.setScore(0f);
        } else {
            double avg = Double.parseDouble(maps.get(0).get("avg").toString());
            Float score = Float.valueOf(new BigDecimal(avg).setScale(1, RoundingMode.HALF_UP).toString());
            app.setScore(score);
        }
        appMapper.updateById(app);
    }

    @Override
    public Map<String, Object> deleteAppEvlById(Integer id) {
        Map<String, Object> resp = new HashMap<>();
        User user;
        try {
            UsernamePasswordAuthenticationToken authentication =
                    (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
            user = loginUser.getUser();
        } catch (Exception e) {
            resp.put("msg", "无权限");
            return resp;
        }
        AppEvaluation appEvl = appEvaluationMapper.selectById(id);
        if (appEvl == null) {
            resp.put("msg", "该评论不存在");
            return resp;
        }
        if (!Objects.equals(appEvl.getUserId(), user.getId())) {
            resp.put("msg", "无权限");
            return resp;
        }

        QueryWrapper<AppEvaluationLikeRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("app_evaluation_id", id).select("id");
        List<Object> idList = appEvaluationLikeRecordMapper.selectObjs(wrapper);

        appEvaluationLikeRecordMapper.deleteBatchIds(idList);
        appEvaluationMapper.deleteById(id);
        resp.put("msg", "success");

        updateAppScore(appEvl.getAppId());

        return resp;
    }

    @Override
    public Map<String, Object> updateLike(Integer id) {
        Map<String, Object> resp = new HashMap<>();
        User user;
        try {
            UsernamePasswordAuthenticationToken authentication =
                    (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
            user = loginUser.getUser();
        } catch (Exception e) {
            resp.put("msg", "未登录");
            return resp;
        }

        AppEvaluation appEvaluation = appEvaluationMapper.selectById(id);
        if (Objects.equals(appEvaluation.getUserId(), user.getId())) {
            resp.put("msg", "不能给自己点赞");
            return resp;
        }

        QueryWrapper<AppEvaluationLikeRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_evaluation_id", id).eq("user_id", user.getId());
        AppEvaluationLikeRecord record = appEvaluationLikeRecordMapper.selectOne(queryWrapper);
        if (record == null) {
            AppEvaluationLikeRecord newRecord = new AppEvaluationLikeRecord();
            newRecord.setAppEvaluationId(id);
            newRecord.setUserId(user.getId());
            newRecord.setCreateTime(new Date());
            appEvaluationLikeRecordMapper.insert(newRecord);
        } else {
            appEvaluationLikeRecordMapper.deleteById(record);
        }
        updateAppEvaluationLike(id);
        resp.put("msg", "success");
        return resp;
    }

    private void updateAppEvaluationLike(Integer id) {
        QueryWrapper<AppEvaluationLikeRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_evaluation_id", id);
        Integer liked = Math.toIntExact(appEvaluationLikeRecordMapper.selectCount(queryWrapper));
        AppEvaluation appEvaluation = new AppEvaluation();
        appEvaluation.setId(id);
        appEvaluation.setLiked(liked);
        appEvaluationMapper.updateById(appEvaluation);
    }

    @Override
    public Map<String, Object> getAppEvlListByLoginUser(Integer page, Integer pageSize) {
        Map<String, Object> resp = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        User user;
        try {
            UsernamePasswordAuthenticationToken authentication =
                    (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
            user = loginUser.getUser();
        } catch (Exception e) {
            resp.put("msg", "未登录");
            return resp;
        }

        QueryWrapper<AppEvaluation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId()).orderByDesc("create_time");

        List<AppEvaluation> list;
        if (page != null && pageSize != null) {
            IPage<AppEvaluation> iPage = new Page<>(page, pageSize);
            list = appEvaluationMapper.selectPage(iPage, queryWrapper).getRecords();
            data.put("total", appEvaluationMapper.selectCount(queryWrapper));
        } else {
            list = appEvaluationMapper.selectList(queryWrapper);
        }
        for (AppEvaluation item: list) {
            item.setAppName(appMapper.selectById(item.getAppId()).getAppName());
        }
        data.put("list", list);

        resp.put("msg", "success");
        resp.put("data", data);
        return resp;
    }
}
