package com.amayadream.webchat.service;

import com.amayadream.webchat.dao.GroupDAO;
import com.amayadream.webchat.pojo.Group;
import com.amayadream.webchat.pojo.GroupMember;
import com.amayadream.webchat.utils.Result;
import com.amayadream.webchat.utils.ResultDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupDAO groupDAO;

    public ResultDTO<List<Group>> queryAllGroup() {
        return ResultDTO.successWith(groupDAO.findAllGroup());
    }

    public ResultDTO addGroup(Group group) {
        try {
            if (group == null || StringUtils.isEmpty(group.getGroupName()))
                return ResultDTO.fail("参数错误");
            int i = groupDAO.insertGroup(group);
            if (i == 1)
                return ResultDTO.success();
            else
                return ResultDTO.fail("保存失败");

        } catch (Exception e) {
            e.printStackTrace();
            return ResultDTO.fail("保存异常："+e.getMessage());
        }
    }

    public ResultDTO addGroupMember(GroupMember groupMember){
        try {
            if (groupMember == null || groupMember.getGroupId() == null || groupMember.getUserId() == null)
                return ResultDTO.fail("参数错误");
            int i = groupDAO.insertGroupMember(groupMember);
            if (i == 1)
                return ResultDTO.success();
            else
                return ResultDTO.fail("保存失败");

        } catch (Exception e) {
            e.printStackTrace();
            return ResultDTO.fail("保存异常："+e.getMessage());
        }
    }
}
