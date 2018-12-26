package net.zn.ddxj.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.ScreenAdvert;
import net.zn.ddxj.service.ScreenAdvertService;
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
public class ScreenAdvertController {
	
	@Autowired 
	private ScreenAdvertService screenAdvertService;
	
	/**
	 * 查询所有的闪屏广告
	 * @param request
	 * @param response
	 * @param requestVo
	 * @return
	 */
	@RequestMapping(value="/manager/screen/list.ddxj")
	public ResponseBase screenAdvert(HttpServletRequest request, HttpServletResponse response,CmsRequestVo requestVo){
		ResponseBase result=ResponseBase.getInitResponse();
		PageHelper.startPage(requestVo.getPageNum(), requestVo.getPageSize());
		List<ScreenAdvert> advertList = screenAdvertService.queryScreenAdvertList(requestVo);
		PageInfo<ScreenAdvert> pageInfo = new PageInfo<ScreenAdvert>(advertList);
		if(!CmsUtils.isNullOrEmpty(pageInfo)){
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.push("advertList", pageInfo);
			result.setResponseMsg("查询成功");
		}else{
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.push("advertList", pageInfo);
			result.setResponseMsg("查询失败");
		}
		return result;
		
	}
	
	/**
	 * 查看开屏广告详情
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/manager/screen/detail.ddxj")
	public ResponseBase queryScreenadvertDetail(HttpServletRequest request, HttpServletResponse response,int advertId)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		ScreenAdvert screenAdvert = screenAdvertService.selectByPrimaryKey(advertId);
		if("1,2".equals(screenAdvert.getPushPlatform()))
		{
			screenAdvert.setPushPlatform("3");
		}
		result.push("screenAdvert", screenAdvert);
		result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("查询开屏广告详情成功");
		return result;
	}
	
	/**
     * 删除开屏广告
     * 
     * @param request
     * @param response
     * @param requestVo
     * @author 
     * @throws ClientException
     */
	@RequestMapping(value="/manager/screen/delete.ddxj")
	public ResponseBase screenAdvertDelete(HttpServletRequest request, HttpServletResponse response,int advertId)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		screenAdvertService.updateScreenAdvertFlag(advertId);
		result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("删除成功");
		return result;
	}
	
	/**
	 * 新增/修改开屏广告
	 * 
	 * @param request
	 * @param response
	 * @param requestVo
	 * @author 
	 * @throws ParseException 
	 * @throws ClientException
	 */
	@RequestMapping(value="/manager/screen/update.ddxj")
	public ResponseBase updateScreenadvert(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo) throws ParseException
	{
		ResponseBase result = ResponseBase.getInitResponse();
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
		Date start =  simpleDateFormat.parse(startTime);
        Date end =  simpleDateFormat.parse(endTime);
        Date dbStartTime = null;
        Date dbEndTime = null;
        Date dbIosStartTime =null;
        Date dbIosEndTime=null;
        Date dbAndroidStartTime=null;
        Date dbAndroidEndTime=null;
        Date dbAllStartTime = null;
        Date dbAllEndTime = null;
        if("3".equals(requestVo.getPushPlatform()))
        {
        	List<ScreenAdvert> queryIOS = screenAdvertService.queryEndTime("1");
        	List<ScreenAdvert> queryAndroid = screenAdvertService.queryEndTime("2");
        	List<ScreenAdvert> androidAndIOS = screenAdvertService.queryAndroidAndIOS("1,2");
        	if(!CmsUtils.isNullOrEmpty(queryIOS))
        	{
        		dbIosStartTime=queryIOS.get(0).getStartTime();
        		dbIosEndTime=queryIOS.get(0).getEndTime();
        	}
        	if(!CmsUtils.isNullOrEmpty(queryAndroid))
        	{
        		dbAndroidStartTime=queryAndroid.get(0).getStartTime();
        		dbAndroidEndTime=queryAndroid.get(0).getEndTime();
        	}
        	if(!CmsUtils.isNullOrEmpty(androidAndIOS))
        	{
        		if(start.getTime()==end.getTime())
        		{
        			result.setResponse(Constants.FALSE);
    				result.setResponseCode(Constants.SUCCESS_200);
    				result.setResponseMsg("上架时间不能等于下架时间");
    				return result;
        		}
        		if(start.getTime()>end.getTime())
        		{
        			result.setResponse(Constants.FALSE);
    				result.setResponseCode(Constants.SUCCESS_200);
    				result.setResponseMsg("上架时间不能小于或等于下架时间");
    				return result;
        		}
        		if(!CmsUtils.isNullOrEmpty(dbIosEndTime) &&!CmsUtils.isNullOrEmpty(dbAndroidEndTime))
        		{
        			if(start.getTime()<=dbIosEndTime.getTime())
            		{
            			result.setResponse(Constants.FALSE);
        				result.setResponseCode(Constants.SUCCESS_200);
        				result.setResponseMsg("上架时间不能小于或等于上次苹果下架时间");
        				return result;
            		}
            		if(start.getTime()<=dbAndroidEndTime.getTime())
            		{
            			result.setResponse(Constants.FALSE);
        				result.setResponseCode(Constants.SUCCESS_200);
        				result.setResponseMsg("上架时间不能小于或等于上次安卓下架时间");
        				return result;
            		}
            		if(end.getTime()<=dbIosEndTime.getTime())
            		{
            			result.setResponse(Constants.FALSE);
        				result.setResponseCode(Constants.SUCCESS_200);
        				result.setResponseMsg("下架时间不能小于或等于上次苹果下架时间");
        				return result;
            		}
            		if(end.getTime()<=dbAndroidEndTime.getTime())
            		{
            			result.setResponse(Constants.FALSE);
        				result.setResponseCode(Constants.SUCCESS_200);
        				result.setResponseMsg("下架时间不能小于或等于上次安卓下架时间");
        				return result;
            		}
        		}
        		
        	}
        	else
        	{
        		if(start.getTime()==end.getTime())
        		{
        			result.setResponse(Constants.FALSE);
    				result.setResponseCode(Constants.SUCCESS_200);
    				result.setResponseMsg("上架时间不能等于下架时间");
    				return result;
        		}
        		if(!CmsUtils.isNullOrEmpty(queryIOS) && !CmsUtils.isNullOrEmpty(queryAndroid))
        		{
        			if(start.getTime()<=dbIosEndTime.getTime())
            		{
            			result.setResponse(Constants.FALSE);
        				result.setResponseCode(Constants.SUCCESS_200);
        				result.setResponseMsg("上架时间不能小于或等于上次苹果下架时间");
        				return result;
            		}
            		if(start.getTime()<=dbAndroidEndTime.getTime())
            		{
            			result.setResponse(Constants.FALSE);
        				result.setResponseCode(Constants.SUCCESS_200);
        				result.setResponseMsg("上架时间不能小于或等于上次安卓下架时间");
        				return result;
            		}
            		if(end.getTime()<=dbIosEndTime.getTime())
            		{
            			result.setResponse(Constants.FALSE);
        				result.setResponseCode(Constants.SUCCESS_200);
        				result.setResponseMsg("下架时间不能小于或等于上次苹果下架时间");
        				return result;
            		}
            		if(end.getTime()<=dbAndroidEndTime.getTime())
            		{
            			result.setResponse(Constants.FALSE);
        				result.setResponseCode(Constants.SUCCESS_200);
        				result.setResponseMsg("下架时间不能小于或等于上次安卓下架时间");
        				return result;
            		}
        		}
        	}
        }
        else 
        {
        	 List<ScreenAdvert>  list= screenAdvertService.queryEndTime(requestVo.getPushPlatform());//添加IOS和添加Android
			 List<ScreenAdvert> androidAndIOS = screenAdvertService.queryAndroidAndIOS("1,2");//查询添加IOS和Android
			   if(null == androidAndIOS || androidAndIOS.size() ==0)
			   {
				if(!CmsUtils.isNullOrEmpty(list))
				{

					 dbStartTime=list.get(0).getStartTime();
					 dbEndTime=list.get(0).getEndTime();
				}

				if(start.getTime()>end.getTime())
				{
					result.setResponse(Constants.FALSE);
					result.setResponseCode(Constants.SUCCESS_200);
					result.setResponseMsg("上架时间不能小于或等于下架时间");
					return result;
				}
				if(start.getTime()==end.getTime())
				{
					result.setResponse(Constants.FALSE);
					result.setResponseCode(Constants.SUCCESS_200);
					result.setResponseMsg("上架时间不能等于下架时间");
					return result;
				}
				if(!(CmsUtils.isNullOrEmpty(dbStartTime) && CmsUtils.isNullOrEmpty(dbEndTime)))
				{
					if(start.getTime()<=dbEndTime.getTime())
					{
						result.setResponse(Constants.FALSE);
						result.setResponseCode(Constants.SUCCESS_200);
						result.setResponseMsg("上架时间不能小于或等于上次下架时间");
					return result;
					}
					if(start.getTime()<=dbStartTime.getTime())
					{
						result.setResponse(Constants.FALSE);
						result.setResponseCode(Constants.SUCCESS_200);
						result.setResponseMsg("上架时间不能小于或等于上次上架时间");
						return result;
					}
					if(end.getTime()<=dbEndTime.getTime())
					{
						result.setResponse(Constants.FALSE);
						result.setResponseCode(Constants.SUCCESS_200);
						result.setResponseMsg("下架时间不能小于或等于上次下架时间");
						return result;
					}
				}
			   }
			   else
			   {
				   	dbAllStartTime = androidAndIOS.get(0).getStartTime();
					dbAllEndTime = androidAndIOS.get(0).getEndTime();
					if(start.getTime()==end.getTime())
					{
						result.setResponse(Constants.FALSE);
						result.setResponseCode(Constants.SUCCESS_200);
						result.setResponseMsg("上架时间不能等于下架时间");
						return result;
					}
					if(start.getTime()<=dbAllEndTime.getTime())
					{
						result.setResponse(Constants.FALSE);
						result.setResponseCode(Constants.SUCCESS_200);
						result.setResponseMsg("上架时间不能小于上次下架时间");
						return result;
					}
					if(end.getTime()<dbAllEndTime.getTime())
					{
						result.setResponse(Constants.FALSE);
						result.setResponseCode(Constants.SUCCESS_200);
						result.setResponseMsg("结束时间不能小于上次下架时间");
						return result;
					}
			   }

       }


		ScreenAdvert screenAdvert = null;
		if(requestVo.getId() != null && requestVo.getId() > 0)
		{
			screenAdvert = screenAdvertService.selectByPrimaryKey(requestVo.getId());
		}
		else
		{
			screenAdvert = new ScreenAdvert();
		}
		screenAdvert.setBannerUrl(requestVo.getBannerUrl());
		screenAdvert.setBannerLink(requestVo.getBannerLink());
		Integer status=requestVo.getStatus();
		if(status==1)
		{
			screenAdvert.setTimer(requestVo.getTimer());
		}
		else
		{
			screenAdvert.setTimer("0");
		}
		screenAdvert.setStatus(status);
       if("3".equals(requestVo.getPushPlatform()))
       {

       	screenAdvert.setPushPlatform("1,2");
       }
       else
       {
       	screenAdvert.setPushPlatform(requestVo.getPushPlatform());
       }

		screenAdvert.setStartTime(DateUtils.getDate(requestVo.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
		screenAdvert.setEndTime(DateUtils.getDate(requestVo.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
		screenAdvert.setUpdateTime(new Date());

		if(requestVo.getId() != null && requestVo.getId() > 0)
		{
			screenAdvert.setId(requestVo.getId());
			screenAdvertService.updateByPrimaryKeySelective(screenAdvert);
		}
		else
		{
			screenAdvert.setCreateTime(new Date());
			screenAdvertService.insertSelective(screenAdvert);
		}

		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("更新幻灯片成功");
		return result;
        }
	}
		
	


