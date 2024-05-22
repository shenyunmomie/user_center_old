package com.swshenyun.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.swshenyun.usercenter.common.BaseResponse;
import com.swshenyun.usercenter.common.ErrorCode;
import com.swshenyun.usercenter.common.ResultUtils;
import com.swshenyun.usercenter.contant.UserConstant;
import com.swshenyun.usercenter.exception.BusinessException;
import com.swshenyun.usercenter.pojo.dto.UserDTO;
import com.swshenyun.usercenter.pojo.dto.UserLoginDTO;
import com.swshenyun.usercenter.pojo.dto.UserRegisterDTO;
import com.swshenyun.usercenter.pojo.entity.User;
import com.swshenyun.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
/**
 * 用户接口
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterDTO
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterDTO userRegisterDTO) {
        // 校验
        if (userRegisterDTO == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String username = userRegisterDTO.getUsername();
        String password = userRegisterDTO.getPassword();
        String checkPassword = userRegisterDTO.getCheckPassword();
        if (StringUtils.isAnyBlank(username, password, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.register(username, password, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginDTO
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request) {
        if (userLoginDTO == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();
        if (StringUtils.isAnyBlank(username, password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.login(username, password, request);
        return ResultUtils.success(user);
    }

    /**
     * 用户退出登陆
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前用户
     *
     * @param request
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        long userId = currentUser.getId();
        //校验用户是否合法

        User user = userService.getById(userId);
        User safetyUser = userService.getSafeUser(user);
        return ResultUtils.success(safetyUser);
    }

    /**
     * 管理员查询
     * @param username
     * @param request
     * @return
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request){
        //是否为管理员
        if(!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH,"缺少管理员权限");
        }
        QueryWrapper<User> query = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
            query.like("username",username);
        }
        //查询
        List<User> users = userService.list(query);
        //脱敏
        List<User> list = users.stream().map(user -> userService.getSafeUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    /**
     * 管理员删除
     * @param id
     * @return
     */
    @DeleteMapping
    public BaseResponse<Boolean> delete(Long id, HttpServletRequest request) {
        //是否为管理员
        if(!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH,"缺少管理员权限");
        }
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //TODO 检测用户是否处于登陆状态，强制退出

        boolean result = userService.removeById(id);
        return ResultUtils.success(result);
    }

    /**
     * 是否为管理员
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        //获取登陆用户
        User user = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        //鉴权-管理员
        return user != null && user.getUserRole() == UserConstant.ADMIN_ROLE;
    }

    @PutMapping
    public BaseResponse<Boolean> update(@RequestBody UserDTO userDTO) {
        if (userDTO == null || userDTO.getId() == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        if (userDTO.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        boolean result = userService.update(user);
        return ResultUtils.success(result);
    }
}
