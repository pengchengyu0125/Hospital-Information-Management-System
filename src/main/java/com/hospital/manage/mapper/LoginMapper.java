package com.hospital.manage.mapper;

import com.hospital.manage.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface LoginMapper {

    //判断用户名密码正确
    @Select("select password from user where user_name=#{username}")
    String loginVerify(@Param(value = "username") String username);

    //查找用户信息
    @Select("select a.*,b.hospital_name,b.icon_url from user as a left join database_source as b on a.hospital_code = b.hospital_code where a.token=#{token}")
    User findUser(@Param(value = "token") String token);

    //更新用户token
    @Update("update user set token=#{token} where user_name=#{username}")
    void updateToken(@Param(value = "username") String username, @Param(value = "token") String myToken);

    //获取医院编码
    @Select("SELECT hospital_code FROM user WHERE user_name = #{username}")
    String getDatabase(@Param(value = "username") String username);

    //判断用户手机号是否匹配
    @Select("select count(*) from user where user_name=#{username} and phone=#{phone}")
    int judge(@Param(value = "username") String username, @Param(value = "phone") String phone);

    //更新用户密码
    @Update("update user set password=#{password} where user_name=#{username} and phone=#{phone}")
    void updatePassword(@Param(value = "username") String username, @Param(value = "phone") String phone, @Param(value = "password") String password);
}
