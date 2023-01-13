package com.lierlier.backend.service;

import com.lierlier.backend.pojo.User;

import java.util.Map;

public interface UserService {
    Map<String, Object> getToken(String username, String password);
    Map<String, Object> register(String username, String password, String confirmedPassword);
    Map<String, Object> getInfo();
    Map<String, Object> getUserList(Map<String, Object> queryUser);
    Map<String, Object> updateStatus(Integer id, Integer status);
    Map<String, Object> resetPassword(Integer id);
    Map<String, Object> addUser(User user);
    Map<String, Object> updateUser(User user);
}
