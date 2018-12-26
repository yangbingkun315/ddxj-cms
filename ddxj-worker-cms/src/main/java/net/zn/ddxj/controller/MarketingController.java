package net.zn.ddxj.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.common.PageHelperModel;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.AppMenu;
import net.zn.ddxj.entity.CmsUser;
import net.zn.ddxj.entity.Information;
import net.zn.ddxj.entity.InformationCategory;
import net.zn.ddxj.entity.InformationType;
import net.zn.ddxj.entity.InviteLink;
import net.zn.ddxj.entity.InviteRecord;
import net.zn.ddxj.entity.InviteSetting;
import net.zn.ddxj.service.AppMenuService;
import net.zn.ddxj.service.CmsService;
import net.zn.ddxj.service.InformationService;
import net.zn.ddxj.service.InviteLinkService;
import net.zn.ddxj.service.InviteRecordService;
import net.zn.ddxj.service.InviteSettingService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.DateUtils;
import net.zn.ddxj.utils.FrontUtils;
import net.zn.ddxj.utils.ResponseUtils;
import net.zn.ddxj.utils.json.JsonUtil;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiniu.common.QiniuException;
/**
 * 
 * @ClassName:  CodeServer   
 * @Description:TODO(营销管理)   
 * @author: 何俊辉 
 * @date:   2018年8月9日 下午4:44:47   
 * @Copyright: 2018 www.diandxj.com Inc. All rights reserved. 
 * 注意：本内容仅限于上海众宁网络科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Controller
public class MarketingController extends BaseController {
	
	private static final String INFORMATION_LIST = "/marketing/information_list.html";
	private static final String INFORMATION_LIST_TPL = "/marketing/information_list_tpl.html";
	private static final String INFORMATION_PREVIEW = "/marketing/information_preview.html";
	private static final String INFORMATION_EDIT = "/marketing/information_edit.html";
	private static final String INVITE_LIST = "/marketing/invite_list.html";
	private static final String INVITE_LIST_TPL = "/marketing/invite_list_tpl.html";
	private static final String INVITE_EDIT = "/marketing/invite_edit.html";
	private static final String INFORMATION_TYPE_LIST = "/marketing/information_type_list.html";
	private static final String INFORMATION_TYPE_LIST_TPL = "/marketing/information_type_list_tpl.html";
	private static final String INFORMATION_TYPE_EDIT = "/marketing/information_type_edit.html";
	private static final String INVITE_MANAGER = "/marketing/invite_manager.html";
	private static final String INVITE_MANAGER_TPL = "/marketing/invite_manager_tpl.html";
	private static final String INVITE_DETAIL_LIST = "/marketing/invite_detail_list.html";
	private static final String INVITE_DETAIL_LIST_TPL = "/marketing/invite_detail_list_tpl.html";
	private static final String APP_MENU_LIST = "/marketing/app_menu_list.html";
	private static final String APP_MENU_LIST_TPL = "/marketing/app_menu_list_tpl.html";
	private static final String APP_MENU_EDIT = "/marketing/app_menu_edit.html";
	@Autowired
	private InformationService informationService;
	@Autowired
	private InviteSettingService inviteSettingService;
	@Autowired
	private CmsService cmsService;
	@Autowired
	private InviteLinkService inviteLinkService;
	@Autowired
	private InviteRecordService inviteRecordService;
	@Autowired
	private AppMenuService appMenuService;
	
	
	/**
	 * 首页业务模块管理
	* @Title: appMenuListGet
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/app/menu/list.htm", method = RequestMethod.GET)
	public String appMenuListGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		List<AppMenu> appMenuList = appMenuService.queryAppMenuList(new CmsRequestVo());
		model.addAttribute("appMenuList", appMenuList);
		return FrontUtils.findFrontTpl(request, response, model, APP_MENU_LIST);
	}
	
	/**
	 * 首页业务模块管理
	* @Title: appMenuListPost
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/app/menu/list.htm", method = RequestMethod.POST)
	public String appMenuListPost(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) {
		PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
		List<AppMenu> appMenuList = appMenuService.queryAppMenuList(requestVo);
		PageInfo<AppMenu> pageInfo = new PageInfo<AppMenu>(appMenuList);
		PageHelperModel.responsePageModel(pageInfo,model);
		model.addAttribute("appMenuList", pageInfo.getList());
		return FrontUtils.findFrontTpl(request, response, model, APP_MENU_LIST_TPL);
	}
	
	/**
	 * 首页业务模块增改-GET
	 * @param request
	 * @param response
	 * @param model
	 * @author Rao
	 * @return
	 */
	@RequestMapping(value = "/app/menu/edit.htm", method = RequestMethod.GET)
	public String appMentEditGet(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer id) 
	{
		if(!CmsUtils.isNullOrEmpty(id))
		{
			AppMenu appMenu = appMenuService.selectByPrimaryKey(id);
			if(!CmsUtils.isNullOrEmpty(appMenu))
			{
				model.addAttribute("appMenu", appMenu);
			}
		}
		return FrontUtils.findFrontTpl(request, response, model, APP_MENU_EDIT);
	}
	
	/**
	 * 首页业务模块增改-POST
	 * @param request
	 * @param response
	 * @param model
	 * @author Rao
	 * @return
	 */
	@RequestMapping(value = "/app/menu/edit.htm", method = RequestMethod.POST)
	public String appMentEditPost(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) 
	{
		AppMenu appMenu = null;
		CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		if(requestVo.getId() != null && requestVo.getId() > 0)
		{
			appMenu = appMenuService.selectByPrimaryKey(requestVo.getId());
		}else
		{
			appMenu = new AppMenu();
			appMenu.setCreateTime(new Date());
		}
		if(!CmsUtils.isNullOrEmpty(requestVo.getMenuSort()))
		{
			appMenu.setMenuSort(requestVo.getMenuSort());
		}
		if(!CmsUtils.isNullOrEmpty(requestVo.getType()))
		{
			appMenu.setType(requestVo.getType());
		}
		if(!CmsUtils.isNullOrEmpty(requestVo.getTitle()))
		{
			appMenu.setTitle(requestVo.getTitle());
		}
		if(!CmsUtils.isNullOrEmpty(requestVo.getContent()))
		{
			appMenu.setContent(requestVo.getContent());
		}
		if(!CmsUtils.isNullOrEmpty(requestVo.getLogo()))
		{
			appMenu.setLogo(requestVo.getLogo());
		}
		if(!CmsUtils.isNullOrEmpty(requestVo.getMenuUrl()))
		{
			appMenu.setMenuUrl(requestVo.getMenuUrl());
		}
		appMenu.setUpdateTime(new Date());
		if(requestVo.getId() != null && requestVo.getId() > 0)
		{
			appMenuService.updateByPrimaryKeySelective(appMenu);
		    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "营销管理-首页业务模块管理-模块更新-（"+appMenu.getTitle()+"）",sessionUser.getId());
			
		}else
		{
			appMenuService.insertSelective(appMenu);
		    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "营销管理-首页业务模块管理-模块添加-（"+appMenu.getTitle()+"）",sessionUser.getId());
		}
		return FrontUtils.findFrontTpl(request, response, model, APP_MENU_LIST);
	}
	
	 /**
	  * 删除-POST
	  * @param request
	  * @param response
	  * @param model
	  * @param id
	  * @return
	  */
	@RequestMapping(value = "/app/menu/delete.htm", method = RequestMethod.POST)
	public String deleteappMenu(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer id) 
	{
		ResponseBase result = ResponseBase.getInitResponse();
	    CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
	    AppMenu appMenu = appMenuService.selectByPrimaryKey(id);
	    appMenu.setFlag(2);
	    appMenuService.updateByPrimaryKeySelective(appMenu);
	    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "营销管理-首页业务模块管理-模块删除-（"+appMenu.getTitle()+"）",sessionUser.getId());
	    result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	
	/**
	 * 分销设置
	* @Title: inviteListGet
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/marketing/invite/list.htm", method = RequestMethod.GET)
	public String inviteListGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		List<InviteSetting> inviteSettingList = inviteSettingService.queryInviteSettingList(new CmsRequestVo());
		model.addAttribute("inviteList", inviteSettingList);
		return FrontUtils.findFrontTpl(request, response, model, INVITE_LIST);
	}
	
	/**
	 * 分销设置
	* @Title: inviteListPost
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/marketing/invite/list.htm", method = RequestMethod.POST)
	public String inviteListPost(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) {
		PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
		List<InviteSetting> inviteSettingList = inviteSettingService.queryInviteSettingList(requestVo);
		PageInfo<InviteSetting> pageInfo = new PageInfo<InviteSetting>(inviteSettingList);
		PageHelperModel.responsePageModel(pageInfo,model);
		model.addAttribute("inviteList", pageInfo.getList());
		return FrontUtils.findFrontTpl(request, response, model, INVITE_LIST_TPL);
	}
	
	/**t
	 * 修改、新增分销-GET
	 * @param request
	 * @param response
	 * @param model
	 * @author Rao
	 * @return
	 */
	@RequestMapping(value = "/marketing/invite/edit.htm", method = RequestMethod.GET)
	public String inviteEditGet(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer inviteId) 
	{
		if(!CmsUtils.isNullOrEmpty(inviteId))
		{
			InviteSetting inviteSetting = inviteSettingService.findById(inviteId);
			if(!CmsUtils.isNullOrEmpty(inviteSetting))
			{
				model.addAttribute("inviteSetting", inviteSetting);
			}
		}
		return FrontUtils.findFrontTpl(request, response, model, INVITE_EDIT);
	}
	
	/**
	 * 修改、新增分销-POST
	 * @param request
	 * @param response
	 * @param model
	 * @author Rao
	 * @return
	 */
	@RequestMapping(value = "/marketing/invite/edit.htm", method = RequestMethod.POST)
	public String inviteEditPost(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) 
	{
		InviteSetting inviteSetting = null;
	    CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		if(requestVo.getId() != null && requestVo.getId() > 0)
		{
			inviteSetting = inviteSettingService.findById(requestVo.getId());
		}else
		{
			inviteSetting = new InviteSetting();
			inviteSetting.setCreateTime(new Date());
		}
		if(!CmsUtils.isNullOrEmpty(requestVo.getInviteSetting().getLinkUrl()))
		{
			inviteSetting.setLinkUrl(requestVo.getInviteSetting().getLinkUrl());
		}
		if(!CmsUtils.isNullOrEmpty(requestVo.getInviteSetting().getContent()))
		{
			inviteSetting.setContent(requestVo.getInviteSetting().getContent());
		}
		if(!CmsUtils.isNullOrEmpty(requestVo.getInviteSetting().getImg()))
		{
			inviteSetting.setImg(requestVo.getInviteSetting().getImg());
		}	
		inviteSetting.setType(requestVo.getInviteSetting().getType());
		inviteSetting.setTitle(requestVo.getInviteSetting().getTitle());
		inviteSetting.setUpdateTime(new Date());
		if(requestVo.getId() != null && requestVo.getId() > 0)
		{
			inviteSettingService.updateByPrimaryKeySelective(inviteSetting);
		    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "营销管理-分销设置-分销更新-（"+inviteSetting.getTitle()+"）",sessionUser.getId());
			
		}else
		{
			inviteSettingService.insertSelective(inviteSetting);
		    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "营销管理-分销设置-分销添加-（"+inviteSetting.getTitle()+"）",sessionUser.getId());
		}
		return FrontUtils.findFrontTpl(request, response, model, INVITE_LIST);
	}
	
	 /**
	  * 删除-POST
	  * @param request
	  * @param response
	  * @param model
	  * @param id
	  * @return
	  */
		@RequestMapping(value = "/marketing/invite/delete.htm", method = RequestMethod.POST)
		public String deleteInvite(HttpServletRequest request,HttpServletResponse response,ModelMap model,int id) 
		{
			ResponseBase result = ResponseBase.getInitResponse();
		    CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
			InviteSetting inviteSetting = inviteSettingService.findById(id);
			inviteSettingService.deleteInvite(id);
		    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "营销管理-分销设置-分销删除-（"+inviteSetting.getTitle()+"）",sessionUser.getId());
		    result.setResponseCode(Constants.SUCCESS_200);
			result.setResponse(Constants.TRUE);
			result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
			ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
			return null;
		}
	
	/**
	 * 资讯管理
	* @Title: informationListGet
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/information/list.htm", method = RequestMethod.GET)
	public String informationListGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		List<InformationType> catgegoryList = informationService.queryInformationTypeList(new RequestVo());
		model.addAttribute("catgegoryList",catgegoryList);
		return FrontUtils.findFrontTpl(request, response, model, INFORMATION_LIST);
	}
	/**
	 * 资讯列表
	* @Title: informationListPost
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
	@RequestMapping(value = "/information/list.htm", method = RequestMethod.POST)
	public String informationListPost(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) {
		
		PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
    	List<Information> informationList = informationService.queryInformationList(requestVo);
    	PageInfo<Information> pageInfo = new PageInfo<Information>(informationList);
    	PageHelperModel.responsePageModel(pageInfo,model);
		model.addAttribute("informationList",pageInfo.getList());
		return FrontUtils.findFrontTpl(request, response, model, INFORMATION_LIST_TPL);
	}
	/**
	 * 预览资讯
	* @Title: informationPerviewGet
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
	@RequestMapping(value = "/information/preview.htm", method = RequestMethod.GET)
	public String informationPerviewGet(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer id) {
		
		Information information = informationService.selectByPrimaryKey(id);
		model.addAttribute("information",information);
		return FrontUtils.findFrontTpl(request, response, model, INFORMATION_PREVIEW);
	}
	/**
	 * 添加、修改资讯
	* @Title: informationEditGet
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
	@RequestMapping(value = "/information/edit.htm", method = RequestMethod.GET)
	public String informationEditGet(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer id) {
		
		List<InformationType> categoryList = informationService.queryInformationTypeList(new RequestVo());
		model.addAttribute("categoryList",categoryList);
		
		if(!CmsUtils.isNullOrEmpty(id) && id > 0)
		{
			Information information = informationService.selectByPrimaryKey(id);
			model.addAttribute("information",information);
		}
		return FrontUtils.findFrontTpl(request, response, model, INFORMATION_EDIT);
	}
	/**
	 * 添加、修改资讯
	* @Title: informationEditGet
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
	@RequestMapping(value="/information/edit.htm", method = RequestMethod.POST)
    public String informationEditPost(HttpServletRequest request, HttpServletResponse response,CmsRequestVo requestVo)
    { 
    	ResponseBase result = ResponseBase.getInitResponse();
        CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
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
//    	info.setInfoLabel(requestVo.getInfoLabel());
    	info.setStick(requestVo.getStick());
    	if(requestVo.getInfoType() != null && requestVo.getInfoType() > Constants.Number.ZERO_INT)
    	{
    		info.setInfoType(requestVo.getInfoType());
    		if(requestVo.getInfoType() == Constants.Number.TWO_INT)
    		{
    				
    				info.setImageOne(requestVo.getBannerImageList().get(0));
    				info.setImageTwo(requestVo.getBannerImageList().get(1));
    				info.setImageThree(requestVo.getBannerImageList().get(2));
    		}
    		else if(requestVo.getInfoType() == Constants.Number.THREE_INT)
    		{
    			info.setImageOne(requestVo.getVideoUrl());
    		}
    		else if(requestVo.getInfoType() == Constants.Number.FOUR_INT)
    		{
    			info.setImageOne(requestVo.getBannerImageList().get(0));
    		}
    	}
    	info.setStartTime(DateUtils.getDate(requestVo.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
    	info.setEndTime(DateUtils.getDate(requestVo.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
    	info.setUpdateTime(new Date());
    	if(requestVo.getInforId() != null && requestVo.getInforId() > Constants.Number.ZERO_INT)
    	{
    		informationService.updateByPrimaryKeySelective(info);
    		informationService.deleteInformationCategoryByInfoId(info.getId());//删除原有的分类
    	    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "营销管理-咨询管理-咨询修改-（"+info.getInfoTitle()+"）",sessionUser.getId());
    	}
    	else
    	{
    		informationService.insertSelective(info);
    	    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "营销管理-咨询管理-咨询添加-（"+info.getInfoTitle()+"）",sessionUser.getId());

    		
    	}
    	
    	if(!CmsUtils.isNullOrEmpty(requestVo.getCategoryIdList()))//资讯分类
    	{
    		
    		for(Integer categoryId : requestVo.getCategoryIdList())
    		{
    			InformationCategory cay = new InformationCategory();
    			cay.setInfoId(info.getId());
    			cay.setCategoryId(Integer.valueOf(categoryId));
    			cay.setUpdateTime(new Date());
    			cay.setCreateTime(new Date());
    			informationService.addInformationCategory(cay);
    		}
    	}
    	
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("更新资讯成功");
    	ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
    }
	/**
	 * 咨询删除POST
	 * @param request
	 * @param response
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/information/record/delete.htm", method = RequestMethod.POST)
	public String deleteInfo(HttpServletRequest request, HttpServletResponse response,ModelMap model,int id){
		
		ResponseBase result = ResponseBase.getInitResponse();
	    CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		Information information = informationService.selectByPrimaryKey(id);
		informationService.deleteInformation(id);
	    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "营销管理-咨询管理-咨询删除-（"+information.getInfoTitle()+"）",sessionUser.getId());
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	/**
	 * 咨询置顶POST
	 * @param request
	 * @param response
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/information/record/stick.htm", method = RequestMethod.POST)
	public String changeInfoStick(HttpServletRequest request, HttpServletResponse response,ModelMap model,Integer id,Integer type){
		ResponseBase result = ResponseBase.getInitResponse();
		CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		informationService.changeInfoStick(id,type);
		Information information = informationService.selectByPrimaryKey(id);
	    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "营销管理-咨询管理-咨询置顶-（"+information.getInfoTitle()+"）",sessionUser.getId());
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
		
	}
	
	/**
	 * 查询分销管理GET
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/marketing/invite/manager.htm", method = RequestMethod.GET)
	public String queryInviteManagerListGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		return FrontUtils.findFrontTpl(request, response, model, INVITE_MANAGER);
	}
	
	/**
	 * 查询分销详情GET
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/marketing/invite/detail.htm", method = RequestMethod.GET)
	public String queryInviteDetailListGet(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer userId) {
		model.addAttribute("userId", userId);
		return FrontUtils.findFrontTpl(request, response, model, INVITE_DETAIL_LIST);
	}
	
	/**
	 * 查询分销详情POST
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/marketing/invite/detail.htm", method = RequestMethod.POST)
	public String queryInviteDetailListPOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) {
		PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
		List<InviteRecord> inviteRecordList = inviteRecordService.queryInviteRecordCms(requestVo);
		PageInfo<InviteRecord> pageInfo = new PageInfo<InviteRecord>(inviteRecordList);
    	PageHelperModel.responsePageModel(pageInfo,model);
		model.addAttribute("inviteRecordList",pageInfo.getList());
		return FrontUtils.findFrontTpl(request, response, model, INVITE_DETAIL_LIST_TPL);
	}
	
	/**
	 * 查询分销管理POST
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/marketing/invite/manager.htm", method = RequestMethod.POST)
	public String queryInviteManagerListPOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) {
		PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
		List<InviteLink> inviteLinkList = inviteLinkService.queryInviteLinkRecordCms(requestVo);
		PageInfo<InviteLink> pageInfo = new PageInfo<InviteLink>(inviteLinkList);
    	PageHelperModel.responsePageModel(pageInfo,model);
		model.addAttribute("inviteLinkList",pageInfo.getList());
		return FrontUtils.findFrontTpl(request, response, model, INVITE_MANAGER_TPL);
	}
	
	/**
	 * 查询咨询分类列表GET
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/information/type/list.htm", method = RequestMethod.GET)
	public String queryInfoTypeListGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		
		return FrontUtils.findFrontTpl(request, response, model, INFORMATION_TYPE_LIST);
	}
	
	/**
	 * 查询咨询分类列表POST
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/information/type/list.htm", method = RequestMethod.POST)
	public String queryInfoTypeListPost(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) {
		PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
		List<InformationType> infoTypeList = informationService.queryInfoTypeList(requestVo);
		PageInfo<InformationType> pageInfo = new PageInfo<InformationType>(infoTypeList);
    	PageHelperModel.responsePageModel(pageInfo,model);
		model.addAttribute("infoTypeList",pageInfo.getList());
		return FrontUtils.findFrontTpl(request, response, model, INFORMATION_TYPE_LIST_TPL);
	}
	
	/**
	 * 修改、新增咨询分类-GET
	 * @param request
	 * @param response
	 * @param model
	 * @author FanCunXin
	 * @return
	 */
	@RequestMapping(value = "/information/type/edit.htm", method = RequestMethod.GET)
	public String addInfoTypeGet(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer typeId) 
	{
		if(!CmsUtils.isNullOrEmpty(typeId) && typeId>0)
		{
			InformationType informationType = informationService.selectInfoTypeByPrimaryKey(typeId);
			model.addAttribute("informationType",informationType);
		}
		return FrontUtils.findFrontTpl(request, response, model, INFORMATION_TYPE_EDIT);
	}
	
	/**
	 * 修改、新增咨询分类-POST
	 * @param request
	 * @param response
	 * @param model
	 * @author FanCunXin
	 * @return
	 * @throws IOException 
	 * @throws QiniuException 
	 */
	@RequestMapping(value = "/information/type/edit.htm", method = RequestMethod.POST)
	public String addInfoTypePOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo)
	{
		ResponseBase result = ResponseBase.getInitResponse();
	    CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		InformationType InformationType = requestVo.getInformationType();
		//修改
		if(!CmsUtils.isNullOrEmpty(InformationType.getId()) && InformationType.getId() > 0)
		{
			InformationType.setUpdateTime(new Date());
			result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
			informationService.updateInformationCategory(InformationType);
			cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "营销管理-咨询管理-咨询分类-更新（"+InformationType.getName()+"）",sessionUser.getId());
		}
		else
		{
			InformationType.setUpdateTime(new Date());
			InformationType.setCreateTime(new Date());
			result.setResponseMsg(Constants.Prompt.ADD_SUCCESSFUL);
			informationService.insertInformationCategory(InformationType);	
			cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "营销管理-咨询管理-咨询分类-添加（"+InformationType.getName()+"）",sessionUser.getId());
		}
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	
	 /**
	  * 删除资讯分类-POST
	  * @param request
	  * @param response
	  * @param model
	  * @param id
	  * @return
	  */
		@RequestMapping(value = "/information/type/delete.htm", method = RequestMethod.POST)
		public String deleteAdvertBanner(HttpServletRequest request,HttpServletResponse response,ModelMap model,int id) 
		{
			ResponseBase result = ResponseBase.getInitResponse();
			CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
			InformationType informationType = informationService.selectInfoTypeByPrimaryKey(id);
			informationService.deleteInformationCategory(id);
			cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "营销管理-咨询管理-咨询分类-删除（"+informationType.getName()+"）",sessionUser.getId());
	    	result.setResponseCode(Constants.SUCCESS_200);
			result.setResponse(Constants.TRUE);
			result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
			ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
			return null;
		}
		
}
