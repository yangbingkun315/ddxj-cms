package net.zn.ddxj.mapper.manager;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.manager.ManagerMenu;

public interface ManagerMenuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ManagerMenu record);

    int insertSelective(ManagerMenu record);

    ManagerMenu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ManagerMenu record);

    int updateByPrimaryKey(ManagerMenu record);
    
    List<ManagerMenu> queryUserMenu(Integer roleId);
    
    List<ManagerMenu> queryUserMenuAll(@Param("menuName")String menuName);
    
    ManagerMenu queryParentMenuById(Integer pid);
    
    List<ManagerMenu> queryParentMenu();
    
    int querySettingMenu(@Param("menuId")Integer menuId);
}