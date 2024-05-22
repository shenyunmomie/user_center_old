package com.swshenyun.usercenter.service;
import java.util.List;

import com.swshenyun.usercenter.pojo.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import javax.annotation.Resource;

@SpringBootTest
public class UserServiceImpltest {

    @Resource
    private UserService userService;

    @Test
    void testAddUser() {
        User user = new User();
//        user.setId(0L);
        user.setAccount("1027669175");
        user.setUsername("swshenyun");
        user.setPassword("123456");
        user.setAvatarUrl("");
        user.setGender(1);
        user.setPhone("18732566535");
        user.setEmail("18731548870@163.com");
//        user.setCreateTime();
//        user.setUpdateTime();
        user.setStatus(0);
        user.setDeleted(0);
        userService.save(user);
        List<User> list = userService.list();
        System.out.println(list);
        Assertions.assertEquals(list.size(), 1);
    }

    @Test
    void register() {
        String account = "symm";
        String password = "12345678sw";
        String checkPwd = "12345678sw";
        Long result = userService.register(account, password, checkPwd);
        Assertions.assertTrue(result > 0);
        account = "sy";
        result = userService.register(account, password, checkPwd);
        Assertions.assertEquals(-1L, result);
        account = "";
        result = userService.register(account, password, checkPwd);
        Assertions.assertEquals(-1L, result);
        account = null;
        result = userService.register(account, password, checkPwd);
        Assertions.assertEquals(-1L, result);

        account = "symm";
        password = "123456789sw";
        result = userService.register(account, password, checkPwd);
        Assertions.assertEquals(-1L, result);

        password = "123456??sw";
        checkPwd = "123456??sw";
        result = userService.register(account, password, checkPwd);
        Assertions.assertEquals(-1L, result);

        password = "123456";
        checkPwd = "123456";
        result = userService.register(account, password, checkPwd);
        Assertions.assertEquals(-1L, result);

        password = "12345678sw";
        checkPwd = "12345678sw";
        result = userService.register(account, password, checkPwd);
        Assertions.assertEquals(-1L, result);
    }
}
