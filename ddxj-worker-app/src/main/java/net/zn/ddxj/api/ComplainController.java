package net.zn.ddxj.api;

import java.util.Date;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.zn.ddxj.base.BaseController;
import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.Complain;
import net.zn.ddxj.service.UserService;
import net.zn.ddxj.service.ValidateKeywordsService;
import net.zn.ddxj.utils.CmsUtils;
/**
 * 投诉模块
 * 
 */
@RestController
@Slf4j

public class ComplainController extends BaseController{
	@Autowired
	private UserService userService;
	@Autowired
	private ValidateKeywordsService validateKeywords;
	@RequestMapping(value="/add/complaint.ddxj")
	public ResponseBase addComplaint(HttpServletRequest request, HttpServletResponse response, Integer userId,String complaintContent,Integer type,String banners){
		ResponseBase result=ResponseBase.getInitResponse();
		if(! CmsUtils.isNullOrEmpty(userId) && !CmsUtils.isNullOrEmpty(complaintContent)){
			Set<String> sensitiveWord = validateKeywords.validateKeywords(complaintContent);
	    	if(!CmsUtils.isNullOrEmpty(sensitiveWord))
	    	{
	    		result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.TEXT_ILLEGALITY+ sensitiveWord);
				return result;
	    	}
			Complain complain=new Complain();
			complain.setUserId(userId);
			complain.setComplainContent(complaintContent);
			complain.setType(type);
			if(!CmsUtils.isNullOrEmpty(banners))//投诉图片列表
			{
				complain.setImgUrl(banners);
			}
			else
			{
				complain.setImgUrl("");	
			}
			
			complain.setCreateTime(new Date());
			complain.setUpdateTime(new Date());
			complain.setStatus(1);
			userService.insertComplain(complain);
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.ADD_SUCCESSFUL);
		}else{
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.ADD_FAILURE);
		}
		return result;
		
	}

}
