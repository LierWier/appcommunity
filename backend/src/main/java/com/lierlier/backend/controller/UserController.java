package com.lierlier.backend.controller;

import com.lierlier.backend.pojo.User;
import com.lierlier.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

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
    public Map<String, Object> getUserList(@RequestParam Map<String, Object> queryUser) {
        return userService.getUserList(queryUser);
    }

    @PostMapping("/updateStatus")
    public Map<String, Object> updateStatus(Integer id, Integer status) {
        return userService.updateStatus(id, status);
    }

    @PostMapping("/resetPwd")
    public Map<String, Object> resetPassword(Integer id) {
        return userService.resetPassword(id);
    }

    @PostMapping("/add")
    public Map<String, Object> addUser(User user) {
        return userService.addUser(user);
    }

    @PostMapping("/update")
    public Map<String, Object> updateUser(User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/visit")
    public Map<String, Object> visitUser(Integer id) {
        return userService.visitUser(id);
    }

    @GetMapping("/is_follow")
    public boolean isFollow(Integer id) {
        return userService.isFollow(id);
    }

    @GetMapping("/follow")
    public Map<String, Object> getUserFollow(Integer userId) {
        return userService.getUserFollow(userId);
    }

    @PostMapping("/follow")
    public Map<String, Object> followUser(Integer id) {
        return userService.followUser(id);
    }

    @DeleteMapping("/follow")
    public Map<String, Object> cancelFollowUser(Integer id) {
        return userService.cancelFollowUser(id);
    }
}
