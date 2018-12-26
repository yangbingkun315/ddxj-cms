package net.zn.ddxj.mapper.manager;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.manager.ManagerRole;

public interface ManagerRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ManagerRole record);

    int insertSelective(ManagerRole record);

    ManagerRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ManagerRole record);

    int updateByPrimaryKey(ManagerRole record);
    
    ManagerRole queryRole(Integer roleId);
    
    List<ManagerRole> queryRoleList(@Param("roleName")String roleName);
    
    int updateRoleMenu(@Param("roleId")Integer roleId, @Param("menuId")String menuId);
    
    int deleteRoleMenu(Integer roleId);
}