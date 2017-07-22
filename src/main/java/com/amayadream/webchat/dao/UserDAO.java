package com.amayadream.webchat.dao;

import com.amayadream.webchat.pojo.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface UserDAO {

    String TABLE_NAME = "ow_user";
    String SELECT_COLUMN = "userId,userName,password,nickName,sex,age,avatarUrl,profile,createTime,lastTime,status,level";
    String INSERT_COLUMN = "userName,password,nickName,sex,age,avatarUrl,profile,createTime,lastTime,status,level";
    String INSERT_VALUE = "#{entity.userName},#{entity.password},#{entity.nickName},#{entity.sex},#{entity.age},#{entity.avatarUrl},#{entity.profile},NOW(),NOW(),1,1";

    @Select({"<script>" +
            "select ",SELECT_COLUMN," from ",TABLE_NAME," where userId = #{userId}",
            "</script>"})
    User findByUserId(@Param("userId") Integer userId);

    @Select({"<script>" +
            "select ",SELECT_COLUMN," from ",TABLE_NAME," where userName = #{userName}",
            "</script>"})
    User findByUserName(@Param("userName") String userName);

    @Insert({"insert into ",TABLE_NAME," (",INSERT_COLUMN,") values(",INSERT_VALUE,")"})
    @Options(useGeneratedKeys = true,keyProperty = "entity.userId")
    int insert(@Param("entity") User user);

    @Update({"update ",TABLE_NAME," set nickName= #{entity.nickName},avatarUrl = #{entity.avatarUrl},sex = #{entity.sex},age = #{entity.age},profile = #{entity.profile} where userId = #{entity.userId}"})
    int update(@Param("entity")User user);

    @Update({"update ",TABLE_NAME," set lastTime= #{lastTime} where userId = #{userId}"})
    int updateLoginTime(@Param("lastTime")Date lastTime ,@Param("userId") Integer userId);

    @Update({"update ",TABLE_NAME," set password= #{entity.password}  where userId = #{entity.userId}"})
    int updatePassword(@Param("entity")User user);

    @Update({"update ",TABLE_NAME," set avatarUrl= #{entity.avatarUrl}  where userId = #{userId}"})
    int uploadAvatar(@Param("avatarUrl")String avatarUrl,@Param("userId") Integer userId);
}
