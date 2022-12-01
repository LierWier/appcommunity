package com.lierlier.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lierlier.backend.mapper.UserMapper;
import com.lierlier.backend.pojo.User;
import com.lierlier.backend.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommonServiceImpl implements CommonService {
    @Autowired
    private UserMapper userMapper;

}