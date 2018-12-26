package net.zn.ddxj.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;
import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.common.PageHelperModel;
import net.zn.ddxj.common.upload.UploadUtils;
import net.zn.ddxj.config.shiro.redis.RedisSessionDAO;
import net.zn.ddxj.constants.CmsConstants;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.Category;
import net.zn.ddxj.entity.CmsLogs;
import net.zn.ddxj.entity.CmsResource;
import net.zn.ddxj.entity.CmsRole;
import net.zn.ddxj.entity.CmsStorage;
import net.zn.ddxj.entity.CmsUser;
import net.zn.ddxj.entity.Complain;
import net.zn.ddxj.entity.Message;
import net.zn.ddxj.entity.ProblemLib;
import net.zn.ddxj.entity.ScreenAdvert;
import net.zn.ddxj.entity.User;
import net.zn.ddxj.entity.UserWithdraw;
import net.zn.ddxj.service.CategoryService;
import net.zn.ddxj.service.CmsService;
import net.zn.ddxj.service.ProblemLibService;
import net.zn.ddxj.service.ScreenAdvertService;
import net.zn.ddxj.service.UserService;
import net.zn.ddxj.service.UserWithdrawService;
import net.zn.ddxj.tool.MessageService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.DateUtils;
import net.zn.ddxj.utils.FrontUtils;
import net.zn.ddxj.utils.MD5Util;
import net.zn.ddxj.utils.MD5Utils;
import net.zn.ddxj.utils.PropertiesUtils;
import net.zn.ddxj.utils.QiNiuUploadManager;
import net.zn.ddxj.utils.RedisUtils;
import net.zn.ddxj.utils.ResponseUtils;
import net.zn.ddxj.utils.aliyun.FindBankUtils;
import net.zn.ddxj.utils.json.JsonUtil;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.ExprotVo;
import net.zn.ddxj.vo.RequestVo;
import net.zn.ddxj.vo.ZtreeView;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.aliyuncs.exceptions.ClientException;
import com.baidu.ueditor.ActionEnter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiniu.common.QiniuException;
import com.qiniu.util.Auth;
/**
 * 
 * @ClassName:  CodeServer   
 * @Description:TODO(CMS核心Controller)   
 * @author: 何俊辉 
 * @date:   2018年8月9日 下午4:44:47   
 * @Copyright: 2018 www.diandxj.com Inc. All rights reserved. 
 * 注意：本内容仅限于上海众宁网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Controller
@Slf4j
public class CmsController extends BaseController {
	
	private static final String LOGIN = "/admin/login.html";
	private static final String INDEX = "/admin/index.html";
	private static final String CMS_USER = "/admin/cms_user.html";
	private static final String CMS_USER_TPL = "/admin/cms_user_tpl.html";
	private static final String CMS_ROLE = "/admin/cms_role.html";
	private static final String CMS_ROLE_TPL= "/admin/cms_role_tpl.html";
	private static final String CMS_RESOURCE = "/admin/cms_resource.html";
	private static final String CMS_RESOURCE_TPL= "/admin/cms_resource_tpl.html";
	private static final String CMS_RESOURCE_EDIT= "/admin/cms_resource_edit.html";
	private static final String CMS_USER_EDIT= "/admin/cms_user_edit.html";
	private static final String CMS_ROLE_EDIT= "/admin/cms_role_edit.html";
	private static final String CMS_ROLE_RESOURCE_EDIT = "/admin/cms_role_resource_edit.html";
	private static final String CMS_LOGS = "/admin/cms_logs.html";
	private static final String CMS_LOGS_TPL= "/admin/cms_logs_tpl.html";
	private static final String CMS_USER_PASSWORD= "/admin/cms_user_password.html";
	private static final String UEDITOR_INDEX= "/ueditor/index.html";
	private static final String CMS_MESSAGE_LIST = "/admin/cms_message_list.html";
	private static final String CMS_MESSAGE_TPL = "/admin/cms_message_tpl.html";
	private static final String CMS_MESSAGE_READ = "/admin/cms_message_read.html";
	private static final String CMS_MESSAGE_TPL_READ = "/admin/cms_message_tpl_read.html";
	private static final String CMS_COMPLAIN = "/admin/cms_complain.html";
	private static final String CMS_COMPLAIN_TPL = "/admin/cms_complain_tpl.html";
	private static final String CMS_STORAGE = "/admin/cms_storage.html";
	private static final String CMS_STORAGE_TPL = "/admin/cms_storage_tpl.html";
    private static final String IMAGES_MANAGER_TPL = "/common/upload/imageManagerTpl.html";
    private static final String IMAGES_SELCTOR = "/common/upload/imageSelector.html";
    private static final String IMAGES_SELCTOR_TPL = "/common/upload/imageSelectorTpl.html";
    private static final String SCREEN_RECORD_LIST="/admin/screen_record_list.html";
	private static final String SCREEN_RECORD_LIST_TPL="/admin/screen_record_list_tpl.html";
	private static final String SCREEN_RECORD_EDIT="/admin/screen_record_edit.html";
	private static final String EXCEL_EXPORT="/admin/excel_export.html";
	private static final String PROBLEM_LIB_LIST = "/admin/problem_lib_list.html";
	private static final String PROBLEM_LIB_LIST_TPL = "/admin/problem_lib_list_tpl.html";
	private static final String PROBLEM_LIB_DETAIL = "/admin/problem_lib_detial.html";
	private static final String PROBLEM_LIB_EDIT = "/admin/problem_lib_edit.html";
	@Autowired
	public RedisUtils redisUtils;
	@Autowired
	private CmsService cmsService;
	@Autowired
	private RedisSessionDAO redisSessionDAO;
	@Autowired 
	private UserService userService;
	@Autowired
	private MessageService messageService;
	@Autowired 
	private ScreenAdvertService screenAdvertService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private UserWithdrawService userWithdrawService;
	@Autowired
	private ProblemLibService problemLibService;
	/**
	 * 登陆
	* @Title: cmsLoginGet
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/cms/login.htm", method = RequestMethod.GET)
	public String cmsLoginGet(HttpServletRequest request,HttpServletResponse response,ModelMap model,String m) {
		
		if(!CmsUtils.isNullOrEmpty(m))
		{
			model.addAttribute("forceQuit",m);
		}
		return FrontUtils.findFrontTpl(request, response, model, LOGIN);
	}
	/**
	 * 登陆
	* @Title: cmsLoginPost
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @param userName
	* @param @param cmsPassword
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/cms/login.htm", method = RequestMethod.POST)
	public String cmsLoginPost(HttpServletRequest request,HttpServletResponse response,ModelMap model,String userName,String cmsPassword) {
		ResponseBase result = ResponseBase.getInitResponse();
		try {
			 Subject subject = SecurityUtils.getSubject();
			 UsernamePasswordToken token = new UsernamePasswordToken(userName, cmsPassword);
			 if(!subject.isAuthenticated())
			 {
				 subject.login(token);
				 token.setRememberMe(true);  
			 }
			result.setResponse(Constants.TRUE);
			result.setResponseMsg(Constants.Prompt.LOGIN_SUCCESSFUL);
			ResponseUtils.renderHtml(response, JsonUtil.bean2jsonToString(result));
		} catch (AuthenticationException e) {
			result.setResponse(Constants.FALSE);
			result.setResponseMsg(e.getMessage());
			ResponseUtils.renderHtml(response, JsonUtil.bean2jsonToString(result));
		}
		result.setResponseCode(Constants.SUCCESS_200);
		return null;
	}
	/**
	 * 首页
	* @Title: cmsIndexGet
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/cms/index.htm", method = RequestMethod.GET)
	public String cmsIndexGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		CmsUser user = (CmsUser)SecurityUtils.getSubject().getPrincipal();
    	model.addAttribute("user", user);
    	model.addAttribute("todayCount", userService.queryTodayUserCount(2));
		/*model.addAttribute("onlineCount", redisSessionDAO.getActiveSessions().size());*/
    	model.addAttribute("totalCount", userService.queryTodayUserCount(1));
    	model.addAttribute("massageCount", messageService.queryMessageTotalCount(0));
    	model.addAttribute("massageTrue", messageService.queryMessageTotalCount(1));
    	model.addAttribute("massageFalse", messageService.queryMessageTotalCount(2));
    	model.addAttribute("worker", userService.queryUserByRole(1));
    	model.addAttribute("foreman", userService.queryUserByRole(2));
    	model.addAttribute("regosterSumMoney", cmsService.findRegisterAwardMoneyCount());
    	model.addAttribute("withdrawSuccessMoney", cmsService.findWithdrawSuccessCount());
    	model.addAttribute("realAuthCount", cmsService.findRealAuthCount());
    	model.addAttribute("circleCount", cmsService.findCircleCount());
    	
    	
		model.addAttribute("resourceList",user.getRole().getResourceList());
		
		return FrontUtils.findFrontTpl(request, response, model, INDEX);
	}
	/**
	 * 首页
	 * @Title: cmsIndexPOST
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cms/index.htm", method = RequestMethod.POST)
	public String cmsIndexPOST(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		ResponseBase result = ResponseBase.getInitResponse();
    	result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.push("dataList", userService.queryMonthEveryDay());
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	/**
	 * 用户列表
	* @Title: cmsUserListGet
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/cms/user/list.htm", method = RequestMethod.GET)
	public String cmsUserListGet(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer roleId) {
		if(!CmsUtils.isNullOrEmpty(roleId))
		{
			model.addAttribute("roleId",roleId);
		}
		List<CmsRole> findRoleList = cmsService.findRoleList(new CmsRequestVo());
		model.addAttribute("roleList",findRoleList);
		return FrontUtils.findFrontTpl(request, response, model, CMS_USER);
	}
	/**
	 * 用户列表
	* @Title: cmsUserListPost
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @param requestVo
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/cms/user/list.htm", method = RequestMethod.POST)
	public String cmsUserListPost(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) {
		
		PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
		List<CmsUser> findUserList = cmsService.findUserList(requestVo);
		PageInfo<CmsUser> page = new PageInfo<CmsUser>(findUserList);
		PageHelperModel.responsePageModel(page,model);
		model.addAttribute("cmsUserList",page.getList());
		return FrontUtils.findFrontTpl(request, response, model, CMS_USER_TPL);
	}
	/**
	 * 退出登陆
	* @Title: cmsUserLogoutGet
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/cms/user/logout.htm", method = RequestMethod.GET)
	public void cmsUserLogoutGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		
		redirect(response, request.getContextPath() + "/cms/login.htm");
	}
	/**
	 * 角色管理页面
	* @Title: cmsUserRoleGet
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/cms/role/list.htm", method = RequestMethod.GET)
	public String cmsUserRoleGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		
		return FrontUtils.findFrontTpl(request, response, model, CMS_ROLE);
	}
	/**
	 * 角色管理
	* @Title: cmsRolePost
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @param requestVo
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/cms/role/list.htm", method = RequestMethod.POST)
	public String cmsRolePost(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) {
		PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
		List<CmsRole> roleList = cmsService.findRoleList(requestVo);
		PageInfo<CmsRole> page = new PageInfo<CmsRole>(roleList);
		PageHelperModel.responsePageModel(page,model);
		model.addAttribute("cmsRoleList",page.getList());
		return FrontUtils.findFrontTpl(request, response, model, CMS_ROLE_TPL);
	}
	/**
	 * 资源管理页面
	* @Title: cmsResourceGet
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/cms/resource/list.htm", method = RequestMethod.GET)
	public String cmsResourceGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		
		List<CmsResource> parentResourceList = cmsService.findParentResourceList();
		model.addAttribute("parentResourceList",parentResourceList);
		return FrontUtils.findFrontTpl(request, response, model, CMS_RESOURCE);
	}
	/**
	 * 资源管理
	* @Title: cmsResourcePost
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @param requestVo
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/cms/resource/list.htm", method = RequestMethod.POST)
	public String cmsResourcePost(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) {
		
		PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
		List<CmsResource> resourceList = cmsService.findResourceList(requestVo);
		PageInfo<CmsResource> page = new PageInfo<CmsResource>(resourceList);
		PageHelperModel.responsePageModel(page,model);
		model.addAttribute("cmsResourceList",resourceList);
		return FrontUtils.findFrontTpl(request, response, model, CMS_RESOURCE_TPL);
	}
	@RequestMapping(value = "/cms/resource/edit.htm", method = RequestMethod.GET)
	public String cmsResourceEditGet(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer resourceId) {
		if(!CmsUtils.isNullOrEmpty(resourceId) && resourceId > 0)
		{
			CmsResource resource = cmsService.findResourceById(resourceId);
			model.addAttribute("resource",resource);
		}
		List<CmsResource> parentResourceList = cmsService.findParentResourceList();
		model.addAttribute("parentResourceList",parentResourceList);
		List<CmsResource> menuResourceList = cmsService.findMenuResourceList();
		model.addAttribute("menuResourceList",menuResourceList);
		
		return FrontUtils.findFrontTpl(request, response, model, CMS_RESOURCE_EDIT);
	}
	@RequestMapping(value = "/cms/resource/edit.htm", method = RequestMethod.POST)
	public String cmsResourceEditPost(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) {
		ResponseBase result = ResponseBase.getInitResponse();
		CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		//修改
		if(!CmsUtils.isNullOrEmpty(requestVo.getId()) && requestVo.getId() > 0)
		{
			result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
			cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "系统管理-资源管理-修改资源-（"+requestVo.getResourceName()+"）",sessionUser.getId());
		}
		else
		{
			result.setResponseMsg(Constants.Prompt.ADD_SUCCESSFUL);
			cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "系统管理-资源管理-添加资源-（"+requestVo.getResourceName()+"）",sessionUser.getId());
		}
		cmsService.updateResource(requestVo);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		
		return null;
	}
	/**
	 * 角色编辑-添加-更新页面
	* @Title: cmsRoleEditGet
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @param roleId
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/cms/role/edit.htm", method = RequestMethod.GET)
	public String cmsRoleEditGet(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer roleId) {
		if(!CmsUtils.isNullOrEmpty(roleId) && roleId > 0)
		{
			CmsRole role = cmsService.findRoleById(roleId);
			model.addAttribute("role",role);
		}
		return FrontUtils.findFrontTpl(request, response, model, CMS_ROLE_EDIT);
	}
	/**
	 * 角色编辑-添加-更新
	* @Title: cmsRoleEditPost
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @param cmsRole
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/cms/role/edit.htm", method = RequestMethod.POST)
	public String cmsRoleEditPost(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRole cmsRole) {
		ResponseBase result = ResponseBase.getInitResponse();
		CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		//修改
		if(!CmsUtils.isNullOrEmpty(cmsRole.getId()) && cmsRole.getId() > 0)
		{
			result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
			cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "系统管理-角色管理-修改角色-("+cmsRole.getRoleName()+")",sessionUser.getId());
		}
		else
		{
			result.setResponseMsg(Constants.Prompt.ADD_SUCCESSFUL);
			cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "系统管理-角色管理-添加角色-（"+cmsRole.getRoleName()+"）",sessionUser.getId());
		}
		cmsService.updateRole(cmsRole);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	/**
	 * 角色删除
	* @Title: cmsRoleDeletePost
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @param id
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/cms/role/delete.htm", method = RequestMethod.POST)
	public String cmsRoleDeletePost(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer id) {
		ResponseBase result = ResponseBase.getInitResponse();
		cmsService.deleteRole(id);
		//日志记录
		CmsRole oldRole = cmsService.findRoleById(id);
		CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "系统管理-角色管理-删除角色-（"+oldRole.getRoleName()+"）",sessionUser.getId());
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	/**
	 * 角色分配资源页面
	* @Title: cmsRoleResourceEditGet
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @param roleId
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/cms/role/resource/edit.htm", method = RequestMethod.GET)
	public String cmsRoleResourceEditGet(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer roleId) {
		
		model.addAttribute("roleId",roleId);
		return FrontUtils.findFrontTpl(request, response, model, CMS_ROLE_RESOURCE_EDIT);
	}
	/**
	 * 角色分配资源查询
	* @Title: cmsRoleResourceListPost
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @param roleId
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/cms/role/resource/list.htm", method = RequestMethod.POST)
	public String cmsRoleResourceListPost(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer roleId) {
		ResponseBase result = ResponseBase.getInitResponse();
		
		List<ZtreeView> ztreeViewList = cmsService.findRoleResourceList(roleId);
		result.push("roleResourceList", ztreeViewList);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	/**
	 * 角色分配资源-更新
	* @Title: cmsRoleResourceEditPost
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @param roleId
	* @param @param resourceIds
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/cms/role/resource/edit.htm", method = RequestMethod.POST)
	public String cmsRoleResourceEditPost(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer roleId,String resourceIds) {
		ResponseBase result = ResponseBase.getInitResponse();
		cmsService.updateRoleResource(roleId, resourceIds);
		CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		CmsRole oldRole = cmsService.findRoleById(roleId);
		cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "系统管理-角色管理-权限资源-（"+oldRole.getRoleName()+"）",sessionUser.getId());
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	
	/**
	 * 修改、新增用户-GET
	 * @param request
	 * @param response
	 * @param model
	 * @author FanCunXin
	 * @return
	 */
	@RequestMapping(value = "/cms/user/edit.htm", method = RequestMethod.GET)
	public String cmsUserEditGET(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo,String self) 
	{
		CmsUser user = null;
		if(requestVo.getUserId() != null && requestVo.getUserId() > 0)
		{
			user = cmsService.queryUserById(requestVo.getUserId());
		}
		model.addAttribute("user",user);
		if(!CmsUtils.isNullOrEmpty(self))
		{
			model.addAttribute("self",self);
		}
		List<CmsRole> roleList = cmsService.findRoleList(requestVo);
		
		model.addAttribute("cmsRoleList",roleList);
		
		return FrontUtils.findFrontTpl(request, response, model, CMS_USER_EDIT);
	}
	
	/**
	 * 修改、新增用户-POST
	 * @param request
	 * @param response
	 * @param model
	 * @author FanCunXin
	 * @return
	 */
	@RequestMapping(value = "/cms/user/edit.htm", method = RequestMethod.POST)
	public String cmsUserEditPOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) 
	{
		ResponseBase result = ResponseBase.getInitResponse();
		CmsUser user = null;
		if(requestVo.getUserId() != null && requestVo.getUserId() > 0)
		{
			user = cmsService.queryUserById(requestVo.getUserId());
		}
		else
		{
			user = new CmsUser();
			user.setCreateTime(new Date());
			user.setUpdateTime(new Date());
			user.setLocked(0);
		}
		user.setNickName(requestVo.getNickName());
		user.setUserName(requestVo.getUserName());
		if(!CmsUtils.isNullOrEmpty(requestVo.getPassword()))
		{
			user.setPassword(MD5Util.MD5(requestVo.getPassword()));
		}
		user.setAddress(requestVo.getAddress());
		user.setRoleId(requestVo.getRoleId());
		user.setSex(requestVo.getSex());
		user.setTelphone(requestVo.getTelphone());
		user.setEmail(requestVo.getEmail());
		user.setAddress(requestVo.getAddress());
		user.setDescription(requestVo.getDescription());
		user.setBirthday(DateUtils.getDate(requestVo.getBirthday(), Constants.YYYY_MM_DD_HH_MM_SS));
		
		if(requestVo.getUserId() != null && requestVo.getUserId() > 0)
		{
			cmsService.updateCmsUser(user);
			CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
			cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "系统管理-用户管理-更新用户-（"+user.getUserName()+"）",sessionUser.getId());
		}
		else
		{
			cmsService.insertCmsUser(user);
			CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
			cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "系统管理-用户管理-添加用户-（"+user.getUserName()+"）",sessionUser.getId());
		}
		
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.ADD_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	
	/**
	 * 删除用户-POST
	 * @param request
	 * @param response
	 * @param model
	 * @author FanCunXin
	 * @return
	 */
	@RequestMapping(value = "/cms/user/delete.htm", method = RequestMethod.POST)
	public String cmsUserDeletePOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer id) 
	{
		ResponseBase result = ResponseBase.getInitResponse();
		cmsService.deleteCmsUser(id);
		//日志记录
		CmsUser user = cmsService.queryUserById(id);
		CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "系统管理-用户管理-删除用户-（"+user.getUserName()+"）",sessionUser.getId());
		
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;

	}
	@RequestMapping(value = "/cms/resource/delete.htm", method = RequestMethod.POST)
	public String cmsResourceDeletePOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,int id) 
	{
		ResponseBase result = ResponseBase.getInitResponse();
		cmsService.deleteResource(id);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	/**
	 * 根据菜单id查询按钮组
	* @Title: cmsResourceGroupPOST
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @param menuId
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/cms/resource/group.htm", method = RequestMethod.POST)
	public String cmsResourceGroupPOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer menuId) 
	{
		ResponseBase result = ResponseBase.getInitResponse();
		result.push("groupList", cmsService.findResourceBtnGroup(menuId));
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	
	/**
	 * 禁用用户-POST
	 * @param request
	 * @param response
	 * @param model
	 * @author FanCunXin
	 * @return
	 */
	@RequestMapping(value = "/cms/user/disable.htm", method = RequestMethod.POST)
	public String cmsUserDisablePOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,int id,int locked) 
	{
		ResponseBase result = ResponseBase.getInitResponse();
		CmsUser user = new CmsUser();
		user.setId(id);
		user.setLocked(locked);
		cmsService.updateCmsUser(user);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	
	/**
	 * 修改密码-GET
	 * @param request
	 * @param response
	 * @param model
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/cms/user/changepwd.htm",method = RequestMethod.GET)
	public String cmsUserChangePasswordGET(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer userId)
	{
		model.addAttribute("userId",userId);
		return FrontUtils.findFrontTpl(request, response, model, CMS_USER_PASSWORD);
		
	}
	/**
	 * 修改密码-POST
	 * @param request
	 * @param response
	 * @param model
	 * @param userId
	 * @param newPassword
	 * @return
	 */
	@RequestMapping(value="/cms/user/changepwd.htm",method = RequestMethod.POST)
	public String cmsUserChangePasswordPOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,int userId,String newPassword)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		cmsService.changeUserPassword(userId,MD5Utils.generatePasswordMD5(newPassword, CmsConstants.MD5_SALT));
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
		
	}
	/**
	 * 角色管理页面
	* @Title: cmsUserRoleGet
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/cms/logs/list.htm", method = RequestMethod.GET)
	public String cmsLogsGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		
		return FrontUtils.findFrontTpl(request, response, model, CMS_LOGS);
	}
	/**
	 * 角色管理
	* @Title: cmsRolePost
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @param requestVo
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/cms/logs/list.htm", method = RequestMethod.POST)
	public String  cmsLogsPost(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) {
		PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
		CmsUser user = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		requestVo.setUserId(user.getId());
		List<CmsLogs> logs = cmsService.findLogsByUserId(requestVo);
		PageInfo<CmsLogs> page = new PageInfo<CmsLogs>(logs);
		PageHelperModel.responsePageModel(page,model);
		
		model.addAttribute("cmsLogsList",page.getList());
		
		return FrontUtils.findFrontTpl(request, response, model, CMS_LOGS_TPL);
	}
	@RequestMapping(value = "/cms/ueditor.htm", method = RequestMethod.GET)
	public String cmsUeditorGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		
		return FrontUtils.findFrontTpl(request, response, model, UEDITOR_INDEX);
	}
	/**
	 * 获取七牛token
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/member/query/qiniu/token.ddxj")
	public ResponseBase queryQiNiuToken(HttpServletRequest request, HttpServletResponse response) {
		ResponseBase result = ResponseBase.getInitResponse();
		Auth auth = Auth.create(PropertiesUtils.getPropertiesByName("access_key"), PropertiesUtils.getPropertiesByName("secret_key"));
		result.push("token", auth.uploadToken(PropertiesUtils.getPropertiesByName("bucket_name")));
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.SUCCESS);
		return result;
	}
	/**
	 * 读取配置文件
	* @Title: cmsUeditorConfigGet
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @param action
	* @param @throws IOException    参数
	* @return void    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/cms/ueditor/config.htm")
	public void cmsUeditorConfigGet(HttpServletRequest request,HttpServletResponse response,ModelMap model,String action) throws IOException {
		response.setContentType("application/json");
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        try {
            String exec = new ActionEnter(request, rootPath).exec();
            PrintWriter writer = response.getWriter();
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

	}
	/**
	 * 消息列表
	* @Title: cmsMessageGet
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value="/cms/message/list.htm", method = RequestMethod.GET)
	public String cmsMessageGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		return FrontUtils.findFrontTpl(request, response, model, CMS_MESSAGE_LIST);
	}
	/**
	 * 消息列表
	* @Title: cmsMessagePOST
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @param requestVo
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value="/cms/message/list.htm", method = RequestMethod.POST)
	public String cmsMessagePOST(HttpServletRequest request,HttpServletResponse response,ModelMap model ,CmsRequestVo requestVo) {
		PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
		List<Message> messageList = messageService.queryMessageList(requestVo.getType());
		PageInfo<Message> page = new PageInfo<Message>(messageList);
		PageHelperModel.responsePageModel(page,model);
		
		model.addAttribute("messageList",page.getList());
		
		return FrontUtils.findFrontTpl(request, response, model, CMS_MESSAGE_TPL);
	}
	/**
	 * 已读消息列表
	* @Title: cmsMessageReadGet
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value="/cms/message/read.htm", method = RequestMethod.GET)
	public String cmsMessageReadGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		
		
		return FrontUtils.findFrontTpl(request, response, model, CMS_MESSAGE_READ);
	}
	/**
	 * 已读消息列表
	* @Title: cmsMessageReadPOST
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @param requestVo
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value="/cms/message/read.htm", method = RequestMethod.POST)
	public String cmsMessageReadPOST(HttpServletRequest request,HttpServletResponse response,ModelMap model ,CmsRequestVo requestVo) {
		PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
		List<Message> messageList = messageService.queryMessageList(requestVo.getType());
		PageInfo<Message> page = new PageInfo<Message>(messageList);
		PageHelperModel.responsePageModel(page,model);
		
		model.addAttribute("messageList",page.getList());
		
		return FrontUtils.findFrontTpl(request, response, model, CMS_MESSAGE_TPL_READ);
	}
	/**
	 * 更新消息状态
	 * @param request
	 * @param response
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/sign/message/read.htm", method = RequestMethod.POST)
	public String queryMessageList(HttpServletRequest request, HttpServletResponse response,ModelMap model,Integer id)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		Message message = messageService.queryMessage(id);
		messageService.updateSignMessageRead(id);
		Integer messageCount = (Integer) redisUtils.get(Constants.VALIDATE_MESSAGE_NOTIC);
		if(messageCount > 0)
		{
			redisUtils.set(Constants.VALIDATE_MESSAGE_NOTIC, messageCount - 1);
		}
		//消息记录
		CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "系统设置-消息记录-更新消息状态-（"+message.getMessageContent()+"）",sessionUser.getId());
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("更新成功");
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	/**
	 * 投诉与建议GET
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/cms/complain/list.htm", method = RequestMethod.GET)
	public String cmsComplainGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		
		return FrontUtils.findFrontTpl(request, response, model, CMS_COMPLAIN);
	}
	/**
	 * 投诉与建议POST
	 * @param request
	 * @param response
	 * @param model
	 * @param requestVo
	 * @return
	 */
	@RequestMapping(value = "/cms/complain/list.htm", method = RequestMethod.POST)
	public String  cmsComplainPost(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) {
		PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
		List<Complain> complainList = userService.queryComplainList(requestVo);
		PageInfo<Complain> page = new PageInfo<Complain>(complainList);
		PageHelperModel.responsePageModel(page,model);
		
		model.addAttribute("complainList",page.getList());
		
		return FrontUtils.findFrontTpl(request, response, model, CMS_COMPLAIN_TPL);
	}
	/**
	 * 投诉与建议审核
	 * @param request
	 * @param response
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/cms/comPlain/handle.htm", method = RequestMethod.POST)
	public String complainUpdateStatusPost(HttpServletRequest request, HttpServletResponse response,ModelMap model,Integer id)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		Complain com = new Complain();
    	com.setId(id);
    	com.setStatus(2);
    	userService.handleComPlain(com);
		Complain complain = userService.queryComPlainBy(id);
    	//日志记录
		CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "系统设置-投诉建议-受理-（"+complain.getUser().getRealName()+"）",sessionUser.getId());
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("审核成功");
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	/**
	 * 投诉与建议删除
	 * @param request
	 * @param response
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/cms/comPlain/delete.htm", method = RequestMethod.POST)
	public String complainDeletePost(HttpServletRequest request, HttpServletResponse response,ModelMap model,Integer id)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		Complain complain = userService.queryComPlainBy(id);
		userService.deleteComPlain(id);
		//日志记录
		CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "系统设置-投诉建议-删除-（"+complain.getUser().getRealName()+"）",sessionUser.getId());
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("删除成功");
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	/**
	 * 查询消息列表
	* @Title: queryNoticListPost
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/query/notic/list.htm", method = RequestMethod.POST)
	public String queryNoticListPost(HttpServletRequest request, HttpServletResponse response,ModelMap model)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		PageHelper.startPage(1,10);
		List<Message> messageList = messageService.queryMessageList(1);
		PageInfo<Message> messagePageInfo = new PageInfo<Message>(messageList);
		result.push("messageList", messagePageInfo.getList());
		result.push("totalCount", messagePageInfo.getTotal());
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	@RequestMapping(value="/add/storate.htm",method=RequestMethod.GET)
	public String addStorate(HttpServletRequest request, HttpServletResponse response,ModelMap model)
	{
		cmsService.addAllQiNiuStorage();
		return null;
	}
	/**
	 * 查询文件列表
	* @Title: cmsStorageListGet
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value="/cms/storage/list.htm",method=RequestMethod.GET)
	public String cmsStorageListGet(HttpServletRequest request, HttpServletResponse response,ModelMap model)
	{
		return FrontUtils.findFrontTpl(request, response, model, CMS_STORAGE);
	}
	/**
	 * 查询文件列表
	* @Title: cmsStorageListGet
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value="/cms/storage/list.htm",method=RequestMethod.POST)
	public String cmsStorageListPost(HttpServletRequest request, HttpServletResponse response,ModelMap model,CmsRequestVo requestVo)
	{
		PageHelper.startPage(requestVo.getCurrentPage(),200);
		List<CmsStorage> queryAllStorage = cmsService.queryAllStorage(requestVo);
		model.addAttribute("storageList",queryAllStorage);
		return FrontUtils.findFrontTpl(request, response, model, CMS_STORAGE_TPL);
	}
	/**
	 * 删除文件
	* @Title: cmsStorageDeletePost
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @param requestVo
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value="/cms/storage/delete.htm",method=RequestMethod.POST)
	public String cmsStorageDeletePost(HttpServletRequest request, HttpServletResponse response,ModelMap model,CmsRequestVo requestVo)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		cmsService.deleteCmsStorage(requestVo);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	/**
	 * 上传图片
	* @Title: uploadFileGET
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @param callback
	* @param @param act
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/file/upload.htm", method = RequestMethod.GET)
    public String uploadFileGET(HttpServletRequest request, HttpServletResponse response, ModelMap model,String callback,String act)
    {
    	model.addAttribute("callback", callback);
    	model.addAttribute("act", act);
    	model.addAttribute("uploadUrl", "/file/images/upload.htm");
    	return FrontUtils.findFrontTpl(request, response, model, "/common/upload/uploadFile.html");
    }
	/**
	 * 上传图片
	* @Title: uploadFile_POST
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @param file
	* @param @return
	* @param @throws IllegalStateException
	* @param @throws IOException    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	 @RequestMapping(value = "/file/images/upload.htm", method = RequestMethod.POST)
    public String uploadFile_POST(HttpServletRequest request, HttpServletResponse response, ModelMap model, @RequestParam("file") MultipartFile file) throws IllegalStateException, IOException
    {
        if(!UploadUtils.isImageType(file.getContentType()))
		{
			ResponseUtils.renderHtml(response, 98);
            return null;
		}
        // 得到拓展名
        int index = file.getOriginalFilename().lastIndexOf(".");
        String extension = (index != -1) ?  file.getOriginalFilename().substring(index).toLowerCase().intern() : "";
        if(!UploadUtils.isImageExtension(extension))
        {
        	ResponseUtils.renderHtml(response, 98);
        	return null;
        }
        // 生成文件名 每个月用一个文件夹
        Calendar cal = Calendar.getInstance();
        String fileName = CmsUtils.generateStr(16)+ extension;
        String filePath = Constants.UPLOAD_IMAGE_PATH + cal.get(Calendar.YEAR) + Constants.SPT + (cal.get(Calendar.MONTH) + 1) + Constants.SPT +(cal.get(Calendar.DATE) + 1) + Constants.SPT + fileName;
        // 上传文件
        try
        {
        	QiNiuUploadManager.uploadFile(filePath, file.getBytes());
        }
        catch(Exception e)
        {
        	ResponseUtils.renderHtml(response, 1039);
            return null;
        }
        CmsStorage uploadFile = new CmsStorage();
    	// 鉴黄
    	try
    	{
    		if(!QiNiuUploadManager.checkImage(Constants.SPT + filePath))
    		{
    			ResponseUtils.renderHtml(response, 1000);
	            return null;
    		}
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
        
        uploadFile.setStorageKey(filePath);
        uploadFile.setStorageHash(fileName);
        uploadFile.setStorageSize(file.getSize()/1024+"KB");
        uploadFile.setCreateTime(new Date());
        uploadFile.setStorageTime(new Date());
        uploadFile.setStorageType(file.getContentType());
        cmsService.addStorate(uploadFile);
        Map<String,Object> result = new HashMap<String,Object>();
        
    	result.put("ret", 0);
    	result.put("url",filePath);
    	ResponseUtils.renderHtml(response, JsonUtil.map2jsonToString(result));
    	return null;
        
    }
	 @RequestMapping(value = "/video/upload.htm", method = RequestMethod.GET)
	    public String uploadVideoGET(HttpServletRequest request, HttpServletResponse response, ModelMap model,String callback)
	    {
	    	model.addAttribute("callback", callback);
	    	model.addAttribute("uploadUrl", "/file/video/upload.htm");
	    	return FrontUtils.findFrontTpl(request, response, model, "/common/upload/uploadVideo.html");
	    }
	 /**
	  * 上传视频
	 * @Title: uploadVideo_POST
	 * @Description: TODO()
	 * @param @param request
	 * @param @param response
	 * @param @param model
	 * @param @param videoFile
	 * @param @return
	 * @param @throws IllegalStateException
	 * @param @throws IOException    参数
	 * @return String    返回类型
	 * @author  上海众宁网络科技有限公司-何俊辉
	 * @throws
	  */
	 @RequestMapping(value = "/file/video/upload.htm", method = RequestMethod.POST)
	 public String uploadVideo_POST(HttpServletRequest request, HttpServletResponse response, ModelMap model, @RequestParam("videoFile") MultipartFile videoFile) throws IllegalStateException, IOException
    {
		 
		 if(videoFile.getContentType().indexOf("video/") == -1)//不是视频类型
		 {
			 ResponseUtils.renderHtml(response, 98);
	         return null;
		 }
		 
		 if(videoFile.getSize()/1024 > 1024*100)//如果视频大于100MB
		 {
			 ResponseUtils.renderHtml(response, 99);
	         return null;
		 }
		 int index = videoFile.getOriginalFilename().lastIndexOf(".");
         String extension = (index != -1) ?  videoFile.getOriginalFilename().substring(index).toLowerCase().intern() : "";//视频后缀
         
         
         // 生成文件名 每个月用一个文件夹
         Calendar cal = Calendar.getInstance();
         String fileName = CmsUtils.generateStr(16)+ extension;
         String filePath = Constants.UPLOAD_IMAGE_PATH + cal.get(Calendar.YEAR) + Constants.SPT + (cal.get(Calendar.MONTH) + 1) + Constants.SPT +(cal.get(Calendar.DATE) + 1) + Constants.SPT + fileName;
         // 上传文件
         try
         {
         	QiNiuUploadManager.uploadFile(filePath, videoFile.getBytes());
         }
         catch(Exception e)
         {
         	ResponseUtils.renderHtml(response, 1039);
             return null;
         }
         CmsStorage uploadFile = new CmsStorage();
         uploadFile.setStorageKey(filePath);
         uploadFile.setStorageHash(fileName);
         uploadFile.setStorageSize(videoFile.getSize()/1024+"KB");
         uploadFile.setCreateTime(new Date());
         uploadFile.setStorageTime(new Date());
         uploadFile.setStorageType(videoFile.getContentType());
         cmsService.addStorate(uploadFile);
         Map<String,Object> result = new HashMap<String,Object>();
      	 result.put("ret", 0);
     	 result.put("url", CmsConstants.Tag.STATIC_URL + filePath);
     	 ResponseUtils.renderHtml(response, JsonUtil.map2jsonToString(result));
    	 return null;
    }
	 /**
	  * 选择图片
	 * @Title: loadImageSelector_GET
	 * @Description: TODO()
	 * @param @param request
	 * @param @param response
	 * @param @param model
	 * @param @param requestVo
	 * @param @return    参数
	 * @return String    返回类型
	 * @author  上海众宁网络科技有限公司-何俊辉
	 * @throws
	  */
	 @RequestMapping(value = "/images/selector/index.htm",method = RequestMethod.GET)
	    public String loadImageSelector_GET(HttpServletRequest request, HttpServletResponse response, ModelMap model,CmsRequestVo requestVo)
	    {
	    	managerImages_POST(request,response,model,requestVo);
	    	
	    	return FrontUtils.findFrontTpl(request, response, model, IMAGES_SELCTOR);
	    }
	    @RequestMapping(value = "/images/selector/index.htm",method = RequestMethod.POST)
	    public String loadImageSelector_POST(HttpServletRequest request, HttpServletResponse response, ModelMap model,CmsRequestVo requestVo)
	    {
	    	managerImages_POST(request,response,model,requestVo);
	    	
	    	return FrontUtils.findFrontTpl(request, response, model, IMAGES_SELCTOR_TPL);
	    }
	    /**
	     * 查询所有图片 POST
	     * @param request
	     * @param response
	     * @param model
	     * @param memberVo
	     * @param id
	     * @return
	     */
	    @RequestMapping(value = "/images/manager/index.htm",method = RequestMethod.POST)
	    public String managerImages_POST(HttpServletRequest request, HttpServletResponse response, ModelMap model,CmsRequestVo requestVo)
	    {
	    	if (requestVo.getCurrentPage() == null || requestVo.getCurrentPage() <= 0)
	        {
	    		requestVo.setCurrentPage(1);
	        }
	    	PageHelper.startPage(requestVo.getCurrentPage(),200);
			List<CmsStorage> queryAllStorage = cmsService.queryAllStorage(requestVo);
			PageInfo<CmsStorage> page = new PageInfo<CmsStorage>(queryAllStorage);
			PageHelperModel.responsePageModel(page,model);
	    	model.addAttribute("storageList",queryAllStorage);
	    	return FrontUtils.findFrontTpl(request, response, model, IMAGES_MANAGER_TPL);
	    }
	    /**
	     * 验证消息
	    * @Title: validateMessageNotic
	    * @Description: TODO()
	    * @param @param request
	    * @param @param response
	    * @param @return
	    * @param @throws InterruptedException    参数
	    * @return String    返回类型
	    * @author  上海众宁网络科技有限公司-何俊辉
	    * @throws
	     */
	    @RequestMapping(value = "/validate/message/notic.htm",method = RequestMethod.GET)
		public String validateMessageNotic(HttpServletRequest request, HttpServletResponse response) throws InterruptedException
		{
			ResponseBase result = ResponseBase.getInitResponse();
			if(CmsUtils.isNullOrEmpty(redisUtils.get(Constants.VALIDATE_MESSAGE_NOTIC)))
			{
				redisUtils.set(Constants.VALIDATE_MESSAGE_NOTIC, messageService.queryMessageTotalCount(1));//重置redis数量
			}
			result.push("totalCount", redisUtils.get(Constants.VALIDATE_MESSAGE_NOTIC));
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
			ResponseUtils.renderHtml(response, JsonUtil.bean2jsonToString(result));
	    	return null;
		}
	    /**
		 * 手机闪屏GET
		 * @param request
		 * @param response
		 * @param model
		 * @return
		 */
		@RequestMapping(value = "/cms/screen/list.htm", method = RequestMethod.GET)
		public String screenRecordListGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
			
			return FrontUtils.findFrontTpl(request, response, model, SCREEN_RECORD_LIST);
		}
		/**
	     * 手机闪屏POST
	     *
	     * @param request
	     * @param response
	     * @throws ClientException 
	     */
		@RequestMapping(value = "/cms/screen/list.htm", method = RequestMethod.POST)
		public String screenRecordListPOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) throws IllegalAccessException
		{
			PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
			List<ScreenAdvert> advertList = screenAdvertService.queryScreenAdvertList(requestVo);
			PageInfo<ScreenAdvert> pageInfo = new PageInfo<ScreenAdvert>(advertList);
			PageHelperModel.responsePageModel(pageInfo,model);
			model.addAttribute("screenAdvertList",pageInfo.getList());
			
			return FrontUtils.findFrontTpl(request, response, model, SCREEN_RECORD_LIST_TPL);
		}
		/**
		 * 手机闪屏删除
		 * @param request
		 * @param response
		 * @param id
		 * @return
		 */
		@RequestMapping(value="/cms/screen/delete.htm", method = RequestMethod.POST)
		public String screenDelete(HttpServletRequest request, HttpServletResponse response,int id)
		{
			ResponseBase result = ResponseBase.getInitResponse();
			ScreenAdvert advert = screenAdvertService.queryScreenAdvertById(id);
			screenAdvertService.updateScreenAdvertFlag(id);
			//日志记录
			CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
			cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "营销管理-闪屏设置-删除闪屏-（"+advert.getId()+"）",sessionUser.getId());
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponse(Constants.TRUE);
			result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
			ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
			return null;
		}
		/**
		 * 修改、新增闪屏-GET
		 * @param request
		 * @param response
		 * @param model
		 * @author FanCunXin
		 * @return
		 */
		@RequestMapping(value = "/cms/screen/edit.htm", method = RequestMethod.GET)
		public String screenEditGET(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer id) 
		{
			if(!CmsUtils.isNullOrEmpty(id) && id>0)
			{
				ScreenAdvert screenAdvert = screenAdvertService.selectByPrimaryKey(id);
				 model.addAttribute("screenAdvert",screenAdvert);
			}
			return FrontUtils.findFrontTpl(request, response, model, SCREEN_RECORD_EDIT);
		}
		
		/**
		 * 修改、新增闪屏-POST
		 * @param request
		 * @param response
		 * @param model
		 * @author FanCunXin
		 * @return
		 * @throws IOException 
		 * @throws QiniuException 
		 * @throws ParseException 
		 */
		@RequestMapping(value = "/cms/screen/edit.htm", method = RequestMethod.POST)
		public String screenEditPOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) throws QiniuException, IOException, ParseException 
		{
			ResponseBase result = ResponseBase.getInitResponse();
			ScreenAdvert screenAdvert = requestVo.getScreenAdvert();
			
			CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
			Date start =  DateUtils.getDate(requestVo.getStartTime(), Constants.YYYY_MM_DD_HH_MM_SS);
	        Date end =  DateUtils.getDate(requestVo.getEndTime(), Constants.YYYY_MM_DD_HH_MM_SS);
	        if(start.getTime()==end.getTime())
    		{
    			result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg("上架时间不能等于下架时间");
				ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
				return null;
    		}
    		if(start.getTime()>end.getTime())
    		{
    			result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg("上架时间不能小于或等于下架时间");
				ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
				return null;
    		}
    		List<ScreenAdvert> maxScreen1List = screenAdvertService.validateTimeIsNotScreeAdvert(DateUtils.getStringDate(new Date(),Constants.YYYY_MM_DD_HH_MM_SS));
    		for(ScreenAdvert screenAdverts : maxScreen1List)
    		{
    			if(Constants.Number.ONE_TWO_STRING.equals(screenAdverts.getPushPlatform()))
    			{
    				validateGroundingDate(start, end, screenAdverts, result);//验证上下架时间是否冲突
    			}
    			else if(Constants.Number.ONE_STRING.equals(screenAdverts.getPushPlatform()))//ios
    			{
    				if(Constants.Number.ONE_STRING.equals(screenAdvert.getPushPlatform()) || Constants.Number.THREE_STRING.equals(screenAdvert.getPushPlatform()))
    				{
    					validateGroundingDate(start, end, screenAdverts, result);//验证上下架时间是否冲突
    				}
    			}
    			else if(Constants.Number.TWO_STRING.equals(screenAdverts.getPushPlatform()))//ios
    			{
    				if(Constants.Number.TWO_STRING.equals(screenAdvert.getPushPlatform()) || Constants.Number.THREE_STRING.equals(screenAdvert.getPushPlatform()))
    				{
    					validateGroundingDate(start, end, screenAdverts, result);//验证上下架时间是否冲突
    				}
    			}
    		}
			screenAdvert.setBannerUrl(screenAdvert.getBannerUrl());
			screenAdvert.setBannerLink(screenAdvert.getBannerLink());
			
			if(screenAdvert.getStatus() == 2)
			{
				screenAdvert.setTimer(Constants.Number.ZERO_STRING);
			}
	       if(Constants.Number.THREE_STRING.equals(requestVo.getPushPlatform()))
	       {
	       		screenAdvert.setPushPlatform(Constants.Number.ONE_TWO_STRING);
	       }
	       else
	       {
	       		screenAdvert.setPushPlatform(screenAdvert.getPushPlatform());
	       }
			screenAdvert.setStartTime(start);
			screenAdvert.setEndTime(end);
			screenAdvert.setUpdateTime(new Date());
			//修改
			if(!CmsUtils.isNullOrEmpty(screenAdvert.getId()) && screenAdvert.getId() > 0)
			{
				screenAdvertService.updateByPrimaryKeySelective(screenAdvert);
				//日志记录
				cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "营销管理-闪屏设置-修改闪屏-（"+screenAdvert.getId()+"）",sessionUser.getId());
			}
			else
			{
				screenAdvert.setCreateTime(new Date());
				screenAdvertService.insertSelective(screenAdvert);
				//日志记录
				cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "营销管理-闪屏设置-增加闪屏-（"+screenAdvert.getId()+"）",sessionUser.getId());
			}
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponse(Constants.TRUE);
			ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
			//return FrontUtils.findFrontTpl(request, response, model, SCREEN_RECORD_LIST);
			return null;
		}
		/**
		 * 验证上下架时间
		* @Title: validateGroundingDate
		* @Description: TODO()
		* @param @param start
		* @param @param end
		* @param @param screenAdverts
		* @param @param result
		* @param @return    参数
		* @return String    返回类型
		* @author  上海众宁网络科技有限公司-何俊辉
		* @throws
		 */
		public String validateGroundingDate(Date start,Date end,ScreenAdvert screenAdverts,ResponseBase result)
		{
			if(start.after(screenAdverts.getStartTime()) && start.before(screenAdverts.getEndTime()))
			{
				result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg("上架时间冲突，请检查后在提交");
				ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
				return null;
			}
			if(end.after(screenAdverts.getStartTime()) && end.before(screenAdverts.getEndTime()))
			{
				result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg("下架时间冲突，请检查后在提交");
				ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
				return null;
			}
			return null;
		}
		/**
		 * 查询我的菜单资源
		* @Title: myResourceListPost
		* @Description: TODO()
		* @param @param request
		* @param @param response
		* @param @param model
		* @param @return    参数
		* @return String    返回类型
		* @author  上海众宁网络科技有限公司-何俊辉
		* @throws
		 */
		@RequestMapping(value = "/query/my/resource.htm", method = RequestMethod.POST)
		public String myResourceListPost(HttpServletRequest request,HttpServletResponse response,ModelMap model)
		{
			ResponseBase result = ResponseBase.getInitResponse();
			CmsUser user = (CmsUser)SecurityUtils.getSubject().getPrincipal();
			result.push("resourceList",user.getRole().getResourceList());
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponse(Constants.TRUE);
			ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
			return null;
		}
		/**
		 * excel导出
		* @Title: excelExport
		* @Description: TODO()
		* @param @param request
		* @param @param response
		* @param @param model
		* @param @return    参数
		* @return String    返回类型
		* @author  上海众宁网络科技有限公司-何俊辉
		* @throws
		 */
		@RequestMapping(value = "/excel/export.htm", method = RequestMethod.GET)
		public String excelExportGET(HttpServletRequest request,HttpServletResponse response,ModelMap model,String act)
		{
			if("customer".equals(act))
			{
				List<Category> categoryList = categoryService.queryCategoryList(new CmsRequestVo());
				model.addAttribute("categoryUserList",categoryList);
			}
			else if("withdraw".equals(act))
			{
				String str = FindBankUtils.read(CmsController.class.getResourceAsStream("/config/bank.json"));
				JSONArray json = new JSONArray().parseArray(str);
				model.addAttribute("bankList",json);
			}
			model.addAttribute("act",act);
			return FrontUtils.findFrontTpl(request, response, model, EXCEL_EXPORT);
		}
		/**
		 * 导出Excel
		 * @param request
		 * @param response
		 * @param model
		 * @param exprotVo
		 * @return
		 * @throws ParsePropertyException
		 * @throws InvalidFormatException
		 * @throws IOException
		 */
		@RequestMapping(value = "/excel/export.htm", method = RequestMethod.POST)
		public String excelExportPost(HttpServletRequest request,HttpServletResponse response,ModelMap model,ExprotVo exprotVo) throws ParsePropertyException, InvalidFormatException, IOException
		{
		    InputStream is = null;
		    log.info("###正在导出中请稍后###");
		    long startTime = System.currentTimeMillis();
		    Map<String,Object> beans = new HashMap<String,Object>();
			if("customer".equals(exprotVo.getAct()))//客户导出
			{
				long userStartTime = System.currentTimeMillis();
				List<User> userList = userService.queryExprotUserList(exprotVo);
				long userEndTime = System.currentTimeMillis();
				log.info("###数据查询耗时,"+(userEndTime - userStartTime) / 1000 + "秒");
				List<ExprotVo> vo = new ArrayList<ExprotVo>();
				for(User user : userList)//数据遍历
				{
					ExprotVo exprotVo1 = new ExprotVo();
					exprotVo1.setUser(user);
					if(!CmsUtils.isNullOrEmpty(user.getCategoryList()) && user.getCategoryList().size() > 0)
					{
						StringBuffer categoryName = new StringBuffer();
						for(Category category : user.getCategoryList())
						{
							categoryName.append(category.getCategoryName()+"/");
						}
						exprotVo1.setCategoryName(categoryName.toString().substring(0, categoryName.length() - 1));
					}
					if(!CmsUtils.isNullOrEmpty(user.getSex()))
					{
						if("M".equals(user.getSex()))//男
						{
							exprotVo1.setSex("男");
						}
						else //女
						{
							exprotVo1.setSex("女");
						}
					}
					if(!CmsUtils.isNullOrEmpty(user.getRole()) && user.getRole() != 0)
					{
						if(user.getRole() == 1)
						{
							exprotVo1.setRoleName("工人");
						}
						else
						{
							exprotVo1.setRoleName("工头");
						}
					}
					if(!CmsUtils.isNullOrEmpty(user.getRegisterChannel()) && user.getRegisterChannel() != 0)
					{
						if(user.getRegisterChannel() == 1)
						{
							exprotVo1.setRegisterChannelName("公众号");
						}
						else if(user.getRegisterChannel() == 2)
						{
							exprotVo1.setRegisterChannelName("APP");
						}
						else if(user.getRegisterChannel() == 3)
						{
							exprotVo1.setRegisterChannelName("小程序");
						}
					}
					if(user.getIsAttestation() > 0)
					{
						exprotVo1.setIsAttestation("已认证");
					}
					else
					{
						exprotVo1.setIsAttestation("未认证");
					}
					if(user.getStatus() > 0)
					{
						exprotVo1.setJobStatusName("在职");
					}
					else
					{
						exprotVo1.setJobStatusName("空闲");
					}
					vo.add(exprotVo1);
				}
				beans.put("exprotVo", vo);
		        beans.put("total", userList.size());
		        is = new BufferedInputStream(CmsController.class.getResourceAsStream("/excel/customer.xlsx"));
			}
			else if("withdraw".equals(exprotVo.getAct()))//提现列表
			{
				long userStartTime = System.currentTimeMillis();
				RequestVo requestVo = new RequestVo();
				requestVo.setName(exprotVo.getRealName());
				requestVo.setPhone(exprotVo.getPhone());
				requestVo.setBankName(exprotVo.getBankName());
				requestVo.setStartTime(exprotVo.getStartTime());
				requestVo.setEndTime(exprotVo.getEndTime());
				requestVo.setPageSize(99999999);
				requestVo.setStatus(exprotVo.getWithdrawStatus());
				List<UserWithdraw> withdrawList = userWithdrawService.queryWithdrawRecord(requestVo);
				long userEndTime = System.currentTimeMillis();
				log.info("###数据查询耗时,"+(userEndTime - userStartTime) / 1000 + "秒");
				List<ExprotVo> vo = new ArrayList<ExprotVo>();
				for(UserWithdraw user : withdrawList)//数据遍历
				{
					ExprotVo exprotVo1 = new ExprotVo();
					if(!CmsUtils.isNullOrEmpty(user.getUser()))
					{
						exprotVo1.setUserId(user.getUser().getId());
						exprotVo1.setUserName(user.getUser().getRealName());
						exprotVo1.setPhone(user.getUser().getPhone());
					}
					exprotVo1.setBankName(user.getBankName());
					exprotVo1.setBankOn(user.getBankOn());
					exprotVo1.setBankType(user.getBankType());
					exprotVo1.setMoney(user.getMoney());
					if(user.getWithdrawStatus() == 0)
					{
						exprotVo1.setStatus("审核中");
					}
					else if(user.getWithdrawStatus() == 1)
					{
						exprotVo1.setStatus("客服审核成功");
					}
					else if(user.getWithdrawStatus() == 2)
					{
						exprotVo1.setStatus("客服审核失败");
					}
					else if(user.getWithdrawStatus() == 3)
					{
						exprotVo1.setStatus("财务审核成功");
					}
					else if(user.getWithdrawStatus() == 4)
					{
						exprotVo1.setStatus("财务审核失败");
					}
					else if(user.getWithdrawStatus() == 5)
					{
						exprotVo1.setStatus("财经审核成功");
					}
					else if(user.getWithdrawStatus() == 6)
					{
						exprotVo1.setStatus("财经审核失败");
					}
					else if(user.getWithdrawStatus() == 7)
					{
						exprotVo1.setStatus("boss审核成功");
					}
					else if(user.getWithdrawStatus() == 8)
					{
						exprotVo1.setStatus("boss审核失败");
					}
					else if(user.getWithdrawStatus() == 9)
					{
						exprotVo1.setStatus("打款成功");
					}
					exprotVo1.setCreateTime(DateUtils.getStringDate(user.getCreateTime(), Constants.YYYY_MM_DD_HH_MM_SS));
					vo.add(exprotVo1);
				}
				beans.put("exprotVo", vo);
		        beans.put("total", withdrawList.size());
		        is = new BufferedInputStream(CmsController.class.getResourceAsStream("/excel/withdraw.xlsx"));
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			beans.put("dateFormat", dateFormat); 
			// 生成excel
			XLSTransformer transformer = new XLSTransformer();
			Workbook wb = transformer.transformXLS(is, beans);
			// 清空response
			response.reset();
			// 设置response的Header
			response.setCharacterEncoding("UTF-8");
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/vnd.ms-excel");
			wb.write(toClient);
			toClient.flush();
			toClient.close();
			long endTime = System.currentTimeMillis();
			log.info("###导出成功，总共耗时"+(endTime - startTime) / 1000+"秒");
		    return null;
		}
		/**
		 * 查询问题库列表GET
		 * @param request
		 * @param response
		 * @param model
		 * @return
		 */
		@RequestMapping(value = "/query/problemLib/list.htm", method = RequestMethod.GET)
		public String queryProblemLibListGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) 
		{
			return FrontUtils.findFrontTpl(request, response, model, PROBLEM_LIB_LIST);
		}
		
		/**
		 * 查询问题库列表POST
		 * @param request
		 * @param response
		 * @param model
		 * @param requestVo
		 * @return
		 */
	    @RequestMapping(value = "/query/problemLib/list.htm", method = RequestMethod.POST)
	    public String queryProblemLibListPost(HttpServletRequest request, HttpServletResponse response,ModelMap model,RequestVo requestVo)
	    {
	    	PageHelper.startPage(requestVo.getCurrentPage(), 20);
	    	List<ProblemLib> problemLibList = problemLibService.findProblemLibList(requestVo);
	    	PageInfo<ProblemLib> pageInfo = new PageInfo<ProblemLib>(problemLibList);
			PageHelperModel.responsePageModel(pageInfo,model);
			model.addAttribute("problemLibList",pageInfo.getList());
			return FrontUtils.findFrontTpl(request, response, model, PROBLEM_LIB_LIST_TPL);
	    }
	    
	    /**
		 * 查询问题库详情GET
		 * @param request
		 * @param response
		 * @param model
		 * @return
		 */
		@RequestMapping(value = "/query/problemLib/detail.htm", method = RequestMethod.GET)
		public String queryProblemLibDetail(HttpServletRequest request,HttpServletResponse response,ModelMap model,int id) 
		{	
			ProblemLib lib = problemLibService.queryProblemLibDetails(id);
			model.addAttribute("lib", lib);
			return FrontUtils.findFrontTpl(request, response, model, PROBLEM_LIB_DETAIL);
		}
		
		/**
		 * 删除问题POST
		 * @param request
		 * @param response
		 * @param model
		 * @return
		 */
		@RequestMapping(value = "/problem/lib/delete.htm", method = RequestMethod.POST)
		public String problemLibDelete(HttpServletRequest request,HttpServletResponse response,ModelMap model,int id) 
		{	
			ResponseBase result=ResponseBase.getInitResponse();
			
			ProblemLib pro = problemLibService.selectByPrimaryKey(id);
			problemLibService.deleteProblemLib(id);
			//日志记录
    		CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
    		cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "系统设置-问题库-问题库删除-（"+pro.getProblemTitle()+"）",sessionUser.getId());
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
			ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
			return null;
		}
		
		/**
		 * 问题库-新增修改GET
		 * @param request
		 * @param response
		 * @param model
		 * @return
		 */
		@RequestMapping(value = "/problem/lib/edit.htm", method = RequestMethod.GET)
		public String problemLibEditGet(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer id) 
		{
			if(id != null && id > 0)
			{
				ProblemLib lib = problemLibService.queryProblemLibDetails(id);
				model.addAttribute("problemLib", lib);
			}
			return FrontUtils.findFrontTpl(request, response, model, PROBLEM_LIB_EDIT);
		}
		
		/**
		 * 问题库-新增修改GET
		 * @param request
		 * @param response
		 * @param model
		 * @return
		 */
		@RequestMapping(value = "/problem/lib/edit.htm", method = RequestMethod.POST)
		public String problemLibEditPOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) 
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
	    		//日志记录
	    		CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
	    		cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "系统设置-问题库-问题库编辑-（"+pro.getProblemTitle()+"）",sessionUser.getId());

	    	}
	    	else
	    	{
	    		problemLibService.insertSelective(pro);
	    		//日志记录
	    		CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
	    		cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "系统设置-问题库-问题库添加-（"+pro.getProblemTitle()+"）",sessionUser.getId());
	    	}
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("保存成功");
			ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
			return null;
		}
		/**
		 * 404页面
		* @Title: error404
		* @Description: TODO()
		* @param @param request
		* @param @param response
		* @param @param model
		* @param @return    参数
		* @return String    返回类型
		* @author  上海众宁网络科技有限公司-何俊辉
		* @throws
		 */
		@RequestMapping(value = "/cms/404.htm", method = RequestMethod.GET)
		public String error404(HttpServletRequest request,HttpServletResponse response,ModelMap model) 
		{
			return FrontUtils.findFrontTpl(request, response, model, "/admin/error/404.html");
		}
		/**
		 * 500页面
		* @Title: error500
		* @Description: TODO()
		* @param @param request
		* @param @param response
		* @param @param model
		* @param @return    参数
		* @return String    返回类型
		* @author  上海众宁网络科技有限公司-何俊辉
		* @throws
		 */
		@RequestMapping(value = "/cms/500.htm", method = RequestMethod.GET)
		public String error500(HttpServletRequest request,HttpServletResponse response,ModelMap model) 
		{
			return FrontUtils.findFrontTpl(request, response, model, "/admin/error/500.html");
		}
		/**
		 * 403页面
		* @Title: error403
		* @Description: TODO()
		* @param @param request
		* @param @param response
		* @param @param model
		* @param @return    参数
		* @return String    返回类型
		* @author  上海众宁网络科技有限公司-何俊辉
		* @throws
		 */
		@RequestMapping(value = "/cms/403.htm", method = RequestMethod.GET)
		public String error403(HttpServletRequest request,HttpServletResponse response,ModelMap model) 
		{
			return FrontUtils.findFrontTpl(request, response, model, "/admin/error/403.html");
		}
		/**
		 * 查询发布项目信息
		* @Title: cmsRecruitCount
		* @Description: TODO()
		* @param @param request
		* @param @param response
		* @param @param model
		* @param @return    参数
		* @return String    返回类型
		* @author  上海众宁网络科技有限公司-何俊辉
		* @throws
		 */
		@RequestMapping(value = "/cms/recruit/count.htm", method = RequestMethod.POST)
		public String cmsRecruitCount(HttpServletRequest request,HttpServletResponse response,ModelMap model) 
		{
			ResponseBase result = ResponseBase.getInitResponse();
			List<Map<String,Object>> findRecruitCount = cmsService.findRecruitCount();
			result.push("recruitList", JsonUtil.list2jsonToArray(findRecruitCount));
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
			ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
			return null;
		}
		/**
		 * 查询用户注册信息
		* @Title: cmsUserCount
		* @Description: TODO()
		* @param @param request
		* @param @param response
		* @param @param model
		* @param @return    参数
		* @return String    返回类型
		* @author  上海众宁网络科技有限公司-何俊辉
		* @throws
		 */
		@RequestMapping(value = "/cms/user/count.htm", method = RequestMethod.POST)
		public String cmsUserCount(HttpServletRequest request,HttpServletResponse response,ModelMap model) 
		{
			ResponseBase result = ResponseBase.getInitResponse();
			List<Map<String,Object>> findUserCount = cmsService.findUserCount();
			result.push("userList", JsonUtil.list2jsonToArray(findUserCount));
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
			ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
			return null;
		}
		
		/**
		 * 注册
		 * @param request
		 * @param response
		 * @param model
		 * @author FanCunXin
		 * @return
		 */
		@RequestMapping(value = "/cms/register.htm", method = RequestMethod.POST)
		public String cmsRegisterPOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) 
		{
			ResponseBase result = ResponseBase.getInitResponse();
			CmsUser beforeUser = cmsService.findCmsUserByUserName(requestVo.getRegisterUserName());
			if(!CmsUtils.isNullOrEmpty(beforeUser))
			{
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponse(Constants.FALSE);
				result.setResponseMsg("用户名已被注册，请更改用户名");
				ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
				return null;
			}
			CmsUser user  = new CmsUser();
			user.setCreateTime(new Date());
			user.setUpdateTime(new Date());
			user.setLocked(0);
			user.setNickName(requestVo.getRegisterNickName());
			user.setUserName(requestVo.getRegisterUserName());
			if(!CmsUtils.isNullOrEmpty(requestVo.getRegisterPassword()))
			{
				user.setPassword(MD5Utils.generatePasswordMD5(requestVo.getRegisterPassword(), CmsConstants.MD5_SALT));
			}
			user.setAddress(requestVo.getRegisterAddress());
			user.setSex(requestVo.getSex());
			user.setTelphone(requestVo.getRegisterPhone());
			user.setEmail(requestVo.getRegisterEmail());
			user.setBirthday(DateUtils.getDate(requestVo.getRegisterBirthday(), Constants.YYYY_MM_DD));
			cmsService.insertCmsUser(user);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponse(Constants.TRUE);
			result.setResponseMsg(Constants.Prompt.ADD_SUCCESSFUL);
			ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
			return null;
		}
		
}
