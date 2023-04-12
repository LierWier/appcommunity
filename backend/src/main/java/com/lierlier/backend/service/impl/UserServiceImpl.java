package com.lierlier.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lierlier.backend.mapper.UserMapper;
import com.lierlier.backend.pojo.User;
import com.lierlier.backend.service.UserService;
import com.lierlier.backend.service.impl.utils.UserDetailsImpl;
import com.lierlier.backend.utils.FileUploadUtils;
import com.lierlier.backend.utils.FileUtil;
import com.lierlier.backend.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    /**
     * 登录 获取 JWT-token
     * @param username 用户名
     * @param password 密码
     * @return JWT-token
     */
    @Override
    public Map<String, Object> getToken(String username, String password) {
        Map<String, Object> map = new HashMap<>();

        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, password);
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();
            User user = loginUser.getUser();
            String token = JwtUtil.createJWT(user.getId().toString());

            if (user.getStatus() == 0) {
                map.put("msg", "账号已被封禁！");
                return map;
            }
            if (user.getStatus() == -1) {
                map.put("msg", "error");
                return map;
            }
            map.put("msg", "success");
            map.put("data", token);
        } catch (AuthenticationException e) {
            map.put("msg", "error");
            return map;
        }
        return map;
    }

    @Override
    public Map<String, Object> register(String username, String password, String confirmedPassword) {
        Map<String, Object> map = new HashMap<>();
        if (username == null) {
            map.put("msg", "用户名不能为空");
            return map;
        }

        username = username.trim();
        if (username.length() == 0) {
            map.put("msg", "用户名长度不能为0");
            return map;
        }
        if (username.length() > 20) {
            map.put("msg", "用户名长度不能大于20");
            return map;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        List<User> users = userMapper.selectList(queryWrapper);
        if (!users.isEmpty()) {
            map.put("msg", "用户名已存在");
            return map;
        }

        if (password == null || confirmedPassword == null) {
            map.put("msg", "密码不能为空");
            return map;
        }
        if (password.length() == 0 || confirmedPassword.length() == 0) {
            map.put("msg", "密码长度不能为0");
            return map;
        }
        if (password.length() > 20) {
            map.put("msg", "密码长度不能大于20");
            return map;
        }
        if (!password.equals(confirmedPassword)) {
            map.put("msg", "两次密码不一致");
            return map;
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setCreateTime(new Date());
        userMapper.insert(user);
        map.put("msg", "success");
        return map;
    }

    @Override
    public Map<String, Object> getInfo() {
        Map<String, Object> map = new HashMap<>();

        try {
            UsernamePasswordAuthenticationToken authentication =
                    (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

            UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
            User user = loginUser.getUser();
            if (user.getStatus() == 0) {
                map.put("msg", "账号已被封禁！");
                return map;
            }
            if (user.getStatus() == -1) {
                map.put("msg", "error");
                return map;
            }
            user.setPassword("");
            map.put("msg", "success");
            map.put("data", user);
        } catch (Exception e) {
            map.put("msg", "error");
        }

        return map;
    }

    @Override
    public Map<String, Object> getUserList(Map<String, Object> queryUser) {
        HashMap<String, Object> map = new HashMap<>();
        User user;
        try {
            UsernamePasswordAuthenticationToken authentication =
                    (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
            user = loginUser.getUser();
        } catch (Exception e) {
            map.put("msg", "该用户不存在！");
            return map;
        }
        if (user.getIsManager() == 0) {
            map.put("msg", "无权限！");
            return map;
        }

        String username = (String) queryUser.get("username");
        String tel = (String) queryUser.get("tel");
        String sex = (String) queryUser.get("sex");
        String status = (String) queryUser.get("status");

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(username)) queryWrapper.like("username", username);
        if (StringUtils.isNotEmpty(tel)) queryWrapper.like("tel", tel);
        if (StringUtils.isNotEmpty(sex)) queryWrapper.eq("sex", sex);
        if (StringUtils.isNotEmpty(status)) queryWrapper.eq("status", Integer.parseInt(status));

        List<User> users;

        if (queryUser.get("page") != null && queryUser.get("pageSize") != null) {
            String page = queryUser.get("page").toString();
            String pageSize = queryUser.get("pageSize").toString();
            IPage<User> userIPage = new Page<>(Integer.parseInt(page), Integer.parseInt(pageSize));
            users = userMapper.selectPage(userIPage, queryWrapper).getRecords();
            map.put("total", userMapper.selectCount(queryWrapper));
        } else {
            users = userMapper.selectList(queryWrapper);
        }

        for (User u: users) u.setPassword(null);

        map.put("msg", "success");
        map.put("users", users);
        return map;
    }

    @Override
    public Map<String, Object> updateStatus(Integer id, Integer status) {
        HashMap<String, Object> map = new HashMap<>();

        try {
            UsernamePasswordAuthenticationToken authentication =
                    (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
            User user = loginUser.getUser();
            if (user.getIsManager() < 1) {
                map.put("msg", "无权限");
                return map;
            }
        } catch (Exception e) {
            map.put("msg", "该用户不存在！");
            return map;
        }

        User user = userMapper.selectById(id);
        user.setStatus(status);
        userMapper.updateById(user);

        map.put("msg", "success");
        return map;
    }

    @Override
    public Map<String, Object> resetPassword(Integer id) {
        Map<String, Object> resp = new HashMap<>();

        try {
            UsernamePasswordAuthenticationToken authentication =
                    (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
            User user = loginUser.getUser();
            if (user.getIsManager() < 1) {
                resp.put("msg", "无权限");
                return resp;
            }
        } catch (Exception e) {
            resp.put("msg", "登录异常！");
            return resp;
        }

        User user = userMapper.selectById(id);
        user.setPassword(passwordEncoder.encode(user.getUsername()));
        userMapper.updateById(user);
        resp.put("msg", "success");
        resp.put("data", user.getUsername());
        return resp;
    }

    @Override
    public Map<String, Object> addUser(User user) {
        HashMap<String, Object> map = new HashMap<>();
        String msg = commonValid(user);
        map.put("msg", msg);
        if (!"success".equals(msg)) return map;
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        if (StringUtils.isEmpty(user.getTel())) user.setTel(null);
        if (StringUtils.isEmpty(user.getSex())) user.setSex(null);
        user.setCreateTime(new Date());
        userMapper.insert(user);
        return map;
    }

    @Override
    public Map<String, Object> updateUser(User user) {
        Map<String, Object> resp = new HashMap<>();

        User loginUser;
        try {
            UsernamePasswordAuthenticationToken authentication =
                    (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl login = (UserDetailsImpl) authentication.getPrincipal();
            loginUser = login.getUser();
        } catch (Exception e) {
            resp.put("msg", "未登录");
            return resp;
        }

        boolean isManager = loginUser.getIsManager() > 0;

        // 非管理员只允许更改自己的信息，且不得修改个别信息
        if (!isManager && (
                !Objects.equals(user.getId(), loginUser.getId())
                || user.getIsManager() != null
                || user.getStatus() != null
                || user.getPassword() != null
        )) {
            resp.put("msg", "无权限");
            return resp;
        }
        // 任何人都不得更改用户名
        if (user.getUsername() != null && !Objects.equals(user.getUsername(), loginUser.getUsername())) {
            resp.put("msg", "用户名不可更改");
            return resp;
        }
        // 任何人都不得更改注册时间，修改密码走另外接口
        user.setPassword(null);
        if (user.getCreateTime() != null && user.getCreateTime() != loginUser.getCreateTime()) {
            resp.put("msg", "非法修改");
            return resp;
        }

        if (StringUtils.isNotEmpty(user.getPhoto())) {
            try {
                if (StringUtils.isNotEmpty(loginUser.getPhoto())
                        && !FileUtil.deleteFile(FileUtil.urlToPath(loginUser.getPhoto()))) {
                    resp.put("msg", "头像更换失败");
                    return resp;
                }
                String newPhoto = FileUploadUtils.saveFile(user.getPhoto(), "image/user/avatar/");
                user.setPhoto(newPhoto);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        userMapper.updateById(user);
        resp.put("msg", "success");
        return resp;
    }

    private String commonValid(User user) {
        if (StringUtils.isEmpty(user.getUsername())) return "用户名不能为空！";
        if (StringUtils.isEmpty(user.getPassword())) return "密码不能为空！";
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        List<User> users = userMapper.selectList(queryWrapper);
        if (!users.isEmpty()) return "用户名已存在";
        return "success";
    }
}
