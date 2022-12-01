package com.lierlier.backend;

import com.lierlier.backend.mapper.UserMapper;
import com.lierlier.backend.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class BackendApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        for (int i = 1; i <= 4; i++) {
            User user = userMapper.selectById(i);
            user.setCreateTime(new Date());
            userMapper.updateById(user);
        }
    }

}
