package com.lierlier.backend.controller;

import com.lierlier.backend.pojo.User;
import com.lierlier.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/getuserlist")
    public Map<String, Object> getUserList(User queryUser) {
        return userService.getUserList(queryUser);
    }

    @PostMapping("/gettoken")
    public Map<String, Object> getToken(Map<String, String> data) {
        String username = data.get("username");
        String password = data.get("password");
        return userService.getToken(username, password);
    }

    @PostMapping("/register")
    public Map<String, Object> register(String username, String password, String confirmedPassword) {
//        String username = map.get("username");
//        String password = map.get("password");
//        String confirmedPassword = map.get("confirmedPassword");
        return userService.register(username, password, confirmedPassword);
    }
}
