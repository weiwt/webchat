package com.amayadream.webchat.dao;

import com.amayadream.webchat.pojo.Chat;
import javafx.scene.control.Tab;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatDAO {

    String TABLE_NAME = "ow_chats";
    /**
     * 查询与某一个好友的聊天记录
     * @param userId
     * @return
     */
    @Select({"select * from ",TABLE_NAME," where userId = #{userId} and type = 0"})
    List<Chat> queryChatsHistory(@Param("userId") Integer userId);

    /**
     * 查询聊天记录的列表
     * @return
     */
    @Select({
            "select DISTINCT * from ",TABLE_NAME," GROUP BY userId ORDER BY updateTime desc LIMIT 10"
    })
    List<Chat> queryChatsHistoryList(@Param("pageSize") Integer pageSize);


    @Insert({"insert into " , TABLE_NAME," (fromId,toId,message,subject,type) values(#{chat.fromId},#{chat.toId},#{chat.message},#{chat.subject},#{chat.type})"})
    int insert(@Param("chat") Chat chat);
}
