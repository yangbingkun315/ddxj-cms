package net.zn.ddxj.api;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.Circle;
import net.zn.ddxj.entity.Recruit;
import net.zn.ddxj.entity.RecruitCategory;
import net.zn.ddxj.entity.RecruitCredit;
import net.zn.ddxj.entity.RecruitRecord;
import net.zn.ddxj.service.CircleService;
import net.zn.ddxj.service.RecruitCreditService;
import net.zn.ddxj.service.RecruitRecordService;
import net.zn.ddxj.service.RecruitService;
import net.zn.ddxj.service.UserService;
import net.zn.ddxj.tool.AsycService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.DateUtils;
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
 * 招聘管理
* @ClassName: RecruitController   
* @author 上海众宁网络科技有限公司-范存鑫
* @date 2018年5月2日  
*
 */
public class RecuritController {
	
	@Autowired
	private RecruitService recruitService;
	@Autowired
	private CircleService circleService;
	@Autowired
	private UserService userService;
	@Autowired
	private RecruitRecordService recruitRecordService;
	@Autowired
	private AsycService asycService;
	@Autowired
	private RecruitCreditService recruitCreditService;
	
	/**
     * 查询招聘列表
     *
     * @param request
     * @param response
     * @throws ClientException 
     */
	@RequestMapping(value = "/manager/recruit/list.ddxj")
	public ResponseBase queryRecruitList(HttpServletRequest request,HttpServletResponse response,CmsRequestVo requestVo) throws IllegalAccessException
	{
		ResponseBase result = ResponseBase.getInitResponse();
		PageHelper.startPage(requestVo.getPageNum(), requestVo.getPageSize());
		List<Recruit> recruitList = recruitService.findRecruitList(requestVo);
		PageInfo<Recruit> pageInfo = new PageInfo<Recruit>(recruitList);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.push("recruitList", pageInfo);
		result.setResponseMsg("查询成功");
		return result;
	}
	
    /**
     * 更新招聘状态
     * @param request
     * @param response
     * @param recruitId
     * @param validateStatus
     * @return
     */
    @RequestMapping(value="/manager/recruit/update/status.ddxj")
    public ResponseBase updateRecruit(HttpServletRequest request, HttpServletResponse response,Integer recruitId, Integer validateStatus, String validateCause,
			Integer recruitStatus  ){
    	ResponseBase result=ResponseBase.getInitResponse();
    	int updateByRecruitId = recruitService.updateByRecruitId(recruitId, validateStatus, validateCause, recruitStatus);
    	if(updateByRecruitId > 0)
    	{
    		asycService.pushRecruitValidataStatus(validateStatus, recruitId);//推送审核状态给用户
    		if(validateStatus == 3)//审核成功
    		{
    			asycService.pushRecruitBatchByCategory(recruitId);
    		}
    		if(validateStatus == 2)//审核失败
    		{
    			RecruitCredit recruitCredit = recruitCreditService.findRecruitCredit(recruitId);
    			if(!CmsUtils.isNullOrEmpty(recruitCredit))
    			{
    				recruitCreditService.deleteByPrimaryKey(recruitCredit.getId());
    			}
    		}
    		result.setResponse(Constants.TRUE);
        	result.setResponseCode(Constants.SUCCESS_200);
        	result.setResponseMsg("更新成功");
    	}
    	else
    	{
    		result.setResponse(Constants.FALSE);
        	result.setResponseCode(Constants.SUCCESS_200);
        	result.setResponseMsg("更新失败");
    	}
    	
		return result;
    	
    }
    
    /**
     * 删除
     * @param request
     * @param response
     * @param recruitId
     * @return
     */
    @RequestMapping(value="/manager/recruit/delete.ddxj")
    public ResponseBase deleteRecruit(HttpServletRequest request, HttpServletResponse response,Integer recruitId){
    	ResponseBase result=ResponseBase.getInitResponse();
    	recruitService.deleteByRecruitId(recruitId);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("删除成功");
		return result;
    	
    }
    
    /**
     * 更新招聘信息
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value="/manager/recruit/update.ddxj")
    public ResponseBase updateRecruit(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo)
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	
    	Recruit recruit = null;
    	if(requestVo.getRecruitId() != null && requestVo.getRecruitId() > 0)
    	{
    		recruit = recruitService.selectByPrimaryKey(requestVo.getRecruitId());
    	}
    	else
    	{
    		recruit = new Recruit();
    		if(CmsUtils.isNullOrEmpty(requestVo.getUserId()))
    		{
    			recruit.setUserId(1);// userId = 1 为系统管理员
    		}
    		else
    		{
    			recruit.setUserId(requestVo.getUserId());
    		}
    		
    		if(CmsUtils.isNullOrEmpty(requestVo.getUserId()))
    		{
    			recruit.setProjectNumber(String.valueOf(System.currentTimeMillis() + 1));// userId = 1 为系统管理员
    		}
    		else
    		{
    			recruit.setProjectNumber(String.valueOf(System.currentTimeMillis() + requestVo.getUserId()));
    		}
    		recruit.setCreateTime(new Date());
    	}
    	recruit.setRecruitTitle(requestVo.getRecruitTitle());
    	recruit.setRecruitContent(requestVo.getRecruitContent());
    	recruit.setStartTime(DateUtils.getDate(requestVo.getStartTime(), "yyyy-MM-dd"));
    	recruit.setEndTime(DateUtils.getDate(requestVo.getEndTime(), "yyyy-MM-dd"));
    	recruit.setRecruitProvince(requestVo.getRecruitProvince());
    	recruit.setRecruitCity(requestVo.getRecruitCity());
    	recruit.setRecruitArea(requestVo.getRecruitArea());
    	recruit.setRecruitLong(requestVo.getRecruitLong());
    	recruit.setRecruitLat(requestVo.getRecruitLat());
    	recruit.setRecruitAddress(requestVo.getRecruitAddress());
    	recruit.setRecruitPerson(requestVo.getRecruitPerson());
    	recruit.setContractor(requestVo.getContractor());
    	recruit.setCoverImage(requestVo.getCoverImage());
    	recruit.setBalanceWay(requestVo.getBalanceWay());
    	recruit.setStartPrice(new BigDecimal(requestVo.getStartPrice()));
    	recruit.setEndPrice(new BigDecimal(requestVo.getEndPrice()));
    	recruit.setStopTime(DateUtils.getDate(requestVo.getStopTime(), "yyyy-MM-dd"));
    	recruit.setBalanceWay(requestVo.getBalanceWay());
    	recruit.setStick(requestVo.getStick());
    	recruit.setUpdateTime(new Date());
    	
    	if(requestVo.getRecruitId() != null && requestVo.getRecruitId() > 0)
    	{
    		recruitService.updateRecruit(recruit);//修改招聘信息
    		
    		recruitService.deleteRecruitCategory(recruit.getId());//删除招聘分类
    	}
    	else
    	{
    		recruitService.addRecruit(recruit);//添加招聘信息
    	}
    	
    	if(!CmsUtils.isNullOrEmpty(requestVo.getCategorys()))//招聘工种分类
    	{
    		String[] category = null;
    		if(requestVo.getCategorys().indexOf(",") >= -1)
    		{
    			category = requestVo.getCategorys().split(",");
    		}
    		else
    		{
    			category = new String[1];
    			category[0] = requestVo.getCategorys();
    		}
    		
    		for(String categoryId : category)
    		{
    			RecruitCategory cay = new RecruitCategory();
    			cay.setRecruitId(recruit.getId());
    			cay.setCategoryId(Integer.valueOf(categoryId));
    			cay.setUpdateTime(new Date());
    			cay.setCreateTime(new Date());
    			recruitService.addRecruitCategory(cay);
    		}
    	}
    	
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("更新招聘信息成功");
    	log.info("#####################更新招聘信息成功#####################");
    	return result;
    }
    
    /**
     * 查询招聘详情
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value="/manager/recruit/detail.ddxj")
    public ResponseBase recruitDetail(HttpServletRequest request, HttpServletResponse response,int recruitId)
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	//根据招聘id查出招聘信息
    	Recruit recruit = recruitService.selectByPrimaryKey(recruitId);
    	result.push("recruit", recruit);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("查询招聘详情成功");
    	log.info("#####################查询招聘详情成功#####################");
    	return result;
    }
    
    /**
     * 查询报名工人信息
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws IllegalAccessException 
     * @throws ClientException 
     */
    @RequestMapping(value = "/manager/recruit/user/list.ddxj")
    public ResponseBase queryEnlistUserList(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo) throws IllegalAccessException 
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	PageHelper.startPage(requestVo.getPageNum(),requestVo.getPageSize());
		List<RecruitRecord> recruitRecordList = recruitRecordService.queryRecruitUserList(requestVo.getRecruitId());
		PageInfo<RecruitRecord> pageInfo = new PageInfo<RecruitRecord>(recruitRecordList);
		if(!CmsUtils.isNullOrEmpty(recruitRecordList))
		{
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("已查询到符合条件的记录");
			result.push("recruitRecordList",pageInfo);
		}
		else
		{
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("没有查询到符合条件的记录");
			result.push("recruitRecordList",pageInfo);
		}
		log.info("#####################查询报名工人信息成功#####################");
    	return result;
    }
    
    /**
     * 删除报名工人
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/manager/recruit/record/delete.ddxj")
    public ResponseBase updateRecruitRecord(HttpServletRequest request, HttpServletResponse response,Integer recordId) 
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	recruitRecordService.updateRecordFlag(recordId);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("删除报名工人成功");
    	log.info("#####################删除报名工人成功#####################");
    	return result;
    }
    
    /**
     * 更新报名工人状态
     * 
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value="/manager/record/update/status.ddxj")
    public ResponseBase updateRecordStatus(HttpServletRequest request, HttpServletResponse response,Integer recordId,Integer enlistStatus,String refuseCause)
    {
    	ResponseBase result=ResponseBase.getInitResponse();
    	recruitRecordService.updateByRecordId(recordId, enlistStatus, refuseCause);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("更新成功");
		return result;
    }
    
    /**
     * 查询圈子列表
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/query/circle/list.ddxj")
    public ResponseBase queryCircleList(HttpServletRequest request, HttpServletResponse response,CmsRequestVo requestVo)
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	PageHelper.startPage(requestVo.getPageNum(),requestVo.getPageSize());
		List<Circle> circleList = circleService.queryCircleList(requestVo);
		PageInfo<Circle> pageInfo = new PageInfo<Circle>(circleList);
		if(!CmsUtils.isNullOrEmpty(circleList))
		{
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("已查询到符合条件的记录");
			result.push("circleList",pageInfo);
		}
		else
		{
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("没有查询到符合条件的记录");
			result.push("circleList",pageInfo);
		}
		log.info("#####################查询圈子列表成功#####################");
    	return result;
    }
    
    /**
     * 删除圈子
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value="/manager/circle/delete.ddxj")
    public ResponseBase deleteCircle(HttpServletRequest request, HttpServletResponse response,Integer circleId)
    {
    	ResponseBase result=ResponseBase.getInitResponse();
    	circleService.deleteCricleById(circleId);
    	try {
    		asycService.sendAppTemplateFromCircle(circleId);
		} catch (Exception e) {}
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("删除成功");
		return result;
    }
}
