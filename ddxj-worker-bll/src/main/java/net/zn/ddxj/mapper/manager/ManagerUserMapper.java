package net.zn.ddxj.mapper.manager;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.manager.ManagerUser;

public interface ManagerUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ManagerUser record);

    int insertSelective(ManagerUser record);

    ManagerUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ManagerUser record);

    int updateByPrimaryKey(ManagerUser record);
    
    ManagerUser queryUserInfo(String userName);
    
    List<ManagerUser> queryUserList(@Param("realName")String realName);
    
    int querySettingRole(@Param("roleId")Integer roleId);
}