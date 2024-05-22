package com.swshenyun.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.swshenyun.usercenter.common.ErrorCode;
import com.swshenyun.usercenter.contant.UserConstant;
import com.swshenyun.usercenter.exception.BusinessException;
import com.swshenyun.usercenter.mapper.UserMapper;
import com.swshenyun.usercenter.pojo.entity.User;
import com.swshenyun.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;


/**
* @author 神殒魔灭
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-05-04 17:15:35
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    /**
     * 密码盐值
     */
    private static final String SALT = "symm";

    /**
     * 用户注册
     * @param username 账户
     * @param password 密码
     * @param checkPwd 再次确认
     * @return id 用户
     */
    public Long register(String username, String password, String checkPwd) {
        // 1.校验
        if (!password.equals(checkPwd)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次密码输入不一致");
        }

        checkUser(username, password);

        //username不能重复
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .eq("username",username);
        int count = this.count(wrapper);
        if (count>0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户已存在");
        }

        // 2.md5加密
        String md5Password = DigestUtils.md5DigestAsHex((SALT + password).getBytes());

        // 3.插入
        User user = new User();
        user.setUsername(username);
        user.setPassword(md5Password);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"用户名插入未成功");
        }
        return user.getId();
    }

    @Override
    public User login(String username, String password, HttpServletRequest request) {
        // 1.校验
        if (StringUtils.isAnyBlank(username,password)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        checkUser(username, password);

        // 2.查询
        String md5Password = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
               .eq("username",username)
               .eq("password",md5Password);
        User user = this.getOne(wrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "未知账户名密码");
        }
        // 3.脱敏
        User safeUser = getSafeUser(user);

        // 4.记录登录状态session
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE,safeUser);
        return safeUser;
    }

    /**
     * 校验用户名密码
     * @param username
     * @param password
     * @return
     */
    public Boolean checkUser(String username, String password) {
        if (username.length()<4 || password.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"长度校验未通过");
        }

        final String REGEX_USERNAME = "^[a-zA-Z0-9]{4,20}$";
        if (!username.matches(REGEX_USERNAME)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名校验未通过");
        }

        final String REGEX_PASSWORD = "^(?!\\d+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
        if (!password.matches(REGEX_PASSWORD)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码校验未通过");
        }
        return true;
    }


    /**
     * 脱敏
     * @param user
     * @return
     */
    @Override
    public User getSafeUser(User user) {
        if (user == null) {
            return null;
        }
        User safeUser = new User();
        safeUser.setId(user.getId());
        safeUser.setUsername(user.getUsername());
        safeUser.setAvatarUrl(user.getAvatarUrl());
        safeUser.setGender(user.getGender());
        safeUser.setPhone(user.getPhone());
        safeUser.setEmail(user.getEmail());
        safeUser.setCreateTime(user.getCreateTime());
        safeUser.setUpdateTime(user.getUpdateTime());
        safeUser.setStatus(user.getStatus());
        safeUser.setUserRole(user.getUserRole());
        return safeUser;
    }

    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        // 移除登录态
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return 1;
    }

    /**
     * 用户修改
     * @param user
     * @return
     */
    @Override
    public Boolean update(User user) {
        String username = user.getUsername();
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .eq("username",username);
        int count = this.count(wrapper);
        if (count>0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名已存在");
        }

        return this.updateById(user);
    }
}




