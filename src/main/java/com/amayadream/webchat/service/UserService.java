package com.amayadream.webchat.service;

import com.amayadream.webchat.dao.UserDAO;
import com.amayadream.webchat.pojo.GroupMember;
import com.amayadream.webchat.pojo.User;
import com.amayadream.webchat.utils.ResultDTO;
import com.amayadream.webchat.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private GroupService groupService;
    public ResultDTO<User> queryUser(Integer userId){
        if (userId == null)
            return ResultDTO.fail("参数为空");

        User user = userDAO.findByUserId(userId);
        return ResultDTO.successWith(user);
    }

    public ResultDTO<User> queryUserByName(String userName){
        if (StringUtils.isEmpty(userName))
            return ResultDTO.fail("参数为空");

        User user = userDAO.findByUserName(userName);
        return ResultDTO.successWith(user);
    }

    public ResultDTO addUser(User user,Integer groupId){
        if (user == null || StringUtils.isEmpty(user.getUserName()))
            return ResultDTO.fail("参数错误");

        try{
            User byUserName = userDAO.findByUserName(user.getUserName());
            if (byUserName != null)
                return ResultDTO.fail("该用户已经存在");
            int insert = userDAO.insert(user);
            if (insert == 1){
                if (groupId == null)
                    return ResultDTO.success();
                ResultDTO resultDTO = groupService.addGroupMember(GroupMember.builder()
                        .groupId(groupId)
                        .userId(user.getUserId())
                        .build());
                if (resultDTO == null)
                    return ResultDTO.fail(resultDTO.getErrorMsg());

                return ResultDTO.success();
            }else{
                return ResultDTO.fail("添加失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultDTO.fail("添加异常："+e.getMessage());
        }
    }

    public ResultDTO editUser(User user){
        try {
            int update = userDAO.update(user);
            if (update == 1)
                return ResultDTO.success();
            else
                return ResultDTO.fail("更新失败");
        }catch (Exception e){
            e.printStackTrace();
            return ResultDTO.fail("更新异常："+e.getMessage());
        }
    }

    public ResultDTO updateLoginTime(Integer userId){
        userDAO.updateLoginTime(new Date(),userId);
        return ResultDTO.success();
    }

    public ResultDTO resetPassword(User user){
        User byUserId = userDAO.findByUserId(user.getUserId());
        if (byUserId == null)
            return ResultDTO.fail("该用户为空");

        int i = userDAO.updatePassword(user);

        return ResultDTO.success();
    }
}
