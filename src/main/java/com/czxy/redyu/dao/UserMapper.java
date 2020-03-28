package com.czxy.redyu.dao;

import com.czxy.redyu.model.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;
/**
 * userMapper
 */
@org.apache.ibatis.annotations.Mapper
public interface UserMapper extends Mapper<User> {

    @Select("select * from user where username=#{username}")
    User findUserByUsername(@Param("username") String username);

    @Select("select * from user where email = #{email}")
    User findUserByEmail(@Param("email") String email);
}