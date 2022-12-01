package com.lierlier.backend.controller;

import com.lierlier.backend.pojo.User;
import com.lierlier.backend.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/common")
public class CommonController {
    @Autowired
    private CommonService commonService;

}
