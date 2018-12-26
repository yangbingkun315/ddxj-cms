package net.zn.ddxj.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.common.PageHelperModel;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.AdvertBanner;
import net.zn.ddxj.entity.CmsUser;
import net.zn.ddxj.service.AdvertBannerService;
import net.zn.ddxj.service.CmsService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.DateUtils;
import net.zn.ddxj.utils.FrontUtils;
import net.zn.ddxj.utils.ResponseUtils;
import net.zn.ddxj.utils.json.JsonUtil;
import net.zn.ddxj.vo.CmsRequestVo;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
public class AdvertController extends BaseController{
	
	private static final String ADVERT_BANNER = "/advert/banner.html";
	private static final String ADVERT_BANNER_TPL = "/advert/banner_tpl.html";
	private static final String ADVERT_BANNER_EDIT = "/advert/banner_edit.html";
	@Autowired
	private AdvertBannerService advertBannerService;
	@Autowired
	private CmsService cmsService;
	
	/**
	 * 轮播图列表GET-TEST
	* @Title: advertBannerListGet
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-饶开宇
	* @throws
	 */
	@RequestMapping(value = "/advert/banner/list.htm", method = RequestMethod.GET)
	public String advertBannerListGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		
		return FrontUtils.findFrontTpl(request, response, model, ADVERT_BANNER);
	}
	
	/**
	 * 轮播图列表POST
	 * @param request
	 * @param response
	 * @param model
	 * @param requestVo
	 * @return
	 */
	@RequestMapping(value = "/advert/banner/list.htm", method = RequestMethod.POST)
	public String  advertBannerListPost(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) {
		PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
		List<AdvertBanner> bannerList = advertBannerService.queryCmsAdvertBannerList(requestVo);
		PageInfo<AdvertBanner> page = new PageInfo<AdvertBanner>(bannerList);
		PageHelperModel.responsePageModel(page,model);
		model.addAttribute("bannerList",page.getList());
		
		return FrontUtils.findFrontTpl(request, response, model, ADVERT_BANNER_TPL);
	}
	
	/**
	 * 修改、新增轮播图-GET
	 * @param request
	 * @param response
	 * @param model
	 * @author Rao
	 * @return
	 */
	@RequestMapping(value = "/advert/banner/edit.htm", method = RequestMethod.GET)
	public String advertBannerEditGet(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer advertBannerId) 
	{
		if(!CmsUtils.isNullOrEmpty(advertBannerId))
		{
			AdvertBanner advertBanner = advertBannerService.selectByPrimaryKey(advertBannerId);
			if(!CmsUtils.isNullOrEmpty(advertBanner))
			{
				model.addAttribute("advertBanner", advertBanner);
			}
		}
		return FrontUtils.findFrontTpl(request, response, model, ADVERT_BANNER_EDIT);
	}
	
	/**
	 * 修改、新增轮播图-POST
	 * @param request
	 * @param response
	 * @param model
	 * @author Rao
	 * @return
	 */
	@RequestMapping(value = "/advert/banner/edit.htm", method = RequestMethod.POST)
	public String advertBannerEditPost(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) 
	{
		AdvertBanner advertBanner = null;
		CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		if(requestVo.getId() != null && requestVo.getId() > 0)
		{
			advertBanner = advertBannerService.selectByPrimaryKey(requestVo.getId());
		}else
		{
			advertBanner = new AdvertBanner();
			advertBanner.setCreateTime(new Date());
		}
		if(!CmsUtils.isNullOrEmpty(requestVo.getBannerUrl()))
		{
			advertBanner.setBannerUrl(requestVo.getBannerUrl());
		}
		else if(!CmsUtils.isNullOrEmpty(requestVo.getVideoUrl()))
		{
			advertBanner.setBannerUrl(requestVo.getVideoUrl());
		}
    	advertBanner.setBannerLink(requestVo.getBannerLink());
    	advertBanner.setBannerType(requestVo.getBannerType());
    	advertBanner.setStartTime(DateUtils.getDate(requestVo.getStartTime(), "yyyy-MM-dd"));
    	advertBanner.setEndTime(DateUtils.getDate(requestVo.getEndTime(), "yyyy-MM-dd"));
    	advertBanner.setUpdateTime(new Date());
    	if(requestVo.getId() != null && requestVo.getId() > 0)
    	{
    		advertBannerService.updateByPrimaryKeySelective(advertBanner);
    		//日志记录
			cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "营销管理-首页轮播图-修改轮播图-（"+advertBanner.getId()+"）",sessionUser.getId());
    	}
    	else
    	{
    		advertBannerService.insertSelective(advertBanner);
    		//日志记录
			cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "营销管理-首页轮播图-添加轮播图-（"+advertBanner.getId()+"）",sessionUser.getId());
    	}
		return FrontUtils.findFrontTpl(request, response, model, ADVERT_BANNER);
	}
	
	 /**
	  * 删除轮播图-POST
	  * @param request
	  * @param response
	  * @param model
	  * @param id
	  * @return
	  */
		@RequestMapping(value = "/advert/banner/delete.htm", method = RequestMethod.POST)
		public String deleteAdvertBanner(HttpServletRequest request,HttpServletResponse response,ModelMap model,int id) 
		{
			ResponseBase result = ResponseBase.getInitResponse();
			AdvertBanner banner = advertBannerService.selectByPrimaryKey(id);
			advertBannerService.deleteAdvertBanne(id);
			//日志记录
			CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
			cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "营销管理-首页轮播图-删除轮播图-（"+banner.getId()+"）",sessionUser.getId());
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponse(Constants.TRUE);
			result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
			ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
			return null;
		}
}
