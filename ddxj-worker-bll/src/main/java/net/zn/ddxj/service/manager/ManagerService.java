package net.zn.ddxj.service.manager;

import java.util.List;

import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.entity.manager.ManagerMenu;
import net.zn.ddxj.entity.manager.ManagerRole;
import net.zn.ddxj.entity.manager.ManagerUser;

public interface ManagerService {
	
	ManagerUser queryUserInfo(String userName);
	ManagerUser queryUserDetails(Integer id);
	List<ManagerMenu> queryUserMenu(Integer roleId);
	List<ManagerMenu> queryUserMenuAll(String menuName);
	List<ManagerUser> queryUserList(String realName);
	List<ManagerRole> queryRoleList(String roleName);
	int addUser(ManagerUser user);
	int updateUser(ManagerUser user);
	int deleteUser(Integer id);
	void updateRoleMenu(Integer roleId,String menuIds);
	int deleteRole(Integer roleId);
	ResponseBase updateRole(Integer roleId,String roleName,String roleDesc);
	ManagerRole queryRoleById(Integer roleId);
	int querySettingRole(Integer roleId);
	List<ManagerMenu> queryMenuParent();
	ResponseBase updateMenu(Integer id,Integer pid,String name,String url,String icon,Integer sort);
	ManagerMenu queryMenuById(Integer menuId);
	int deleteMenu(Integer menuId);
	int querySettingMenu(Integer menuId);
}