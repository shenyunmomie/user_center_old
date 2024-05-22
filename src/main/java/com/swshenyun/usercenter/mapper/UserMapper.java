package com.swshenyun.usercenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swshenyun.usercenter.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 神殒魔灭
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2024-05-04 17:15:35
* @Entity com.swshenyun.usercenter.pojo.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




