package com.lierlier.backend.service;

import com.lierlier.backend.pojo.User;

import java.util.Map;

public interface UserService {
    Map<String, Object> getUserList(User queryUser);
    Map<String, Object> getToken(String username, String password);
    Map<String, Object> register(String username, String password, String confirmedPassword);
}
