package com.lierlier.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lierlier.backend.mapper.UserMapper;
import com.lierlier.backend.pojo.User;
import com.lierlier.backend.service.UserService;
import com.lierlier.backend.service.impl.utils.UserDetailsImpl;
import com.lierlier.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserSeriviceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, Object> getUserList(User queryUser) {
        HashMap<String, Object> map = new HashMap<>();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> users = userMapper.selectList(queryWrapper);
        for (User user: users) {
            user.setPassword("");
        }
        map.put("msg", "success");
        map.put("users", users);
        return map;
    }

    /**
     * 登录 获取 JWT-token
     * @param username 用户名
     * @param password 密码
     * @return JWT-token
     */
    @Override
    public Map<String, Object> getToken(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();
        User user = loginUser.getUser();

        String token = JwtUtil.createJWT(user.getId().toString());

        Map<String, Object> map = new HashMap<>();
        map.put("msg", "success");
        map.put("token", token);
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
}
