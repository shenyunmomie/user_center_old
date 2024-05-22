package com.swshenyun.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swshenyun.usercenter.pojo.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
* @author 神殒魔灭
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-05-04 17:15:35
*/
public interface UserService extends IService<User> {

    Long register(String username, String password, String checkPwd);

    User login(String username, String password, HttpServletRequest request);

    int userLogout(HttpServletRequest request);

    /**
     * 用户脱敏
     *
     * @param user
     * @return
     */
    User getSafeUser(User user);

    Boolean update(User user);
}
