package net.zn.ddxj.api;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;
import net.zn.ddxj.base.BaseController;
import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.AdvertBanner;
import net.zn.ddxj.entity.SiteBespeakConsult;
import net.zn.ddxj.entity.SiteJobDemand;
import net.zn.ddxj.entity.SiteRecruit;
import net.zn.ddxj.entity.SiteSlide;
import net.zn.ddxj.entity.WebRecruit;
import net.zn.ddxj.service.SiteService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.DateUtils;
import net.zn.ddxj.utils.json.JsonUtil;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;
@RestController
@Slf4j
public class WebSiteContrroller extends  BaseController{
	@Autowired
	private SiteService siteService;
	
	/**
	 * 查询幻灯片列表
	 * @param request
	 * @param response
	 * @param requestVo
	 * @return
	 * @throws IllegalAccessException 
	 */
	@RequestMapping(value = "/index/slide/list.ddxj")
	public ResponseBase queryIndexSlideList(HttpServletRequest request, HttpServletResponse response,CmsRequestVo requestVo) throws IllegalAccessException{
		ResponseBase result=ResponseBase.getInitResponse();
		List<SiteSlide> siteSlideList = siteService.querySiteSlideList(requestVo);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
		result.push("siteSlideList", JsonUtil.list2jsonToArray(siteSlideList));
		return result;
		
	}
	/**
	 * 新增预约咨询
	 * @param request
	 * @param response
	 * @param requestVo
	 * @return
	 */
	@RequestMapping(value = "/add/site/bespeakConsult.ddxj")
	public ResponseBase addSiteBespeakConsult(HttpServletRequest request, HttpServletResponse response, String companyName,String province,String city,String area,String name,String phone){
		ResponseBase result=ResponseBase.getInitResponse();
		SiteBespeakConsult record=new SiteBespeakConsult();
		record.setCompanyName(companyName);
		record.setProvince(province);
		record.setCity(city);
		record.setArea(area);
		record.setName(name);
		record.setPhone(phone);
		record.setCreateTime(new Date());
		record.setUpdateTime(new Date());
		record.setIp(CmsUtils.getIpAddr(request));
		siteService.addSiteBespeakConsult(record);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.ADD_SUCCESSFUL);
		return result;
		
	}
	/**
	 * 新增招贤纳士
	 * @param request
	 * @param response
	 * @param requestVo
	 * @return
	 */
	@RequestMapping(value = "/add/site/recruit.ddxj")
	public ResponseBase addSiteRecruit(HttpServletRequest request, HttpServletResponse response, String recruitImg,String recruitName,String recruitDemand,String recuritDuties,String recruitTenure,String name,String phone,String email){
		ResponseBase result=ResponseBase.getInitResponse();
		SiteRecruit record=new SiteRecruit();
		record.setRecruitImg(recruitImg);
		record.setRecruitName(recruitName);
		record.setRecruitDemand(recruitDemand);
		record.setRecuritDuties(recuritDuties);
		record.setRecruitTenure(recruitTenure);
		record.setName(name);
		record.setPhone(phone);
		record.setEmail(email);
		record.setCreateTime(new Date());
		record.setUpdateTime(new Date());
		siteService.addSiteRecruit(record);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.ADD_SUCCESSFUL);
		return result;
		
	}
	/**
	 * 查询所有招贤列表
	 * @param request
	 * @param response
	 * @param requestVo
	 * @return
	 * @throws IllegalAccessException 
	 */
	@RequestMapping(value = "/query/siteRecruit/list.ddxj")
	public ResponseBase querySiteRecruitList(HttpServletRequest request, HttpServletResponse response, CmsRequestVo requestVo) throws IllegalAccessException{
		ResponseBase result=ResponseBase.getInitResponse();
		List<SiteRecruit> siteRecruitList = siteService.queryAllSiteRecruit(requestVo);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
		result.push("siteRecruitList", JsonUtil.list2jsonToArray(siteRecruitList));
		return result;
		
	}
	/**
	 * 查询招贤详情
	 * @param request
	 * @param response
	 * @param recruitId
	 * @return
	 */
	@RequestMapping(value="/query/siteRecruit/detail.ddxj")
	public ResponseBase querySiteRecruitDetail(HttpServletRequest request, HttpServletResponse response,Integer recruitId){
		ResponseBase result=ResponseBase.getInitResponse();
		SiteRecruit siteRecruit = siteService.querySiteRecruitDetail(recruitId);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
		result.push("siteRecruit", JsonUtil.bean2jsonObject(siteRecruit));
		return result;
	}
	/**
	 * 新增工程需求
	 * @param request
	 * @param response
	 * @param address
	 * @param categoryName
	 * @param startTime
	 * @param endTime
	 * @param demandDesc
	 * @param estimateWages
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/siteJob/demand/add.ddxj")
	public ResponseBase addSiteJobDemand(HttpServletRequest request, HttpServletResponse response, String address,String categoryName,String startTime,String endTime,String demandDesc,String estimateWages,String phone){
		ResponseBase result=ResponseBase.getInitResponse();
		SiteJobDemand record=new SiteJobDemand();
		record.setAddress(address);
		record.setCategoryName(categoryName);
		record.setStartTime(DateUtils.getDate(startTime, "yyyy-MM-dd"));
		record.setEndTime(DateUtils.getDate(endTime, "yyyy-MM-dd"));
		record.setDemandDesc(demandDesc);
		record.setEstimateWages(new BigDecimal(estimateWages));
		record.setPhone(phone);
		record.setCreateTime(new Date());
		record.setUpdateTime(new Date());
		record.setIp(CmsUtils.getIpAddr(request));
		siteService.addSiteJobDemand(record);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.ADD_SUCCESSFUL);
		return result;
		
	}
	/**
	 * 官网多条件查询招聘列表
	 * @param request
	 * @param response
	 * @param province
	 * @param city
	 * @param area
	 * @param categoryName
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="/query/webSite/recruit/list.ddxj")
	public ResponseBase queryWebSiteRecruit(HttpServletRequest request, HttpServletResponse response,String province,String city,String area,String categoryName,Integer pageNum,Integer pageSize){
		ResponseBase result=ResponseBase.getInitResponse();
		Map<String, Object>params=new HashMap<String, Object>();
		params.put("province", province);
		params.put("city", city);
		params.put("area", area);
		params.put("categoryName", categoryName);
		if(CmsUtils.isNullOrEmpty(province))
		{
			province="";
		}
		if(CmsUtils.isNullOrEmpty(city))
		{
			city="";
		}
		if(CmsUtils.isNullOrEmpty(area))
		{
			area="";
		}
		if(CmsUtils.isNullOrEmpty(categoryName))
		{
			categoryName="";
		}
		PageHelper.startPage(pageNum, pageSize);
		List<WebRecruit> webRecruitlist = siteService.queryMultipleConditional(params);
		PageInfo<WebRecruit> page = new PageInfo<WebRecruit>(webRecruitlist);
		long total=page.getTotal();
		result.push("webRecruitlist",JsonUtil.list2jsonToArray(webRecruitlist));
		result.push("total",total);
		result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	return result;
		
	}
	
	/**
	 * 查询官网置顶的招聘列表
	 * @param request
	 * @param response
	 * @return
	 * @throws IllegalAccessException 
	 */
	@RequestMapping(value = "/index/slide/webRecruitList.ddxj")
	public ResponseBase queryIndexRecruitList(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException{
		ResponseBase result=ResponseBase.getInitResponse();
		List<WebRecruit> webRecruitList = siteService.queryWebRecruitListForStick();
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
		result.push("webRecruitList", JsonUtil.list2jsonToArray(webRecruitList));
		return result;
	}
	/**
	 * 查询招聘详情
	 * @param request
	 * @param response
	 * @param recruitId
	 * @return
	 */
	@RequestMapping(value="/query/webSite/recruit/detail.ddxj")
	public ResponseBase queryWebSiteRecruitDetail(HttpServletRequest request, HttpServletResponse response,Integer recruitId){
		ResponseBase result=ResponseBase.getInitResponse();
		WebRecruit WebRecruitDetail = siteService.queryWebSiteRecruitDetail(recruitId);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
		result.push("WebRecruitDetail", JsonUtil.bean2jsonObject(WebRecruitDetail));
		return result;
	}
}
