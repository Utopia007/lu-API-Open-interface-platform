package com.luyouxiao.project.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 用户服务测试
 *
 * @author yupi
 */
@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Test
    void testAddUser() {
        userInterfaceInfoService.invokeCount(6, 1);
    }

}