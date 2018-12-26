package net.zn.ddxj.service.manager.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.manager.ManagerMenu;
import net.zn.ddxj.entity.manager.ManagerRole;
import net.zn.ddxj.entity.manager.ManagerUser;
import net.zn.ddxj.mapper.manager.ManagerMenuMapper;
import net.zn.ddxj.mapper.manager.ManagerRoleMapper;
import net.zn.ddxj.mapper.manager.ManagerUserMapper;
import net.zn.ddxj.service.manager.ManagerService;
import net.zn.ddxj.utils.CmsUtils;

@Service
@Component
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
public class ManagerServiceImpl implements ManagerService{

	@Autowired
	private ManagerUserMapper managerUserMapper;
	@Autowired
	private ManagerMenuMapper managerMenuMapper;
	@Autowired
	private ManagerRoleMapper managerRoleMapper;
	@Override
	public ManagerUser queryUserInfo(String userName) {
		// TODO Auto-generated method stub
		return managerUserMapper.queryUserInfo(userName);
	}
	@Override
	public List<ManagerMenu> queryUserMenu(Integer roleId) {
		// TODO Auto-generated method stub
		return managerMenuMapper.queryUserMenu(roleId);
	}
	@Override
	public List<ManagerMenu> queryUserMenuAll(String menuName) {
		// TODO Auto-generated method stub
		return managerMenuMapper.queryUserMenuAll(menuName);
	}
	@Override
	public List<ManagerUser> queryUserList(String realName) {
		// TODO Auto-generated method stub
		return managerUserMapper.queryUserList(realName);
	}
	@Override
	public List<ManagerRole> queryRoleList(String roleName) {
		// TODO Auto-generated method stub
		return managerRoleMapper.queryRoleList(roleName);
	}
	@Override
	public int addUser(ManagerUser user) {
		// TODO Auto-generated method stub
		return managerUserMapper.insertSelective(user);
	}
	@Override
	public int updateUser(ManagerUser user) {
		// TODO Auto-generated method stub
		return managerUserMapper.updateByPrimaryKeySelective(user);
	}
	@Override
	public ManagerUser queryUserDetails(Integer id) {
		// TODO Auto-generated method stub
		return managerUserMapper.selectByPrimaryKey(id);
	}
	@Override
	public int deleteUser(Integer id) {
		// TODO Auto-generated method stub
		return managerUserMapper.deleteByPrimaryKey(id);
	}
	@Override
	public void updateRoleMenu(Integer roleId, String menuIds) {
		
		managerRoleMapper.deleteRoleMenu(roleId);
		if(menuIds.indexOf(",") > -1)
		{
			String[] menuId = menuIds.split(",");
			for(int d = 0;d<menuId.length;d++)
			{
				managerRoleMapper.updateRoleMenu(roleId,menuId[d]);
			}
		}
		else
		{
			managerRoleMapper.updateRoleMenu(roleId,menuIds);
		}
	}
	@Override
	public int deleteRole(Integer roleId) {
		// TODO Auto-generated method stub
		return managerRoleMapper.deleteByPrimaryKey(roleId);
	}
	@Override
	public ResponseBase updateRole(Integer roleId, String roleName, String roleDesc) {
		ResponseBase result = ResponseBase.getInitResponse();
		ManagerRole  managerRole = null;
		if(!CmsUtils.isNullOrEmpty(roleId))
		{
			managerRole = managerRoleMapper.selectByPrimaryKey(roleId);
			managerRole.setRoleDesc(roleDesc);
			managerRole.setRoleName(roleName);
			managerRoleMapper.updateByPrimaryKey(managerRole);
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("更新成功");
		}
		else
		{
			managerRole = new ManagerRole();
			managerRole.setRoleName(roleName);
			managerRole.setRoleDesc(roleDesc);
			managerRole.setCreateTime(new Date());
			managerRoleMapper.insertSelective(managerRole);
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("添加成功");
		}
		return result;
	}
	@Override
	public ManagerRole queryRoleById(Integer roleId) {
		// TODO Auto-generated method stub
		return managerRoleMapper.selectByPrimaryKey(roleId);
	}
	@Override
	public int querySettingRole(Integer roleId) {
		// TODO Auto-generated method stub
		return managerUserMapper.querySettingRole(roleId);
	}
	@Override
	public List<ManagerMenu> queryMenuParent() {
		// TODO Auto-generated method stub
		return managerMenuMapper.queryParentMenu();
	}
	@Override
	public ResponseBase updateMenu(Integer id,Integer pid, String name, String url, String icon, Integer sort) {
		ResponseBase result = ResponseBase.getInitResponse();
		ManagerMenu  managerMenu = null;
		if(!CmsUtils.isNullOrEmpty(id))
		{
			managerMenu = managerMenuMapper.selectByPrimaryKey(id);
			managerMenu.setPid(pid);
			managerMenu.setName(name);
			managerMenu.setIcon(icon);
			managerMenu.setSort(sort);
			managerMenu.setUrl(url);
			managerMenu.setUpdateTime(new Date());
			managerMenuMapper.updateByPrimaryKeySelective(managerMenu);
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("更新成功");
		}
		else
		{
			managerMenu = new ManagerMenu();
			managerMenu.setPid(pid);
			managerMenu.setName(name);
			managerMenu.setIcon(icon);
			managerMenu.setSort(sort);
			managerMenu.setUrl(url);
			managerMenu.setCreateTime(new Date());
			managerMenu.setUpdateTime(new Date());
			managerMenuMapper.insertSelective(managerMenu);
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("添加成功");
		}
		return result;
	}
	@Override
	public ManagerMenu queryMenuById(Integer menuId) {
		// TODO Auto-generated method stub
		return managerMenuMapper.selectByPrimaryKey(menuId);
	}
	@Override
	public int deleteMenu(Integer menuId) {
		// TODO Auto-generated method stub
		return managerMenuMapper.deleteByPrimaryKey(menuId);
	}
	@Override
	public int querySettingMenu(Integer menuId) {
		// TODO Auto-generated method stub
		return managerMenuMapper.querySettingMenu(menuId);
	}

}
