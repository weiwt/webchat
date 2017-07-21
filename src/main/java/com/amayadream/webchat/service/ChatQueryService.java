package com.amayadream.webchat.service;

import com.amayadream.webchat.dao.ChatDAO;
import com.amayadream.webchat.pojo.Chat;
import com.amayadream.webchat.utils.ResultDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatQueryService {

    @Autowired
    private ChatDAO chatDAO;

    public ResultDTO<List<Chat>> queryChatByUserId(Integer userId){
        if (userId == null)
            return ResultDTO.fail("参数错误");
        List<Chat> chats = chatDAO.queryChatsHistory(userId);
        return ResultDTO.successWith(chats);
    }

     public ResultDTO addChats(Chat chat){
        try{
            if (chat == null)
                return ResultDTO.fail("入参为空");
            if (chat.getFromId() == null)
                return ResultDTO.fail("用户标识为空");
            if (chat.getToId() == null)
                return ResultDTO.fail("发送标示为空");
            if (chat.getSubject() == null)
                return ResultDTO.fail("消息类型为空为空");
            if (StringUtils.isEmpty(chat.getMessage()))
                return ResultDTO.fail("消息体为空");

            int insert = chatDAO.insert(chat);
            if (insert != 1)
                return ResultDTO.fail("插入消息记录失败");
            return ResultDTO.success();
        }catch (Exception e){
            e.printStackTrace();
            return ResultDTO.fail("插入消息记录异常："+e.getMessage());
        }
     }
}
