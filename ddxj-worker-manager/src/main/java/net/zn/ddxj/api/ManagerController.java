package net.zn.ddxj.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.AdvertBanner;
import net.zn.ddxj.entity.Category;
import net.zn.ddxj.entity.Information;
import net.zn.ddxj.entity.InformationCategory;
import net.zn.ddxj.entity.InformationType;
import net.zn.ddxj.entity.Message;
import net.zn.ddxj.entity.ProblemLib;
import net.zn.ddxj.entity.UserBank;
import net.zn.ddxj.entity.WechatUser;
import net.zn.ddxj.entity.manager.ManagerMenu;
import net.zn.ddxj.entity.manager.ManagerRole;
import net.zn.ddxj.entity.manager.ManagerUser;
import net.zn.ddxj.service.AdvertBannerService;
import net.zn.ddxj.service.CategoryService;
import net.zn.ddxj.service.InformationService;
import net.zn.ddxj.service.ProblemLibService;
import net.zn.ddxj.service.UserService;
import net.zn.ddxj.service.manager.ManagerService;
import net.zn.ddxj.tool.MessageService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.DateUtils;
import net.zn.ddxj.utils.PropertiesUtils;
import net.zn.ddxj.utils.QiNiuUploadManager;
import net.zn.ddxj.utils.QiNiuUtil;
import net.zn.ddxj.utils.RedisUtils;
import net.zn.ddxj.utils.json.JsonUtil;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

import org.apache.commons.io.FileUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiniu.common.QiniuException;

@RestController
@Slf4j
/**
 * 系统管理
* @ClassName: ManagerController   
* @author 上海众宁网络科技有限公司-何俊辉
* @date 2018年4月29日  
*
 */
public class ManagerController {
	@Autowired
	public RedisUtils redisUtils;
	@Autowired
	private ManagerService managerService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private AdvertBannerService advertBannerService;
	@Autowired
	private InformationService informationService;
	@Autowired
	private ProblemLibService problemLibService;
	@Autowired
	private UserService userService;
	/**
	 * 登录
	* @Title: login  
	* @param @param request
	* @param @param response
	* @param @param userName
	* @param @param userPassword
	* @param @return    参数  
	* @return ResponseBase    返回类型  
	* @author 上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/manager/login.ddxj")
	public ResponseBase login(HttpServletRequest request, HttpServletResponse response, String userName,String userPassword) 
	{
		ResponseBase result = ResponseBase.getInitResponse();
		
		ManagerUser userInfo = managerService.queryUserInfo(userName);
		if(!CmsUtils.isNullOrEmpty(userInfo))
		{
			if(userInfo.getFlag() == 2)
			{
				result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg("账号被冻结，请联系管理员");
				return result;
			}
			if(userPassword.equals(userInfo.getUserPassword()))
			{
				redisUtils.set("managerUserInfo#"+userInfo.getUserName(), JsonUtil.bean2jsonToString(userInfo));
				result.setResponse(Constants.TRUE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg("登录成功");
			}
			else
			{
				result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg("密码不正确，请重新输入");
			}
		}
		else
		{
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("用户名不存在，请重新输入");
		}
		return result;
	}
	
	/**
     * 首页
     * 
     * @param request
     * @param response
     * @author fancunxin
	 * @throws IllegalAccessException 
     * @throws ClientException
     */
    @RequestMapping(value="/manager/index.ddxj")
    public ResponseBase userIndex(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	
    	Integer count = userService.queryTodayUserCount(2);
    	result.push("todayCount", count);
    	
    	Integer totalCount = userService.queryTodayUserCount(1);
    	result.push("totalCount", totalCount);
    	
    	Integer massageCount = messageService.queryMessageTotalCount(0);//总数
    	result.push("massageCount", massageCount);
    	
    	Integer massageTrue = messageService.queryMessageTotalCount(1);//1-未读
    	result.push("massageTrue", massageTrue);
    	
    	Integer massageFalse = messageService.queryMessageTotalCount(2);//2-已读
    	result.push("massageFalse", massageFalse);
    	
    	List<Map<String, Object>> dataList = userService.queryMonthEveryDay();
    	result.push("dataList", dataList);
    	
    	Integer worker = userService.queryUserByRole(1);//工人
    	result.push("worker", worker);
    	
    	Integer foreman = userService.queryUserByRole(2);//工头
    	result.push("foreman", foreman);
    	
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("查询成功");
    	return result;
    }
	
	/**
	 * 查询所有菜单
	* @Title: managerMenu  
	* @param @param request
	* @param @param response
	* @param @param userName
	* @param @return
	* @param @throws IllegalAccessException    参数  
	* @return ResponseBase    返回类型  
	* @author 上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/manager/menu.ddxj")
	public ResponseBase managerMenu(HttpServletRequest request, HttpServletResponse response, String userName) throws IllegalAccessException 
	{
		ResponseBase result = ResponseBase.getInitResponse();
		String valueOf = String.valueOf(redisUtils.get("managerUserInfo#"+userName));
		if(!CmsUtils.isNullOrEmpty(valueOf))
		{
			JSONObject user = (JSONObject) JSONObject.parse(valueOf);
			List<ManagerMenu> queryUserMenu = managerService.queryUserMenu(user.getInteger("roleId"));
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("success");
			result.push("menuList", queryUserMenu);
		}
	    else
	    {
	    	result.setResponse(Constants.TRUE);
	    	result.setResponseCode(Constants.SUCCESS_200);
	    	result.setResponseMsg("success");
	    	result.push("menuList", null);
	    }
		return result;
	}
	/**
	 * 查询用户列表
	* @Title: managerUser  
	* @param @param request
	* @param @param response
	* @param @param realName
	* @param @param pageNum
	* @param @param pageSize
	* @param @return
	* @param @throws IllegalAccessException    参数  
	* @return ResponseBase    返回类型  
	* @author 上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/manager/user/list.ddxj")
	public ResponseBase managerUser(HttpServletRequest request, HttpServletResponse response, String realName,Integer pageNum,Integer pageSize) throws IllegalAccessException 
	{
		ResponseBase result = ResponseBase.getInitResponse();
		PageHelper.startPage(pageNum, pageSize);
		List<ManagerUser> userList = managerService.queryUserList(realName);
		PageInfo<ManagerUser> pageInfo = new PageInfo<ManagerUser>(userList);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("success");
		result.push("userList", pageInfo);
		return result;
	}
	/**
	 * 查询角色列表
	* @Title: managerRoleList  
	* @param @param request
	* @param @param response
	* @param @return
	* @param @throws IllegalAccessException    参数  
	* @return ResponseBase    返回类型  
	* @author 上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/manager/role/list.ddxj")
	public ResponseBase managerRoleList(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException 
	{
		ResponseBase result = ResponseBase.getInitResponse();
		List<ManagerRole> roleList = managerService.queryRoleList(null);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("success");
		result.push("roleList", JsonUtil.list2jsonToArray(roleList));
		return result;
	}
	/**
	 * 查询用户信息
	* @Title: queryUserDetail  
	* @param @param request
	* @param @param response
	* @param @param id
	* @param @return    参数  
	* @return ResponseBase    返回类型  
	* @author 上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/manager/query/user.ddxj")
	public ResponseBase queryUserDetail(HttpServletRequest request, HttpServletResponse response,Integer id)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		ManagerUser userDetails = managerService.queryUserDetails(id);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("success");
		result.push("user", JsonUtil.bean2jsonObject(userDetails));
		return result;
	}
	/**
	 * 删除用户信息
	* @Title: deleteUser  
	* @param @param request
	* @param @param response
	* @param @param id
	* @param @return    参数  
	* @return ResponseBase    返回类型  
	* @author 上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/manager/delete/user.ddxj")
	public ResponseBase deleteUser(HttpServletRequest request, HttpServletResponse response,Integer id)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		managerService.deleteUser(id);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("删除成功");
		return result;
	}
	/**
	 * 更新用户状态
	* @Title: updateUserStatus  
	* @param @param request
	* @param @param response
	* @param @param id
	* @param @return    参数  
	* @return ResponseBase    返回类型  
	* @author 上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/manager/update/user/status.ddxj")
	public ResponseBase updateUserStatus(HttpServletRequest request, HttpServletResponse response,Integer id)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		ManagerUser userDetails = managerService.queryUserDetails(id);
		if(userDetails.getFlag() == 1)
		{
			userDetails.setFlag(2);
		}
		else if(userDetails.getFlag() == 2)
		{
			userDetails.setFlag(1);
		}
		managerService.updateUser(userDetails);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("更改成功");
		result.push("user", JsonUtil.bean2jsonObject(userDetails));
		return result;
	}
	/**
	 * 更新添加用户信息
	* @Title: updateUser  
	* @param @param request
	* @param @param response
	* @param @param id
	* @param @param realName
	* @param @param userName
	* @param @param userPassword
	* @param @param sex
	* @param @param phone
	* @param @param roleId
	* @param @return    参数  
	* @return ResponseBase    返回类型  
	* @author 上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/update/user.ddxj")
	public ResponseBase updateUser(HttpServletRequest request, HttpServletResponse response,Integer id,String realName,String userName,String userPassword,String sex,String phone,Integer roleId)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		ManagerUser user = null;
		if(!CmsUtils.isNullOrEmpty(id))//更新
		{
			user = managerService.queryUserDetails(id);
			user.setPhone(phone);
			user.setRealName(realName);
			user.setUpdateTime(new Date());
			user.setSex(sex);
			user.setRoleId(roleId);
			user.setUserPassword(userPassword);
			managerService.updateUser(user);
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("更新成功");
		}
		else
		{
			ManagerUser queryUserInfo = managerService.queryUserInfo(userName);
			if(!CmsUtils.isNullOrEmpty(queryUserInfo))
			{
				result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg("用户已被注册，请重新输入用户名");
				return result;
			}
			user = new ManagerUser();
			user.setUserName(userName);
			user.setPhone(phone);
			user.setRealName(realName);
			user.setSex(sex);
			user.setRoleId(roleId);
			user.setUserPassword(userPassword);
			user.setCreateTime(new Date());
			user.setUpdateTime(new Date());
			managerService.addUser(user);
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("添加成功");
		}
		return result;
	}
	
	@RequestMapping(value = "/manager/query/role/page/list.ddxj")
	public ResponseBase queryRolePageList(HttpServletRequest request, HttpServletResponse response, String roleName,Integer pageNum,Integer pageSize) throws IllegalAccessException 
	{
		ResponseBase result = ResponseBase.getInitResponse();
		PageHelper.startPage(pageNum, pageSize);
		List<ManagerRole> roleList = managerService.queryRoleList(roleName);
		PageInfo<ManagerRole> pageInfo = new PageInfo<ManagerRole>(roleList);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("success");
		result.push("roleList", pageInfo);
		return result;
	}
	@RequestMapping(value = "/manager/role/menu.ddxj")
	public ResponseBase managerRoleMenu(HttpServletRequest request, HttpServletResponse response,Integer roleId) throws IllegalAccessException 
	{
		ResponseBase result = ResponseBase.getInitResponse();
		List<ManagerMenu> roleMenuList = managerService.queryUserMenu(roleId);
		List<ManagerMenu> menuListAll = managerService.queryUserMenuAll(null);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("success");
		result.push("roleMenuList", roleMenuList);
		result.push("menuListAll", menuListAll);
		return result;
	}
	@RequestMapping(value = "/manager/update/role/menu.ddxj")
	public ResponseBase updateManagerRoleMenu(HttpServletRequest request, HttpServletResponse response,Integer roleId,String menuIds) throws IllegalAccessException 
	{
		ResponseBase result = ResponseBase.getInitResponse();
		if(!CmsUtils.isNullOrEmpty(menuIds))
		{
			managerService.updateRoleMenu(roleId,menuIds);
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("设置成功");
		}
		else
		{
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("请选择菜单");
		}
		return result;
	}
	@RequestMapping(value = "/manager/delete/role.ddxj")
	public ResponseBase deleteRole(HttpServletRequest request, HttpServletResponse response,Integer roleId) 
	{
		ResponseBase result = ResponseBase.getInitResponse();
		if(roleId == 1)
		{
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("系统总管理员不能被删除");
			return result;
		}
		int querySettingRole = managerService.querySettingRole(roleId);
		if(querySettingRole > 0)
		{
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("此角色有用户在使用，请更改后在删除");
			return result;
		}
		managerService.deleteRole(roleId);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("删除成功");
		return result;
	}
	@RequestMapping(value = "/manager/update/role.ddxj")
	public ResponseBase updateRole(HttpServletRequest request, HttpServletResponse response,Integer id,String roleName,String roleDesc) 
	{
		return managerService.updateRole(id,roleName,roleDesc);
	}
	@RequestMapping(value = "/manager/query/role/details.ddxj")
	public ResponseBase queryRoleDetail(HttpServletRequest request, HttpServletResponse response,Integer roleId,String roleName,String roleDesc) 
	{
		ResponseBase result = ResponseBase.getInitResponse();
		ManagerRole role = managerService.queryRoleById(roleId);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.push("role", JsonUtil.bean2jsonObject(role));
		result.setResponseMsg("查询成功");
		return result;
	}
	@RequestMapping(value = "/manager/query/menu/page/list.ddxj")
	public ResponseBase queryMenuPageList(HttpServletRequest request, HttpServletResponse response,Integer pageNum,Integer pageSize,String menuName) 
	{
		ResponseBase result = ResponseBase.getInitResponse();
		PageHelper.startPage(pageNum, pageSize);
		List<ManagerMenu> userMenuAll = managerService.queryUserMenuAll(menuName);
		PageInfo<ManagerMenu> pageInfo = new PageInfo<ManagerMenu>(userMenuAll);
		result.push("menuList", pageInfo);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("查询成功");
		return result;
	}
	@RequestMapping(value = "/manager/query/menu/parent.ddxj")
	public ResponseBase queryMenuParent(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException 
	{
		ResponseBase result = ResponseBase.getInitResponse();
		List<ManagerMenu> menuParentList = managerService.queryMenuParent();
		result.push("menuParentList", menuParentList);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("查询成功");
		return result;
	}
	@RequestMapping(value = "/manager/update/menu.ddxj")
	public ResponseBase updateMenu(HttpServletRequest request, HttpServletResponse response,Integer id,Integer pid,String name,String url,String icon,Integer sort) 
	{
		
		return managerService.updateMenu(id,pid,name,url,icon,sort);
	}
	@RequestMapping(value = "/manager/query/menu/details.ddxj")
	public ResponseBase queryMenuParent(HttpServletRequest request, HttpServletResponse response,Integer menuId) throws IllegalAccessException 
	{
		ResponseBase result = ResponseBase.getInitResponse();
		ManagerMenu managerMenu = managerService.queryMenuById(menuId);
		result.push("menu", managerMenu);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("查询成功");
		return result;
	}
	@RequestMapping(value = "/manager/delete/menu.ddxj")
	public ResponseBase deleteMenu(HttpServletRequest request, HttpServletResponse response,Integer menuId)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		int settingMenu = managerService.querySettingMenu(menuId);
		if(settingMenu > 0)
		{
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("菜单有角色在使用，请更改后再删除");
			return result;
		}
		managerService.deleteMenu(menuId);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("删除成功");
		return result;
	}
	
	/**
     * 查询工种列表
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
	@RequestMapping(value = "/manager/category/list.ddxj")
	public ResponseBase queryCategoryList(HttpServletRequest request,HttpServletResponse response,CmsRequestVo requestVo) throws IllegalAccessException
	{
		ResponseBase result = ResponseBase.getInitResponse();
		PageHelper.startPage(requestVo.getPageNum(), requestVo.getPageSize());
		List<Category> categoryList = categoryService.queryCategoryList(requestVo);
		PageInfo<Category> pageInfo = new PageInfo<Category>(categoryList);
		result.push("categoryList", pageInfo);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("查询成功");
		return result;
	}
	
	/**
	 * 查询工种详情
	 *
	 * @param request
	 * @param response
	 * @author fancunxin
	 * @throws ClientException 
	 */
	@RequestMapping(value = "/manager/category/details.ddxj")
	public ResponseBase queryCategoryDetail(HttpServletRequest request, HttpServletResponse response,Integer categoryId) throws IllegalAccessException 
	{
		ResponseBase result = ResponseBase.getInitResponse();
		Category category = categoryService.queryCategoryDetail(categoryId);
		result.push("category", category);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("查询成功");
		return result;
	}
	
	/**
	 * 查询工种上级
	 *
	 * @param request
	 * @param response
	 * @author fancunxin
	 * @throws ClientException 
	 */
	@RequestMapping(value = "/manager/category/parent.ddxj")
	public ResponseBase queryCategoryParent(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException 
	{
		ResponseBase result = ResponseBase.getInitResponse();
		List<Category> categoryParentList = categoryService.queryParentCategory();
		result.push("categoryParentList", categoryParentList);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("查询成功");
		return result;
	}
	
	/**
     * 更新工种列表
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
	@RequestMapping(value = "/manager/update/category.ddxj")
	public ResponseBase updateCategory(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo) 
	{
		return categoryService.updateCategory(requestVo);
	}
	
	/**
     * 删除工种列表
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
	@RequestMapping(value = "/manager/delete/category.ddxj")
	public ResponseBase deleteCategory(HttpServletRequest request, HttpServletResponse response,Integer categoryId)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		categoryService.deleteCategory(categoryId);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("删除成功");
		return result;
	}
	/**
	 * 验证码是否登录
	 * @param request
	 * @param response
	 * @param userName
	 * @return
	 */
	@RequestMapping(value = "/query/user/validate/login.ddxj")
	public ResponseBase queryUserValidateLogin(HttpServletRequest request, HttpServletResponse response,String userName)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		if(!CmsUtils.isNullOrEmpty(userName))
		{
			String userJson = String.valueOf(redisUtils.get("managerUserInfo#"+userName));
			JSONObject user = (JSONObject) JSONObject.parse(userJson);
			result.push("user", user);
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("已登陆");
		}
		else
		{
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("登陆超时，请重新登陆");
		}
		return result;
	}
	/**
	 * 注销登录
	 * @param request
	 * @param response
	 * @param userName
	 * @return
	 */
	@RequestMapping(value = "/remove/user/login.ddxj")
	public ResponseBase removeUserLogin(HttpServletRequest request, HttpServletResponse response,String userName)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		if(!CmsUtils.isNullOrEmpty(userName))
		{
			redisUtils.remove("managerUserInfo#"+userName);
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("success");
		}
		else
		{
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("登陆超时，请重新登陆");
		}
		return result;
	}
	@RequestMapping(value = "/query/message/list.ddxj")
	public ResponseBase queryMessageList(HttpServletRequest request, HttpServletResponse response,Integer type,Integer pageNum,Integer pageSize)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		PageHelper.startPage(pageNum, pageSize);
		List<Message> queryMessageList = messageService.queryMessageList(type);
		PageInfo<Message> pageInfo = new PageInfo<Message>(queryMessageList);
		result.push("messageList", pageInfo);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("查询成功");
		return result;
	}
	@RequestMapping(value = "/sign/message/read.ddxj")
	public ResponseBase queryMessageList(HttpServletRequest request, HttpServletResponse response,Integer id,String userName)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		messageService.updateSignMessageRead(id);
		redisUtils.set(Constants.VALIDATE_MESSAGE_NOTIC+userName,true);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("更新成功");
		return result;
	}
	@RequestMapping(value = "/validate/message/notic.ddxj")
	public ResponseBase validateMessageNotic(HttpServletRequest request, HttpServletResponse response,String userName) throws InterruptedException
	{
		ResponseBase result = ResponseBase.getInitResponse();
		if(!CmsUtils.isNullOrEmpty(String.valueOf(redisUtils.get(Constants.VALIDATE_MESSAGE_NOTIC+userName))))
		{
			redisUtils.remove(Constants.VALIDATE_MESSAGE_NOTIC+userName);
			result.push("status", 1);
		}
		else
		{
			result.push("status", 2);
		}
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("更新成功");
		return result;
	}
	
	/**
     * 轮播图列表
     * 
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException
     */
    @RequestMapping(value="/advert/banner/list.ddxj")
    public ResponseBase queryAdvertBanner(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo)
    { 
    	ResponseBase result = ResponseBase.getInitResponse();
    	PageHelper.startPage(requestVo.getPageNum(), requestVo.getPageSize());
    	List<AdvertBanner> bannerList = advertBannerService.queryAdvertBannerList(requestVo);
    	PageInfo<AdvertBanner> pageInfo = new PageInfo<AdvertBanner>(bannerList);
    	result.push("bannerList", pageInfo);
    	result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("查询轮播图列表成功");
    	return result;
    }
    
    /**
     * 删除轮播图
     * 
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException
     */
    @RequestMapping(value="/delete/advert/banner.ddxj")
    public ResponseBase deleteAdvertBanner(HttpServletRequest request, HttpServletResponse response,int bannerId)
    { 
    	ResponseBase result = ResponseBase.getInitResponse();
    	advertBannerService.deleteAdvertBanne(bannerId);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("删除轮播图成功");
    	return result;
    }
    
    /**
     * 轮播图详情
     * 
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException
     */
    @RequestMapping(value="/advert/banner/detail.ddxj")
    public ResponseBase advertBannerDetail(HttpServletRequest request, HttpServletResponse response,int bannerId)
    { 
    	ResponseBase result = ResponseBase.getInitResponse();
    	AdvertBanner advertBanner = advertBannerService.selectByPrimaryKey(bannerId);
    	result.push("advertBanner", advertBanner);
    	result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("查询轮播图详情成功");
    	return result;
    }
    
    /**
     * 新增/修改轮播图
     * 
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException
     */
    @RequestMapping(value="/advert/banner/edit.ddxj")
    public ResponseBase advertBannerEdit(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo)
    { 
    	ResponseBase result = ResponseBase.getInitResponse();
    	
    	AdvertBanner advertBanner = null;
    	if(requestVo.getId() != null && requestVo.getId() > 0)
    	{
    		advertBanner = advertBannerService.selectByPrimaryKey(requestVo.getId());
    	}
    	else
    	{
    		advertBanner = new AdvertBanner();
    		advertBanner.setCreateTime(new Date());
    	}
    	advertBanner.setBannerUrl(requestVo.getBannerUrl());
    	advertBanner.setBannerLink(requestVo.getBannerLink());
    	advertBanner.setBannerType(requestVo.getBannerType());
    	advertBanner.setStartTime(DateUtils.getDate(requestVo.getStartTime(), "yyyy-MM-dd"));
    	advertBanner.setEndTime(DateUtils.getDate(requestVo.getEndTime(), "yyyy-MM-dd"));
    	advertBanner.setUpdateTime(new Date());
    	if(requestVo.getId() != null && requestVo.getId() > 0)
    	{
    		advertBannerService.updateByPrimaryKeySelective(advertBanner);
    	}
    	else
    	{
    		advertBannerService.insertSelective(advertBanner);
    	}
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("更新资讯成功");
    	return result;
    }
    
    @RequestMapping(value="/upload/qiniu/image.ddxj")
    public String uploadQiNiuImage(HttpServletRequest request, HttpServletResponse response,@Param("file") MultipartFile file) throws IOException
    { 
    	QiNiuUtil qiNiuTest = new QiNiuUtil();
    	String upload = qiNiuTest.upload(file.getBytes());
    	Map<String,Object> result = new HashMap<String,Object>();
    	Map<String,Object> result1 = new HashMap<String,Object>();
    	if(!CmsUtils.isNullOrEmpty(upload))
    	{
    		JSONObject parse = (JSONObject)JSONObject.parse(upload);
    		result.put("code", 0);
    		result.put("msg", "");
    		result1.put("src", PropertiesUtils.getPropertiesByName("static_url")+ Constants.SPT+parse.getString("key"));
    		result1.put("title", parse.getString("key"));
    		result.put("data", result1);
    	}
    	return JsonUtil.map2jsonToString(result);
    }
    
    /**
     * 资讯列表
     * 
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException
     */
    @RequestMapping(value="/info/mation/list.ddxj")
    public ResponseBase queryInformation(HttpServletRequest request, HttpServletResponse response,CmsRequestVo requestVo)
    { 
    	ResponseBase result = ResponseBase.getInitResponse();
    	PageHelper.startPage(requestVo.getPageNum(), requestVo.getPageSize());
    	List<Information> informationList = informationService.queryInformationList(requestVo);
    	PageInfo<Information> pageInfo = new PageInfo<Information>(informationList);
    	result.push("informationList", pageInfo);
    	
//    	List<InformationType> catgegoryList = informationService.queryInformationTypeList(requestVo);
//    	PageInfo<InformationType> info = new PageInfo<InformationType>(catgegoryList);
//    	result.push("categoryList", info);
    	
    	result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("查询资讯列表成功");
    	return result;
    }
    
    /**
     * 删除资讯
     * 
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException
     */
    @RequestMapping(value="/delete/info/mation.ddxj")
    public ResponseBase deleteInformation(HttpServletRequest request, HttpServletResponse response,int inforId)
    { 
    	ResponseBase result = ResponseBase.getInitResponse();
    	informationService.deleteInformation(inforId);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("删除资讯成功");
    	return result;
    }
    
    /**
     * 资讯详情
     * 
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException
     */
    @RequestMapping(value="/infor/mation/detail.ddxj")
    public ResponseBase informationDetail(HttpServletRequest request, HttpServletResponse response,int inforId)
    { 
    	ResponseBase result = ResponseBase.getInitResponse();
    	Information information = informationService.selectByPrimaryKey(inforId);
    	result.push("information", information);
    	result.setResponse(Constants.TRUE);
    	result.setResponseMsg("查询资讯详情成功");
    	return result;
    }
    
    /**
     * 新增/修改资讯
     * 
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException
     */
    @RequestMapping(value="/infor/mation/edit.ddxj")
    public ResponseBase informationEdit(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo)
    { 
    	ResponseBase result = ResponseBase.getInitResponse();
    	Information info = null;
    	if(requestVo.getInforId() != null && requestVo.getInforId() > Constants.Number.ZERO_INT)
    	{
    		info = informationService.selectByPrimaryKey(requestVo.getInforId());
    	}
    	else
    	{
    		info = new Information();
    		info.setCreateTime(new Date());
    	}
    	info.setInfoTitle(requestVo.getInfoTitle());
    	info.setInfoSummary(requestVo.getInfoSummary());
    	info.setStringContent(requestVo.getInfoContent().getBytes());
    	info.setAuthor(!CmsUtils.isNullOrEmpty(requestVo.getAuthor())?requestVo.getAuthor():"点点小匠");
    	info.setInfoLabel(requestVo.getInfoLabel());
    	info.setStick(requestVo.getStick());
    	if(requestVo.getInfoType() != null && requestVo.getInfoType() > Constants.Number.ZERO_INT)
    	{
    		info.setInfoType(requestVo.getInfoType());
    		if(requestVo.getInfoType() == Constants.Number.TWO_INT)
    		{
    			info.setImageOne(requestVo.getImageOne());
    			info.setImageTwo(requestVo.getImageTwo());
    			info.setImageThree(requestVo.getImageThree());
    		}
    		else if(requestVo.getInfoType() == Constants.Number.THREE_INT || requestVo.getInfoType() == Constants.Number.FOUR_INT)
    		{
    			info.setImageOne(requestVo.getImageOne());
    		}
    	}
    	info.setStartTime(DateUtils.getDate(requestVo.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
    	info.setEndTime(DateUtils.getDate(requestVo.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
    	info.setUpdateTime(new Date());
    	List<Information> informationList = null;
    	if(requestVo.getInforId() != null && requestVo.getInforId() > Constants.Number.ZERO_INT)
    	{
    		informationService.updateByPrimaryKeySelective(info);
    		informationService.deleteInformationCategoryByInfoId(info.getId());//删除原有的分类
    	}
    	else
    	{
    		informationService.insertSelective(info);
    	}
    	
    	if(!CmsUtils.isNullOrEmpty(requestVo.getCategorys()))//资讯分类
    	{
    		String[] category = null;
    		if(requestVo.getCategorys().indexOf(Constants.COMMA) >= Constants.Number.REDUCE_ONE_INT)
    		{
    			category = requestVo.getCategorys().split(Constants.COMMA);
    		}
    		else
    		{
    			category = new String[Constants.Number.ONE_INT];
    			category[Constants.Number.ZERO_INT] = requestVo.getCategorys();
    		}
    		
    		for(String categoryId : category)
    		{
    			InformationCategory cay = new InformationCategory();
    			cay.setInfoId(info.getId());
    			cay.setCategoryId(Integer.valueOf(categoryId));
    			cay.setUpdateTime(new Date());
    			cay.setCreateTime(new Date());
    			informationService.addInformationCategory(cay);
    		}
    	}
    	
    	RequestVo vo = new RequestVo();
    	informationList  = informationService.selectInformation(vo);
		redisUtils.set("information", informationList);//将最新的数据放入缓存
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("更新资讯成功");
    	result.push("information", informationList);
    	return result;
    }
    
    /**
     * 资讯分类列表
     * 
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException
     */
    @RequestMapping(value="/info/mation/category/list.ddxj")
    public ResponseBase queryInformationCategory(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo)
    { 
    	ResponseBase result = ResponseBase.getInitResponse();
    	PageHelper.startPage(requestVo.getPageNum(), requestVo.getPageSize());
    	List<InformationType> catgegoryList = informationService.queryInformationTypeList(requestVo);
    	PageInfo<InformationType> pageInfo = new PageInfo<InformationType>(catgegoryList);
    	result.push("categoryList", pageInfo);
    	result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("查询资讯分类列表成功");
    	return result;
    }
    
    /**
     * 资讯分类详情
     * 
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException
     */
    @RequestMapping(value="/info/mation/category/details.ddxj")
    public ResponseBase InformationCategoryDetails(HttpServletRequest request, HttpServletResponse response,int categoryId)
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	InformationType category = informationService.InformationCategoryDetail(categoryId);
    	result.push("category", category);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("查询分类详情成功");
    	return result;
    }
    
    /**
     * 新增-修改资讯分类
     * 
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException
     */
    @RequestMapping(value="/info/mation/category/update.ddxj")
    public ResponseBase InformationCategoryUpdate(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo)
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	InformationType category = null;
    	if(requestVo.getId() != null && requestVo.getId() > 0)
    	{
    		category = informationService.InformationCategoryDetail(requestVo.getId());
    		category.setUpdateTime(new Date());
    	}
    	else
    	{
    		category = new InformationType();
    		category.setCreateTime(new Date());
    		category.setUpdateTime(new Date());
    	}
    	category.setSort(requestVo.getSort());
    	category.setName(requestVo.getName());
    	
    	if(requestVo.getId() != null && requestVo.getId() > 0)
    	{
    		informationService.updateInformationCategory(category);
    	}
    	else
    	{
    		informationService.insertInformationCategory(category);
    	}
    	
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("保存成功");
    	return result;
    }
    
    /**
     * 删除资讯分类
     * 
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException
     */
    @RequestMapping(value="/info/mation/category/delete.ddxj")
    public ResponseBase deleteInformationCategory(HttpServletRequest request, HttpServletResponse response,int categoryId)
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	informationService.deleteInformationCategory(categoryId);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("删除成功");
    	return result;
    }
    
    /**
     * 上传图片
    * @Title: MemberController.java  
    * @param @param request
    * @param @param response
    * @param @param base64
    * @param @return
    * @param @throws QiniuException
    * @param @throws IOException参数  
    * @return ResponseBase    返回类型 
    * @throws
    * @Package net.zn.ddxj.api  
    * @author 上海众宁网络科技有限公司-何俊辉
    * @date 2018年5月18日  
    * @version V1.0
     */
    @RequestMapping(value = "/file/images/upload.ddxj", method = RequestMethod.POST)
    public ResponseBase uploadWeixinFile(HttpServletRequest request, HttpServletResponse response,String base64) throws QiniuException, IOException
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	
    	if(CmsUtils.isNullOrEmpty(base64))
    	{
    		result.setResponse(Constants.FALSE);
    		result.setResponseCode(Constants.SUCCESS_200);
    		result.setResponseMsg("图片不能为空");
    		return result;
    	}
    	if(CmsUtils.imageSize(base64) / 1024 > 5120)//文件不能大于5M
        {
    		result.setResponse(Constants.FALSE);
    		result.setResponseCode(Constants.SUCCESS_200);
    		result.setResponseMsg("图片大小不能超过5M");
    		return result;
        }
        // 生成文件名 每个月用一个文件夹
        Calendar cal = Calendar.getInstance();
        String fileName = CmsUtils.generateStr(32);
        String filePath = Constants.UPLOAD_IMAGE_PATH + cal.get(Calendar.YEAR) + Constants.SPT + (cal.get(Calendar.MONTH) + 1) + Constants.SPT +(cal.get(Calendar.DATE) + 1) + Constants.SPT + fileName;
        // 上传文件
    	QiNiuUploadManager.uploadFile(filePath, CmsUtils.getImageIo(base64));
        // 鉴黄
        if(!QiNiuUploadManager.checkImage(filePath))
		{
        	result.setResponse(Constants.FALSE);
    		result.setResponseCode(Constants.SUCCESS_200);
    		result.setResponseMsg("图片涉及到暴露、血腥、色情，请更换图片");
    		return result;
		}
    	result.push("url", PropertiesUtils.getPropertiesByName("static_url")+filePath);
    	result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("上传成功");
		return result;
    }
    
    /**
     * 查询问题列表
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/query/problemLib/list.ddxj")
    public ResponseBase queryProblemLibList(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo) throws IllegalAccessException
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	
    	PageHelper.startPage(requestVo.getPageNum(), requestVo.getPageSize());
    	List<ProblemLib> problemLibList = problemLibService.findProblemLibList(requestVo);
    	PageInfo<ProblemLib> pageInfo = new PageInfo<ProblemLib>(problemLibList);
		if(!CmsUtils.isNullOrEmpty(problemLibList))
		{
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("已查询到符合条件的记录");
			result.push("problemLibList",pageInfo);
			log.info("#####################已查询到符合条件的记录#####################");
		}
		else
		{
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("没有查询到符合条件的记录");
			result.push("problemLibList",pageInfo);
			log.info("#####################没有查询到符合条件的记录#####################");
		}
    	return result;
    }
    
    /**
     * 删除问题
     * 
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException
     */
    @RequestMapping(value="/delete/problem/lib.ddxj")
    public ResponseBase deleteProblemLib(HttpServletRequest request, HttpServletResponse response,int libId)
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	problemLibService.deleteProblemLib(libId);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("删除问题成功");
    	return result;
    }
    
    /**
     * 问题详情
     * 
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException
     */
    @RequestMapping(value="/problem/lib/detail.ddxj")
    public ResponseBase problemLibDetail(HttpServletRequest request, HttpServletResponse response,int libId)
    { 
    	ResponseBase result = ResponseBase.getInitResponse();
    	ProblemLib problemLib = problemLibService.selectByPrimaryKey(libId);
    	result.push("problemLib", problemLib);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("查询问题详情成功");
    	return result;
    }
    
    /**
     * 新增/修改问题
     * 
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException
     */
    @RequestMapping(value="/problem/lib/edit.ddxj")
    public ResponseBase problemLibEdit(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo)
    { 
    	ResponseBase result = ResponseBase.getInitResponse();
    	
    	ProblemLib pro = null;
    	if(requestVo.getLibId() != null && requestVo.getLibId() > 0)
    	{
    		pro = problemLibService.selectByPrimaryKey(requestVo.getLibId());
    	}
    	else
    	{
    		pro = new ProblemLib();
    		pro.setCreateTime(new Date());
    	}
    	pro.setProblemTitle(requestVo.getProblemTitle());
    	pro.setProblemDifficulty(requestVo.getProblemDifficulty());
    	pro.setProblemContent(requestVo.getProblemContent().getBytes());
    	pro.setAuthor(requestVo.getAuthor());
    	pro.setUpdateTime(new Date());
    	if(requestVo.getLibId() != null && requestVo.getLibId() > 0)
    	{
    		problemLibService.updateByPrimaryKeySelective(pro);
    	}
    	else
    	{
    		problemLibService.insertSelective(pro);
    	}
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("更新问题成功");
    	return result;
    }
    
    /**
     * 查询微信用户
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/query/wechatUser/list.ddxj")
    public ResponseBase queryWechatUserList(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo) throws IllegalAccessException
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	
    	PageHelper.startPage(requestVo.getPageNum(), requestVo.getPageSize());
    	List<WechatUser> wechatUserList = userService.queryWechatUserList(requestVo);
    	PageInfo<WechatUser> pageInfo = new PageInfo<WechatUser>(wechatUserList);
		if(!CmsUtils.isNullOrEmpty(wechatUserList))
		{
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("已查询到符合条件的记录");
			result.push("wechatUserList",pageInfo);
			log.info("#####################已查询到符合条件的记录#####################");
		}
		else
		{
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("没有查询到符合条件的记录");
			result.push("wechatUserList",pageInfo);
			log.info("#####################没有查询到符合条件的记录#####################");
		}
    	return result;
    }
    
    /**
     * 查询用户绑定银行卡
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/query/userBank/list.ddxj")
    public ResponseBase queryUserBankList(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo)
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	
    	PageHelper.startPage(requestVo.getPageNum(), requestVo.getPageSize());
    	List<UserBank> userBankList = userService.findUserBankList(requestVo.getUserId());
    	PageInfo<UserBank> pageInfo = new PageInfo<UserBank>(userBankList);
		if(!CmsUtils.isNullOrEmpty(userBankList))
		{
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("已查询到符合条件的记录");
			result.push("userBankList",pageInfo);
			log.info("#####################已查询到符合条件的记录#####################");
		}
		else
		{
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("没有查询到符合条件的记录");
			result.push("userBankList",pageInfo);
			log.info("#####################没有查询到符合条件的记录#####################");
		}
    	return result;
    }
    
    /**
     * 删除用户绑定银行卡
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
	@RequestMapping(value = "/manager/userBank/delete.ddxj")
	public ResponseBase deleteUserBank(HttpServletRequest request, HttpServletResponse response,Integer id)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		userService.deleteUserBank(id);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("删除成功");
		return result;
	}
	
	/**
	 * 导出用户
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/manager/userList/export.ddxj")
	public String exportUserList(HttpServletRequest request, HttpServletResponse response, RequestVo requestVo) throws IOException
	{
		ResponseBase result = ResponseBase.getInitResponse();
		log.info("################用户导出################");
		JSONArray obj = (JSONArray) new JSONArray().parse(requestVo.getUserIdList());
		List<Integer> userIdList = new ArrayList<>();
		for (int i = 0; i < obj.size(); i++) {
			userIdList.add(obj.getInteger(i));
		}
		if(obj.size() > 0)
		{
			/*HSSFWorkbook workbook = userService.userListExport(userIdList);
			if(!CmsUtils.isNullOrEmpty(workbook))
			{
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				String fileName = "点点小匠用户" + new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()) + ".xls";
				String fileNameAbout = URLEncoder.encode(fileName, "UTF-8");
				String headStr = "attachment; filename=\"" + fileNameAbout + "\"";
				response.setContentType("APPLICATION/OCTET-STREAM");
				response.setHeader("Content-Disposition", headStr);
				FileOutputStream fos = new FileOutputStream(new File("E:/work/20180802/myexcel.xls"));
				workbook.write(out);
				out.flush();
				out.close();
				response.getOutputStream().write(out.toByteArray());
			}*/
			return userService.userListExport(userIdList);
		}
		return null;
	}
	
	@RequestMapping(value = "/manager/downloadLocalFileByPath/export.ddxj")
	public void downloadLocalFileByPath(HttpServletResponse response,HttpServletRequest request) throws IOException{
		//获得地址
		//添加下载文件的头信息。此信息在下载时会在下载面板上显示
		log.info("################用户导出文件下载################");
		String fileName = request.getParameter("fileName");
		fileName = URLDecoder.decode(fileName, "UTF-8");; // 前台两次转码
		fileName = new String(fileName.getBytes(), "ISO-8859-1"); // 各浏览器基本都支持ISO编码
		// path是指欲下载的文件的路径。
		String filePath = request.getParameter("filePath");
		File file = new File(filePath);
		// 以流的形式下载文件。
        this.writeToClient(fileName, file,response);
        //判断是否需要删除
        //删除文件
    	FileUtils.deleteQuietly(file);
	}
	
	/**
	 * 输出到客户端
	 * @param fileName
	 * @param localFile
	 */
	private void writeToClient(String fileName, File localFile,HttpServletResponse response) {
		response.reset();
		response.setContentType("application/x-msdownload");
		response.setCharacterEncoding("UTF-8");
		try {
			response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			int fileLength = (int) localFile.length();
			response.setContentLength(fileLength);
			/* 创建输入流 */
			InputStream inStream = new FileInputStream(localFile);
			byte[] buf = new byte[4096];
			/* 创建输出流 */
			ServletOutputStream servletOS = response.getOutputStream();
			int readLength;
			while (((readLength = inStream.read(buf)) != -1)) {
				servletOS.write(buf, 0, readLength);
			}
			inStream.close();
			servletOS.flush();
			servletOS.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
