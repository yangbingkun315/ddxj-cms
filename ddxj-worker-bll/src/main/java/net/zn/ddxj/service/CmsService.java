package net.zn.ddxj.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import net.zn.ddxj.entity.CmsLogs;
import net.zn.ddxj.entity.CmsResource;
import net.zn.ddxj.entity.CmsRole;
import net.zn.ddxj.entity.CmsStorage;
import net.zn.ddxj.entity.CmsUser;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.ZtreeView;

public interface CmsService {

	CmsUser findByUserName(String userName);
	List<CmsUser> findUserList(CmsRequestVo requestVo);
	List<CmsRole> findRoleList(CmsRequestVo requestVo);
	List<CmsResource> findResourceList(CmsRequestVo requestVo);
	List<CmsResource> findParentResourceList();
	List<CmsResource> findMenuResourceList();
	int updateRole(CmsRole role);
	CmsRole findRoleById(Integer roleId);
	int deleteRole(Integer id);
	List<ZtreeView> findRoleResourceList(Integer roleId);
	void updateRoleResource(Integer roleId,String resourceIds);
	CmsResource findResourceById(Integer resourceId);
	
	CmsUser queryUserById(Integer userId);//根据userId查询用户信息
	
	int updateCmsUser(CmsUser user);//修改用户
	
	int insertCmsUser(CmsUser user);//添加用户
	
	int deleteCmsUser(Integer id);//删除用户
	
	void addOperateLogs(String idAddress,String content,Integer userId);
	
	List<CmsLogs> findLogsByUserId(CmsRequestVo requestVo);
	
	int updateResource(CmsRequestVo requestVo);
	
	int deleteResource(Integer id);
	int changeUserPassword(int id, String newPassword);
	List<CmsResource> findResourceBtnGroup(Integer parentMenuId);
	
	int addAllQiNiuStorage();
	
	List<CmsStorage> queryAllStorage(CmsRequestVo requestVo);
	
	int deleteCmsStorage(CmsRequestVo requestVo);
	
	int addStorate(CmsStorage cmsStorage);
	
	List<Map<String,Object>> findRecruitCount();
	
	List<Map<String,Object>> findUserCount();
	
	CmsUser findCmsUserByUserName(String userName);
	
	BigDecimal findRegisterAwardMoneyCount();
	BigDecimal findWithdrawSuccessCount();
	int findRealAuthCount();
	int findCircleCount();
}
