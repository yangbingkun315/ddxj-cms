package net.zn.ddxj.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.common.PageHelperModel;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.Category;
import net.zn.ddxj.entity.CmsUser;
import net.zn.ddxj.entity.Recruit;
import net.zn.ddxj.entity.RecruitBanner;
import net.zn.ddxj.entity.RecruitCategory;
import net.zn.ddxj.entity.SiteBespeakConsult;
import net.zn.ddxj.entity.SiteJobDemand;
import net.zn.ddxj.entity.SiteRecruit;
import net.zn.ddxj.entity.SiteSlide;
import net.zn.ddxj.entity.WebRecruit;
import net.zn.ddxj.service.CategoryService;
import net.zn.ddxj.service.CmsService;
import net.zn.ddxj.service.RecruitService;
import net.zn.ddxj.service.SiteService;
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

import com.aliyuncs.exceptions.ClientException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiniu.common.QiniuException;

@Controller
public class WebSiteContrroller extends BaseController {
	private static final String SLIDE_RECORD_LIST="/sites/slide_record_list.html";
	private static final String SLIDE_RECORD_LIST_TPL="/sites/slide_record_list_tpl.html";
	private static final String SLIDE_RECORD_EDIT="/sites/slide_record_edit.html";
	private static final String BESPEAK_RECORD_LIST="/sites/bespeak_record_list.html";
	private static final String BESPEAK_RECORD_LIST_TPL="/sites/bespeak_record_list_tpl.html";
	private static final String RECRUITMENT_RECORD_LIST="/sites/recruitment_record_list.html";
	private static final String RECRUITMENT_RECORD_LIST_TPL="/sites/recruitment_record_list_tpl.html";
	private static final String RECRUITMENT_RECORD_EDIT="/sites/recruitment_record_edit.html";
	private static final String PROJECT_RECORD_LIST="/sites/project_record_list.html";
	private static final String PROJECT_RECORD_LIST_TPL="/sites/project_record_list_tpl.html";
	private static final String WEB_RECRUIT_LIST = "/sites/web_recruit_list.html";
	private static final String WEB_RECRUIT_LIST_TPL = "/sites/web_recruit_list_tpl.html";
	private static final String WEB_RECRUIT_EDIT = "/sites/web_recruit_edit.html";
	
	
	@Autowired
	private SiteService siteService;
	@Autowired
	private CmsService cmsService;
	@Autowired
	private CategoryService categoryService;
	
	/**
	 * 官网幻灯片GET
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sites/slide/list.htm", method = RequestMethod.GET)
	public String slideRecordListGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		
		return FrontUtils.findFrontTpl(request, response, model, SLIDE_RECORD_LIST);
	}
	/**
     * 官网幻灯片POST
     *
     * @param request
     * @param response
     * @throws ClientException 
     */
	@RequestMapping(value = "/sites/slide/list.htm", method = RequestMethod.POST)
	public String slideRecordListPOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) throws IllegalAccessException
	{
		PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
		List<SiteSlide> siteSlideList = siteService.querySiteSlideList(requestVo);
		PageInfo<SiteSlide> pageInfo = new PageInfo<SiteSlide>(siteSlideList);
		PageHelperModel.responsePageModel(pageInfo,model);
		model.addAttribute("siteSlideList",pageInfo.getList());
		
		return FrontUtils.findFrontTpl(request, response, model, SLIDE_RECORD_LIST_TPL);
	}
	/**
	 * 幻灯片删除POST
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/sites/slide/delete.htm", method = RequestMethod.POST)
	public String querySlideImageDelete(HttpServletRequest request, HttpServletResponse response,int id)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		SiteSlide siteSlide = siteService.selectByPrimaryKey(id);
		siteService.updateSiteSlideFlag(id);
		//日志记录
		CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "官网管理-幻灯片列表-删除-（"+siteSlide.getImgName()+"）",sessionUser.getId());
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	/**
	 * 修改、新增幻灯片-GET
	 * @param request
	 * @param response
	 * @param model
	 * @author FanCunXin
	 * @return
	 */
	@RequestMapping(value = "/sites/slide/edit.htm", method = RequestMethod.GET)
	public String slideEditGET(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer slideId) 
	{
		if(!CmsUtils.isNullOrEmpty(slideId) && slideId>0)
		{
			 SiteSlide siteSlide = siteService.selectByPrimaryKey(slideId);
			 model.addAttribute("siteSlide",siteSlide);
		}
		return FrontUtils.findFrontTpl(request, response, model, SLIDE_RECORD_EDIT);
	}
	
	/**
	 * 修改、新增幻灯片-POST
	 * @param request
	 * @param response
	 * @param model
	 * @author FanCunXin
	 * @return
	 * @throws IOException 
	 * @throws QiniuException 
	 */
	@RequestMapping(value = "/sites/slide/edit.htm", method = RequestMethod.POST)
	public String slideEditPOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) throws QiniuException, IOException 
	{
		ResponseBase result = ResponseBase.getInitResponse();
		SiteSlide siteSlide = requestVo.getSiteSlide();
		CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		//修改
		if(!CmsUtils.isNullOrEmpty(siteSlide.getId()) && siteSlide.getId() > 0)
		{
			siteSlide.setUpdateTime(new Date());
			siteSlide.setStartTime(DateUtils.getDate(requestVo.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
			siteSlide.setEndTime(DateUtils.getDate(requestVo.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
			result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
			cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "官网管理-幻灯片列表-修改幻灯片-（"+requestVo.getSiteSlide().getImgName()+"）",sessionUser.getId());
			siteService.updateByPrimaryKeySelective(siteSlide);
		}
		else
		{
			siteSlide.setUpdateTime(new Date());
			siteSlide.setCreateTime(new Date());
			siteSlide.setStartTime(DateUtils.getDate(requestVo.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
			siteSlide.setEndTime(DateUtils.getDate(requestVo.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
			result.setResponseMsg(Constants.Prompt.ADD_SUCCESSFUL);
			cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "官网管理-幻灯片列表-添加幻灯片-（"+requestVo.getSiteSlide().getImgName()+"）",sessionUser.getId());
			siteService.insertSelective(siteSlide);	
		}
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	/**
	 * 预约咨询GET
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sites/bespeak/list.htm", method = RequestMethod.GET)
	public String bespeakRecordListGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		
		return FrontUtils.findFrontTpl(request, response, model, BESPEAK_RECORD_LIST);
	}
	/**
     * 预约咨询POST
     *
     * @param request
     * @param response
     * @throws ClientException 
     */
	@RequestMapping(value = "/sites/bespeak/list.htm", method = RequestMethod.POST)
	public String bespeakRecordListPOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) throws IllegalAccessException
	{
		PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
		List<SiteBespeakConsult> siteBespeakConsultList = siteService.siteBespeakConsultList(requestVo);
		PageInfo<SiteBespeakConsult> pageInfo = new PageInfo<SiteBespeakConsult>(siteBespeakConsultList);
		PageHelperModel.responsePageModel(pageInfo,model);
		model.addAttribute("siteBespeakConsultList",pageInfo.getList());
		
		return FrontUtils.findFrontTpl(request, response, model, BESPEAK_RECORD_LIST_TPL);
	}
	/**
	 * 预约咨询删除POST
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/sites/bespeak/delete.htm", method = RequestMethod.POST)
	public String bespeakRecordDelete(HttpServletRequest request, HttpServletResponse response,int id)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		SiteBespeakConsult bespeakConsult = siteService.querybespeakById(id);
		siteService.updateSiteBespeakConsult(id);
		//添加日志记录
		CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "官网管理-预约咨询-咨询删除-（"+bespeakConsult.getName()+"）",sessionUser.getId());
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	/**
	 * 预约受理POST
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/sites/bespeak/update.htm",method=RequestMethod.POST)
	public String bespeakRecordUpdate(HttpServletRequest request, HttpServletResponse response,int id)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		SiteBespeakConsult site = new SiteBespeakConsult();
		site.setId(id);
		site.setStatus(2);
		site.setUpdateTime(new Date());
		siteService.updateSiteBespeakConsult(site);
		SiteBespeakConsult bespeakConsult = siteService.querybespeakById(id);
		//添加日志记录
		CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "官网管理-预约咨询-咨询受理-（"+bespeakConsult.getName()+"）",sessionUser.getId());
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	/**
	 * 招贤纳士GET
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sites/recruitment/list.htm", method = RequestMethod.GET)
	public String recruitmentRecordListGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		
		return FrontUtils.findFrontTpl(request, response, model, RECRUITMENT_RECORD_LIST);
	}
	/**
     * 招贤纳士POST
     *
     * @param request
     * @param response
     * @throws ClientException 
     */
	@RequestMapping(value = "/sites/recruitment/list.htm", method = RequestMethod.POST)
	public String recruitmentRecordListPOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) throws IllegalAccessException
	{
		PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
		List<SiteRecruit> siteRecruitList = siteService.queryAllSiteRecruit(requestVo);
		PageInfo<SiteRecruit> pageInfo = new PageInfo<SiteRecruit>(siteRecruitList);
		PageHelperModel.responsePageModel(pageInfo,model);
		model.addAttribute("siteRecruitList",pageInfo.getList());
		
		return FrontUtils.findFrontTpl(request, response, model, RECRUITMENT_RECORD_LIST_TPL);
	}
	/**
	 * 招贤纳士删除POST
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/sites/recruitment/delete.htm", method = RequestMethod.POST)
	public String queryRecruitmentDelete(HttpServletRequest request, HttpServletResponse response,int id)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		SiteRecruit recruitDetail = siteService.querySiteRecruitDetail(id);
		siteService.delSiteRecruit(id);
		//添加日志记录
		CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "官网管理-招贤纳士-删除职位-（"+recruitDetail.getRecruitName()+"）",sessionUser.getId());
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	/**
	 * 修改、新增职位-GET
	 * @param request
	 * @param response
	 * @param model
	 * @author FanCunXin
	 * @return
	 */
	@RequestMapping(value = "/sites/recruitment/edit.htm", method = RequestMethod.GET)
	public String recruitmentEditGET(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer id) 
	{
		if(!CmsUtils.isNullOrEmpty(id) && id>0)
		{
			 SiteRecruit siteRecruit = siteService.querySiteRecruitDetail(id);
			 model.addAttribute("siteRecruit",siteRecruit);
		}
		return FrontUtils.findFrontTpl(request, response, model, RECRUITMENT_RECORD_EDIT);
	}
	
	/**
	 * 修改、新增职位-POST
	 * @param request
	 * @param response
	 * @param model
	 * @author FanCunXin
	 * @return
	 * @throws IOException 
	 * @throws QiniuException 
	 */
	@RequestMapping(value = "/sites/recruitment/edit.htm", method = RequestMethod.POST)
	public String recruitmentEditPOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) throws QiniuException, IOException 
	{
		ResponseBase result = ResponseBase.getInitResponse();
		SiteRecruit siteRecruit = requestVo.getSiteRecruit();
	    CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		//修改
		if(!CmsUtils.isNullOrEmpty(siteRecruit.getId()) && siteRecruit.getId() > 0)
		{
			siteRecruit.setUpdateTime(new Date());
			result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
		    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "官网管理-招贤纳士-修改职位-（"+requestVo.getSiteRecruit().getRecruitName()+"）",sessionUser.getId());
			siteService.updateSiteRecruit(siteRecruit);
		}
		else
		{
			siteRecruit.setUpdateTime(new Date());
			siteRecruit.setCreateTime(new Date());
			result.setResponseMsg(Constants.Prompt.ADD_SUCCESSFUL);
		    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "官网管理-招贤纳士-添加职位-（"+requestVo.getSiteRecruit().getRecruitName()+"）",sessionUser.getId());
		    siteService.addSiteRecruit(siteRecruit);	
		}
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	/**
	 * 工程需求GET
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/sites/project/list.htm", method = RequestMethod.GET)
	public String projectRecordListGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		
		return FrontUtils.findFrontTpl(request, response, model, PROJECT_RECORD_LIST);
	}
	/**
     * 工程需求POST
     *
     * @param request
     * @param response
     * @throws ClientException 
     */
	@RequestMapping(value = "/sites/project/list.htm", method = RequestMethod.POST)
	public String projectRecordListPOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) throws IllegalAccessException
	{
		PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
		List<SiteJobDemand> demandList = siteService.querySiteJobDemandList(requestVo);
		PageInfo<SiteJobDemand> pageInfo = new PageInfo<SiteJobDemand>(demandList);
		PageHelperModel.responsePageModel(pageInfo,model);
		model.addAttribute("demandList",pageInfo.getList());
		
		return FrontUtils.findFrontTpl(request, response, model, PROJECT_RECORD_LIST_TPL);
	}
	/**
	 * 工程需求删除POST
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/sites/project/delete.htm", method = RequestMethod.POST)
	public String projectRecordDelete(HttpServletRequest request, HttpServletResponse response,int id)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		SiteJobDemand jobDemand = siteService.querySiteJobDemandDetail(id);
		//日志记录
	    CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
	    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "官网管理-工程需求-删除需求-（"+jobDemand.getCategoryName()+"）",sessionUser.getId());
		siteService.delSiteJobDemand(id);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	/**
	 * 工程需求受理POST
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/sites/project/update.htm",method=RequestMethod.POST)
	public String projectRecordUpdate(HttpServletRequest request, HttpServletResponse response,int id)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		SiteJobDemand siteJobDemand=new SiteJobDemand();
		siteJobDemand.setId(id);
		siteJobDemand.setStatus(2);
		siteJobDemand.setUpdateTime(new Date());
		siteService.updateSiteJobDemandStatus(siteJobDemand);
		SiteJobDemand jobDemand = siteService.querySiteJobDemandDetail(id);
		//日志记录
	    CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
	    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "官网管理-工程需求-受理需求-（"+jobDemand.getCategoryName()+"）",sessionUser.getId());
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	
	/**
	 * 官网招聘列表 GET
	 * @param request
	 * @param response
	 * @param model
	 * @author fancunxin
	 * @return
	 */
	@RequestMapping(value="/web/recruit/list.htm", method = RequestMethod.GET)
	public String webRecruitListGet(HttpServletRequest request,HttpServletResponse response,ModelMap model)
	{
		return FrontUtils.findFrontTpl(request, response, model, WEB_RECRUIT_LIST);
	}
	
	/**
	 * 官网招聘列表 POST
	 * @param request
	 * @param response
	 * @param model
	 * @author fancunxin
	 * @return
	 */
	@RequestMapping(value="/web/recruit/list.htm", method = RequestMethod.POST)
	public String webRecruitListPost(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo)
	{
		PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
		List<WebRecruit> webRecruitList = siteService.queryWebRecruitList(requestVo);
		PageInfo<WebRecruit> pageInfo = new PageInfo<WebRecruit>(webRecruitList);
		PageHelperModel.responsePageModel(pageInfo, model);
		model.addAttribute("webRecruitList",webRecruitList);
		return FrontUtils.findFrontTpl(request, response, model, WEB_RECRUIT_LIST_TPL);
	}
	
	/**
	 * 官网招聘信息-新增/修改 GET
	 * @param request
	 * @param response
	 * @param model
	 * @param id
	 * @author fancunxin
	 * @return
	 */
	@RequestMapping(value="/web/recruit/edit.htm", method = RequestMethod.GET)
	public String webRecruitEditGet(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer id)
	{
		List<Category> categoryList = categoryService.getCategoryByType(2);
		model.addAttribute("categoryUserList", categoryList);
		if(id != null && id > 0)
		{
			WebRecruit recruit = siteService.queryWebRecruitById(id);
			model.addAttribute("webRecruit",recruit);
		}
		return FrontUtils.findFrontTpl(request, response, model, WEB_RECRUIT_EDIT);
	}
	
	/**
	 * 官网招聘信息-新增/修改 POST
	 * @param request
	 * @param response
	 * @param model
	 * @param id
	 * @author fancunxin
	 * @return
	 */
	@RequestMapping(value="/web/recruit/edit.htm", method = RequestMethod.POST)
	public String webRecruitEditPost(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		WebRecruit recruit = requestVo.getWebRecruit();
		if(recruit == null)
		{
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("招聘信息不存在");
			ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		}
		recruit.setStartTime(DateUtils.getDate(requestVo.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
		recruit.setEndTime(DateUtils.getDate(requestVo.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
		recruit.setStopTime(DateUtils.getDate(requestVo.getStopTime(), "yyyy-MM-dd HH:mm:ss"));
		
		if(recruit.getId() == null || recruit.getId() <= 0)
		{
			recruit.setValidateStatus(Constants.Number.THREE_INT);
			recruit.setRecruitStatus(Constants.Number.ONE_INT);
			recruit.setCreateTime(new Date());
			recruit.setUpdateTime(new Date());
			siteService.insertWebRecruit(recruit);
			//日志记录
		    CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "官网管理-官网招聘-新增-（"+recruit.getRecruitTitle()+"）",sessionUser.getId());
		    
		    result.setResponseCode(Constants.SUCCESS_200);
			result.setResponse(Constants.TRUE);
			result.setResponseMsg(Constants.Prompt.ADD_SUCCESSFUL);
			ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
			return null;
		}
		else
		{
			siteService.updateWebRecruit(recruit);
			//日志记录
		    CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "官网管理-官网招聘-修改-（"+recruit.getRecruitTitle()+"）",sessionUser.getId());
		    
		    result.setResponseCode(Constants.SUCCESS_200);
			result.setResponse(Constants.TRUE);
			result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
			ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
			return null;
		}
	}
	
	/**
	 * 官网招聘信息删除 POST
	 * @param request
	 * @param response
	 * @param model
	 * @param recruitId
	 * @author fancunxin
	 * @return
	 */
	@RequestMapping(value = "/web/recruit/delete.htm", method = RequestMethod.POST)
	public String recruitRecordDeletePOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer recruitId) throws IllegalAccessException
	{
		ResponseBase result = ResponseBase.getInitResponse();
		
		WebRecruit recruitDetail = siteService.queryWebRecruitById(recruitId);
		siteService.deleteWebRecruitById(recruitId);
		
		//日志记录
	    CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
	    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "官网管理-官网招聘-删除-（"+recruitDetail.getRecruitTitle()+"）",sessionUser.getId());
	    
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	
	/**
	 * 官网招聘信息置顶POST
	 * @param request
	 * @param response
	 * @param model
	 * @param id
	 * @param type
	 * @return
	 */
	@RequestMapping(value="/web/recruit/record/stick.htm", method = RequestMethod.POST)
	public String changeRecruitStick(HttpServletRequest request, HttpServletResponse response,ModelMap model,Integer id,Integer type){
		
		ResponseBase result = ResponseBase.getInitResponse();
		
		WebRecruit recruitDetail = siteService.queryWebRecruitById(id);
		siteService.changeWebRecruitStick(type,id);
		
		//日志记录
	    CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
	    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "官网管理-官网招聘-置顶-（"+recruitDetail.getRecruitTitle()+"）",sessionUser.getId());

		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
		
	}
}
