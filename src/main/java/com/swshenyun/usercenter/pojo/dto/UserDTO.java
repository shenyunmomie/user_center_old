package com.swshenyun.usercenter.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 头像图片地址
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 电子邮箱
     */
    private String email;

}
