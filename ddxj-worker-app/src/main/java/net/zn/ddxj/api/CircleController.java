package net.zn.ddxj.api;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aliyuncs.exceptions.ClientException;
import com.github.pagehelper.PageHelper;

import lombok.extern.slf4j.Slf4j;
import net.zn.ddxj.base.BaseController;
import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.Circle;
import net.zn.ddxj.entity.CircleComment;
import net.zn.ddxj.entity.CircleImage;
import net.zn.ddxj.entity.CircleLaud;
import net.zn.ddxj.service.CircleService;
import net.zn.ddxj.service.ValidateKeywordsService;
import net.zn.ddxj.tool.AsycService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.OrderNumUtils;
import net.zn.ddxj.utils.json.JsonUtil;

@Slf4j
@RestController
public class CircleController extends BaseController{

	/**
     * 查询所有圈子动态
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/query/circle/all/record.ddxj")
    public ResponseBase queryCircleAllRecord(HttpServletRequest request, HttpServletResponse response,int userId,String city,int pageNum,int pageSize) throws IllegalAccessException
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	
    	PageHelper.startPage(pageNum, pageSize);
		List<Circle> circleList = circleSvc.queryCircleAllRecord(userId,city);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
		result.push("circleList",JsonUtil.list2jsonToArray(circleList));
    	return result;
    }
    
    /**
     * 点赞
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/update/awesome.ddxj")
    public ResponseBase updateAwesome(HttpServletRequest request, HttpServletResponse response,int userId,int circleId) throws IllegalAccessException
    {
		ResponseBase result = ResponseBase.getInitResponse();
		CircleLaud circleLaud = circleSvc.queryCircleLaudById(userId,circleId);
    	if(!CmsUtils.isNullOrEmpty(circleLaud))
    	{
    		if(circleLaud.getFlag() == Constants.Number.ONE_INT)
    		{
    			//更新为取消点赞
    			circleSvc.updateCircleLaudById(circleLaud.getId(),userId, Constants.Number.TWO_INT);//更新点赞状态
    			result.push("awesomeStatus", Constants.Number.TWO_INT);
    		}
    		else
    		{
    			//更新为已点赞
    			circleSvc.updateCircleLaudById(circleLaud.getId(),userId, Constants.Number.ONE_INT);//更新点赞状态
    			result.push("awesomeStatus", Constants.Number.ONE_INT);
    		}
    	}
    	else
    	{
    		//创建点赞记录，返回更新状态
    		circleLaud = new CircleLaud();
    		circleLaud.setCircleId(circleId);
    		circleLaud.setUserId(userId);
    		circleLaud.setUpdateTime(new Date());
    		circleLaud.setCreateTime(new Date());
    		circleSvc.insertCircleLaud(circleLaud);//生成点赞记录
			result.push("awesomeStatus", Constants.Number.ONE_INT);
    	}
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);;
    	return result;
    }
    
    /**
     * 查询圈子动态详情
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/query/circle/details.ddxj")
    public ResponseBase queryCircleDetails(HttpServletRequest request, HttpServletResponse response,int userId,int circleId) throws IllegalAccessException
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	Circle circle = circleSvc.findCircleDetail(userId,circleId);
		result.push("circle", JsonUtil.bean2jsonObject(circle));
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	return result;
    }
    
    /**
     * 查询圈子评论列表
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/query/circle/comment/list.ddxj")
    public ResponseBase queryCircleCommentList(HttpServletRequest request, HttpServletResponse response,int circleId,int pageNum,int pageSize) throws IllegalAccessException
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	PageHelper.startPage(pageNum, pageSize);
     	List<CircleComment> circleCommentList = circleSvc.queryCircleCommentList(circleId);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
		result.push("circleCommentList", JsonUtil.list2jsonToArray(circleCommentList));
    	return result;
    }
    
    /**
     * 查询我发布的圈子动态
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/query/my/circle/record.ddxj")
    public ResponseBase queryMyCircleRecord(HttpServletRequest request, HttpServletResponse response,int userId,int pageNum,int pageSize) throws IllegalAccessException
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	PageHelper.startPage(pageNum, pageSize);
    	List<Circle> circleList = circleSvc.queryMyCircleRecord(userId);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
		result.push("circleList", JsonUtil.list2jsonToArray(circleList));
		log.info("#####################已查询到符合条件的记录#####################");
    	return result;
    }
    
    /**
     * 删除我的圈子动态
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/delete/my/circle/record.ddxj")
    public ResponseBase deleteMyCircleRecord(HttpServletRequest request, HttpServletResponse response,int userId,int circleId) throws IllegalAccessException
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	circleSvc.deleteCommentByCircleId(circleId);
    	circleSvc.deleteMyCircleRecord(userId,circleId);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
    	return result;
    }
    /**
     * 发布圈子动态
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/add/circle/record.ddxj")
    public ResponseBase addCircleRecord(HttpServletRequest request, HttpServletResponse response,int userId,String content,String address,
    		String lng,String lat,String model,String images,String city) throws IllegalAccessException
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	
    	String number = OrderNumUtils.getOredrNum() + userId;
    	
    	Circle circle = new Circle();
    	circle.setUserId(userId);
    	Set<String> sensitiveWord = validateKeywords.validateKeywords(content);
    	if(!CmsUtils.isNullOrEmpty(sensitiveWord))
    	{
    		result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.TEXT_ILLEGALITY+ sensitiveWord);
			return result; 
    	}
    	circle.setContent(content);
    	circle.setAddress(address);
    	circle.setLat(lat);
    	circle.setLng(lng);
    	circle.setCity(city);
    	circle.setModel(model);
    	circle.setIp(CmsUtils.getIpAddr(request));
    	circle.setCreateTime(new Date());
    	circle.setUpdateTime(new Date());
    	circle.setCircleNumber(number);
    	circleSvc.addCircleRecord(circle);//发布圈子动态
    	
    	int circleId = circleSvc.findCircleByNumber(userId,number);//根据Number查询圈子
    	
    	if(!CmsUtils.isNullOrEmpty(images))
		{
			String[] img = null;
			if(images.indexOf("###") >= Constants.Number.REDUCE_ONE_INT)
			{
				img = images.split("###");
			}
			else
			{
				img = new String[1];
				img[0] = images;
			}
			
			for(String i : img)
			{
				CircleImage image = new CircleImage();
		    	image.setCircleId(circleId);
		    	image.setPictureUrl(i);
		    	image.setUpdateTime(new Date());
		    	image.setCreateTime(new Date());
		    	circleSvc.addCircleImageByCircle(image);//添加圈子图片
			}
		}
		
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.ADD_SUCCESSFUL);
    	return result;
    }
    
    /**
     * 添加圈子动态评论
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/add/circle/comment.ddxj")
    public ResponseBase addCircleComment(HttpServletRequest request, HttpServletResponse response,int circleId,int fromUserId,Integer toUserId,String content,Integer parentId) throws IllegalAccessException
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	
    	CircleComment comment = new CircleComment();
    	comment.setCircleId(circleId);
    	if(!CmsUtils.isNullOrEmpty(parentId) && parentId > Constants.Number.ZERO_INT)
    	{
    		comment.setParentId(parentId);
    	}
    	comment.setFromUserId(fromUserId);
    	comment.setContent(content);
    	comment.setToUserId(toUserId);
    	Set<String> sensitiveWord = validateKeywords.validateKeywords(content);
    	if(!CmsUtils.isNullOrEmpty(sensitiveWord))
    	{
    		result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.TEXT_ILLEGALITY+ sensitiveWord);
			return result;
    	}
    	comment.setIp(CmsUtils.getIpAddr(request));
    	comment.setCreateTime(new Date());
    	comment.setUpdateTime(new Date());
    	circleSvc.addCircleComment(comment);//添加圈子动态评论
    	if(fromUserId != toUserId)//不是自己评论自己圈子
    	{
    		StringBuffer paramBuffer = new StringBuffer();
    		paramBuffer.append("{").append("\"circleId\":").append(circleId).append("}");
    		String param = paramBuffer.toString();
    		asycService.addUserMessage(toUserId, Constants.MESSAGE_CIRECLE_COMMENT, content, 3, param, 21);
    		asycService.pushCircleComment(comment.getId());
    	}
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.ADD_SUCCESSFUL);
    	return result;
    }
    
    @Autowired
	private CircleService circleSvc;
    @Autowired
	private ValidateKeywordsService validateKeywords;
    @Autowired
    private AsycService asycService;
}
