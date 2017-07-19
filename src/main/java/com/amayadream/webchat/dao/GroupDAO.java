package com.amayadream.webchat.dao;

import com.amayadream.webchat.pojo.Group;
import com.amayadream.webchat.pojo.GroupMember;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface GroupDAO {

    @Select({" select * from ow_group"})
    List<Group> findAllGroup();

    @Insert({"insert into ow_group (groupName) values(#{group.groupName})"})
    @Options(useCache = false,keyProperty = "group.groupId")
    int insertGroup(@Param("group") Group group);

    @Insert({"insert into ow_group_member (userId,groupId) VALUES(#{entity.userId},#{entity.groupId})"})
    @Options(useCache = false,keyProperty = "entity.id")
    int insertGroupMember(@Param("entity")GroupMember groupMember);
}
