package net.zn.ddxj.api;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.Category;
import net.zn.ddxj.entity.Complain;
import net.zn.ddxj.entity.RealAuth;
import net.zn.ddxj.entity.Recruit;
import net.zn.ddxj.entity.RecruitRecord;
import net.zn.ddxj.entity.User;
import net.zn.ddxj.entity.UserTransfer;
import net.zn.ddxj.service.AppMessageService;
import net.zn.ddxj.service.CategoryService;
import net.zn.ddxj.service.CircleService;
import net.zn.ddxj.service.PayService;
import net.zn.ddxj.service.RecruitCreditService;
import net.zn.ddxj.service.RecruitRecordService;
import net.zn.ddxj.service.RecruitService;
import net.zn.ddxj.service.SalaryRecordService;
import net.zn.ddxj.service.UserCollectionService;
import net.zn.ddxj.service.UserCommentService;
import net.zn.ddxj.service.UserService;
import net.zn.ddxj.service.UserTransferService;
import net.zn.ddxj.service.UserWithdrawService;
import net.zn.ddxj.tool.AsycService;
import net.zn.ddxj.tool.WechatService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.DateUtils;
import net.zn.ddxj.utils.RedisUtils;
import net.zn.ddxj.utils.json.JsonUtil;
import net.zn.ddxj.utils.wechat.WXException;
import net.zn.ddxj.utils.wechat.WechatUtils;
import net.zn.ddxj.vo.CmsRequestVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@RestController
@Slf4j
/**
 * 系统管理
* @ClassName: ManagerController   
* @author 上海众宁网络科技有限公司-何俊辉
* @date 2018年4月29日  
*
 */
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private RedisUtils redisUtils;
	@Autowired
	private AsycService asycService;
	@Autowired
	private UserTransferService userTransferService;//转账,结算
	@Autowired
	private WechatService wechatService;
	@Autowired
	private UserCollectionService userCollectionService;
	@Autowired
	private UserWithdrawService userWithdrawService;
	@Autowired
	private RecruitService recruitService;
	@Autowired
	private RecruitCreditService recruitCreditService;
	@Autowired
	private RecruitRecordService recruitRecordService;
	@Autowired
	private SalaryRecordService salaryRecordService;
	@Autowired
	private PayService payService;
	@Autowired
	private AppMessageService appMessageService;
	@Autowired
	private UserCommentService userCommentService;
	@Autowired
	private CircleService circleService;
	
	@RequestMapping(value = "/query/user/list.ddxj")
	public ResponseBase managerMenu(HttpServletRequest request, HttpServletResponse response,String realName,String phone,Integer role,String startTime,String endTime,String staffNum,Integer pageNum,Integer pageSize) throws IllegalAccessException 
	{
		ResponseBase result = ResponseBase.getInitResponse();
	    Map<String,Object> param = new HashMap<String,Object>();
	    param.put("realName", realName);
	    param.put("phone", phone);
	    param.put("role", role);
	    param.put("startTime", startTime);
	    param.put("endTime", endTime);
	    param.put("staffNum", staffNum);
	    PageHelper.startPage(pageNum, pageSize);
		List<User> managerUserList = userService.queryManagerUserList(param);
		PageInfo<User> pageInfo = new PageInfo<User>(managerUserList);
		result.push("userList", pageInfo);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("查询成功");
		return result;
	}
	@RequestMapping(value = "/query/user/details.ddxj")
	public ResponseBase managerMenu(HttpServletRequest request, HttpServletResponse response,Integer userId)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		User userDetails = userService.queryUserDetail(userId);
		result.push("userDetails", userDetails);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("查询成功");
		return result;
	}
	/**
	 * 查询工种
	 * 
	 * @param request
	 * @param response
	 * @param categoryType
	 * @return
	 * @throws IllegalAccessException 
	 */
	@RequestMapping(value = "/member/query/worker/type.ddxj")
	public ResponseBase queryWorkerTypeList(HttpServletRequest request, HttpServletResponse response,
			Integer categoryType) throws IllegalAccessException {
		ResponseBase result = ResponseBase.getInitResponse();
		List<Category> categoryList = categoryService.getCategoryByType(categoryType);
		result.push("categoryList", JsonUtil.list2jsonToArray(categoryList));
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.SUCCESS);
		return result;
	}
	/**
     * 更新用户信息
     *
     * @param request
     * @param response
	 * @throws ClientException 
     */
    @RequestMapping(value = "/update/user/info.ddxj")
    public ResponseBase updateUserInfo(HttpServletRequest request, HttpServletResponse response,Integer id,String realName,String birthDate,String phone,String sex,String standing,
    		Integer role,String registeredProvince,String registeredCity,String registeredArea,String registeredAddress,
    		String workProvince,String workCity,String workArea,String workAddress,String personDesc,String categorys) throws ClientException 
    {
    	ResponseBase result=ResponseBase.getInitResponse();
    	
		User user = userService.SelectUser(phone);
		if(CmsUtils.isNullOrEmpty(user))
		{
			result.push("user", "");
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("该用户不存在");
			return result;
		}
		user.setRealName(realName);
		user.setPhone(phone);
		user.setAge(CmsUtils.getAgeByBirth(DateUtils.getDate(birthDate, "yyyy-MM-dd")));
		user.setSex(sex);
		user.setBirthDate(DateUtils.getDate(birthDate, "yyyy-MM-dd"));
		user.setRole(role);
		user.setStanding(standing);
		user.setRegisteredProvince(registeredProvince);
		user.setRegisteredCity(registeredCity);
		user.setRegisteredArea(registeredArea);
		user.setRegisteredAddress(registeredAddress);
		user.setWorkProvince(workProvince);
		user.setWorkCity(workCity);
		user.setWorkArea(workArea);
		user.setWorkAddress(workAddress);
		user.setPersonDesc(personDesc);
		user.setUpdateTime(new Date());
		if(!CmsUtils.isNullOrEmpty(categorys))
		{
			userService.deleteUserCategory(id);//删除用户所有信息
			String[] category = null;
			if(categorys.indexOf(",") >= -1)
			{
				category = categorys.split(",");
			}
			else
			{
				category = new String[1];
				category[0] = categorys;
			}
			
			for(String categoryId : category)
			{
				Map<String, Object> date = new HashMap<String, Object>();
				date.put("userId", user.getId());
				date.put("categoryId", categoryId);
				date.put("createTime", new Date());
				date.put("updateTime", new Date());
				userService.insertUserCategory(date);
			}
		}
		userService.updateByPrimaryKeySelective(user);
		result.push("user", JsonUtil.bean2jsonObject(user));
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("更新成功");
    	return result;
    }
    @RequestMapping(value = "/forbidden/user.ddxj")
    public ResponseBase forbiddenUser(HttpServletRequest request, HttpServletResponse response,Integer userId,Integer type)
    {
    	ResponseBase result=ResponseBase.getInitResponse();
    	userService.forbiddenUser(userId,type);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("禁用成功");
    	return result;
    }
    
    /**
     * 查询实名认证列表
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException
     */
    @RequestMapping(value = "/user/real/auth/list.ddxj")
    public ResponseBase updateRecruitRecord(HttpServletRequest request, HttpServletResponse response,CmsRequestVo requestVo) 
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	PageHelper.startPage(requestVo.getPageNum(), requestVo.getPageSize());
    	List<RealAuth> realAuthList = userService.queryRealAuthList(requestVo);
    	PageInfo<RealAuth> pageInfo = new PageInfo<RealAuth>(realAuthList);
		result.push("realAuthList", pageInfo);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("查询实名认证列表成功");
		log.info("#####################查询实名认证列表成功#####################");
    	return result;
    }
    
    /**
     * 更新实名认证状态
     * 
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value="/user/realAuth/update/status.ddxj")
    public ResponseBase updateRealAuthStatus(HttpServletRequest request, HttpServletResponse response,Integer id,Integer status)
    { 
    	ResponseBase result=ResponseBase.getInitResponse();
    	RealAuth auth = userService.queryRealAuthById(id);
    	User users = userService.queryUserDetail(auth.getUserId());
    	userService.updateRealAuthStatus(id, status);
    	if(status == 3)//审核成功
    	{
        	JSONObject object = JSONObject.parseObject(auth.getValidateData());
        	
        	JSONObject json = object.getJSONObject("result");
        	
        	User user = new User();
        	user.setId(auth.getUserId());
        	user.setRealName(json.getString("realName"));
        	user.setBirthDate(json.getJSONObject("details").getDate("birth"));
        	Integer sex = json.getJSONObject("details").getIntValue("sex");
        	if(sex == 1)
        	{
        		user.setSex("M");
        	}
        	else if(sex == 0)
        	{
        		user.setSex("F");
        	}
        	//user.setRegisteredAddress(json.getJSONObject("details").getString("addr"));
        	user.setRegisteredProvince(json.getJSONObject("details").getString("province"));
        	//user.setRegisteredCity(json.getJSONObject("details").getString("city"));
        	//直辖市
        	if("市辖区".equals(json.getJSONObject("details").getString("city")))
        	{
        		user.setRegisteredCity(json.getJSONObject("details").getString("province"));
        	}
        	else
        	{
        		user.setRegisteredCity(json.getJSONObject("details").getString("city"));
        	}
        	//不是直辖市下的市区
        	if("市辖区".equals(json.getJSONObject("details").getString("area")))
        	{
        		user.setRegisteredArea(json.getJSONObject("details").getString("其它区"));
        	}
        	else
        	{
        		user.setRegisteredArea(json.getJSONObject("details").getString("area"));
        	}
        	//user.setRegisteredArea(json.getJSONObject("details").getString("area"));
        	user.setUpdateTime(new Date());
        	userService.updateByPrimaryKeySelective(user);//实名认证通过后更新用户信息
    	}
    	asycService.pushRealName(users.getId(), id);
    	if(!CmsUtils.isNullOrEmpty(users.getOpenid()))
		{
			//发送微信模板消息推送
			try {
				Map<String,Object> template = new HashMap<String,Object>();
				template.put("touser", users.getOpenid());
				template.put("template_id", "OIDokFhfa-mFj5avDTDb0vUHrEAS1fVkLwFKfcbX4wA");
				template.put("url", "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx183170965aafbd8a&redirect_uri=http://app.diandxj.com/worker-wechat/html/login/login.html&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect");
				template.put("topcolor", "#44b549");
				
				Map<String,Object> data = new HashMap<String,Object>();
				
				Map<String,String> data1 = new HashMap<String,String>();
				data1.put("value", "来自点点小匠的实名制认证通知");
				data.put("first", data1);
				
				Map<String,String> data2 = new HashMap<String,String>();
				data2.put("value", "实名制认证");
				data.put("keyword1", data2);
				
				Map<String,String> data3 = new HashMap<String,String>();
				if(status == 3)
				{
					data3.put("value", "认证通过");
				}
				else if(status == 2)
				{
					data3.put("value", "认证失败");
				}
				data.put("keyword2", data3);
				
				Map<String,String> remark = new HashMap<String,String>();
				remark.put("value", "感谢您的使用");
				data.put("remark", remark);
				
				template.put("data",data);
				
				WechatUtils.sendWechatTemplateMassage(wechatService.queryWechatToken(), JsonUtil.map2jsonToString(template));
			} catch (WXException e) {}
		}
    	
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("更新成功");
		return result;
    }
    
    /**
     * 删除实名认证信息
     * 
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value="/user/realAuth/delete.ddxj")
    public ResponseBase deleteRealAuthFlag(HttpServletRequest request, HttpServletResponse response,Integer realId)
    {
    	ResponseBase result=ResponseBase.getInitResponse();
    	userService.deleteRealAuth(realId);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("更新成功");
    	return result;
    }
    
    /**
     * 用户资金变动详情
     * 
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException
     */
    @RequestMapping(value="/user/transfer/detail.ddxj")
    public ResponseBase queryUserTransferDetail(HttpServletRequest request, HttpServletResponse response,Integer userId)
    { 
    	ResponseBase result=ResponseBase.getInitResponse();
    	
    	Map<String, Object> data = new HashMap<String, Object>();
    	
		Map<String, BigDecimal> maper =  userService.queryUserToUserMoneyLog(userId);
		if(maper != null)
		{
			data.put("zongjiSR", !CmsUtils.isNullOrEmpty(maper.get("zongjiSR")) ? String.valueOf(maper.get("zongjiSR")) : 0.00);
			data.put("todaySR", !CmsUtils.isNullOrEmpty(maper.get("todaySR")) ? String.valueOf(maper.get("todaySR")) : 0.00);
			data.put("weekSR", !CmsUtils.isNullOrEmpty(maper.get("weekSR")) ? String.valueOf(maper.get("weekSR")) : 0.00);
			data.put("monthSR", !CmsUtils.isNullOrEmpty(maper.get("monthSR")) ? String.valueOf(maper.get("monthSR")) : 0.00);
		}
		
    	//所有
    	BigDecimal zongjiZZ =  userService.totalMoneyByType(userId,1,1);
    	BigDecimal zongjiJS =  userService.totalMoneyByType(userId,2,1);
    	BigDecimal zongjiCZ =  userService.totalMoneyByType(userId,3,1);
    	BigDecimal zongjiTX =  userService.totalMoneyByType(userId,4,1);
    	BigDecimal zongjiFF =  userService.totalMoneyByType(userId,5,2);
    	
    	data.put("zongjiZZ", !CmsUtils.isNullOrEmpty(zongjiZZ) ? String.valueOf(zongjiZZ) : 0.00);
		data.put("zongjiJS", !CmsUtils.isNullOrEmpty(zongjiJS) ? String.valueOf(zongjiJS) : 0.00);
		data.put("zongjiCZ", !CmsUtils.isNullOrEmpty(zongjiCZ) ? String.valueOf(zongjiCZ) : 0.00);
		data.put("zongjiTX", !CmsUtils.isNullOrEmpty(zongjiTX) ? String.valueOf(zongjiTX) : 0.00);
		data.put("zongjiFF", !CmsUtils.isNullOrEmpty(zongjiFF) ? String.valueOf(zongjiFF) : 0.00);
		
		//今天
		BigDecimal todayZZ =  userService.sumMoneyByTypeAndToday(userId,1,1);
		BigDecimal todayJS =  userService.sumMoneyByTypeAndToday(userId,2,1);
		BigDecimal todayCZ =  userService.sumMoneyByTypeAndToday(userId,3,1);
		BigDecimal todayTX =  userService.sumMoneyByTypeAndToday(userId,4,1);
		BigDecimal todayFF =  userService.sumMoneyByTypeAndToday(userId,5,2);
		
		data.put("todayZZ", !CmsUtils.isNullOrEmpty(todayZZ) ? String.valueOf(todayZZ) : 0.00);
		data.put("todayJS", !CmsUtils.isNullOrEmpty(todayJS) ? String.valueOf(todayJS) : 0.00);
		data.put("todayCZ", !CmsUtils.isNullOrEmpty(todayCZ) ? String.valueOf(todayCZ) : 0.00);
		data.put("todayTX", !CmsUtils.isNullOrEmpty(todayTX) ? String.valueOf(todayTX) : 0.00);
		data.put("todayFF", !CmsUtils.isNullOrEmpty(todayFF) ? String.valueOf(todayFF) : 0.00);
		
		//本周
		BigDecimal weekZZ =  userService.sumMoneyByTypeAndWeek(userId,1,1);
		BigDecimal weekJS =  userService.sumMoneyByTypeAndWeek(userId,2,1);
		BigDecimal weekCZ =  userService.sumMoneyByTypeAndWeek(userId,3,1);
		BigDecimal weekTX =  userService.sumMoneyByTypeAndWeek(userId,4,1);
		BigDecimal weekFF =  userService.sumMoneyByTypeAndWeek(userId,5,2);
		
		data.put("weekZZ", !CmsUtils.isNullOrEmpty(weekZZ) ? String.valueOf(weekZZ) : 0.00);
		data.put("weekJS", !CmsUtils.isNullOrEmpty(weekJS) ? String.valueOf(weekJS) : 0.00);
		data.put("weekCZ", !CmsUtils.isNullOrEmpty(weekCZ) ? String.valueOf(weekCZ) : 0.00);
		data.put("weekTX", !CmsUtils.isNullOrEmpty(weekTX) ? String.valueOf(weekTX) : 0.00);
		data.put("weekFF", !CmsUtils.isNullOrEmpty(weekFF) ? String.valueOf(weekFF) : 0.00);
		
		//本月
		BigDecimal monthZZ =  userService.sumMoneyByTypeAndMonth(userId,1,1);
		BigDecimal monthJS =  userService.sumMoneyByTypeAndMonth(userId,2,1);
		BigDecimal monthCZ =  userService.sumMoneyByTypeAndMonth(userId,3,1);
		BigDecimal monthTX =  userService.sumMoneyByTypeAndMonth(userId,4,1);
		BigDecimal monthFF =  userService.sumMoneyByTypeAndMonth(userId,5,2);
		
		data.put("monthZZ", !CmsUtils.isNullOrEmpty(monthZZ) ? String.valueOf(monthZZ) : 0.00);
		data.put("monthJS", !CmsUtils.isNullOrEmpty(monthJS) ? String.valueOf(monthJS) : 0.00);
		data.put("monthCZ", !CmsUtils.isNullOrEmpty(monthCZ) ? String.valueOf(monthCZ) : 0.00);
		data.put("monthTX", !CmsUtils.isNullOrEmpty(monthTX) ? String.valueOf(monthTX) : 0.00);
		data.put("monthFF", !CmsUtils.isNullOrEmpty(monthFF) ? String.valueOf(monthFF) : 0.00);
		result.push("moneyData",data);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("查询用户资金变动成功");
		return result;
    }
    /**
     * 
     * @param request
     * @param response
     * @param requestVo
     * @return
     * @throws IllegalAccessException 
     */
    @RequestMapping(value="/manager/query/user/transfer/list.ddxj")
    public ResponseBase queryUserTransferDetails(HttpServletRequest request, HttpServletResponse response,Integer userId,Integer transferWay,Integer transferType,String startTime,String endTime,Integer pageNum,
    		Integer pageSize,Integer status) throws IllegalAccessException{
    	ResponseBase result=ResponseBase.getInitResponse();
    	Map<String,Object> param = new HashMap<String,Object>();
    	if(status == 1)//支出
    	{
    		param.put("fromUserId", userId);
    	}
    	else if(status == 2)//收入
    	{
    		param.put("toUserId", userId);
    	}
	    param.put("transferWay", transferWay);
	    param.put("transferType", transferType);
	    param.put("startTime", startTime);
	    param.put("endTime", endTime);
	    PageHelper.startPage(pageNum, pageSize);
	    List<UserTransfer> userTransferList = userTransferService.queryUserTransferDetails(param);
	    PageInfo<UserTransfer> pageInfo = new PageInfo<UserTransfer>(userTransferList);
	    result.push("userTransferList", pageInfo);
	    result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("查询用户资金变动成功");
		return result;
    	
    }
    
    /**
     * 投诉与建议
     * 
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException
     */
    @RequestMapping(value="/user/comPlain/list.ddxj")
    public ResponseBase queryComPlainList(HttpServletRequest request, HttpServletResponse response,CmsRequestVo requestVo)
    { 
    	ResponseBase result = ResponseBase.getInitResponse();
    	PageHelper.startPage(requestVo.getPageNum(), requestVo.getPageSize());
    	List<Complain> complainList = userService.queryComplainList(requestVo);
    	PageInfo<Complain> pageInfo = new PageInfo<Complain>(complainList);
    	if(!CmsUtils.isNullOrEmpty(complainList))
    	{
    		result.push("complainList", pageInfo);
    	    result.setResponse(Constants.TRUE);
        	result.setResponseCode(Constants.SUCCESS_200);
        	result.setResponseMsg("已查到符合条件的记录");
    	}
    	else
    	{
    		result.push("complainList", pageInfo);
    	    result.setResponse(Constants.TRUE);
        	result.setResponseCode(Constants.SUCCESS_200);
        	result.setResponseMsg("未查到符合条件的记录");
    	}
    	return result;
    }
    
    /**
     * 处理投诉与建议
     * 
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException
     */
    @RequestMapping(value="/user/comPlain/handle.ddxj")
    public ResponseBase handleComPlain(HttpServletRequest request, HttpServletResponse response,int id)
    { 
    	ResponseBase result=ResponseBase.getInitResponse();
    	Complain com = new Complain();
    	com.setId(id);
    	com.setStatus(2);
    	com.setUpdateTime(new Date());
    	userService.handleComPlain(com);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("处理成功");
    	return result;
    }
    
    /**
     * 删除投诉与建议
     * 
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException
     */
    @RequestMapping(value="/user/comPlain/delete.ddxj")
    public ResponseBase deleteComPlain(HttpServletRequest request, HttpServletResponse response,int id)
    { 
    	ResponseBase result=ResponseBase.getInitResponse();
    	userService.deleteComPlain(id);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("删除成功");
    	return result;
    }
    /**
     * 注销用户
     * @param request
     * @param response
     * @param userId
     * @return
     */
    @RequestMapping(value="/user/delete/userInfo.ddxj")
    public ResponseBase deleteUser(HttpServletRequest request, HttpServletResponse response,Integer userId)
    { 
    	ResponseBase result=ResponseBase.getInitResponse();
    	User userDetail = userService.queryUserDetail(userId);
    	Integer role=userDetail.getRole();
    	if(!CmsUtils.isNullOrEmpty(role)&& role==1)//工人
    	{
    		RecruitRecord workerConduct = recruitRecordService.queryWorkerConduct(userId);//查询正在进行的活动
    		if(CmsUtils.isNullOrEmpty(workerConduct))
    		{
    			redisUtils.remove(Constants.LOGIN_USER_INFO + userDetail.getPhone());
    			userService.delUserInfo(userId);//删除用户信息
        		userService.delUserPasswordRecord(userId);//删除用户密码
        		userService.delUserBankRecord(userId);//删除用户绑定银行卡
        		userService.delUserAuthRecord(userId);//删除用户实名认证
        		userService.deleteUserCategory(userId);//删除用户工种
        		userService.delApplyForemanRecord(userId);//删除申请工头记录
        		userService.delComplainRecord(userId);//删除投诉与建议
        		userCollectionService.delUserCollectionRecord(userId);//删除用户工作收藏
        		userService.delUserRequestRecord(userId);//删除用户职位邀请
        		userCommentService.delUserCommentLabel(userId);//删除活动的评论标签
        		userCommentService.delUserCommentRecord(userId);//删除活动的评论
        		userTransferService.delUserTransferRecord(userId);//删除用户转账
        		userWithdrawService.delUserWithdrawRecord(userId);//删除用户提现
        		recruitRecordService.delRecruitRecord(userId);//删除活动的记录
        		salaryRecordService.delSalaryRecord(userId);//删除发放记录
        		payService.delPayMentRecord(userId);//删除用户充值记录
        		recruitCreditService.delCreditRepayMentRecord(userId);//删除授信发放记录
        		appMessageService.delAppMessageRecord(userId);//删除app消息
        		circleService.delUserCircleLaud(userId);//删除圈子点赞
        		circleService.delUserCircleComment(userId);//删除圈子评论
        		circleService.delUserCircleImage(userId);//删除圈子图片
        		circleService.delUserCircle(userId);//删除圈子
        		result.setResponse(Constants.TRUE);
            	result.setResponseCode(Constants.SUCCESS_200);
            	result.setResponseMsg("删除成功");
            	return result;
    		}
    		else
    		{
    	    	result.setResponse(Constants.FALSE);
    	    	result.setResponseCode(Constants.SUCCESS_200);
    	    	result.setResponseMsg("该用户在职，删除失败！");
    	    	return result;
    		}
    		
    	}
    	else if(!CmsUtils.isNullOrEmpty(role)&& role==2)//工头
    	{
    		List<Recruit> recruitUnderway = recruitService.selectRecruitUnderway(userId);//活动正在进行也不能删除
    		if(CmsUtils.isNullOrEmpty(recruitUnderway))
    		{
    			redisUtils.remove(Constants.LOGIN_USER_INFO + userDetail.getPhone());
    			userService.delUserInfo(userId);//删除用户信息
        		userService.delUserPasswordRecord(userId);//删除用户密码
        		userService.delUserBankRecord(userId);//删除用户绑定银行卡
        		userService.delUserAuthRecord(userId);//删除用户实名认证
        		userService.deleteUserCategory(userId);//删除用户工种
        		userService.delComplainRecord(userId);//删除投诉与建议
        		userCollectionService.delUserCollectionRecord(userId);//删除用户工作收藏
        		userService.delUserRequestRecord(userId);//删除用户职位邀请
        		userCommentService.delUserCommentLabel(userId);//删除活动的评论标签
        		userCommentService.delUserCommentRecord(userId);//删除活动的评论
        		userTransferService.delUserTransferRecord(userId);//删除用户转账
        		userWithdrawService.delUserWithdrawRecord(userId);//删除用户提现
        		recruitService.delRecruitBannerRecord(userId);//删除活动的广告
        		recruitService.delRecruitCategoryRecord(userId);//删除活动的职位
        		recruitService.delUserRecruitRecord(userId);//删除活动的招聘
        		recruitRecordService.delRecruitRecord(userId);//删除活动的记录
        		salaryRecordService.delSalaryRecord(userId);//删除发放记录
        		recruitCreditService.delCreditRepayMentRecord(userId);//删除授信还款记录
        		payService.delPayMentRecord(userId);//删除用户充值记录
        		recruitRecordService.delCreditContactsImageRecord(userId);//删除授信图片
        		recruitRecordService.delCreditUrgentContactRecord(userId);//删除授信联系人
        		recruitCreditService.delRecruitCreditRecord(userId);//删除活动授信
        		appMessageService.delAppMessageRecord(userId);//删除app消息   
        		result.setResponse(Constants.TRUE);
            	result.setResponseCode(Constants.SUCCESS_200);
            	result.setResponseMsg("删除成功");
            	return result;
    		}
    		else
    		{
    			result.setResponse(Constants.FALSE);
    	    	result.setResponseCode(Constants.SUCCESS_200);
    	    	result.setResponseMsg("删除失败，有在进行的活动！");
    	    	return result;
    		}
    				
    	}
    	else if(!CmsUtils.isNullOrEmpty(role)&& role==0)
    	{
    		userService.delUserInfo(userId);//删除用户信息
    		result.setResponse(Constants.TRUE);
        	result.setResponseCode(Constants.SUCCESS_200);
        	result.setResponseMsg("删除成功");
        	return result;
    	}
    	else if(CmsUtils.isNullOrEmpty(role))
    	{
    		userService.delUserInfo(userId);//删除用户信息
    		result.setResponse(Constants.TRUE);
        	result.setResponseCode(Constants.SUCCESS_200);
        	result.setResponseMsg("删除成功");
        	return result;
    	}
    	
    	return result;
    }
    
    @RequestMapping("/user/promotion.ddxj")
    public ResponseBase queryRegisUser(HttpServletRequest request,HttpServletResponse response,Integer pageNum,Integer pageSize,String realName,String phone,String staffNum,String startTime,String endTime){
    	ResponseBase result=ResponseBase.getInitResponse();
    	PageHelper.startPage(pageNum, pageSize);
    	List<User> list = userService.queryUserByStaffNum(realName,phone,staffNum,startTime,endTime);
    	PageInfo<User> pageInfo = new PageInfo<User>(list);
    	result.push("userList", pageInfo);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("查询成功");
    	return result;
    }
    
}
