package net.zn.ddxj.api;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.github.pagehelper.PageHelper;

import lombok.extern.slf4j.Slf4j;
import net.zn.ddxj.base.BaseController;
import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.Credit;
import net.zn.ddxj.entity.CreditContactsImage;
import net.zn.ddxj.entity.CreditRepayment;
import net.zn.ddxj.entity.CreditUrgentContacts;
import net.zn.ddxj.entity.Notice;
import net.zn.ddxj.entity.RealAuth;
import net.zn.ddxj.entity.Recruit;
import net.zn.ddxj.entity.RecruitBanner;
import net.zn.ddxj.entity.RecruitCategory;
import net.zn.ddxj.entity.RecruitCredit;
import net.zn.ddxj.entity.RecruitRecord;
import net.zn.ddxj.entity.SalaryRecord;
import net.zn.ddxj.entity.User;
import net.zn.ddxj.entity.UserBank;
import net.zn.ddxj.entity.UserCollection;
import net.zn.ddxj.entity.UserComment;
import net.zn.ddxj.entity.UserPassword;
import net.zn.ddxj.entity.UserRequest;
import net.zn.ddxj.entity.ValidateManager;
import net.zn.ddxj.service.RecruitCreditService;
import net.zn.ddxj.service.RecruitRecordService;
import net.zn.ddxj.service.RecruitService;
import net.zn.ddxj.service.SalaryRecordService;
import net.zn.ddxj.service.UserCollectionService;
import net.zn.ddxj.service.UserCommentService;
import net.zn.ddxj.service.UserService;
import net.zn.ddxj.service.ValidateKeywordsService;
import net.zn.ddxj.tool.AsycService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.DateUtils;
import net.zn.ddxj.utils.aliyun.BankValidateUtils;
import net.zn.ddxj.utils.aliyun.FindBankUtils;
import net.zn.ddxj.utils.aliyun.IDCardValidateUtils;
import net.zn.ddxj.utils.json.JsonUtil;

@Slf4j
@RestController
/**
 * 招聘
* @ClassName: RecruitController   
* @author 上海众宁网络科技有限公司-何俊辉
* @date 2018年4月18日  
*
 */
public class RecruitController extends BaseController{
	
	@Autowired
	private RecruitService recruitService;
	@Autowired
	private RecruitRecordService recruitRecordService;
	@Autowired
	private UserCollectionService collectionService;
	@Autowired
	private RecruitCreditService recruitCreditService;
	@Autowired
	private UserService userService;
	@Autowired
	private SalaryRecordService salaryRecordService;
	
	@Autowired
	private RecruitCreditService creditService;
	@Autowired
	private UserCommentService  userCommentService;
    @Autowired
	private ValidateKeywordsService validateKeywords;
    @Autowired
	private AsycService asycService;
    
	/**
     * 查询我发布的招聘，是否被邀请过
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
	 * @throws IllegalAccessException 
     */
    @RequestMapping(value = "/query/my/release/woke.ddxj")
    public ResponseBase queryMyReleaseWoke(HttpServletRequest request, HttpServletResponse response,Integer fromUserId,Integer toUserId) throws ClientException, IllegalAccessException 
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	List<Recruit> recruitList = recruitService.queryMyReleaseWork(fromUserId,toUserId);
		result.push("recruitList", JsonUtil.list2jsonToArray(recruitList));
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	log.info(Constants.Prompt.QUERY_SUCCESSFUL);                           
    	return result;
    }
    
    /**
     * 查询附近工作
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
	 * @throws IllegalAccessException 
     */
    @RequestMapping(value = "/query/my/nearRecruit.ddxj")
    public ResponseBase queryMyNearRecruit(HttpServletRequest request, HttpServletResponse response,String provice,String longitude,String latitude,Integer pageNum,Integer pageSize) throws ClientException, IllegalAccessException 
    {
    	
    	ResponseBase result = ResponseBase.getInitResponse();
		PageHelper.startPage(pageNum, pageSize);
    	List<Recruit> recruitList = recruitService.queryMyNearRecruit(provice,longitude,latitude);
		result.push("recruitList", JsonUtil.list2jsonToArray(recruitList));
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	log.info(Constants.Prompt.QUERY_SUCCESSFUL);                           
    	return result;
    }
    
    /**
     * 查询工头在招职位数量
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/user/recruit/person/count.ddxj")
    public ResponseBase queryUserRecruitPersonCount(HttpServletRequest request, HttpServletResponse response,Integer userId) throws ClientException, IllegalAccessException 
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	PageHelper.startPage(Constants.Number.ONE_INT, Constants.Number.FIVE_INT);
    	Integer count = recruitService.queryUserRecruitPersonCount(userId);
    	List<Recruit> recruitList = recruitService.queryUserRecruitPersonList(userId);
		result.push("recruitCount", count);
		result.push("recruitList", JsonUtil.list2jsonToArray(recruitList));
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	log.info(Constants.Prompt.QUERY_SUCCESSFUL);
    	return result;
    }
    /**
     * 查询招聘信息
     * @param request
     * @param response
     * @param recruitTitle
     * @param city
     * @param categoryId
     * @param startTimeOrderBy
     * @param pageNum
     * @param pageSize
     * @return
     * @throws Exception
     * @author ddxj
     */
    @RequestMapping("/recruit/list.ddxj")
    public ResponseBase queryRecruitList(HttpServletRequest request, HttpServletResponse response,String recruitTitle,String city,String categoryId,String startTimeOrderBy,String projectType,Integer pageNum,Integer pageSize) throws Exception{
		ResponseBase result=ResponseBase.getInitResponse();
		Map<String, Object>params=new HashMap<String, Object>();
		params.put("recruitTitle", recruitTitle);
		params.put("city", city);
		params.put("categoryId", categoryId);
		if(CmsUtils.isNullOrEmpty(startTimeOrderBy))
		{
			startTimeOrderBy = "0";
		}
		params.put("startTimeOrderBy", startTimeOrderBy);
		params.put("projectType", projectType);
		PageHelper.startPage(pageNum, pageSize);
		List<Recruit>recruitList=recruitService.queryAllRecruitLists(params);
		result.push("recruitList",JsonUtil.list2jsonToArray(recruitList));
		result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	return result;
    	
    }
    /**
     * 查询用户报名状态
     * @param request
     * @param response
     * @param userId
     * @param recruitId
     * @return
     * @author ddxj
     */
    @RequestMapping("/deliver/status.ddxj")
    public ResponseBase queryWorkerDeliver(HttpServletRequest request, HttpServletResponse response,Integer userId,Integer recruitId){
    	ResponseBase result=ResponseBase.getInitResponse();
    	Recruit recruit = recruitService.selectByPrimaryKey(recruitId);
    	RecruitRecord recruitRecords = recruitRecordService.queryAllWorkerDeliver(recruitId, userId);
    	UserCollection collection = collectionService.queryUserIsCollection(userId,recruitId);
    	int overplusEnlist = recruitRecordService.selectByUserFlag(userId);
    	System.out.println(JsonUtil.bean2jsonObject(recruit));
    	if(!CmsUtils.isNullOrEmpty(recruit))
    	{
    		result.push("recruit", JsonUtil.bean2jsonObject(recruit));
    		result.push("enlistNumber", recruit.getEnlistNumber());
    	}
    	if(!CmsUtils.isNullOrEmpty(collection)){
    		if(collection.getFlag() == Constants.Number.ONE_INT){
    			//已经收藏
    			result.push("isCollection", Constants.Number.ONE_INT);
    		}else{
    			result.push("isCollection", Constants.Number.TWO_INT);
    		}
    	}
    	else
    	{
    		result.push("isCollection", Constants.Number.TWO_INT);
    	}
    	if(overplusEnlist == Constants.Number.ZERO_INT)
    	{
    		result.push("overplusEnlist", Constants.Number.THREE_INT);
        }
    	if(overplusEnlist == Constants.Number.ONE_INT)
    	{
    		result.push("overplusEnlist", Constants.Number.TWO_INT);
    	}
    	if(overplusEnlist == Constants.Number.TWO_INT)
    	{
    		result.push("overplusEnlist", Constants.Number.ONE_INT);
    	}
    	if(overplusEnlist == Constants.Number.THREE_INT || overplusEnlist > Constants.Number.THREE_INT)
    	{
    		result.push("overplusEnlist", Constants.Number.ZERO_INT);
    	}
    	if(!CmsUtils.isNullOrEmpty(recruitRecords))
    	{
    		int enlistStatus = recruitRecords.getEnlistStatus();
    		int workerStatus = recruitRecords.getWorkerStatus();
    		int foremanStatus = recruitRecords.getForemanStatus();
    		if(recruitRecords.getFlag() == Constants.Number.ONE_INT)
    		{
    			//未取消报名
    			if(enlistStatus == Constants.Number.ONE_INT)
    			{
    				//已报名状态
    				if(workerStatus == Constants.Number.ZERO_INT && foremanStatus == Constants.Number.ZERO_INT)
    				{
    					result.push("deliverStatus",Constants.Number.ONE_INT);
    					result.setResponse(Constants.TRUE);
    					result.setResponseCode(Constants.SUCCESS_200);
    					result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    				}else
    				{
    					result.setResponse(Constants.FALSE);
    					result.setResponseCode(Constants.SUCCESS_200);
    					result.setResponseMsg(Constants.Prompt.QUERY_FAILURE);
    					return result;
    				}
    			}
    			if(enlistStatus == Constants.Number.THREE_INT)
    			{
    				//报名未通过
    				if(workerStatus == Constants.Number.ZERO_INT && foremanStatus == Constants.Number.ZERO_INT)
    				{
    					result.push("deliverStatus",Constants.Number.TWO_INT);
    					result.setResponse(Constants.TRUE);
    					result.setResponseCode(Constants.SUCCESS_200);
    					result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    				}else
    				{
    					result.setResponse(Constants.FALSE);
    					result.setResponseCode(Constants.SUCCESS_200);
    					result.setResponseMsg(Constants.Prompt.QUERY_FAILURE);
    					return result;
    				}
    			}
    			
    			if(enlistStatus == Constants.Number.TWO_INT)
    			{
    				//报名通过
    				if(workerStatus == Constants.Number.ZERO_INT && foremanStatus == Constants.Number.ZERO_INT)
    				{
    					result.push("deliverStatus",Constants.Number.THREE_INT);
    					result.setResponse(Constants.TRUE);
    					result.setResponseCode(Constants.SUCCESS_200);
    					result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    				}
    				if(workerStatus == Constants.Number.FIVE_INT && foremanStatus == Constants.Number.ONE_INT)
    				{
    					result.push("deliverStatus",Constants.Number.THREE_INT);
    					result.setResponse(Constants.TRUE);
    					result.setResponseCode(Constants.SUCCESS_200);
    					result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    				}
    				if(workerStatus == Constants.Number.FOUR_INT && foremanStatus == Constants.Number.ZERO_INT)
    				{
    					result.push("deliverStatus",Constants.Number.FOUR_INT);
    					result.setResponse(Constants.TRUE);
    					result.setResponseCode(Constants.SUCCESS_200);
    					result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    				}
    				if(workerStatus == Constants.Number.ONE_INT && foremanStatus == Constants.Number.ZERO_INT)
    				{
    					result.push("deliverStatus",Constants.Number.FIVE_INT);
    					result.setResponse(Constants.TRUE);
    					result.setResponseCode(Constants.SUCCESS_200);
    					result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    				}
    				if(workerStatus == Constants.Number.TWO_INT && foremanStatus == Constants.Number.TWO_INT)
    				{
    					result.push("deliverStatus",Constants.Number.SIX_INT);
    					result.setResponse(Constants.TRUE);
    					result.setResponseCode(Constants.SUCCESS_200);
    					result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    				}
    				if(workerStatus == Constants.Number.THREE_INT && foremanStatus == Constants.Number.THREE_INT)
    				{
    					if(recruitRecords.getWorkerCommentStatus() == Constants.Number.ZERO_INT)
    					{
    						result.push("deliverStatus",Constants.Number.EIGHT_INT);
    						result.setResponse(Constants.TRUE);
    						result.setResponseCode(Constants.SUCCESS_200);
    						result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    					}
    					if(recruitRecords.getWorkerCommentStatus() == Constants.Number.ONE_INT)
    					{
    						result.push("deliverStatus",Constants.Number.NINE_INT);
    						result.setResponse(Constants.TRUE);
    						result.setResponseCode(Constants.SUCCESS_200);
    						result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    					}
    				}
    			}
    		}else if(recruitRecords.getFlag() == Constants.Number.TWO_INT)
    		{
    			//已经取消报名
    			result.push("deliverStatus",Constants.Number.SEVEN_INT);
				result.setResponse(Constants.TRUE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    		}
    	}else
    	{
    		result.push("deliverStatus",Constants.Number.ZERO_INT);
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	}

	return result;
}
    /**
     * 更新收藏招聘
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/update/collection.ddxj")
    public ResponseBase updateCollection(HttpServletRequest request, HttpServletResponse response,int userId,int recruitId) throws ClientException, IllegalAccessException 
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	UserCollection collection = collectionService.queryUserIsCollection(userId,recruitId);
    	if(!CmsUtils.isNullOrEmpty(collection))
    	{
    		if(collection.getFlag() == Constants.Number.ONE_INT)
    		{
    			//更新为取消收藏
    			recruitService.updateUserCollectionById(collection.getId(),userId, Constants.Number.TWO_INT);//更新收藏状态
    			result.push("isCollection", Constants.Number.TWO_INT);
    		}
    		else
    		{
    			//更新为已收藏
    			recruitService.updateUserCollectionById(collection.getId(),userId, Constants.Number.ONE_INT);//更新收藏状态
    			result.push("isCollection", Constants.Number.ONE_INT);
    		}
    	}
    	else
    	{
    		//创建收藏记录，返回更新状态
    		collection = new UserCollection();
    		collection.setCollectionType(Constants.Number.TWO_INT);
    		collection.setFlag(Constants.Number.ONE_INT);
    		collection.setFromUserId(userId);
    		collection.setRecruitId(recruitId);
    		collection.setUpdateTime(new Date());
    		collection.setCreateTime(new Date());
    		collectionService.insertSelective(collection);//生成收藏记录
			result.push("isCollection", Constants.Number.ONE_INT);
    	}
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
    	log.info(Constants.Prompt.UPDATE_SUCCESSFUL);
    	return result;
    }
    
    /**
     * 查询我的招聘
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/my/recruit/list.ddxj")
    public ResponseBase queryMyRecruitList(HttpServletRequest request, HttpServletResponse response,int userId,int recruitStatus,int pageNum,int pageSize) throws ClientException, IllegalAccessException 
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	PageHelper.startPage(pageNum,pageSize); 
    	List<Recruit> recruitList = recruitService.queryMyRecruitList(userId, recruitStatus);
    	if(!CmsUtils.isNullOrEmpty(recruitList))
    	{
			result.setResponse(Constants.TRUE);
	    	result.setResponseCode(Constants.SUCCESS_200);
	    	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
	    	result.push("recruitList", JsonUtil.list2jsonToArray(recruitList));
	    	log.info(Constants.Prompt.QUERY_SUCCESSFUL);
    	}
	    else
	    {
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.QUERY_NORECORD);
			result.push("recruitList",JsonUtil.list2jsonToArray(recruitList));
			log.info(Constants.Prompt.QUERY_NORECORD);
		}
    	return result;
    }
    
    /**
     * 查询招聘审核失败原因
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/query/recruit/reason.ddxj")
    public ResponseBase queryRecruitReason(HttpServletRequest request, HttpServletResponse response,int userId,int recruitId) throws ClientException, IllegalAccessException 
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	String validateCause = recruitService.queryRecruitReason(userId, recruitId);
    	if(!CmsUtils.isNullOrEmpty(validateCause))
    	{
    		result.push("validateCause", validateCause);
    		result.setResponse(Constants.TRUE);
    		result.setResponseCode(Constants.SUCCESS_200);
    		result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	}
    	else
    	{
    		result.push("validateCause", "");
    		result.setResponse(Constants.FALSE);
    		result.setResponseCode(Constants.SUCCESS_200);
    		result.setResponseMsg(Constants.Prompt.QUERY_NORECORD);
    	}
		log.info(Constants.Prompt.QUERY_SUCCESSFUL);
    	return result;
    }
    
    /**
     * 查询授信机构
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/query/credit/MCN.ddxj")
    public ResponseBase queryCreditMCN(HttpServletRequest request, HttpServletResponse response) throws ClientException, IllegalAccessException 
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	List<Credit> creditList = creditService.queryCreditMCN();
    	if(!CmsUtils.isNullOrEmpty(creditList))
    	{
    		result.push("creditList", JsonUtil.list2jsonToArray(creditList));
	    	result.setResponse(Constants.TRUE);
	    	result.setResponseCode(Constants.SUCCESS_200);
	    	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
	    	log.info(Constants.Prompt.QUERY_SUCCESSFUL);
		}
	    else
	    {
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.QUERY_NORECORD);
			result.push("creditList",JsonUtil.list2jsonToArray(creditList));
			log.info(Constants.Prompt.QUERY_NORECORD);
		}
    	return result;
    }
    
    /**
     * 查询授信机构
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/query/credit/record.ddxj")
    public ResponseBase queryCreditRecord(HttpServletRequest request, HttpServletResponse response,int userId,int recruitId) throws ClientException, IllegalAccessException 
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	RecruitCredit credit = creditService.queryRecruitCredit(userId,recruitId);
    	//查询授信还款记录
    	CreditRepayment repaymentRecord = creditService.queryRecruitRepmentRecord(recruitId);
    	result.push("repaymentRecord", JsonUtil.bean2jsonObject(repaymentRecord));
		result.push("recruitCreditRecord", JsonUtil.bean2jsonObject(credit));
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.QUERY_NORECORD);
		log.info(Constants.Prompt.QUERY_NORECORD);
    	return result;
    }
    
    /**
     * 查询授信还款记录
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/query/credit/repayment/record.ddxj")
    public ResponseBase queryCreditRepaymentRecord(HttpServletRequest request, HttpServletResponse response,int userId,int recruitId) throws ClientException, IllegalAccessException 
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	List<CreditRepayment> creditRepaymentList = creditService.queryCreditRepaymentRecord(recruitId);
		result.push("creditRepaymentList", JsonUtil.list2jsonToArray(creditRepaymentList));
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.QUERY_NORECORD);
    	log.info(Constants.Prompt.QUERY_NORECORD);
    	return result;
    }
    
    /**
     * 提交评论
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/add/comment.ddxj")
    public ResponseBase addComment(HttpServletRequest request, HttpServletResponse response,Integer recruitId,Integer fromUserId,Integer toUserId,Integer star,Integer anonymous,String labels,
    		String commentContent) throws ClientException, IllegalAccessException 
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	UserComment comment = new UserComment();
    	Recruit recruit = recruitService.selectByPrimaryKey(recruitId);
    	comment.setToUserId(toUserId);
    	comment.setFromUserId(fromUserId);
    	comment.setLevel(star);
    	comment.setRecruitId(recruitId);
    	comment.setAnonymousStatus(anonymous);
    	Set<String> sensitiveWord = validateKeywords.validateKeywords(commentContent);
    	if(!CmsUtils.isNullOrEmpty(sensitiveWord))
    	{
    		result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.TEXT_ILLEGALITY+ sensitiveWord);
			return result;
    	}
    	comment.setCommentContent(commentContent);
    	comment.setCreateTime(new Date());
    	comment.setUpdateTime(new Date());
    	userCommentService.addComment(comment);
    	if(!CmsUtils.isNullOrEmpty(labels))
    	{
    		String[] label = null;
			if(labels.indexOf(Constants.COMMA) >= Constants.Number.REDUCE_ONE_INT)
			{
				label = labels.split(Constants.COMMA);
			}
			else
			{
				label = new String[Constants.Number.ONE_INT];
				label[Constants.Number.ZERO_INT] = labels;
			}
			
			for(String id : label)
			{
				Map<String, Object> date = new HashMap<String, Object>();
				date.put("commentId", comment.getId());
				date.put("labelId", id);
				date.put("createTime", new Date());
				date.put("updateTime", new Date());
				userCommentService.addUserCommentLabel(date);
			}
    	}
    	//修改评论状态
    	User fromUser = userService.selectByPrimaryKey(fromUserId);
    	StringBuffer paramBuffer = new StringBuffer();
		paramBuffer.append("{").append("\"commentId\":").append(comment.getId()).append("}");
		String param = paramBuffer.toString();
		String recruitTitle = recruit.getRecruitTitle();
		//Title处理
		if(recruitTitle.length() > 10)
		{
			recruitTitle = recruitTitle.substring(0, 10)+"...";
		}
    	if(fromUser.getRole() == Constants.Number.TWO_INT)
    	{
    		RecruitRecord recruitRecord = recruitRecordService.queryWorker(recruitId, toUserId);
    		//工头给工人评论
    		recruitRecord.setForemanCommentStatus(Constants.Number.ONE_INT);
    		recruitRecord.setUpdateTime(new Date());
    		recruitRecordService.updateByPrimaryKeySelective(recruitRecord);
    		String title = "【 "+recruitTitle+" 】的工头评价了您的工作";
    		asycService.addUserMessage(toUserId, title, commentContent, 3, param, 9);
    	}else if(fromUser.getRole() == Constants.Number.ONE_INT)
    	{
    		RecruitRecord recruitRecord = recruitRecordService.queryWorker(recruitId, fromUserId);
    		//工人给工头评论
    		recruitRecord.setWorkerCommentStatus(Constants.Number.ONE_INT);
    		recruitRecord.setUpdateTime(new Date());
    		recruitRecordService.updateByPrimaryKeySelective(recruitRecord);
    		String title = "【 "+recruitTitle+" 】的工人评价了您的项目";
    		asycService.addUserMessage(toUserId, title, commentContent, 3, param, 9);
    	}
    	//推送
    	asycService.pushRecruitComment(fromUserId, toUserId, recruitTitle);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.ADD_SUCCESSFUL);
    	log.info(Constants.Prompt.ADD_SUCCESSFUL);
    	return result;
    }
    /**
     * 查询已绑定银行卡
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/query/bank/card/list.ddxj")
    public ResponseBase queryBankCardList(HttpServletRequest request, HttpServletResponse response,int userId) throws ClientException, IllegalAccessException 
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	List<UserBank> userBankList = userService.findUserBankList(userId);
    	result.push("userBankList", JsonUtil.list2jsonToArray(userBankList));
		result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	log.info(Constants.Prompt.QUERY_SUCCESSFUL);
    	return result;
    }
    
    /**
     * 绑定银行卡
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws Exception 
     */
    @RequestMapping(value = "/update/bank/card.ddxj")
    public ResponseBase updateBankCard(HttpServletRequest request, HttpServletResponse response,Integer bankId,int userId,String bankCard,String bankName,String bankType,String bankCode,
    		String cardholderName,String phone,String address) throws Exception 
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	UserBank findUserBankByBankCard = userService.findUserBankByBankCard(bankCard);//查询此卡是否被绑定
    	if(!CmsUtils.isNullOrEmpty(findUserBankByBankCard))
    	{
    		result.setResponse(Constants.FALSE);
    		result.setResponseCode(Constants.SUCCESS_200);
    		result.setResponseMsg(Constants.Prompt.NUMBER_EXISTENCE);
    		return result;
    	}
    	RealAuth queryUserRealAuth = userService.queryUserRealAuth(userId);
    	if(CmsUtils.isNullOrEmpty(queryUserRealAuth) || queryUserRealAuth.getRealStatus() != Constants.Number.THREE_INT)//
    	{
    		result.setResponse(Constants.FALSE);
    		result.setResponseCode(Constants.SUCCESS_200);
    		result.setResponseMsg(Constants.Prompt.AUTHENTICATION_FAILURE);
    		return result;
    	}
    	String validateBankInfo = "";
    	if(!CmsUtils.isNullOrEmpty(bankCard)&&!CmsUtils.isNullOrEmpty(queryUserRealAuth.getIdCardNumber())&&!CmsUtils.isNullOrEmpty(phone)&&!CmsUtils.isNullOrEmpty(cardholderName))
    	{
    		if(userService.queryValidateDayCount(userId, Constants.BANK_VALIDATE) == Constants.Number.THREE_INT)//如果操作超过3次，则不能再绑定，避免用户刷银行卡验证
    		{
    			result.setResponse(Constants.FALSE);
	    		result.setResponseCode(Constants.SUCCESS_200);
	    		result.setResponseMsg(Constants.Prompt.BINDING_TOOMANYTIMES);
	    		return result;
    		}
    		validateBankInfo =BankValidateUtils.validateBankInfo(bankCard, queryUserRealAuth.getIdCardNumber(), phone, cardholderName);
    		//添加接口验证限制
    		ValidateManager validateManager = new ValidateManager();
    		validateManager.setUserId(userId);
    		validateManager.setValidateType(Constants.BANK_VALIDATE);
    		validateManager.setValidateMsg(validateBankInfo);
    		validateManager.setCreateTime(new Date());
    		userService.addValidateManager(validateManager);
    		
    		JSONObject jsonObject = JSONObject.parseObject(validateBankInfo);
    		if("0000".equals(jsonObject.get("code")))//如果等于0000表示成功
    		{
    			JSONObject jsonObject2 = jsonObject.getJSONObject("data");
    			if("R002".equals(jsonObject2.getString("resultCode")))//银行卡信息不匹配
    			{
    				result.setResponse(Constants.FALSE);
    	    		result.setResponseCode(Constants.SUCCESS_200);
    	    		result.setResponseMsg(Constants.Prompt.INFORMATION_MISMATCH);
    	    		return result;
    			}
    			else if("R003".equals(jsonObject2.getString("resultCode")))//银行卡无记录
    			{
    				result.setResponse(Constants.FALSE);
    	    		result.setResponseCode(Constants.SUCCESS_200);
    	    		result.setResponseMsg(Constants.Prompt.NOTFOUND_REPLACE_BANKCARD);
    	    		return result;
    			}
    			else if("R004".equals(jsonObject2.getString("resultCode")))//银行卡特殊原因
    			{
    				result.setResponse(Constants.FALSE);
    	    		result.setResponseCode(Constants.SUCCESS_200);
    	    		result.setResponseMsg(Constants.Prompt.UNIDENTIFIED_REPLACE_BANKCARD);
    	    		return result;
    			}
    		}
    		else
    		{
    			result.setResponse(Constants.FALSE);
        		result.setResponseCode(Constants.SUCCESS_200);
        		result.setResponseMsg(jsonObject.getString("msg"));
        		return result;
    		}
    	}
    	else
    	{
    		result.setResponse(Constants.FALSE);
    		result.setResponseCode(Constants.SUCCESS_200);
    		result.setResponseMsg(Constants.Prompt.COMPLETE_INFORMATION);
    		return result;
    	}
    	UserBank bank = new UserBank();
    	bank.setUserId(userId);
    	bank.setRgb(FindBankUtils.getBankColor(bankCode));
    	bank.setBankCode(bankCode);
    	bank.setBankCard(bankCard);
    	bank.setBankName(bankName);
    	bank.setBankType(bankType);
    	bank.setCardholderName(cardholderName);
    	bank.setCardholderIdNumber(queryUserRealAuth.getIdCardNumber());
    	bank.setAddress(address);
    	bank.setPhone(phone);
    	bank.setValidateJson(validateBankInfo);
    	bank.setUpdateTime(new Date());
    	bank.setCreateTime(new Date());
    	
    	if(bankId != null && bankId > Constants.Number.ZERO_INT)//有Id说明是修改
    	{
    		//传入bankId更新银行卡
    		bank.setId(bankId);
    		userService.updateUserBank(bank);
    		
    		result.setResponse(Constants.TRUE);
    		result.setResponseCode(Constants.SUCCESS_200);
    		result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
    		log.info(Constants.Prompt.UPDATE_SUCCESSFUL);
    	}
    	else
    	{
    		//新增银行卡
    		userService.addUserBank(bank);
    		
    		result.setResponse(Constants.TRUE);
    		result.setResponseCode(Constants.SUCCESS_200);
    		result.setResponseMsg(Constants.Prompt.ADD_SUCCESSFUL);
    		log.info(Constants.Prompt.ADD_SUCCESSFUL);
    	}
    	return result;
    }
    /**
     * 验证银行卡信息
    * @Title: RecruitController.java  
    * @param @param request
    * @param @param response
    * @param @param bankCard
    * @param @return
    * @param @throws Exception参数  
    * @return ResponseBase    返回类型 
    * @throws
    * @Package net.zn.ddxj.api  
    * @author 上海众宁网络科技有限公司-何俊辉
    * @date 2018年5月11日  
    * @version V1.0
     */
    @RequestMapping(value = "/validate/bank/info.ddxj")
    public ResponseBase validateBankInfo(HttpServletRequest request, HttpServletResponse response,String bankCard) throws Exception 
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	if(!CmsUtils.isNullOrEmpty(bankCard))
    	{
    		String findBankInfo = FindBankUtils.findBankInfo(bankCard);
    		if(!CmsUtils.isNullOrEmpty(findBankInfo))
    		{
    			JSONObject parse = JSONObject.parseObject(findBankInfo);
    			if(parse.getBooleanValue("validated"))//验证成功
    			{
    				String bankCode = parse.getString("bank");//银行Code
    				String cardType = parse.getString("cardType");//卡类型
    				if(bankCode.indexOf("RCC") > Constants.Number.REDUCE_ONE_INT)//将全国所有的农村信用社转为一个CODE
    				{
    					bankCode = "RCC";
    				}
    				if(bankCode.indexOf("RCB") > Constants.Number.REDUCE_ONE_INT)//将全国所有的农商银行转为一个CODE
    				{
    					bankCode = "RCC";
    				}
    				if(!FindBankUtils.validateSupportBank(bankCode))
    				{
    					result.setResponse(Constants.FALSE);
        	    		result.setResponseCode(Constants.SUCCESS_200);
        	    		result.setResponseMsg(Constants.Prompt.WONTSUPPORT_BANK);
        	    		return result;
    				}
    				if(!"DC".equals(cardType))//DC表示储蓄卡,如果不等于储蓄卡，则返回
    				{
    					result.setResponse(Constants.FALSE);
        	    		result.setResponseCode(Constants.SUCCESS_200);
        	    		result.setResponseMsg(Constants.Prompt.TSUPPORT_SAVINGSBANK);
        	    		return result;
    				}
    				JSONObject bankJSON = FindBankUtils.queryBankJSON();
    				String banName = bankJSON.getString(parse.getString("bank"));
    				JSONObject jsonObject = new JSONObject();
    				jsonObject.put("bankName", banName);
    				jsonObject.put("bankCode", bankCode);
    				result.push("bank", jsonObject);
    			}
    			else
    			{
    				result.setResponse(Constants.FALSE);
    	    		result.setResponseCode(Constants.SUCCESS_200);
    	    		result.setResponseMsg(Constants.Prompt.INPUTCORRECTLY_BANKNUMBER);
    	    		return result;
    			}
    		}
    	}
    	result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.VALIDATE_SUCCESSFUL);
    	return result;
    }
    /**
     * 更新实名认证
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws Exception 
     */
    @RequestMapping(value = "/update/real/name.ddxj")
    public ResponseBase updateRealName(HttpServletRequest request, HttpServletResponse response,int userId,Integer realNameId,String realName,String idCardFront,
    		String idCardOpposite,String idCardNumber,String idCardHand) throws Exception 
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	if(CmsUtils.isNullOrEmpty(realName)){
    		result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.NAME_ISNOTNULL);
			return result;
    	}
    	if(CmsUtils.isNullOrEmpty(idCardNumber)){
    		result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.IDNUMBER_ISNOTNULL);
			return result;
    	}
    	if(CmsUtils.isNullOrEmpty(idCardFront)){
    		result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.IDPOSITIVE_ISNOTNULL);
			return result;
    	}
    	if(CmsUtils.isNullOrEmpty(idCardOpposite)){
    		result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.IDOTHERSIDE_ISNOTNULL);
			return result;
    	}  
//取消手持身份证
//    	if(CmsUtils.isNullOrEmpty(idCardHand)){
//    		result.setResponse(Constants.FALSE);
//			result.setResponseCode(Constants.SUCCESS_200);
//			result.setResponseMsg(Constants.Prompt.HOLDID_ISNOTNULL);
//			return result;
//    	}
    	RealAuth queryUserRealAuth = userService.queryUserRealAuth(userId);
    	int authCount=userService.queryUserRealAuthCount(idCardNumber);
    	RealAuth auth = null;
    	if(realNameId != null && realNameId > Constants.Number.ZERO_INT)
    	{
    		if(!realNameId.equals(queryUserRealAuth.getId()))
    		{
    			result.setResponse(Constants.FALSE);
    			result.setResponseCode(Constants.SUCCESS_200);
    			result.setResponseMsg(Constants.Prompt.SYSTEM_FAILURE);
    			return result;
    		}
    		auth = userService.queryRealAuthById(realNameId);
    		auth.setRealStatus(Constants.Number.ONE_INT);
    		auth.setFlag(Constants.Number.ONE_INT);
    	}
    	else
    	{
        	if(!CmsUtils.isNullOrEmpty(queryUserRealAuth))
        	{
        		result.setResponse(Constants.FALSE);
    			result.setResponseCode(Constants.SUCCESS_200);
    			result.setResponseMsg(Constants.Prompt.ALREADY_AUTHENTICATION);
    			return result;
        	}
        	
        	if(!CmsUtils.isNullOrEmpty(authCount) && authCount==Constants.Number.ONE_INT)
        	{
        		result.setResponse(Constants.FALSE);
        		result.setResponseCode(Constants.SUCCESS_200);
        		result.setResponseMsg(Constants.Prompt.ALREADY_AUTHENTICATION_REPLACE);
        		return result;
        	}
        	
    		auth = new RealAuth();
    	}
    	String validateReadInfo = "";
    	if(!CmsUtils.isNullOrEmpty(realName) && !CmsUtils.isNullOrEmpty(idCardNumber))
    	{
    		if(!CmsUtils.validateRealName(realName))
    		{
    			result.setResponse(Constants.FALSE);
    			result.setResponseCode(Constants.SUCCESS_200);
    			result.setResponseMsg(Constants.Prompt.NAME_FORMATMISTAKEN);
    			return result;
    		}
    		if(!CmsUtils.validateIdCard(idCardNumber))//验证身份证位数，以及是否是身份证
    		{
    			result.setResponse(Constants.FALSE);
    			result.setResponseCode(Constants.SUCCESS_200);
    			result.setResponseMsg(Constants.Prompt.ID_FORMATMISTAKEN);
    			return result;
    		}
    		if(userService.queryValidateDayCount(userId, Constants.REAL_VALIDATE) == Constants.Number.THREE_INT)//如果操作超过3次，则不能再绑定，避免用户刷银行卡验证
    		{
    			result.setResponse(Constants.FALSE);
	    		result.setResponseCode(Constants.SUCCESS_200);
	    		result.setResponseMsg(Constants.Prompt.AUTHENTICATION_TOOMANYTIMES);
	    		return result;
    		}
    		validateReadInfo = IDCardValidateUtils.validateRealNameInterface(realName, idCardNumber);
    		if(CmsUtils.isNullOrEmpty(validateReadInfo))
    		{
    			result.setResponse(Constants.FALSE);
	    		result.setResponseCode(Constants.SUCCESS_200);
	    		result.setResponseMsg(Constants.Prompt.SYSTEM_FAILURE);
	    		return result;
    		}
    		//添加接口验证限制
    		ValidateManager validateManager = new ValidateManager();
    		validateManager.setUserId(userId);
    		validateManager.setValidateType(Constants.REAL_VALIDATE);
    		validateManager.setValidateMsg(validateReadInfo);
    		validateManager.setCreateTime(new Date());
    		userService.addValidateManager(validateManager);
    		
    		JSONObject jsonObject = JSONObject.parseObject(validateReadInfo);
			if("80008".equals(jsonObject.getString("error_code")))//身份证中心维护，请稍后重试
			{
				result.setResponse(Constants.FALSE);
	    		result.setResponseCode(Constants.SUCCESS_200);
	    		result.setResponseMsg(Constants.Prompt.IDCORE_MAINTAIN);
	    		return result;
			}
			else if("90033".equals(jsonObject.getString("error_code")))//无此身份证号码
			{
				result.setResponse(Constants.FALSE);
	    		result.setResponseCode(Constants.SUCCESS_200);
	    		result.setResponseMsg(Constants.Prompt.NOTHING_IDNUMBER);
	    		return result;
			}
			else if("90099".equals(jsonObject.getString("error_code")))//信息填写有误，请检查后再提交
			{
				result.setResponse(Constants.FALSE);
	    		result.setResponseCode(Constants.SUCCESS_200);
	    		result.setResponseMsg(Constants.Prompt.INFORMATION_FILLINGINERROR);
	    		return result;
			}
			else if("0".equals(jsonObject.getString("error_code")))//验证成功
			{
				JSONObject jsonObject2 = jsonObject.getJSONObject("result");
				JSONObject jsonObject3 = jsonObject2.getJSONObject("details");
				auth.setIdCardAddress(jsonObject3.getString("addr"));
			}
    		else
    		{
    			result.setResponse(Constants.FALSE);
        		result.setResponseCode(Constants.SUCCESS_200);
        		result.setResponseMsg(jsonObject.getString("reason"));
        		return result;
    		}
    		
    	}
    	auth.setValidateData(validateReadInfo);
    	auth.setUserId(userId);
    	auth.setRealName(realName);
    	auth.setIdCardFront(idCardFront);
    	auth.setIdCardOpposite(idCardOpposite);
    	auth.setIdCardNumber(idCardNumber);
    	//auth.setIdCardHand(idCardHand);
    	auth.setUpdateTime(new Date());
    	auth.setCreateTime(new Date());
    	
    	if(realNameId != null && realNameId > Constants.Number.ZERO_INT)
    	{
    		auth.setRealStatus(Constants.Number.ONE_INT);
    		userService.updateUserRealAuth(auth);
    		result.setResponse(Constants.TRUE);
        	result.setResponseCode(Constants.SUCCESS_200);
        	result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
        	log.info(Constants.Prompt.UPDATE_SUCCESSFUL);
    	}
    	else
    	{
    		userService.addUserRealAuth(auth);
    		result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.ADD_SUCCESSFUL);
			log.info(Constants.Prompt.ADD_SUCCESSFUL);
    	}
    	asycService.addManagerMessage(Constants.Number.TWO_INT,"用户ID："+userId,"您有一条新的实名认证审核信息，请及时处理。" , null);
    	return result;
    }
    
    /**
     * 查询实名认证信息
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/query/real/name.ddxj")
    public ResponseBase queryRealName(HttpServletRequest request, HttpServletResponse response,int userId) 
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	
    	RealAuth auth = userService.queryUserRealAuth(userId);
    	result.push("realAuth",!CmsUtils.isNullOrEmpty(auth)?auth:"");
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	log.info(Constants.Prompt.QUERY_SUCCESSFUL);
    	return result;
    }
    
    /**
     * 查询招聘详情
     * @param request
     * @param response
     * @param userId
     * @param recruitId
     * @return
     */
    @RequestMapping(value = "/recruit/details.ddxj")
    public ResponseBase queryRecruitDetails(HttpServletRequest request, HttpServletResponse response,Integer userId,Integer recruitId){
    	ResponseBase result = ResponseBase.getInitResponse();
    	//根据招聘id查出招聘信息
    	Recruit recruit = recruitService.selectByPrimaryKey(recruitId);
    	UserCollection collection = collectionService.queryUserIsCollection(userId,recruitId);
    	RecruitRecord allWorkerDeliver = recruitRecordService.queryAllWorkerDeliver(recruitId, userId);
    	if(!CmsUtils.isNullOrEmpty(allWorkerDeliver))
    	{
    		if(allWorkerDeliver.getFlag() == Constants.Number.TWO_INT)
    		{
    			//取消过该报名
    			result.push("isApply", Constants.Number.THREE_INT);
    		}else
    		{
    			if(allWorkerDeliver.getEnlistStatus() == Constants.Number.THREE_INT)
    			{
    				result.push("isApply", Constants.Number.FOUR_INT);
    			}else
    			{
    				if(allWorkerDeliver.getWorkerStatus() == Constants.Number.FOUR_INT)
    				{
    					result.push("isApply", Constants.Number.FOUR_INT);
    				}else
    				{
    					result.push("isApply", Constants.Number.ONE_INT);
    				}
    			}
    		}
    	}else
    	{
    		result.push("isApply", Constants.Number.TWO_INT);
    	}
    	result.push("recruit", JsonUtil.bean2jsonObject(recruit));
    	result.push("enlistNumber", recruit.getEnlistNumber());
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	if(!CmsUtils.isNullOrEmpty(collection)){
    		if(collection.getFlag() == Constants.Number.ONE_INT){
    			//已经收藏
    			result.push("isCollection", Constants.Number.ONE_INT);
    		}else{
    			result.push("isCollection", Constants.Number.TWO_INT);
    		}
    	}
    	else
    	{
    		result.push("isCollection", Constants.Number.TWO_INT);
    	}
    	return result;
    }
    /**
     * 查询招聘详情，报名工人，授信状态
     * @param request
     * @param response
     * @param userId
     * @param recruitId
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/query/recruit/enlist/details.ddxj")
    public ResponseBase queryRecruitEnlistDetails(HttpServletRequest request, HttpServletResponse response,Integer userId,Integer recruitId) throws Exception{
    	ResponseBase result=ResponseBase.getInitResponse();
    	//根据招聘id查询招聘信息
    	Recruit recruit=recruitService.selectByPrimaryKey(recruitId);
    	int countSignUp=recruitRecordService.countSingUp(recruitId);
    	//根据recruitId,userId查询授信信息
    	RecruitCredit recruitCreditRecord=recruitCreditService.queryRecruitCredit(userId,recruitId);
    	//根据招聘id查询报名列表
    	List<User>recruitUserList=userService.queryEnlistUserList(userId, recruitId, null);
		result.push("recruitCreditRecord", JsonUtil.bean2jsonObject(recruitCreditRecord));
		result.push("recruitUserList", JsonUtil.list2jsonToArray(recruitUserList));
		result.push("recruit", JsonUtil.bean2jsonObject(recruit));
		result.push("countSignUp", countSignUp);
    	result.setResponse(Constants.TRUE);
    	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	result.setResponseCode(Constants.SUCCESS_200);
		return result;
    	
    }
    
    /**
     * 工人端更新报名状态
     * @param request
     * @param response
     * @param userId
     * @param recruitId
     * @param enlistStatus
     * @return
     * @throws Exception
     */
    @RequestMapping("/update/deliver/recruit.ddxj")
    public ResponseBase updateDeliverRecruit(HttpServletRequest request, HttpServletResponse response,Integer userId,Integer recruitId,Integer enlistStatus) throws Exception
    {
		ResponseBase result=ResponseBase.getInitResponse();
		//根据工人id和招聘id查询招聘记录
		RecruitRecord recruitRecord = recruitRecordService.queryWorkerDeliver(recruitId, userId);
		Recruit recruit = recruitService.selectByPrimaryKey(recruitId);
		if(!CmsUtils.isNullOrEmpty(recruitRecord))
		{
			//根据入参状态来判断是什么动作
			if(enlistStatus == Constants.Number.TWO_INT)
			{
				//判断是否超过招聘人数
				Integer person = recruitService.findRecruitPersonById(recruitId);
				if(person >= recruit.getRecruitPerson())
				{
					//拒绝报名动作
					recruitRecord.setEnlistStatus(Constants.Number.THREE_INT);
					recruitRecord.setForemanStatus(Constants.Number.ZERO_INT);
					recruitRecord.setWorkerStatus(Constants.Number.ZERO_INT);
					recruitRecord.setUpdateTime(new Date());
					recruitRecord.setWorkerCommentStatus(Constants.Number.ZERO_INT);
					recruitRecord.setForemanCommentStatus(Constants.Number.ZERO_INT);
					recruitRecord.setBalanceStatus(Constants.Number.ZERO_INT);
					recruitRecordService.updateByPrimaryKeySelective(recruitRecord);
					result.push("enlistStatus", Constants.Number.TWO_INT);
					result.setResponse(Constants.FALSE);
					result.setResponseCode(Constants.SUCCESS_200);
					result.setResponseMsg(Constants.Prompt.RECRUITPEOPLE_ISFULL);
					return result;
				}
				//判断工人是否有在进行中的项目
				Integer count = recruitService.findRecruitByUserId(userId);
				if(count != null && count > Constants.Number.ZERO_INT)
				{
					//取消报名动作
					recruitRecord.setFlag(Constants.Number.TWO_INT);
					recruitRecordService.updateByPrimaryKeySelective(recruitRecord);
					result.push("enlistStatus", Constants.Number.SEVEN_INT);
					result.setResponse(Constants.FALSE);
					result.setResponseCode(Constants.SUCCESS_200);
					result.setResponseMsg(Constants.Prompt.HAVEINHANDPROJECT_CANNOTPARTICIPATE);
					return result;
				}
				
				//工人点击我已到达工地操作
				if(recruitRecord.getForemanStatus() == Constants.Number.ZERO_INT)
				{
					//工头未点击确认开工
					recruitRecord.setWorkerStatus(Constants.Number.ONE_INT);
					recruitRecord.setUpdateTime(new Date());
					recruitRecordService.updateByPrimaryKeySelective(recruitRecord);
					//推送
					asycService.pushWorkerOrForeManStatus(userId, recruitId);
					result.push("enlistStatus", Constants.Number.FIVE_INT);
					result.setResponse(Constants.TRUE);
					result.setResponseCode(Constants.SUCCESS_200);
					result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
				}
				if(recruitRecord.getForemanStatus() == Constants.Number.ONE_INT)
				{
					//工头已点击确认开工
					recruitRecord.setWorkerStatus(Constants.Number.TWO_INT);
					recruitRecord.setForemanStatus(Constants.Number.TWO_INT);
					recruitRecord.setUpdateTime(new Date());
					recruitRecordService.updateByPrimaryKeySelective(recruitRecord);
					//推送
					asycService.pushWorkerOrForeManStatus(userId, recruitId);
					result.push("enlistStatus", Constants.Number.SIX_INT);
					result.setResponse(Constants.TRUE);
					result.setResponseCode(Constants.SUCCESS_200);
					result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
				}
				
			}
			if(enlistStatus == Constants.Number.THREE_INT)
			{
				//工人点击取消报名
				//判断是否开工
				if(recruitRecord.getWorkerStatus() == Constants.Number.TWO_INT && recruitRecord.getForemanStatus() == Constants.Number.TWO_INT)
				{
					//已经开工，取消报名失败
					result.setResponse(Constants.FALSE);
					result.setResponseCode(Constants.SUCCESS_200);
					result.setResponseMsg(Constants.Prompt.ALREADYSTART_CANNOTCANCEL);
				}else
				{
					recruitRecord.setFlag(Constants.Number.TWO_INT);
					recruitRecordService.updateByPrimaryKeySelective(recruitRecord);
					result.push("enlistStatus", Constants.Number.SEVEN_INT);
					//推送
					asycService.pushWorkerOrForeManStatus(userId, recruitId);
					result.setResponse(Constants.TRUE);
					result.setResponseCode(Constants.SUCCESS_200);
					result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
				}
			}
		}
		else
		{
			if(enlistStatus == Constants.Number.ZERO_INT)
			{
				//工人点击立即报名,条件判断
				//1、工人最多只能报三次名  
				if(recruitRecordService.selectByUserFlag(userId) == Constants.Number.THREE_INT || recruitRecordService.selectByUserFlag(userId) > Constants.Number.THREE_INT)
				{
		    		result.setResponse(Constants.FALSE);
			    	result.setResponseCode(Constants.SUCCESS_200);
			    	result.setResponseMsg(Constants.Prompt.PEOPLENUMBER_TOOMUCH);
			    	return result;
				}
				//2、工人状态为在职
				User user = userService.selectByPrimaryKey(userId);
				if(user.getStatus() == Constants.Number.ONE_INT || user.getStatus() > Constants.Number.ONE_INT)
				{ 	
		    		result.setResponse(Constants.FALSE); 
			    	result.setResponseCode(Constants.SUCCESS_200);
			    	result.setResponseMsg(Constants.Prompt.ONTHEJOB_FAILURETOSIGNUP);
			    	return result;
				}
				if(2 == user.getRole())
				{
					result.setResponse(Constants.FALSE);
			    	result.setResponseCode(Constants.SUCCESS_200);
			    	result.setResponseMsg(Constants.Prompt.ONTHEJOB_FAIL);
			    	return result;
				}
				else
				{
					RecruitRecord record = new RecruitRecord();
					record.setUserId(userId);
					record.setRecruitId(recruitId);
					record.setEnlistStatus(Constants.Number.ONE_INT);
					record.setForemanStatus(Constants.Number.ZERO_INT);
					record.setWorkerStatus(Constants.Number.ZERO_INT);
					record.setWorkerCommentStatus(Constants.Number.ZERO_INT);
					record.setForemanCommentStatus(Constants.Number.ZERO_INT);
					record.setBalanceStatus(Constants.Number.ZERO_INT);
					record.setCreateTime(new Date());
					record.setUpdateTime(new Date());
					recruitRecordService.insertSelective(record);
					result.push("enlistStatus", Constants.Number.ONE_INT);
					//推送
					asycService.pushWorkerOrForeManStatus(userId, recruitId);
					asycService.addManagerMessage(Constants.Number.FIVE_INT,"项目名："+recruit.getRecruitTitle()+"有工人报名，请及时联系","您有一条新的报名信息，请及时处理。" , null);
					log.info(Constants.Prompt.ADD_SUCCESSFUL);
					result.setResponse(Constants.TRUE);
					result.setResponseCode(Constants.SUCCESS_200);
					result.setResponseMsg(Constants.Prompt.ADD_SUCCESSFUL);
				}
			}
		}
    	return result;
    }
    
    /**
     * 工头端更新工人报名状态
     * @param request
     * @param response
     * @param recruitId
     * @param fromUserId
     * @param toUserId
     * @param enlistStatus
     * @return
     * @throws Exception
     */
    @RequestMapping("/update/worker/enlist/status.ddxj")
    public ResponseBase updateWorkerEnlistStatus(HttpServletRequest request, HttpServletResponse response,Integer recruitId,Integer fromUserId,Integer toUserId,Integer enlistStatus) throws Exception{
		ResponseBase result=ResponseBase.getInitResponse();
		//根据工人id和招聘id查询招聘记录
		RecruitRecord recruitRecord = recruitRecordService.queryWorker(recruitId, toUserId);
		UserPassword userPassword = userService.selectPasswordByUserId(fromUserId);
		Recruit recruit = recruitService.selectByPrimaryKey(recruitId);
		int payStatus;//是否设置过支付密码标识
		if(!CmsUtils.isNullOrEmpty(userPassword))
		{
			if(!CmsUtils.isNullOrEmpty(userPassword.getPayPassword()))
			{
				payStatus = Constants.Number.ONE_INT;
			}else
			{
				payStatus = Constants.Number.ZERO_INT;
			}
		}else
		{
			payStatus = Constants.Number.ZERO_INT;
		}
		String refuseCause = request.getParameter("refuse_cause");
		if(!CmsUtils.isNullOrEmpty(recruitRecord))
		{
			if(recruitRecord.getEnlistStatus() == Constants.Number.THREE_INT)
			{
				//工头拒绝报名
				result.push("enlistStatus", Constants.Number.TWO_INT);
				result.setResponse(Constants.TRUE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
			}
			else
			{
				if(enlistStatus == Constants.Number.ONE_INT)
				{
					//确认报名动作
					recruitRecord.setEnlistStatus(Constants.Number.TWO_INT);
					recruitRecord.setForemanStatus(Constants.Number.ZERO_INT);
					recruitRecord.setWorkerStatus(Constants.Number.ZERO_INT);
					recruitRecord.setUpdateTime(new Date());
					recruitRecord.setWorkerCommentStatus(Constants.Number.ZERO_INT);
					recruitRecord.setForemanCommentStatus(Constants.Number.ZERO_INT);
					recruitRecord.setBalanceStatus(Constants.Number.ZERO_INT);
					recruitRecordService.updateByPrimaryKeySelective(recruitRecord);
					result.push("enlistStatus", Constants.Number.THREE_INT);
					//推送
					asycService.pushWorkerOrForeManStatus(toUserId, recruitId);
					result.setResponse(Constants.TRUE);
					result.setResponseCode(Constants.SUCCESS_200);
					result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
				}
				if(enlistStatus == Constants.Number.TWO_INT)
				{
					//拒绝报名动作
					recruitRecord.setEnlistStatus(Constants.Number.THREE_INT);
					recruitRecord.setForemanStatus(Constants.Number.ZERO_INT);
					recruitRecord.setWorkerStatus(Constants.Number.ZERO_INT);
					recruitRecord.setUpdateTime(new Date());
					recruitRecord.setWorkerCommentStatus(Constants.Number.ZERO_INT);
					recruitRecord.setForemanCommentStatus(Constants.Number.ZERO_INT);
					recruitRecord.setBalanceStatus(Constants.Number.ZERO_INT);
					recruitRecordService.updateByPrimaryKeySelective(recruitRecord);
					result.push("enlistStatus", Constants.Number.TWO_INT);
					//推送
					asycService.pushWorkerOrForeManStatus(toUserId, recruitId);
					result.setResponse(Constants.TRUE);
					result.setResponseCode(Constants.SUCCESS_200);
					result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
				}
				if(enlistStatus == Constants.Number.FIVE_INT)
				{
					//判断是否超过招聘人数
					Integer person = recruitService.findRecruitPersonById(recruitId);
					if(person >= recruit.getRecruitPerson())
					{
						//拒绝报名动作
						recruitRecord.setEnlistStatus(Constants.Number.THREE_INT);
						recruitRecord.setForemanStatus(Constants.Number.ZERO_INT);
						recruitRecord.setWorkerStatus(Constants.Number.ZERO_INT);
						recruitRecord.setUpdateTime(new Date());
						recruitRecord.setWorkerCommentStatus(Constants.Number.ZERO_INT);
						recruitRecord.setForemanCommentStatus(Constants.Number.ZERO_INT);
						recruitRecord.setBalanceStatus(Constants.Number.ZERO_INT);
						recruitRecordService.updateByPrimaryKeySelective(recruitRecord);
						result.push("enlistStatus", Constants.Number.TWO_INT);
						result.setResponse(Constants.FALSE);
						result.setResponseCode(Constants.SUCCESS_200);
						result.setResponseMsg(Constants.Prompt.RECRUITPEOPLE_ISFULL);
						return result;
					}
					//判断工人是否有在进行中的项目
					Integer count = recruitService.findRecruitByUserId(toUserId);
					if(count != null && count > Constants.Number.ZERO_INT)
					{
						//工头点击已拒绝
						recruitRecord.setEnlistStatus(Constants.Number.TWO_INT);
						recruitRecord.setWorkerStatus(Constants.Number.FOUR_INT);//工人开工状态变为被拒绝
						recruitRecordService.updateByPrimaryKeySelective(recruitRecord);
						result.push("enlistStatus", Constants.Number.FOUR_INT);
						result.setResponse(Constants.FALSE);
						result.setResponseCode(Constants.SUCCESS_200);
						result.setResponseMsg(Constants.Prompt.WORKERHAVEINHANDPROJECT_CANNOTPARTICIPATE);
						return result;
					}
					//工头确认到达工地动作
					recruitRecord.setForemanStatus(Constants.Number.ONE_INT);
					if(recruitRecord.getWorkerStatus() == Constants.Number.ONE_INT)
					{
						//工人已经点击确认到达工地，双方变为进行中
						recruitRecord.setWorkerStatus(Constants.Number.TWO_INT);
						recruitRecord.setForemanStatus(Constants.Number.TWO_INT);
						recruitRecordService.updateByPrimaryKeySelective(recruitRecord);
						result.push("enlistStatus", Constants.Number.SIX_INT);
						//推送
						asycService.pushWorkerOrForeManStatus(toUserId, recruitId);
						result.setResponse(Constants.TRUE);
						result.setResponseCode(Constants.SUCCESS_200);
						result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
					}
					else
					{
						//隐含意思录用工人，工人状态变为已录用
						recruitRecord.setWorkerStatus(Constants.Number.FIVE_INT);
						recruitRecordService.updateByPrimaryKeySelective(recruitRecord);
						result.push("enlistStatus", Constants.Number.FIVE_INT);
						//推送
						asycService.pushWorkerOrForeManStatus(toUserId, recruitId);
						result.setResponse(Constants.TRUE);
						result.setResponseCode(Constants.SUCCESS_200);
						result.setResponseMsg(Constants.Prompt.WORKER_UNCONFIRMED);
					}
				}
				if(enlistStatus == Constants.Number.SIX_INT)
				{
					//工头结算工资动作
					//判断工头是否给该工人结算
					if(recruitRecord.getBalanceStatus() == Constants.Number.ZERO_INT)
					{
						result.push("enlistStatus", Constants.Number.SIX_INT);
					}
					else if(recruitRecord.getBalanceStatus() == Constants.Number.ONE_INT)
					{
						result.push("enlistStatus", Constants.Number.SEVEN_INT);
						//推送
						asycService.pushWorkerOrForeManStatus(toUserId, recruitId);
					}
					result.push("payStatus", payStatus);
					result.setResponse(Constants.TRUE);
					result.setResponseCode(Constants.SUCCESS_200);
					result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
					
				}
				if(enlistStatus == Constants.Number.SEVEN_INT)
				{
					//工头结束项目动作
					if(recruitRecord.getBalanceStatus() == Constants.Number.ZERO_INT)
					{
						result.push("enlistStatus", Constants.Number.SIX_INT);
					}
					else if(recruitRecord.getBalanceStatus() == Constants.Number.ONE_INT && recruitRecord.getForemanStatus() == Constants.Number.TWO_INT && recruitRecord.getForemanCommentStatus() == Constants.Number.ZERO_INT)
					{
						recruitRecord.setForemanStatus(Constants.Number.THREE_INT);
						recruitRecord.setWorkerStatus(Constants.Number.THREE_INT);
						recruitRecordService.updateByPrimaryKeySelective(recruitRecord);
						result.push("enlistStatus", Constants.Number.EIGHT_INT);
						//推送
						asycService.pushWorkerOrForeManStatus(toUserId, recruitId);
					}
					else if(recruitRecord.getForemanStatus() == Constants.Number.THREE_INT && recruitRecord.getForemanCommentStatus() == Constants.Number.ZERO_INT)
					{
						result.push("enlistStatus", Constants.Number.EIGHT_INT);
					}
					else if(recruitRecord.getForemanStatus() == Constants.Number.THREE_INT && recruitRecord.getForemanCommentStatus() == Constants.Number.ONE_INT)
					{
						result.push("enlistStatus", Constants.Number.NINE_INT);
					}
					result.setResponse(Constants.TRUE);
					result.setResponseCode(Constants.SUCCESS_200);
					result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
				}
				if(enlistStatus == Constants.Number.FOUR_INT)
				{
					//工头点击已拒绝
					recruitRecord.setEnlistStatus(Constants.Number.TWO_INT);
					recruitRecord.setWorkerStatus(Constants.Number.FOUR_INT);//工人开工状态变为被拒绝
					recruitRecordService.updateByPrimaryKeySelective(recruitRecord);
					result.push("enlistStatus", Constants.Number.FOUR_INT);
					//推送
					asycService.pushWorkerOrForeManStatus(toUserId, recruitId);
					result.setResponse(Constants.TRUE);
					result.setResponseCode(Constants.SUCCESS_200);
					result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
				}
//				if(enlistStatus == Constants.Number.THREE_INT)
//				{
//					//工头点击录用
//					recruitRecord.setWorkerStatus(Constants.Number.FIVE_INT);//工人开工状态变为已录用
//					recruitRecord.setUpdateTime(new Date());
//					recruitRecordService.updateByPrimaryKeySelective(recruitRecord);
//					result.push("enlistStatus", Constants.Number.SIX_INT);
//					result.setResponse(Constants.TRUE);
//					result.setResponseCode(Constants.SUCCESS_200);
//					result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
//				}
			}
		}
    	return result;
    }
    
    /**
     * 取消招聘
     * @param request
     * @param response
     * @param userId
     * @param recruitId
     * @return
     * @throws Exception
     */
    @RequestMapping("/update/recruit/status.ddxj")
    public ResponseBase updateCancelRecruit(HttpServletRequest request, HttpServletResponse response,Integer userId,Integer recruitId) throws Exception{
		ResponseBase result=ResponseBase.getInitResponse();
		int count = recruitService.updateRecruitCancelById(recruitId, userId);
		int recruitCount=recruitService.queryRecruitByUserId(userId);
		if(count >= Constants.Number.ONE_INT){
			result.setResponse(Constants.TRUE);
	    	result.setResponseCode(Constants.SUCCESS_200);
	    	result.push("recruitCount", recruitCount);
	    	result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
		}else{
			result.setResponse(Constants.FALSE);
	    	result.setResponseCode(Constants.SUCCESS_200);
	    	result.setResponseMsg(Constants.Prompt.UPDATE_FAILURE);
		}
    	return result;
    }
    
    /**
     * 修改截止时间
     * @param request
     * @param response
     * @param userId
     * @param recruitId
     * @param stopTime
     * @return
     * @throws Exception
     */
    @RequestMapping("/update/recruit/end/time.ddxj")
    public ResponseBase updateRecruitEndTime(HttpServletRequest request, HttpServletResponse response,Integer userId,Integer recruitId,String stopTime) throws Exception{
		ResponseBase result=ResponseBase.getInitResponse();
		Date stopDatetime = DateUtils.getDate(stopTime,"yyyy-MM-dd");
		int count = recruitService.updateEndTimeById(recruitId, userId, stopDatetime);
		if(count >= Constants.Number.ONE_INT){
			result.setResponse(Constants.TRUE);
	    	result.setResponseCode(Constants.SUCCESS_200);
	    	result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
		}else{
			result.setResponse(Constants.FALSE);
	    	result.setResponseCode(Constants.SUCCESS_200);
	    	result.setResponseMsg(Constants.Prompt.UPDATE_FAILURE);
		}
    	return result;
    }

    /**
     * 添加授信记录
     * @param request
     * @param response
     * @param creditId
     * @param userId
     * @param realName
     * @param idCardFront
     * @param idCardOpposite
     * @param idCardNumber
     * @param idCardHand
     * @param recruitId
     * @return
     */
    @RequestMapping("/add/credit/record")
    public ResponseBase addCreditRecord(HttpServletRequest request, HttpServletResponse response,Integer creditId,Integer userId,String creditContactsImage,String creditUrgentContactsJson,Integer recruitId){
    	ResponseBase result=ResponseBase.getInitResponse();
    	RecruitCredit findRecruitCredit = recruitCreditService.findRecruitCredit(recruitId);
    	
    	if(CmsUtils.isNullOrEmpty(findRecruitCredit))//增加
    	{
    		findRecruitCredit = new RecruitCredit(); 
        	//合同照片参数解析
        	String[] images = null;
        	if(!CmsUtils.isNullOrEmpty(creditContactsImage) && !CmsUtils.isNullOrEmpty(creditUrgentContactsJson))
        	{
            	CreditContactsImage contactsImage = new CreditContactsImage();
            	findRecruitCredit.setCreditId(creditId);
            	findRecruitCredit.setUserId(userId);
            	findRecruitCredit.setRecruitId(recruitId);
            	findRecruitCredit.setCreditStatus(Constants.Number.ONE_INT);
            	findRecruitCredit.setCreateTime(new Date());
            	findRecruitCredit.setUpdateTime(new Date());
        		recruitCreditService.insertSelective(findRecruitCredit);
    	    	log.info("#####################添加授信记录成功#####################");
    	    	if(creditContactsImage.indexOf("###") >= Constants.Number.REDUCE_ONE_INT)
        		{
        			images = creditContactsImage.split("###");
        		}
        		else
        		{
        			images = new String[Constants.Number.ONE_INT];
        			images[Constants.Number.ZERO_INT] = creditContactsImage;
        		}
        		contactsImage.setRecruitCreditId(findRecruitCredit.getId());
        		for (String image : images) {
        			contactsImage.setCreateTime(new Date());
        			contactsImage.setUpdateTime(new Date());
        			contactsImage.setContractImage(image);
        			recruitRecordService.insertImages(contactsImage);
        			log.info("#####################添加合同图片成功#####################");
        		}
        		//紧急联系人参数解析
            	CreditUrgentContacts contacts = new CreditUrgentContacts();
        		JSONArray array = (JSONArray) JSONArray.parse(creditUrgentContactsJson);
        		for (int c = Constants.Number.ZERO_INT ; c < array.size() ; c++) 
        		{
        			String contactsName = array.getJSONObject(c).getString("contactsName");
        			String contactsPhone = array.getJSONObject(c).getString("contactsPhone");
        			String contactsRelation = array.getJSONObject(c).getString("contactsRelation");
        			contacts.setContactsName(contactsName);
        			contacts.setContactsPhone(contactsPhone);
        			contacts.setContactsRelation(contactsRelation);
        			contacts.setCreateTime(new Date());
        			contacts.setUpdateTime(new Date());
        			contacts.setRecruitCreditId(findRecruitCredit.getId());
        			recruitRecordService.insertUrgent(contacts);
        			log.info("#####################添加紧急联系人成功#####################");
    			}
    	    	result.setResponse(Constants.TRUE);
    	    	result.setResponseCode(Constants.SUCCESS_200);
    	    	result.setResponseMsg(Constants.Prompt.ADD_SUCCESSFUL);
        	}else
        	{
        		result.setResponse(Constants.FALSE);
    	    	result.setResponseCode(Constants.SUCCESS_200);
    	    	log.info("#####################添加授信记录失败#####################");
    	    	result.setResponseMsg(Constants.Prompt.ADD_FAILURE);
        	}
    	}
    	else
    	{//修改
        	CreditContactsImage contactsImage = new CreditContactsImage();
    		//合同照片参数解析
        	String[] images = null;
        	if(!CmsUtils.isNullOrEmpty(creditContactsImage) && !CmsUtils.isNullOrEmpty(creditUrgentContactsJson))
        	{
        		
        		findRecruitCredit.setCreditId(creditId);
        		findRecruitCredit.setCreditStatus(Constants.Number.ONE_INT);
        		findRecruitCredit.setUpdateTime(new Date());
        		recruitCreditService.updateRecruitCreditRecrod(findRecruitCredit);
    	    	log.info("#####################修改授信记录成功#####################");
    	    	recruitRecordService.deleteImages(findRecruitCredit.getId());
    	    	if(creditContactsImage.indexOf("###") >= Constants.Number.REDUCE_ONE_INT)
        		{
        			images = creditContactsImage.split("###");
        		}
        		else
        		{
        			images = new String[Constants.Number.ONE_INT];
        			images[Constants.Number.ZERO_INT] = creditContactsImage;
        		}
        		contactsImage.setRecruitCreditId(findRecruitCredit.getId());
        		for (String image : images) {
        			contactsImage.setCreateTime(new Date());
        			contactsImage.setUpdateTime(new Date());
        			contactsImage.setContractImage(image);
        			recruitRecordService.insertImages(contactsImage);
        			log.info("#####################添加合同图片成功#####################");
        		}
        		CreditUrgentContacts contacts = new CreditUrgentContacts();
        		recruitRecordService.deleteUrgent(findRecruitCredit.getId());
        		//紧急联系人参数解析
        		JSONArray array = (JSONArray) JSONArray.parse(creditUrgentContactsJson);
        		for (int c = Constants.Number.ZERO_INT ; c < array.size() ; c++) 
        		{
        			String contactsName = array.getJSONObject(c).getString("contactsName");
        			String contactsPhone = array.getJSONObject(c).getString("contactsPhone");
        			String contactsRelation = array.getJSONObject(c).getString("contactsRelation");
        			contacts.setContactsName(contactsName);
        			contacts.setContactsPhone(contactsPhone);
        			contacts.setContactsRelation(contactsRelation);
        			contacts.setCreateTime(new Date());
        			contacts.setUpdateTime(new Date());
        			contacts.setRecruitCreditId(findRecruitCredit.getId());
        			recruitRecordService.insertUrgent(contacts);
        			log.info("#####################添加紧急联系人成功#####################");
    			}
    	    	result.setResponse(Constants.TRUE);
    	    	result.setResponseCode(Constants.SUCCESS_200);
    	    	result.setResponseMsg(Constants.Prompt.ADD_SUCCESSFUL);
        	}else
        	{
        		result.setResponse(Constants.FALSE);
    	    	result.setResponseCode(Constants.SUCCESS_200);
    	    	log.info("#####################添加授信记录失败#####################");
    	    	result.setResponseMsg(Constants.Prompt.ADD_FAILURE);
        	}
    		
    	}
    	
    	return result;
    }
    
    /**
     * 查看工资发放记录
     * @param request
     * @param response
     * @param userId
     * @param recruitId
     * @return
     * @throws Exception
     */
    @RequestMapping("/query/recruit/wages/record.ddxj")
    public ResponseBase queryRecruitWagesRecord(HttpServletRequest request, HttpServletResponse response,Integer userId,Integer recruitId) throws Exception{
		ResponseBase result=ResponseBase.getInitResponse();
		List<SalaryRecord> records = salaryRecordService.selectByUserIdAndRecruitId(userId, recruitId);
		int totalCount = salaryRecordService.selectCount(userId, recruitId);
		BigDecimal totalMoney=salaryRecordService.totalMoney(userId, recruitId);		
		if(!CmsUtils.isNullOrEmpty(records))
		{
			if(CmsUtils.isNullOrEmpty(totalMoney))
			{
				result.push("totalMoney", Constants.Number.ZERO_INT);
			}
			else{
				result.push("totalMoney", totalMoney);
			}
			result.push("salaryRecordList", JsonUtil.list2jsonToArray(records));
			result.push("totalCount", totalCount);
			result.setResponse(Constants.TRUE);
	    	result.setResponseCode(Constants.SUCCESS_200);
	    	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
		}else
		{
			result.push("salaryRecordList", null);
			result.push("totalCount", Constants.Number.ZERO_INT);
			result.push("totalMoney", Constants.Number.ZERO_INT);
			result.setResponse(Constants.TRUE);
	    	result.setResponseCode(Constants.SUCCESS_200);
	    	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
		}
    	return result;
    }
    /**
     * 查询用户是否以前授信过
     * @param request
     * @param response
     * @param userId
     * @return
     * @author ddxj
     */
    @RequestMapping(value="/query/user/is/credis.ddxj")
	public ResponseBase queryUserIsCredit(HttpServletRequest request, HttpServletResponse response,Integer userId,Integer recruitId){
    	ResponseBase result=ResponseBase.getInitResponse();
    	RecruitCredit recruitCreditRecord=recruitCreditService.queryUserIsCredit(userId,recruitId);
    	if(!CmsUtils.isNullOrEmpty(recruitCreditRecord)){
    		result.setResponse(Constants.TRUE);
        	result.setResponseCode(Constants.SUCCESS_200);
        	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
        	result.push("recruitCreditRecord", JsonUtil.bean2jsonObject(recruitCreditRecord));
    	}else{
    		result.setResponse(Constants.FALSE);
        	result.setResponseCode(Constants.SUCCESS_200);
        	result.setResponseMsg(Constants.Prompt.USER_NOTCREDIT);
    	}
    	
    	return result;
    	
    }
    /**
	 * 查询我收到的职位邀请
	 * @param request
	 * @param response
	 * @param userId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @author ddxj
     * @throws IllegalAccessException 
	 * 
	 */
	@RequestMapping(value="/query/my/received/recruit/list.ddxj")
	public ResponseBase queryMyReceivedRecruitList(HttpServletRequest request,HttpServletResponse response,Integer userId,Integer type,Integer pageNum,Integer pageSize) throws IllegalAccessException{
		ResponseBase result=ResponseBase.getInitResponse();
		PageHelper.startPage(pageNum, pageSize);
		if(type==Constants.Number.ONE_INT)//查询收到的邀请的活动列表
		{
			List<Recruit> recruitLists = recruitService.queryMyReceivedRecruitList(userId);
			result.setResponse(Constants.TRUE);
	    	result.setResponseCode(Constants.SUCCESS_200);
	    	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
	    	result.push("recruitList", JsonUtil.list2jsonToArray(recruitLists)); 
		}
		else if(type==Constants.Number.TWO_INT)//查询收藏的活动列表
		{
			List<Recruit> collectionList = recruitService.queryCollectionList(userId);
			result.setResponse(Constants.TRUE);
	    	result.setResponseCode(Constants.SUCCESS_200);
	    	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
	    	result.push("recruitList", JsonUtil.list2jsonToArray(collectionList)); 
		}
		else if(type==Constants.Number.THREE_INT)//查询报名的活动列表
		{
			List<Recruit> applyList = recruitService.queryApplyList(userId);
			result.setResponse(Constants.TRUE);
	    	result.setResponseCode(Constants.SUCCESS_200);
	    	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
	    	result.push("recruitList", JsonUtil.list2jsonToArray(applyList));
		}
		
		return result;
	}
	
   /**
	 * 查询正在轮询的公告列表
	 * @param request
	 * @param response
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @author ddxj
     * @throws IllegalAccessException 
	 * 
	 */
	@RequestMapping(value="/query/recruit/notice/list.ddxj")
	public ResponseBase queryRecruitNoticeList(HttpServletRequest request,HttpServletResponse response,Integer type,Integer pageNum,Integer pageSize) throws IllegalAccessException{
		ResponseBase result=ResponseBase.getInitResponse();
		PageHelper.startPage(pageNum, pageSize);
		List<Notice> noticeList = null;
		if(!CmsUtils.isNullOrEmpty(type))
		{
			noticeList = recruitService.queryNoticeNow(type);
		}
		result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	result.push("noticeList", JsonUtil.list2jsonToArray(noticeList));
		return result;
	}
	
	/**
     * 添加招聘邀请
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/add/recruit/request.ddxj")
    public ResponseBase addRecruitRequest(HttpServletRequest request, HttpServletResponse response,int fromUserId,int toUserId,int recruitId) throws ClientException, IllegalAccessException 
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	Integer ur = userService.queryUserRequestById(fromUserId, toUserId, recruitId);
    	if(ur != null && ur > Constants.Number.ZERO_INT)
    	{
    		result.setResponse(Constants.TRUE);
    		result.setResponseCode(Constants.SUCCESS_200);
    		result.setResponseMsg(Constants.Prompt.USER_ALREADYINVITATION);
        	return result;
    	}
    	UserRequest req = new UserRequest();
    	req.setAcceptUserId(toUserId);
    	req.setSendUserId(fromUserId);
    	req.setRecruitId(recruitId);
    	req.setUpdateTime(new Date());
    	req.setCreateTime(new Date());
		//添加招聘邀请
		userService.addUserRequest(req);
		asycService.pushRecruitRequest(toUserId, recruitId);//邀请推送
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.ADD_SUCCESSFUL);
		log.info("#####################添加招聘邀请成功#####################");
    	return result;
    }
    
    /**
     * 查询用户发布的招聘数量
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value = "/query/recruit/user/count.ddxj")
    public ResponseBase queryRecruitUserCount(HttpServletRequest request, HttpServletResponse response,int userId) throws ClientException, IllegalAccessException 
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	int count = recruitService.queryRecruitByUserId(userId);
    	result.push("recruitCount", count);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	log.info("#####################查询用户发布的招聘数量成功#####################");
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
    @RequestMapping(value = "/update/recruit.ddxj")
    public ResponseBase updateRecruit(HttpServletRequest request, HttpServletResponse response,Integer recruitId,String recruitTitle,String recruitContent,int userId,
    		String startTime,String endTime,String recruitProvince,String recruitCity,String recruitArea,String recruitLong,String recruitLat,
    		String recruitAddress,int recruitPerson,String contractor,String coverImage,String balanceWay,String startPrice,String endPrice,
    		String stopTime,String banners,String categorys) throws ClientException, IllegalAccessException 
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	RealAuth realAuth = userService.queryUserRealAuth(userId);
    	if(!CmsUtils.isNullOrEmpty(realAuth))
    	{
    		if(realAuth.getRealStatus()==Constants.Number.ONE_INT)
    		{
    			result.setResponse(Constants.FALSE);
            	result.setResponseCode(Constants.SUCCESS_200);
            	result.setResponseMsg(Constants.Prompt.AUTHENTICATION_PLEASEWAIT);
            	return result;
    		}
    		else if(realAuth.getRealStatus()==Constants.Number.TWO_INT)
    		{
    			result.setResponse(Constants.FALSE);
            	result.setResponseCode(Constants.SUCCESS_200);
            	result.setResponseMsg(Constants.Prompt.AUTHENTICATIONFAILURE_PLEASEAGAIN);
            	return result;
    		}
    		else if(realAuth.getRealStatus()==Constants.Number.THREE_INT)
    		{
    			Recruit recruit = null;
    	    	if(recruitId != null && recruitId > Constants.Number.ZERO_INT)
    	    	{
    	    		recruit = recruitService.selectByPrimaryKey(recruitId);
    	    		recruit.setValidateStatus(Constants.Number.ONE_INT);
    	    		recruit.setValidateCause("");
    	    	}
    	    	else
    	    	{
    	    		recruit = new Recruit();
    	    		recruit.setUserId(userId);
    	    		recruit.setProjectNumber(String.valueOf(System.currentTimeMillis() + userId));
    	    		recruit.setCreateTime(new Date());
    	    	}
    	    	recruit.setRecruitTitle(recruitTitle);
    	    	Set<String> sensitiveWord = validateKeywords.validateKeywords(recruitContent);
    	    	if(!CmsUtils.isNullOrEmpty(sensitiveWord))
    	    	{
    	    		result.setResponse(Constants.FALSE);
    				result.setResponseCode(Constants.SUCCESS_200);
    				result.setResponseMsg(Constants.Prompt.TEXT_ILLEGALITY+ sensitiveWord);
    				return result;
    	    	}
    	    	recruit.setRecruitContent(recruitContent);
    	    	recruit.setStartTime(DateUtils.getDate(startTime, "yyyy-MM-dd"));
    	    	endTime = endTime+" 23:59:59";
    	    	recruit.setEndTime(DateUtils.getDate(endTime, "yyyy-MM-dd HH:mm:ss"));
    	    	recruit.setRecruitProvince(recruitProvince);
    	    	recruit.setRecruitCity(recruitCity);
    	    	recruit.setRecruitArea(recruitArea);
    	    	recruit.setRecruitLong(recruitLong);
    	    	recruit.setRecruitLat(recruitLat);
    	    	recruit.setRecruitAddress(recruitAddress);
    	    	recruit.setRecruitPerson(recruitPerson);
    	    	recruit.setContractor(contractor);
    	    	recruit.setCoverImage(coverImage);
    	    	recruit.setBalanceWay(balanceWay);
    	    	recruit.setStartPrice(new BigDecimal(startPrice));
    	    	recruit.setEndPrice(new BigDecimal(endPrice));
    	    	stopTime = stopTime+" 23:59:59";
    	    	recruit.setStopTime(DateUtils.getDate(stopTime, "yyyy-MM-dd HH:mm:ss"));
    	    	recruit.setBalanceType(Constants.Number.ONE_INT);
    	    	recruit.setUpdateTime(new Date());
    	    	
    	    	if(recruitId != null && recruitId > Constants.Number.ZERO_INT)
    	    	{
    	    		recruitService.updateRecruit(recruit);//修改招聘信息
    	    		
    	    		recruitService.deleteRecruitBanner(recruit.getId());//删除招聘图片
    	    		recruitService.deleteRecruitCategory(recruit.getId());//删除招聘分类
    	    	}
    	    	else
    	    	{
    	    		recruitService.addRecruit(recruit);//添加招聘信息
    	    	}
    	    	
    	    	if(!CmsUtils.isNullOrEmpty(banners))//招聘图片列表
    	    	{
    	    		String[] banner = null;
    				if(banners.indexOf("###") >= Constants.Number.REDUCE_ONE_INT)
    				{
    					banner = banners.split("###");
    				}
    				else
    				{
    					banner = new String[Constants.Number.ONE_INT];
    					banner[Constants.Number.ZERO_INT] = banners;
    				}
    				
    				for(String img : banner)
    				{
    					RecruitBanner ban = new RecruitBanner();
    					ban.setId(recruit.getId());
    					ban.setBannerUrl(img);
    					ban.setBannerType(Constants.Number.ONE_INT);
    					ban.setUpdateTime(new Date());
    					ban.setCreateTime(new Date());
    					recruitService.addRecruitBanner(ban);
    				}
    	    	}
    	    	
    	    	if(!CmsUtils.isNullOrEmpty(categorys))//招聘工种分类
    	    	{
    	    		String[] category = null;
    	    		if(categorys.indexOf(Constants.COMMA) >= Constants.Number.REDUCE_ONE_INT)
    	    		{
    	    			category = categorys.split(Constants.COMMA);
    	    		}
    	    		else
    	    		{
    	    			category = new String[Constants.Number.ONE_INT];
    	    			category[Constants.Number.ZERO_INT] = categorys;
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
    	    	asycService.addManagerMessage(Constants.Number.ONE_INT,"招聘ID："+recruit.getId()+"，标题："+recruit.getRecruitTitle(),"您有一条新的招聘审核信息，请及时处理。" , null);
    	    	result.push("recruitId", recruit.getId());
    	    	result.setResponse(Constants.TRUE);
    	    	result.setResponseCode(Constants.SUCCESS_200);
    	    	result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
    	    	log.info("#####################更新招聘信息成功#####################");
    	    	return result;
    		}	
    	}
    	else
    	{
    		result.setResponse(Constants.FALSE);
        	result.setResponseCode(Constants.SUCCESS_200);
        	result.setResponseMsg(Constants.Prompt.UNCERTIFIED_CANNOTPUBLISH);
        	return result;
    	}
    	return result;
    }
    
    /**
     * 根据招聘Id查询招聘详情
     * @param request
     * @param response
     * @param recruitId
     * @return
     */
    @RequestMapping(value="/query/recruitDetail.ddxj")
    public ResponseBase queryRecruitDetail(HttpServletRequest request, HttpServletResponse response,Integer recruitId){
    	ResponseBase result=ResponseBase.getInitResponse();
    	Recruit recruitDetail = recruitService.queryRecruitDetail(recruitId);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	result.push("recruitDetail", JsonUtil.bean2jsonObject(recruitDetail));
    	log.info("#####################查询招聘信息成功#####################");
    	return result;
    }
    
    /**
     * 解绑银行卡
     * @param request
     * @param response
     * @param recruitId
     * @return
     */
    @RequestMapping(value="/bank/card/untie.ddxj")
    public ResponseBase untieBankCard(HttpServletRequest request, HttpServletResponse response,Integer userId,String bankCard){
    	ResponseBase result=ResponseBase.getInitResponse();
    	int userBindingCard = userService.countUserBindingCard(userId);
//    	UserBank userBankInfo = userService.findUserBank(userId,bankCard);
//    	result.push("userBankInfo", JsonUtil.bean2jsonObject(userBankInfo));
    	if(userBindingCard>1)
    	{
    		String id=String.valueOf(userId);
    		userService.untieBankCard(id,bankCard);
    		result.setResponse(Constants.TRUE);
        	result.setResponseCode(Constants.SUCCESS_200);
        	result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
    		log.info("==================可以删除====================");
    	}
    	else
    	{
    		result.setResponse(Constants.TRUE);
        	result.setResponseCode(Constants.SUCCESS_200);
        	result.setResponseMsg(Constants.Prompt.DELETE_FAILURE);
        	log.info("==================不可以删除====================");
    	}
    	return result;
    }
 }
