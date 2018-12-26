package net.zn.ddxj.api;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.SiteBespeakConsult;
import net.zn.ddxj.entity.SiteJobDemand;
import net.zn.ddxj.entity.SiteRecruit;
import net.zn.ddxj.entity.SiteSlide;
import net.zn.ddxj.service.SiteService;
import net.zn.ddxj.tool.AsycService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.DateUtils;
import net.zn.ddxj.utils.json.JsonUtil;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aliyuncs.exceptions.ClientException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@RestController
@Slf4j
/**
 * 官网后台管理controller
* @ClassName: WebSiteContrroller   
* @author FCX
* @date 2018年6月12日  
*
 */
public class WebSiteContrroller {
	
	@Autowired
	private SiteService siteService;
	@Autowired
	private AsycService asycService;
	
	/**
     * 预约咨询列表
     * 
     * @param request
     * @param response
     * @param requestVo
     * @author fancunxin
     * @throws ClientException
     */
	@RequestMapping(value="/site/bespeak/consult/list.ddxj")
	public ResponseBase querySiteBespeakConsultList(HttpServletRequest request, HttpServletResponse response,CmsRequestVo requestVo)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		PageHelper.startPage(requestVo.getPageNum(),requestVo.getPageSize());
		List<SiteBespeakConsult> siteBespeakConsultList = siteService.siteBespeakConsultList(requestVo);
		PageInfo<SiteBespeakConsult> pageInfo = new PageInfo<SiteBespeakConsult>(siteBespeakConsultList);
		if(!CmsUtils.isNullOrEmpty(siteBespeakConsultList))
		{
			result.push("siteBespeakConsultList", pageInfo);
			result.setResponse(Constants.TRUE);
	    	result.setResponseCode(Constants.SUCCESS_200);
	    	result.setResponseMsg("已查到相关记录");
		}
		else
		{
			result.push("siteBespeakConsultList", pageInfo);
			result.setResponse(Constants.TRUE);
	    	result.setResponseCode(Constants.SUCCESS_200);
	    	result.setResponseMsg("未查到相关记录");
		}
		return result;
	}
	
	/**
     * 删除预约咨询
     * 
     * @param request
     * @param response
     * @param requestVo
     * @author fancunxin
     * @throws ClientException
     */
	@RequestMapping(value="/site/bespeak/consult/delete.ddxj")
	public ResponseBase updateSiteBespeakConsult(HttpServletRequest request, HttpServletResponse response,int id)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		siteService.updateSiteBespeakConsult(id);
		result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("删除成功");
		return result;
	}
	
	/**
	 * 受理预约咨询
	 * 
	 * @param request
	 * @param response
	 * @param requestVo
	 * @author fancunxin
	 * @throws ClientException
	 */
	@RequestMapping(value="/site/bespeak/consult/status.ddxj")
	public ResponseBase updateSiteBespeakConsultStatus(HttpServletRequest request, HttpServletResponse response,int id,int status)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		SiteBespeakConsult site = new SiteBespeakConsult();
		site.setId(id);
		site.setStatus(status);
		siteService.updateSiteBespeakConsult(site);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("受理成功");
		return result;
	}
	
	/**
     * 幻灯片列表
     * 
     * @param request
     * @param response
     * @param requestVo
     * @author fancunxin
     * @throws ClientException
     */
	@RequestMapping(value="/site/slide/list.ddxj")
	public ResponseBase querySiteImageList(HttpServletRequest request, HttpServletResponse response,CmsRequestVo requestVo)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		PageHelper.startPage(requestVo.getPageNum(),requestVo.getPageSize());
		List<SiteSlide> siteSlideList = siteService.querySiteSlideList(requestVo);
		PageInfo<SiteSlide> pageInfo = new PageInfo<SiteSlide>(siteSlideList);
		if(!CmsUtils.isNullOrEmpty(siteSlideList))
		{
			result.push("siteSlideList", pageInfo);
			result.setResponse(Constants.TRUE);
	    	result.setResponseCode(Constants.SUCCESS_200);
	    	result.setResponseMsg("已查到相关记录");
		}
		else
		{
			result.push("siteSlideList", pageInfo);
			result.setResponse(Constants.TRUE);
	    	result.setResponseCode(Constants.SUCCESS_200);
	    	result.setResponseMsg("未查到相关记录");
		}
		asycService.pushBaiduLink();
		return result;
	}
	
	/**
     * 幻灯片详情
     * 
     * @param request
     * @param response
     * @param requestVo
     * @author fancunxin
     * @throws ClientException
     */
	@RequestMapping(value="/site/slide/detail.ddxj")
	public ResponseBase querySiteImageDetail(HttpServletRequest request, HttpServletResponse response,int id)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		SiteSlide siteSlide = siteService.selectByPrimaryKey(id);
		result.push("siteSlide", siteSlide);
		result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("查询幻灯片详情成功");
		return result;
	}
	
	/**
     * 删除幻灯片
     * 
     * @param request
     * @param response
     * @param requestVo
     * @author fancunxin
     * @throws ClientException
     */
	@RequestMapping(value="/site/slide/delete.ddxj")
	public ResponseBase querySiteImageDelete(HttpServletRequest request, HttpServletResponse response,int id)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		siteService.updateSiteSlideFlag(id);
		result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("删除成功");
		return result;
	}
	
	/**
	 * 新增/修改幻灯片
	 * 
	 * @param request
	 * @param response
	 * @param requestVo
	 * @author fancunxin
	 * @throws ClientException
	 */
	@RequestMapping(value="/update/site/slide.ddxj")
	public ResponseBase updateSiteSlide(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		SiteSlide siteSlide = null;
		if(requestVo.getId() != null && requestVo.getId() > 0)
		{
			siteSlide = siteService.selectByPrimaryKey(requestVo.getId());
		}
		else
		{
			siteSlide = new SiteSlide();
		}
		siteSlide.setImgName(requestVo.getImgName());
		siteSlide.setImgDesc(requestVo.getImgDesc());
		siteSlide.setImgUrl(requestVo.getImgUrl());
		siteSlide.setStartTime(DateUtils.getDate(requestVo.getStartTime(), "yyyy-MM-dd"));
		siteSlide.setEndTime(DateUtils.getDate(requestVo.getEndTime(), "yyyy-MM-dd"));
		siteSlide.setUpdateTime(new Date());
		
		if(requestVo.getId() != null && requestVo.getId() > 0)
		{
			siteSlide.setId(requestVo.getId());
			siteService.updateByPrimaryKeySelective(siteSlide);
		}
		else
		{
			siteSlide.setCreateTime(new Date());
			siteService.insertSelective(siteSlide);
		}
				
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("更新幻灯片成功");
		return result;
	}
	
	/**
	 * 查询所有招贤纳士列表
	 * @param request
	 * @param response
	 * @param requestVo
	 * @return
	 * @throws IllegalAccessException
	 */
	@RequestMapping(value="/query/siteRecruit/list.ddxj")
	public ResponseBase querySiteRecruitList(HttpServletRequest request, HttpServletResponse response, CmsRequestVo requestVo) throws IllegalAccessException{
		ResponseBase result=ResponseBase.getInitResponse();
		PageHelper.startPage(requestVo.getPageNum(),requestVo.getPageSize());
		List<SiteRecruit> siteRecruitList = siteService.queryAllSiteRecruit(requestVo);
		PageInfo<SiteRecruit> pageInfo = new PageInfo<SiteRecruit>(siteRecruitList);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("查询所有招贤列表成功");
		result.push("siteRecruitList", pageInfo);
		return result;
		
	}
	/**
	 * 新增招贤纳士
	 * @param request
	 * @param response
	 * @param requestVo
	 * @return
	 */
	@RequestMapping(value = "/manager/update/site/recruit.ddxj")
	public ResponseBase addSiteRecruit(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo ){
		ResponseBase result=ResponseBase.getInitResponse();
		SiteRecruit siteRecruit=null;
		if(requestVo.getId()!=null && requestVo.getId()>0)
		{
			siteRecruit=siteService.querySiteRecruitDetail(requestVo.getId());
		}
		else 
		{
			siteRecruit=new SiteRecruit();
		}
		siteRecruit.setRecruitImg(requestVo.getRecruitImg());
		siteRecruit.setRecruitName(requestVo.getRecruitName());
		siteRecruit.setRecruitDemand(requestVo.getRecruitDemand());
		siteRecruit.setRecuritDuties(requestVo.getRecuritDuties());
		siteRecruit.setRecruitTenure(requestVo.getRecruitTenure());
		siteRecruit.setName(requestVo.getName());
		siteRecruit.setPhone(requestVo.getPhone());
		siteRecruit.setEmail(requestVo.getEmail());
		siteRecruit.setUpdateTime(new Date());
		if(requestVo.getId()!=null && requestVo.getId()>0)
		{
			siteRecruit.setId(requestVo.getId());
			siteService.updateSiteRecruit(siteRecruit);
		}
		else
		{
			siteRecruit.setCreateTime(new Date());
			siteService.addSiteRecruit(siteRecruit);
		}
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("添加/更新招聘成功");
		return result;
		
	}
	/**
	 * 删除招贤纳士
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/manager/delete/siteRecruit.ddxj")
	public ResponseBase delSiteRecruit(HttpServletRequest request, HttpServletResponse response, Integer id){
		ResponseBase result=ResponseBase.getInitResponse();		
		siteService.delSiteRecruit(id);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("删除招聘成功");
		return result;
		
	}
	/**
	 * 查询招贤详情
	 * @param request
	 * @param response
	 * @param recruitId
	 * @return
	 */
	@RequestMapping(value="/manager/query/siteRecruit/detail.ddxj")
	public ResponseBase querySiteRecruitDetail(HttpServletRequest request, HttpServletResponse response,Integer recruitId){
		ResponseBase result=ResponseBase.getInitResponse();
		SiteRecruit siteRecruit = siteService.querySiteRecruitDetail(recruitId);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("查询招贤详情成功");
		result.push("siteRecruit", JsonUtil.bean2jsonObject(siteRecruit));
		return result;
	}
	/**
	 * 查询工程详情
	 * @param request
	 * @param response
	 * @param recruitId
	 * @return
	 */
	@RequestMapping(value="/manager/quey/sitJob/detail.ddxj")
	public ResponseBase querySiteJobDemandDetail(HttpServletRequest request, HttpServletResponse response,Integer id){
		ResponseBase result=ResponseBase.getInitResponse();
		siteService.querySiteJobDemandDetail(id);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("查询工程详情成功");
		return result;
	}
	/**
	 * 查询工程列表
	 * @param request
	 * @param response
	 * @param recruitId
	 * @return
	 */
	@RequestMapping(value="/manager/quey/sitJob/list.ddxj")
	public ResponseBase querySiteJobDemandList(HttpServletRequest request, HttpServletResponse response,CmsRequestVo requestVo){
		ResponseBase result=ResponseBase.getInitResponse();
		PageHelper.startPage(requestVo.getPageNum(),requestVo.getPageSize());
		List<SiteJobDemand> demandList = siteService.querySiteJobDemandList(requestVo);
		PageInfo<SiteJobDemand> pageInfo = new PageInfo<SiteJobDemand>(demandList);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.push("demandList", pageInfo);
		result.setResponseMsg("查询工程列表成功");
		return result;
	}
	/**
	 * 删除工程
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/manager/delete/sitJob.ddxj")
	public ResponseBase delSiteJobDemand(HttpServletRequest request, HttpServletResponse response, Integer id){
		ResponseBase result=ResponseBase.getInitResponse();		
		siteService.delSiteJobDemand(id);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("删除招聘成功");
		return result;
		
	}
	/**
	 * 新增/修改工程需求
	 * @param request
	 * @param response
	 * @param requestVo
	 * @return
	 */
	@RequestMapping(value = "/manager/update/sitJob/record.ddxj")
	public ResponseBase addSiteJobDemand(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo ){
		ResponseBase result=ResponseBase.getInitResponse();
		SiteJobDemand siteJobDemand=null;
		if(requestVo.getId()!=null && requestVo.getId()>0)
		{
			siteJobDemand=siteService.querySiteJobDemandDetail(requestVo.getId());
		}
		else 
		{
			siteJobDemand=new SiteJobDemand();
		}
		siteJobDemand.setAddress(requestVo.getAddress());
		siteJobDemand.setCategoryName(requestVo.getCategoryName());
		siteJobDemand.setStartTime(DateUtils.getDate(requestVo.getStartTime(), "yyyy-MM-dd"));
		siteJobDemand.setEndTime(DateUtils.getDate(requestVo.getEndTime(), "yyyy-MM-dd"));
		siteJobDemand.setDemandDesc(requestVo.getDemandDesc());
		siteJobDemand.setEstimateWages(requestVo.getEstimateWages());
		siteJobDemand.setPhone(requestVo.getPhone());
		siteJobDemand.setIp(CmsUtils.getIpAddr(request));
		siteJobDemand.setUpdateTime(new Date());
		if(requestVo.getId()!=null && requestVo.getId()>0)
		{
			siteJobDemand.setId(requestVo.getId());
			siteService.updateSiteJobDemand(siteJobDemand);
		}
		else
		{
			siteJobDemand.setCreateTime(new Date());
			siteService.addSiteJobDemand(siteJobDemand);
		}
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("添加/更新招聘成功");
		return result;
		
	}
	/**
	 * 受理工程需求
	 * 
	 * @param request
	 * @param response
	 * @param requestVo
	 * @author fancunxin
	 * @throws ClientException
	 */
	@RequestMapping(value="/manager/update/sitJob/status.ddxj")
	public ResponseBase updateSiteJobDemandStatus(HttpServletRequest request, HttpServletResponse response,int id,int status)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		SiteJobDemand siteJobDemand=new SiteJobDemand();
		siteJobDemand.setId(id);
		siteJobDemand.setStatus(status);
		siteService.updateSiteJobDemandStatus(siteJobDemand);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("删除成功");
		return result;
	}
}
