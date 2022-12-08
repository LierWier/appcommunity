package com.lierlier.backend.controller;

import com.lierlier.backend.pojo.User;
import com.lierlier.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/gettoken")
    public Map<String, Object> getToken(String username, String password) {
        return userService.getToken(username, password);
    }

    @PostMapping("/register")
    public Map<String, Object> register(String username, String password, String confirmedPassword) {
        return userService.register(username, password, confirmedPassword);
    }

    @GetMapping("/getinfo")
    public Map<String, Object> getInfo() {
        return userService.getInfo();
    }

    @GetMapping("/getuserlist")
    public Map<String, Object> getUserList(@RequestParam Map<String, Object> queryUser, Integer page, Integer pageSize) {
        System.out.println(queryUser);
        System.out.println(page);
        System.out.println(pageSize);
        return userService.getUserList(queryUser, page, pageSize);
    }

    @PostMapping("/updateStatus")
    public Map<String, Object> updateStatus(Integer id, Integer status) {
        return userService.updateStatus(id, status);
    }

    @PostMapping("/add")
    public Map<String, Object> addUser(User user) {
        return userService.addUser(user);
    }
}
