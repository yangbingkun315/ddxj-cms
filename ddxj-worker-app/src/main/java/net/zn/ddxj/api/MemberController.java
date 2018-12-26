package net.zn.ddxj.api;

import io.swagger.client.model.Nickname;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.AdvertBanner;
import net.zn.ddxj.entity.ApplyForeman;
import net.zn.ddxj.entity.Category;
import net.zn.ddxj.entity.EasemobToken;
import net.zn.ddxj.entity.EasemobUser;
import net.zn.ddxj.entity.Information;
import net.zn.ddxj.entity.InformationRecordIp;
import net.zn.ddxj.entity.InformationType;
import net.zn.ddxj.entity.InviteRecord;
import net.zn.ddxj.entity.InviteSetting;
import net.zn.ddxj.entity.MessageCenter;
import net.zn.ddxj.entity.RealAuth;
import net.zn.ddxj.entity.Recruit;
import net.zn.ddxj.entity.RecruitCredit;
import net.zn.ddxj.entity.RecruitRecord;
import net.zn.ddxj.entity.SalaryRecord;
import net.zn.ddxj.entity.ScreenAdvert;
import net.zn.ddxj.entity.User;
import net.zn.ddxj.entity.UserBank;
import net.zn.ddxj.entity.UserCollection;
import net.zn.ddxj.entity.UserPassword;
import net.zn.ddxj.entity.UserTransfer;
import net.zn.ddxj.entity.Version;
import net.zn.ddxj.server.example.api.impl.EasemobIMUsers;
import net.zn.ddxj.server.example.comm.TokenUtil;
import net.zn.ddxj.service.AdvertBannerService;
import net.zn.ddxj.service.CategoryService;
import net.zn.ddxj.service.InformationService;
import net.zn.ddxj.service.InviteRecordService;
import net.zn.ddxj.service.InviteSettingService;
import net.zn.ddxj.service.RecruitCreditService;
import net.zn.ddxj.service.RecruitRecordService;
import net.zn.ddxj.service.RecruitService;
import net.zn.ddxj.service.SalaryRecordService;
import net.zn.ddxj.service.ScreenAdvertService;
import net.zn.ddxj.service.UserCenterService;
import net.zn.ddxj.service.UserCollectionService;
import net.zn.ddxj.service.UserService;
import net.zn.ddxj.service.UserTransferService;
import net.zn.ddxj.service.UserWithdrawService;
import net.zn.ddxj.service.ValidateKeywordsService;
import net.zn.ddxj.tool.AsycService;
import net.zn.ddxj.tool.MessageService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.DateUtils;
import net.zn.ddxj.utils.OrderNumUtils;
import net.zn.ddxj.utils.PropertiesUtils;
import net.zn.ddxj.utils.QiNiuUploadManager;
import net.zn.ddxj.utils.RandomCode;
import net.zn.ddxj.utils.RedisUtils;
import net.zn.ddxj.utils.RequestUtils;
import net.zn.ddxj.utils.aliyun.SmsUtil;
import net.zn.ddxj.utils.aliyun.VmsUtil;
import net.zn.ddxj.utils.json.JsonUtil;
import net.zn.ddxj.utils.wechat.WXException;
import net.zn.ddxj.utils.wechat.WechatUtils;
import net.zn.ddxj.vo.RequestVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiniu.common.QiniuException;
import com.qiniu.util.Auth;

/**
 * 核心Controller
* @ClassName: MemberController
* @author 上海众宁网络科技有限公司-何俊辉
* @date 2018年7月8日
*
 */
@RestController
@Slf4j
public class MemberController{

	@Autowired
	private UserService userService;
	@Autowired
	public RedisUtils redisUtils;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private UserCollectionService userCollectionService;
	@Autowired
	private UserTransferService userTransferService;//转账,结算
	@Autowired
	private AdvertBannerService adverBannerService;
	@Autowired
	private InformationService informationService;
	@Autowired
	private RecruitService recruitService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private UserCollectionService collectionService;
	@Autowired
	private RecruitRecordService recruitRecordService;
	@Autowired
	private SalaryRecordService salaryRecordService;
	@Autowired
	private ValidateKeywordsService validateKeywordsService;
	@Autowired
	private RecruitCreditService recruitCreditService;
	@Autowired
	private InviteRecordService inviteRecordService;
	@Autowired
	private InviteSettingService inviteSettingService;
	@Autowired
	private UserCenterService userCenterService;
	@Autowired
	private AsycService asycService;
	@Autowired
	private ScreenAdvertService screenAdvertService;
	@Autowired
	private UserWithdrawService userWithdrawService;
	/**
	 * 登陆
	 *
	 * @param request
	 * @param response
	 * @param loginType
	 * @param phone
	 * @param identCode
	 * @param password
	 * @author ddxj-YBK
	 * @return
	 */
	@RequestMapping(value = "/member/login.ddxj")
	public ResponseBase login(HttpServletRequest request, HttpServletResponse response, Integer loginType, String phone,String appUserToken,String identCode, String password,String loginChannel,Integer registerChannel,String wechatInfo) {
		ResponseBase result = ResponseBase.getInitResponse();
		if(CmsUtils.isNullOrEmpty(phone) && Constants.Number.THREE_INT != loginType)//手机号码不能为空
		{
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.PHONE_NULL);
			return result;
		}
		if (Constants.Number.TWO_INT == loginType) // 密码登录
		{
			//创建环信账号
			insertEasemobIMUsers(phone);
			User user = userService.SelectUser(phone);
			if (CmsUtils.isNullOrEmpty(user)) //如果用户为空
			{
				result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.USER_NOT_REGISTER_LOGIN);
				return result;
			}
			UserPassword userPassword = userService.selectPasswordByUserId(user.getId());
			String oldPassword = null;
			if(!CmsUtils.isNullOrEmpty(userPassword))
			{
				oldPassword = userPassword.getLoginPassword();
			}
			if (CmsUtils.isNullOrEmpty(oldPassword)) //如果数据库密码为空
			{
				result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.USER_NOT_PASSWORD_LOGIN);
				return result;
			}
			if(!password.equals(oldPassword))//如果密码不相等
			{
				result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.PASSWORD_FAILURE);
				return result;
			}

			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.SUCCESS);
			result.push("loginType",Constants.Number.ONE_INT);
			result.push("user", JsonUtil.bean2jsonObject(user));
			redisUtils.set(Constants.LOGIN_USER_INFO + phone, JsonUtil.bean2jsonToString(user), Constants.Number.MONTH_SECOND);// 验证码1个月有效时间
			asycService.updateUserInfoAsync(phone,CmsUtils.getIpAddr(request),loginChannel);//线程启动-更新用户登录信息
		}
		else if (Constants.Number.ONE_INT == loginType) // 验证码登录
		{
			String idenCode = (String) redisUtils.get(Constants.LOGIN_IDEN_CODE + phone);
			if(CmsUtils.isNullOrEmpty(identCode))//输入的验证码为空
			{
				result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.IDENCODE_NULL);
				return result;
			}
			if (CmsUtils.isNullOrEmpty(idenCode))//缓存中验证码不存在
			{
				result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.IDENCODE_INVALID);
				return result;
			}
			if (!identCode.equals(idenCode))//验证码不正确
			{
				result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.IDENCODE_FAILURE);
				return result;
			}
			//创建环信账号
			insertEasemobIMUsers(phone);
			User user = userService.SelectUser(phone);
			if (!CmsUtils.isNullOrEmpty(user))
			{
				result = userLogin(user,result,CmsUtils.getIpAddr(request),loginChannel);//登陆
			}
			else
			{
				result = addLoginInfo(result,user,phone,appUserToken,loginChannel,CmsUtils.getIpAddr(request));//注册
			}
		}
		else if(Constants.Number.THREE_INT == loginType)//微信自动登录
		{
			if(CmsUtils.isNullOrEmpty(wechatInfo))//微信用户授权信息为空
			{
				result.push("loginType",Constants.Number.TWO_INT);
				result.setResponse(Constants.TRUE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.SUCCESS);
				return result;
			}
			JSONObject wechatInfoJson = (JSONObject)JSONObject.parse(wechatInfo);
			User user = userService.queryUserByunionid(wechatInfoJson.getString("unionid"));//根据unionid获取用户信息
			if(!CmsUtils.isNullOrEmpty(user))
			{
				if(!CmsUtils.isNullOrEmpty(appUserToken))
				{
					user.setAppUserToken(appUserToken);
					userService.updateByPrimaryKeySelective(user);
				}
			}
			if(CmsUtils.isNullOrEmpty(user) || (!CmsUtils.isNullOrEmpty(user.getRole()) && user.getRole() == Constants.Number.ZERO_INT))
			{
				result.push("loginType",Constants.Number.TWO_INT);
				result.setResponse(Constants.TRUE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.SUCCESS);
				return result;
			}
			result.push("user", JsonUtil.bean2jsonObject(user));
			redisUtils.set(Constants.LOGIN_USER_INFO + phone, JsonUtil.bean2jsonToString(user), Constants.Number.MONTH_SECOND);// 验证码1个月有效时间
			asycService.updateUserInfoAsync(phone,CmsUtils.getIpAddr(request),loginChannel);//线程启动-更新用户登录信息
			result.push("loginType",Constants.Number.ONE_INT);
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.LOGIN_SUCCESSFUL);
		}

		changeUserToken(loginChannel,phone,appUserToken,wechatInfo,loginType);//改变用户TOKEN、微信用户信息
		return result;
	}

	/**
	 * 发送验证码
	 *
	 * @param request
	 * @param response
	 * @author dongbisheng
	 * @throws ClientException
	 * @throws IOException
	 */
	@RequestMapping(value = "/member/query/idencode.ddxj")
	public ResponseBase queryIdenCode(HttpServletRequest request, HttpServletResponse response, String phone ,Integer type)
			throws ClientException, IOException {
		ResponseBase result = ResponseBase.getInitResponse();
		User user=userService.SelectUser(phone);
        String timeIdenCode = String.valueOf(redisUtils.get("idenCode:"+phone+"_"+type));
		if(!CmsUtils.isNullOrEmpty(timeIdenCode))
		{
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.OPERATION_FREQUENTLY);
			return result;
		}
		if(type==Constants.Number.ONE_INT)
		{
			String code = SmsUtil.getSmsCode(phone,  type,!CmsUtils.isNullOrEmpty(user) ? Constants.Number.ONE_INT : Constants.Number.TWO_INT);
			redisUtils.set(Constants.LOGIN_IDEN_CODE + phone, code, Constants.Number.THIRTY_SECOND);// 验证码30分钟有效时间
		}
		else if(type==Constants.Number.TWO_INT)
		{//2.修改密码
			if(CmsUtils.isNullOrEmpty(user))
			{
				result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.NOT_USER);
				return result;
			}
			String code = SmsUtil.getSmsCode(phone,  type,!CmsUtils.isNullOrEmpty(user) ? Constants.Number.ONE_INT : Constants.Number.TWO_INT);
			redisUtils.set(Constants.CHANGE_PASS_CODE + phone, code, Constants.Number.THIRTY_SECOND);// 验证码30分钟有效时间
		}
		else if(type==Constants.Number.THREE_INT)
		{//3.修改支付密码
			String code = SmsUtil.getSmsCode(phone,  type,!CmsUtils.isNullOrEmpty(user) ? Constants.Number.ONE_INT : Constants.Number.TWO_INT);
			redisUtils.set(Constants.CHANGE_PAY_CODE + phone, code, Constants.Number.THIRTY_SECOND);// 验证码30分钟有效时间
		}
		else if(type==Constants.Number.FOUR_INT)
		{//4.绑定银行卡
			String code = SmsUtil.getSmsCode(phone,  type,!CmsUtils.isNullOrEmpty(user) ? Constants.Number.ONE_INT : Constants.Number.TWO_INT);
			redisUtils.set(Constants.BOND_BANK_CARD_CODE + phone, code, Constants.Number.THIRTY_SECOND);// 验证码30分钟有效时间
		}
		else if(type==Constants.Number.FIVE_INT)
		{//5.授信验证码
			String code = SmsUtil.getSmsCode(phone,  type,!CmsUtils.isNullOrEmpty(user) ? Constants.Number.ONE_INT : Constants.Number.TWO_INT);
			redisUtils.set(Constants.CREDIT_VERIFY_CODE + phone, code, Constants.Number.THIRTY_SECOND);// 验证码30分钟有效时间
		}
		else if(type==Constants.Number.SIX_INT)
		{//6.更改手机号码
			if(!CmsUtils.isNullOrEmpty(user)){
				result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.PHONE_EXISTENCE);
				return result;
			}
			String code = SmsUtil.getSmsCode(phone,  type,!CmsUtils.isNullOrEmpty(user) ? Constants.Number.ONE_INT : Constants.Number.TWO_INT);
			redisUtils.set(Constants.CHANGE_PHONE_CODE + phone, code, Constants.Number.THIRTY_SECOND);// 验证码30分钟有效时间
		}
		else if(type==Constants.Number.SEVEN_INT)//忘记密码
		{
			if(CmsUtils.isNullOrEmpty(user))
			{
				result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.NOT_USER);
				return result;
			}
			String code = SmsUtil.getSmsCode(phone,  type,!CmsUtils.isNullOrEmpty(user) ? Constants.Number.ONE_INT : Constants.Number.TWO_INT);
			redisUtils.set(Constants.FORGET_PASSWORD + phone, code, Constants.Number.THIRTY_SECOND);// 验证码30分钟有效时间
		}
		else if(type==Constants.Number.NINE_INT)//网站后台发送消息
		{
			String code = SmsUtil.getSmsCode(phone,  type,!CmsUtils.isNullOrEmpty(user) ? Constants.Number.ONE_INT : Constants.Number.TWO_INT);
			redisUtils.set(Constants.WEB_SITE_CODE + phone, code, Constants.Number.THIRTY_SECOND);// 验证码30分钟有效时间
		}
		redisUtils.set("idenCode:"+phone+"_"+type, true, Constants.Number.SIXTY_SECOND);
		log.info("#####################短信验证码发送成功#####################");
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.SEND_SUCCESSFUL);
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
		result.push("categoryList", categoryList);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.SUCCESS);
		return result;
	}

	/**
	 * 获取七牛token
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/member/query/qiniu/token.ddxj")
	public ResponseBase queryQiNiuToken(HttpServletRequest request, HttpServletResponse response) {
		ResponseBase result = ResponseBase.getInitResponse();
		Auth auth = Auth.create(PropertiesUtils.getPropertiesByName("access_key"), PropertiesUtils.getPropertiesByName("secret_key"));
		result.push("token", auth.uploadToken(PropertiesUtils.getPropertiesByName("bucket_name")));
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.SUCCESS);
		return result;
	}

	/**
	 * 更改用户密码
	 *
	 * @param request
	 * @param response
	 * @param phone
	 * @param password
	 * @author ddxj-YBK
	 * @return
	 */
	@RequestMapping(value = "/member/update/login/password.ddxj")
	public ResponseBase updateUserPassword(HttpServletRequest request, HttpServletResponse response,  int userId, String newPassword  )
	{
    	ResponseBase result=ResponseBase.getInitResponse();
    	String oldPassword = request.getParameter("oldPassword");
    	UserPassword password = userService.selectPasswordByUserId(userId);
    	if(!CmsUtils.isNullOrEmpty(password))
    	{
    		if(!CmsUtils.isNullOrEmpty(oldPassword))
        	{
        		//判断原登录密码是否匹配
        		if(!CmsUtils.isNullOrEmpty(password.getLoginPassword()))
        		{
        			if(oldPassword.equals(password.getLoginPassword()))
        			{
        				//修改登录密码
        				password.setLoginPassword(newPassword);
        				userService.updateLoginPassword(password);
        				result.setResponse(Constants.TRUE);
                		result.setResponseCode(Constants.SUCCESS_200);
                		result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
        			}
        			else
        			{
            			result.setResponse(Constants.FALSE);
                		result.setResponseCode(Constants.SUCCESS_200);
                		result.setResponseMsg(Constants.Prompt.OLD_PASSWORD_FAILURE);
        			}
        		}else
        		{
        			result.setResponse(Constants.FALSE);
            		result.setResponseCode(Constants.SUCCESS_200);
            		result.setResponseMsg(Constants.Prompt.NOT_SETTING_LOGIN_PASSWORD);
        		}
        	}
    		else
        	{
        		//验证码修改登录密码
        		if(!CmsUtils.isNullOrEmpty(password.getLoginPassword()))
        		{
        			password.setLoginPassword(newPassword);
        			userService.updateLoginPassword(password);
    				result.setResponse(Constants.TRUE);
            		result.setResponseCode(Constants.SUCCESS_200);
            		result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
        		}
        		else
        		{
        			//无登录密码，设置登录密码
        			password.setLoginPassword(newPassword);
        			userService.updateLoginPassword(password);
        			result.setResponse(Constants.TRUE);
            		result.setResponseCode(Constants.SUCCESS_200);
            		result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
        		}
        	}
    	}
    	else
    	{
    		//未设置密码
    		UserPassword userPassword = new UserPassword();
    		userPassword.setUserId(userId);
    		userPassword.setLoginPassword(newPassword);
    		userPassword.setCreateTime(new Date());
    		userPassword.setUpdateTime(new Date());
    		userService.insertUserPassword(userPassword);
			result.setResponse(Constants.TRUE);
    		result.setResponseCode(Constants.SUCCESS_200);
    		result.setResponseMsg(Constants.Prompt.ADD_SUCCESSFUL);
    	}
    	return result;
    }

	/**
	 * 忘记用户密码
	 *
	 * @param request
	 * @param response
	 * @param phone
	 * @param password
	 * @author ddxj-YBK
	 * @return
	 */
	@RequestMapping(value = "/member/forget/password/update.ddxj")
	public ResponseBase updateForgetPassword(HttpServletRequest request, HttpServletResponse response,String phone, String newPassword)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		if(CmsUtils.isNullOrEmpty(newPassword))
		{
			return null;
		}
		User user  = userService.SelectUser(phone);
		if(CmsUtils.isNullOrEmpty(user))
		{
			result.setResponse(Constants.FALSE);
	        result.setResponseCode(Constants.SUCCESS_200);
	        result.setResponseMsg(Constants.Prompt.NOT_USER);
	        return result;
		}
		else
		{

			if(!CmsUtils.isNullOrEmpty(user.getRole()))
			{
				UserPassword password = userService.selectPasswordByUserId(user.getId());
				if(!CmsUtils.isNullOrEmpty(password))
				{
					//修改登陆密码
		            password.setLoginPassword(newPassword);
		            password.setUpdateTime(new Date());
		            userService.updateLoginPassword(password);
				}
				else
				{
	            	userService.addUserPasswordByPhone(user.getId(), newPassword);
				}
	        	result.setResponse(Constants.TRUE);
	            result.setResponseCode(Constants.SUCCESS_200);
	            result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
			}
			else
			{
				result.setResponse(Constants.FALSE);
	            result.setResponseCode(Constants.SUCCESS_200);
	            result.setResponseMsg(Constants.Prompt.USER_INFO_NOT_NULL);
			}

		}
		return result;
	}

	/**
	 * 用户信息查询用户详情、查询是否关注过该用户
	 * @param request
	 * @param response
	 * @param fromUserId
	 * @param toUserId
	 * @return
	 */
	@RequestMapping(value = "/member/query/user/detail.ddxj")
	public ResponseBase queryFollowByUserId(HttpServletRequest request, HttpServletResponse response,
			Integer fromUserId, Integer toUserId) {
		ResponseBase result = ResponseBase.getInitResponse();
		User user = userService.selectByPrimaryKey(toUserId);
		String newString = OrderNumUtils.getNewString(user.getPersonDesc());
		user.setPersonDesc(newString);
		int count = userCollectionService.selectByUserIdAndToUserId(fromUserId, toUserId);
		if(!CmsUtils.isNullOrEmpty(user)){
			if(count == Constants.Number.ZERO_INT){
				//未关注该用户
				result.push("user", JsonUtil.bean2jsonObject(user));
				result.push("followStatus", Constants.Number.TWO_INT);
				result.setResponse(Constants.TRUE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.SUCCESS);
			}
			if(count == Constants.Number.ONE_INT){
				//已经关注该用户
				result.push("user", JsonUtil.bean2jsonObject(user));
				result.push("followStatus", Constants.Number.ONE_INT);
				result.setResponse(Constants.TRUE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.SUCCESS);
			}
		}else{
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.NOT_USER);
		}
		return result;
	}

    /**
     * 添加用户信息
     *
     * @param request
     * @param response
     * @author fancunxin
	 * @throws ClientException
     */
    @RequestMapping(value = "/member/add/user/info.ddxj")
    public ResponseBase addUserInfo(HttpServletRequest request, HttpServletResponse response,String realName,Integer age,String phone,String sex,String userPortrait,Integer role,String openId,
    		String wxName,String standing,String registeredProvince,String registeredCity,String registeredArea,String registeredAddress,String workProvince,String workCity,String workArea,
    		Integer registerChannel,String workAddress,String personDesc,String categorys,String birthDate,Integer contractType,String staffNum) throws ClientException
    {
    	ResponseBase result=ResponseBase.getInitResponse();
    	User user = userService.SelectUser(phone);
		Integer userId=user.getId();
		if(CmsUtils.isNullOrEmpty(user))
		{
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.ADD_SUCCESSFUL);
			return result;
		}
		else
		{
			if(!CmsUtils.isNullOrEmpty(birthDate))
			{
				user.setBirthDate(DateUtils.getDate(birthDate, Constants.YYYY_MM_DD));
			}
			user.setRealName(realName);
			user.setPhone(phone);
			user.setAge(age);
			user.setSex(sex);
			user.setRole(role);
			if(Constants.Number.ONE_INT == role)
			{
				user.setUserPortrait(!CmsUtils.isNullOrEmpty(userPortrait) ? userPortrait : Constants.DEFAULT_USER_HEAD);
			}
			if(Constants.Number.TWO_INT == role)
			{
				user.setUserPortrait(!CmsUtils.isNullOrEmpty(userPortrait) ? userPortrait : Constants.DEFAULT_FOREMAN_HEAD);
			}
			if(!CmsUtils.isNullOrEmpty(openId))
			{
				user.setOpenid(openId);
			}
			user.setWxName(wxName);
			user.setStanding(standing);
			user.setRegisteredProvince(registeredProvince);
			user.setRegisteredCity(registeredCity);
			user.setRegisteredArea(registeredArea);
			user.setRegisteredAddress(registeredAddress);
			user.setWorkProvince(workProvince);
			user.setWorkCity(workCity);
			user.setWorkArea(workArea);
			user.setRegisterChannel(registerChannel);
			user.setWorkAddress(workAddress);
			user.setPersonDesc(personDesc);
			user.setContractType(contractType);
			user.setStaffNum(staffNum);
			user.setUpdateTime(new Date());
			userService.deleteUserCategory(userId);//删除用户所有信息
			if(!CmsUtils.isNullOrEmpty(categorys))
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

				for(String id : category)
				{
					Map<String, Object> date = new HashMap<String, Object>();
					date.put("userId", user.getId());
					date.put("categoryId", id);
					date.put("createTime", new Date());
					date.put("updateTime", new Date());
					userService.insertUserCategory(date);
				}
			}
		}
		userService.updateByPrimaryKeySelective(user);
		//给新用户加注册奖励金
		asycService.addUserBonus(user.getId(),user.getOpenid(),user.getPhone(),user.getRegisterChannel());
		result.push("user", JsonUtil.bean2jsonObject(userService.SelectUser(phone)));

		//同步修改环信推送昵称
		if(!CmsUtils.isNullOrEmpty(user.getUserName()))
		{
			updateEasemobNickName(user.getUserName(), user.getId());
		}
		else
		{
			updateEasemobNickName(realName, user.getId());
		}

		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.ADD_SUCCESSFUL);
		redisUtils.set(Constants.LOGIN_USER_INFO + phone, JsonUtil.bean2jsonToString(user), Constants.Number.MONTH_SECOND);// 验证码1个月有效时间
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
    public ResponseBase updateUserInfo(HttpServletRequest request, HttpServletResponse response,Integer userId) throws ClientException
    {
    	ResponseBase result=ResponseBase.getInitResponse();
		User user = userService.selectByPrimaryKey(userId);
		//判断该用户是否实名认证过
		RealAuth auth = userService.queryUserRealAuth(userId);
		String realName = request.getParameter("realName");
		String birthDate = request.getParameter("birthDate");
		String phone = request.getParameter("phone");
		String sex = request.getParameter("sex");
		String userPortrait = request.getParameter("userPortrait");
		String standing = request.getParameter("standing");
		String registeredProvince = request.getParameter("registeredProvince");
		String registeredCity = request.getParameter("registeredCity");
		String registeredArea = request.getParameter("registeredArea");
		String registeredAddress = request.getParameter("registeredAddress");
		String workProvince = request.getParameter("workProvince");
		String workCity = request.getParameter("workCity");
		String workArea = request.getParameter("workArea");
		String workAddress = request.getParameter("workAddress");
		String personDesc = request.getParameter("personDesc");
		String categorys = request.getParameter("categorys");
		String contractType = request.getParameter("contractType");
		String staffNum=request.getParameter("staffNum");
		if(CmsUtils.isNullOrEmpty(user))
		{
			result.push("user", Constants.NULL_CHAR);
			result.push("isRealAuth", Constants.Number.TWO_INT);
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.NOT_USER);
			return result;
		}
		else
		{
			if(!CmsUtils.isNullOrEmpty(auth))
			{
				if(auth.getRealStatus() == Constants.Number.THREE_INT)
				{
					//实名认证成功
					result.push("isRealAuth", Constants.Number.ONE_INT);
				}else
				{
					result.push("isRealAuth", Constants.Number.TWO_INT);
				}
			}else
			{
				result.push("isRealAuth", Constants.Number.TWO_INT);
			}
			//修改限制判断
			String validateMessage = user.getValidateMessage();
			List<Category> categoryList = user.getCategoryList();//拿出数据库的分类
			String dbCategory = Constants.NULL_CHAR;
			for(int c = 0;c<categoryList.size();c++)
			{
				dbCategory +=categoryList.get(c).getId()+Constants.COMMA;
			}
			if(!CmsUtils.isNullOrEmpty(dbCategory))
			{
				dbCategory=dbCategory.substring(Constants.Number.ZERO_INT,dbCategory.length()-1);//组装分类
			}

			if(!CmsUtils.isNullOrEmpty(validateMessage))//验证修改用户信息次数
			{
				String[] validateObject = null;
				if(validateMessage.indexOf("#")>Constants.Number.REDUCE_ONE_INT)
				{
					validateObject= validateMessage.split("#");
				}
				else
				{
					validateObject = new String[Constants.Number.ONE_INT];
					validateObject[Constants.Number.ZERO_INT] = validateMessage;
				}
				for(String message : validateObject)
				{
					if(message.trim().equals("realName"))
					{
						if(!realName.equals(user.getRealName()))
						{
							result.setResponse(Constants.FALSE);
							result.setResponseCode(Constants.SUCCESS_200);
							result.setResponseMsg(Constants.Prompt.CONTACT_CUSTOMER_UPDATE_REALNAME);
							return result;
						}
					}
					if(message.trim().equals("sex"))
					{
						if(!sex.equals(user.getSex()))
						{
							result.setResponse(Constants.FALSE);
							result.setResponseCode(Constants.SUCCESS_200);
							result.setResponseMsg(Constants.Prompt.CONTACT_CUSTOMER_UPDATE_SEX);
							return result;
						}
					}
					if(message.trim().equals("age"))
					{
						if(!birthDate.equals(DateUtils.getStringDate(user.getBirthDate(), Constants.YYYY_MM_DD)))
						{
							result.setResponse(Constants.FALSE);
							result.setResponseCode(Constants.SUCCESS_200);
							result.setResponseMsg(Constants.Prompt.CONTACT_CUSTOMER_UPDATE_AGE);
							return result;
						}
					}
					if(message.trim().equals("address"))
					{
						if(!registeredProvince.equals(user.getRegisteredProvince()) || !registeredCity.equals(user.getRegisteredCity()) || !registeredArea.equals(user.getRegisteredArea()))
						{
							result.setResponse(Constants.FALSE);
							result.setResponseCode(Constants.SUCCESS_200);
							result.setResponseMsg(Constants.Prompt.CONTACT_CUSTOMER_UPDATE_PLACE);
							return result;
						}
					}
					if(message.trim().equals("category"))
					{
						if(!CmsUtils.isNullOrEmpty(dbCategory))
						{
							if(Constants.COMMA.equals(dbCategory.substring(Constants.Number.ZERO_INT,dbCategory.length()-1)))
							{
								dbCategory=dbCategory.substring(Constants.Number.ZERO_INT,dbCategory.length()-1);
							}
							if(!categorys.equals(dbCategory))
							{
								result.setResponse(Constants.FALSE);
								result.setResponseCode(Constants.SUCCESS_200);
								result.setResponseMsg(Constants.Prompt.CONTACT_CUSTOMER_UPDATE_CATEGORY);
								return result;
							}
						}
					}
					if(message.trim().equals("contractType"))
					{
						if(!(String.valueOf(user.getContractType()).equals(contractType)))
						{
							result.setResponse(Constants.FALSE);
							result.setResponseCode(Constants.SUCCESS_200);
							result.setResponseMsg(Constants.Prompt.CONTACT_CUSTOMER_UPDATE_CONTRACT);
							return result;
						}
					}
				}
			}
			String validateData = !CmsUtils.isNullOrEmpty(user.getValidateMessage()) ? user.getValidateMessage() : Constants.NULL_CHAR;
			if(!CmsUtils.isNullOrEmpty(categorys))
			{
				if(!categorys.equals(dbCategory))
				{
					userService.deleteUserCategory(userId);//删除用户所有信息
					String[] category = null;
					if(categorys.indexOf(Constants.COMMA) >= Constants.Number.REDUCE_ONE_INT)
					{
						category = categorys.split(Constants.COMMA);
					}
					else
					{
						category = new String[1];
						category[0] = categorys;
					}
					for(String id : category)
					{
						Map<String, Object> date = new HashMap<String, Object>();
						date.put("userId", user.getId());
						date.put("categoryId", id);
						date.put("createTime", new Date());
						date.put("updateTime", new Date());
						userService.insertUserCategory(date);
					}
					validateData +="#category";
				}

			}
		if(!CmsUtils.isNullOrEmpty(realName))
		{
			if(!realName.trim().equals(user.getRealName()))
			{
				user.setRealName(realName);
				validateData +="#realName";
			}
		}
		user.setPhone(phone);
		if(!CmsUtils.isNullOrEmpty(birthDate))
		{
			String date = DateUtils.getStringDate(user.getBirthDate(), Constants.YYYY_MM_DD);
			if(!date.trim().equals(birthDate.trim()))
			{
				user.setAge(CmsUtils.getAgeByBirth(DateUtils.getDate(birthDate, Constants.YYYY_MM_DD)));
				user.setBirthDate(DateUtils.getDate(birthDate, Constants.YYYY_MM_DD));
				validateData +="#age";
			}
		}
		if(!CmsUtils.isNullOrEmpty(sex))
		{
			if(!sex.trim().equals(user.getSex()))
			{
				user.setSex(sex);
				validateData +="#sex";
			}
		}
		if(!CmsUtils.isNullOrEmpty(contractType))
		{
			if(!contractType.trim().equals(String.valueOf(user.getContractType())))
			{
				user.setContractType(Integer.valueOf(contractType));
				validateData +="#contractType";
			}
		}
		user.setUserPortrait(!CmsUtils.isNullOrEmpty(userPortrait) ? userPortrait : PropertiesUtils.getPropertiesByName("static_url")+"/Fgz_XN_PfpcoLflGWJFY475Dqwcm");
		user.setStanding(standing);
		if(!CmsUtils.isNullOrEmpty(registeredProvince)||!CmsUtils.isNullOrEmpty(registeredCity)||!CmsUtils.isNullOrEmpty(registeredArea))
		{
			if(!registeredCity.trim().equals(user.getRegisteredCity()))
			{
				user.setRegisteredProvince(registeredProvince);
				user.setRegisteredCity(registeredCity);
				user.setRegisteredArea(registeredArea);
				validateData +="#address";
			}
		}
		user.setRegisteredAddress(registeredAddress);
		user.setWorkProvince(workProvince);
		user.setWorkCity(workCity);
		user.setWorkArea(workArea);
		user.setWorkAddress(workAddress);
		user.setPersonDesc(personDesc);
		user.setStaffNum(staffNum);
		if(!CmsUtils.isNullOrEmpty(birthDate))
		{
			user.setBirthDate(DateUtils.getDate(birthDate, Constants.YYYY_MM_DD));
		}
		if(!CmsUtils.isNullOrEmpty(validateData))
		{
			if("#".equals(validateData.substring(Constants.Number.ZERO_INT,Constants.Number.ONE_INT)))
			{
				validateData = validateData.substring(Constants.Number.ONE_INT,validateData.length());
			}
		}
		user.setValidateMessage(validateData);
		user.setUpdateTime(new Date());
		userService.updateByPrimaryKeySelective(user);
		//同步修改环信推送昵称
		if(!CmsUtils.isNullOrEmpty(user.getUserName()))
		{
			updateEasemobNickName(user.getUserName(), user.getId());
		}
		else
		{
			updateEasemobNickName(realName, user.getId());
		}
		result.push("user", JsonUtil.bean2jsonObject(user));
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
		redisUtils.set(Constants.LOGIN_USER_INFO + phone, JsonUtil.bean2jsonToString(user), Constants.Number.MONTH_SECOND);// 用户信息1个月有效时间
    	return result;
	  }
    }

	/**
	 * 多条件查询用户信息
	 * @param request
	 * @param response
	 * @param workerName
	 * @param city
	 * @param categoryId
	 * @param scoreSort
	 * @param senioritySort
	 * @param ageSort
	 * @param workerStatus
	 * @return
	 * @throws IllegalAccessException
	 */
    @RequestMapping(value = "/member/query/user/list.ddxj")
	public ResponseBase queryUserList(HttpServletRequest request, HttpServletResponse response, String workerName,
			String city, String categorys, String scoreSort, String senioritySort, String ageSort, String jobStatus, String isAuth,String categoryName,Integer pageNum,
			Integer pageSize) throws IllegalAccessException {
		ResponseBase result = ResponseBase.getInitResponse();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("workerName", workerName);
		params.put("city", city);
		params.put("categoryName", categoryName);
		params.put("categoryId1", Constants.NULL_CHAR);
		params.put("categoryId2", Constants.NULL_CHAR);
		params.put("categoryId3", Constants.NULL_CHAR);
		params.put("seniorityZeroToOne", null);
		params.put("seniorityOneToThree", null);
		params.put("seniorityThreeToFive", null);
		params.put("seniorityFiveToTen", null);
		params.put("seniorityTenOver", null);
		params.put("senioritySort", senioritySort);
		params.put("ageSort", ageSort);


		//分类列表
	  if (!CmsUtils.isNullOrEmpty(categorys))
	  {
		    if (categorys.indexOf(Constants.COMMA) > Constants.Number.REDUCE_ONE_INT)
		    {
			      String[] category = categorys.split(Constants.COMMA);
			      for (int c = 0; c < category.length; c++)
			      {
			    	  params.put("categoryId" + (c+Constants.Number.ONE_INT), category[c]);
			      }
		    }
		    else
		    {
		    	params.put("categoryId1", categorys);
		    }
	  }
		//工龄列表
	  if (!CmsUtils.isNullOrEmpty(senioritySort))
	  {
		    if (senioritySort.indexOf(Constants.COMMA) > Constants.Number.REDUCE_ONE_INT)
		    {
			      String[] senioritySorts = senioritySort.split(Constants.COMMA);
			      for (int c = 0; c < senioritySorts.length; c++)
			      {
			    	  if(Constants.Number.ZERO_TO_ONE.equals(senioritySorts[c]))
			    	  {
			    		  params.put("seniorityZeroToOne", senioritySorts[c]);
			    	  }
			    	  else if(Constants.Number.ONT_TO_THREE.equals(senioritySorts[c]))
			    	  {
			    		  params.put("seniorityOneToThree", senioritySorts[c]);
			    	  }
			    	  else if(Constants.Number.THREE_TO_FIVE.equals(senioritySorts[c]))
			    	  {
			    		  params.put("seniorityThreeToFive", senioritySorts[c]);
			    	  }
			    	  else if(Constants.Number.FIVE_TO_TEN.equals(senioritySorts[c]))
			    	  {
			    		  params.put("seniorityFiveToTen", senioritySorts[c]);
				      }
			    	  else if(Constants.Number.TEN_STRING.equals(senioritySorts[c]))
			    	  {
				    	  params.put("seniorityTenOver", senioritySorts[c]);
				      }
			      }
		    }
		    else
		    {
			  if(Constants.Number.ZERO_TO_ONE.equals(senioritySort))
			  {
				  params.put("seniorityZeroToOne", senioritySort);
			  }
			  else if(Constants.Number.ONT_TO_THREE.equals(senioritySort))
			  {
				  params.put("seniorityOneToThree", senioritySort);
			  }
			  else if(Constants.Number.THREE_TO_FIVE.equals(senioritySort))
			  {
				  params.put("seniorityThreeToFive", senioritySort);
			  }
			  else if(Constants.Number.FIVE_TO_TEN.equals(senioritySort))
			  {
				  params.put("seniorityFiveToTen", senioritySort);
			  }
			  else if(Constants.Number.TEN_STRING.equals(senioritySort))
			  {
				  params.put("seniorityTenOver", senioritySort);
			  }
		    }
		  }
		//年龄列表
	  if (!CmsUtils.isNullOrEmpty(ageSort)) {
		    if (ageSort.indexOf(Constants.COMMA) > Constants.Number.REDUCE_ONE_INT) {
		      String[] ageSorts = ageSort.split(Constants.COMMA);
		      for (int c = 0; c < ageSorts.length; c++)
		      {
		    	  if(Constants.Number.EIGHTEEN_TO_THIRTY.equals(ageSorts[c]))
		    	  {
		    		  params.put("ageEighteenToThirty", ageSorts[c]);
		    	  }
		    	  else if(Constants.Number.THIRTY_TO_FORTY.equals(ageSorts[c]))
		    	  {
		    		  params.put("ageThirtyToForty", ageSorts[c]);
		    	  }
		    	  else if(Constants.Number.FORTY_TO_FIFTY.equals(ageSorts[c]))
		    	  {
		    		  params.put("ageFortyToFifty", ageSorts[c]);
			      }
		    	  else if(Constants.Number.FIFTY_STRING.equals(ageSorts[c]))
		    	  {
			    	  params.put("ageFiftyOver", ageSorts[c]);
			      }
		      }
		    } else {
		    	if(Constants.Number.EIGHTEEN_TO_THIRTY.equals(ageSort))
		    	  {
		    		  params.put("ageEighteenToThirty", ageSort);
		    	  }
		    	  else if(Constants.Number.THIRTY_TO_FORTY.equals(ageSort))
		    	  {
		    		  params.put("ageThirtyToForty", ageSort);
		    	  }
		    	  else if(Constants.Number.FORTY_TO_FIFTY.equals(ageSort))
		    	  {
		    		  params.put("ageFortyToFifty", ageSort);
			      }
		    	  else if(Constants.Number.FIFTY_STRING.equals(ageSort))
		    	  {
			    	  params.put("ageFiftyOver", ageSort);
			      }
		    }
		  }
	  	params.put("jobStatus", jobStatus);
	  	params.put("isAuth", isAuth);
		params.put("scoreSort", scoreSort);
		PageHelper.startPage(pageNum, pageSize);
		List<User> userList = userService.queryUserLists(params);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
		result.push("userList", JsonUtil.list2jsonToArray(userList));
		return result;

	}

    /**
     * 查询该用户是否被工头邀请过
     *
     * @param request
     * @param response
     * @author fancunxin
	 * @throws ClientException
     */
    @RequestMapping(value = "/member/user/is/request.ddxj")
    public ResponseBase queryUserIsRequest(HttpServletRequest request, HttpServletResponse response,Integer fromUserId,Integer toUserId) throws ClientException
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	Integer idList = userService.queryUserIsRequest(fromUserId,toUserId);
    	if(idList != null && idList > Constants.Number.ZERO_INT)
    	{
    		result.push("requestStatus", Constants.Number.ONE_INT);
    	}
    	else
    	{
    		result.push("requestStatus", Constants.Number.TWO_INT);
    	}
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	return result;
    }

    /**
     * 验证码验证
     * @param request
     * @param response
     * @param idenCode
     * @param phone
     * @return
     * @throws ClientException
     */
	@RequestMapping(value = "/member/validate/idenCode.ddxj")
	public ResponseBase validateIdenCode(HttpServletRequest request, HttpServletResponse response, String idenCode,
			String phone,Integer type) throws ClientException {
		ResponseBase result = ResponseBase.getInitResponse();
		String code = null;
		String flag = null;
		if(type == Constants.Number.ONE_INT)
		{
			flag = Constants.LOGIN_IDEN_CODE;
		}
		else if(type == Constants.Number.TWO_INT)
		{
			flag = Constants.CHANGE_PASS_CODE;
		}
		else if(type == Constants.Number.THREE_INT)
		{
			flag = Constants.CHANGE_PAY_CODE;
		}
		else if(type == Constants.Number.FOUR_INT)
		{
			flag = Constants.BOND_BANK_CARD_CODE;
		}
		else if(type == Constants.Number.FIVE_INT)
		{
			flag = Constants.CREDIT_VERIFY_CODE;
		}
		else if(type == Constants.Number.SIX_INT)
		{
			flag = Constants.CHANGE_PHONE_CODE;
			User selectUser = userService.SelectUser(phone);
			if(!CmsUtils.isNullOrEmpty(selectUser))
			{
				result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.PHONE_EXISTENCE);
				return result;
			}
		}
		else if(type == Constants.Number.SEVEN_INT)
		{

			flag = Constants.FORGET_PASSWORD;
			User selectUser = userService.SelectUser(phone);
			if(CmsUtils.isNullOrEmpty(selectUser))
			{
				result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.NOT_USER);
				return result;
			}

		}
		else if(type==Constants.Number.NINE_INT)
		{
			flag = Constants.WEB_SITE_CODE;
		}
		// 判断是否缓存该账号验证码
		boolean isExist = redisUtils.exists(flag + phone);
		if (isExist)
		{
			code = (String) redisUtils.get(flag + phone);
			if (code.equals(idenCode))
			{
				result.setResponse(Constants.TRUE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.VALIDATE_SUCCESSFUL);
			}
			else
			{
				result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.VALIDATE_CODE_ERROR);
			}
		} else {
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.VALIDATE_CODE_ERROR);
		}
		return result;
	}

    /**
     * 更新关注用户
     *
     * @param request
     * @param response
     * @author Rao
     * @throws ClientException
     */
    @RequestMapping(value = "/member/update/user/follow.ddxj")
    public ResponseBase updateUserFollow(HttpServletRequest request, HttpServletResponse response,int fromUserId,int toUserId) throws ClientException, IllegalAccessException
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	UserCollection collection = userCollectionService.queryUserIsFollow(fromUserId, toUserId);
    	if(collection != null)
    	{
    		if(collection.getFlag() == Constants.Number.ONE_INT)
    		{
    			//更新为取消关注
    			userCollectionService.updateUserFollowById(collection.getId(), fromUserId, Constants.Number.TWO_INT);//更新关注状态
    			result.push("followStatus", Constants.Number.TWO_INT);
    		}
    		else
    		{
    			//更新为已关注
    			userCollectionService.updateUserFollowById(collection.getId(), fromUserId, Constants.Number.ONE_INT);//更新关注状态
    			result.push("followStatus", Constants.Number.ONE_INT);
    		}
    	}
    	else
    	{
    		//创建关注记录，返回更新状态
    		collection = new UserCollection();
    		collection.setCollectionType(Constants.Number.ONE_INT);
    		collection.setFlag(Constants.Number.ONE_INT);
    		collection.setFromUserId(fromUserId);
    		collection.setToUserId(toUserId);
    		collection.setUpdateTime(new Date());
    		collection.setCreateTime(new Date());
    		userCollectionService.insertSelective(collection);//生成收藏记录
			result.push("followStatus", Constants.Number.ONE_INT);
    	}
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
    	return result;
    }

    /**
     * 检查用户信息是否存在
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException
     */
    @RequestMapping(value = "/member/validate/login.ddxj")
    public ResponseBase validateLoginStatus(HttpServletRequest request, HttpServletResponse response,String phone)
    {
    	ResponseBase result = ResponseBase.getInitResponse();
		String userInfo = (String) redisUtils.get(Constants.LOGIN_USER_INFO + phone);//查询redis里面是否存在用户信息
		if(!CmsUtils.isNullOrEmpty(userInfo))
		{
			redisUtils.set(Constants.LOGIN_USER_INFO + phone,userInfo, Constants.Number.MONTH_SECOND);
			result.push("user", JSONObject.parse(userInfo));
		}
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.VALIDATE_SUCCESSFUL);
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
    @RequestMapping(value = "/query/enlist/user/list.ddxj")
    public ResponseBase queryEnlistUserList(HttpServletRequest request, HttpServletResponse response,int userId,int recruitId,int pageNum,int pageSize,Integer status) throws IllegalAccessException
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	PageHelper.startPage(pageNum,pageSize);
		List<User> userList = userService.queryEnlistUserList(userId,recruitId,status);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
		result.push("recruitUserList",JsonUtil.list2jsonToArray(userList));
    	return result;
    }
	/**
	 * 查询我的钱包
	 * @param request
	 * @param response
	 * @param userId
	 * @return
	 * @throws IllegalAccessException
	 */
	@RequestMapping(value = "/query/my/wallet.ddxj")
	public ResponseBase queryMyWallet(HttpServletRequest request, HttpServletResponse response,int userId) throws IllegalAccessException
	{
		ResponseBase result = ResponseBase.getInitResponse();
		User user = userService.selectByPrimaryKey(userId);
		List<UserBank> bankList = userService.findUserBankList(userId);
		BigDecimal frozenAmount=userWithdrawService.totlaFrozenAmount(userId);
		if(CmsUtils.isNullOrEmpty(frozenAmount))
		{
			frozenAmount=new BigDecimal("0.00");
		}
		if(!CmsUtils.isNullOrEmpty(user))
		{
			UserPassword password = userService.selectPasswordByUserId(userId);
			if(CmsUtils.isNullOrEmpty(bankList)){
				result.push("binding",Constants.Number.ZERO_INT);//未绑卡
		}else{
			result.push("binding",Constants.Number.ONE_INT);//已绑卡
		}
			if(user.getIsAttestation()==Constants.Number.ZERO_INT)
			{
				result.push("isAttestation", Constants.Number.ZERO_INT);//未认证
			}
			else if(user.getIsAttestation()==Constants.Number.ONE_INT)
			{
				result.push("isAttestation", Constants.Number.ONE_INT);//已认证
			}
			if(!CmsUtils.isNullOrEmpty(password))
			{
				if(!CmsUtils.isNullOrEmpty(password.getPayPassword()))
				{
					result.push("payPassword", Constants.Number.ONE_INT);
				}else
				{
					result.push("payPassword", Constants.Number.TWO_INT);
				}
			}else
			{
				result.push("payPassword", Constants.Number.TWO_INT);
				result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.NOT_SETTING_PAY_PASSWORD);
			}
			result.push("remainderMoney", user.getRemainderMoney().toString());
			result.push("frozenAmount", frozenAmount.toString());
			result.setResponse(Constants.TRUE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
		}else
		{
			result.push("remainderMoney", Constants.Number.ZERO_INT);
			result.push("frozenAmount", Constants.Number.ZERO_INT);
			result.push("payPassword", Constants.Number.TWO_INT);
		result.setResponse(Constants.FALSE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.NOT_USER);
		}
		return result;
	}
    
    /**
     * 查询我关注的工人信息
     * @param request
     * @param response
     * @param userId
     * @return
     * @throws IllegalAccessException
     */
    @RequestMapping(value = "/query/my/follow/user/info.ddxj")
    public ResponseBase queryMyFollowUserInfo(HttpServletRequest request, HttpServletResponse response,Integer userId,int pageNum,int pageSize) throws IllegalAccessException
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	PageHelper.startPage(pageNum, pageSize);
    	List<User> followUserList = userService.queryMyFollowUserInfo(userId);
		result.push("followUserList", JsonUtil.list2jsonToArray(followUserList));
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	return result;
    }

    /**
     * 查询广告轮播图
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/query/banner/list.ddxj")
    public ResponseBase queryBannerList(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	ResponseBase result=ResponseBase.getInitResponse();
    	List<AdvertBanner> advertBanners = adverBannerService.selectBanner();
		result.push("advertList", JsonUtil.list2jsonToArray(advertBanners));
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	return result;
    }

    /**
     * 查询咨询
     * @param request
     * @param response
     * @param pageNum
     * @param pageSize
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/query/information/list.ddxj")
    public ResponseBase queryInformationList(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo) throws Exception
    {
    	ResponseBase result=ResponseBase.getInitResponse();
		List<Information> informations=null;
		PageHelper.startPage(requestVo.getPageNum(), requestVo.getPageSize());
		informations= informationService.selectInformation(requestVo);
		result.push("informationList", JsonUtil.list2jsonToArray(informations));
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	return result;
    }

    /**
     * 查询咨询分类
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException
     */
    @RequestMapping(value="/query/information/category/list.ddxj")
    public ResponseBase queryInformationCateogryList(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo) throws Exception
    {
    	ResponseBase result=ResponseBase.getInitResponse();
    	PageHelper.startPage(Constants.Number.ONE_INT, 99999);
		List<InformationType> catgegoryList = informationService.queryInformationTypeList(requestVo);
    	PageInfo<InformationType> info = new PageInfo<InformationType>(catgegoryList);
    	result.push("categoryList", info);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	return result;
    }

    /**
     * 更新收藏咨询
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException
     */
    @RequestMapping(value = "/update/information/collection.ddxj")
    public ResponseBase updateInformationCollection(HttpServletRequest request, HttpServletResponse response,int userId,int infoId) throws ClientException, IllegalAccessException
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	UserCollection collection = userCollectionService.queryInformationCollection(userId, infoId);
    	if(collection != null)
    	{
    		if(collection.getFlag() == Constants.Number.ONE_INT)
    		{
    			//更新为取消收藏
    			userCollectionService.updateInformationCollection(collection.getId(), userId, Constants.Number.TWO_INT);//更新关注状态
    			result.push("followStatus", Constants.Number.TWO_INT);
    		}
    		else
    		{
    			//更新为已收藏
    			userCollectionService.updateInformationCollection(collection.getId(), userId, Constants.Number.ONE_INT);//更新关注状态
    			result.push("followStatus", Constants.Number.ONE_INT);
    		}
    	}
    	else
    	{
    		//创建关注记录，返回更新状态
    		collection = new UserCollection();
    		collection.setCollectionType(Constants.Number.THREE_INT);
    		collection.setFromUserId(userId);
    		collection.setInfoId(infoId);
    		collection.setUpdateTime(new Date());
    		collection.setCreateTime(new Date());
    		userCollectionService.insertSelective(collection);//生成收藏记录
			result.push("followStatus", Constants.Number.ONE_INT);
    	}
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
    	return result;
    }

    /**
     * 查询收藏咨询
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException
     */
    @RequestMapping(value="/query/information/collection/list.ddxj")
    public ResponseBase queryInformationCollectionList(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo) throws Exception
    {
    	ResponseBase result=ResponseBase.getInitResponse();
    	PageHelper.startPage(requestVo.getPageNum(), requestVo.getPageSize());
		List<Information> informations = userCollectionService.queryCollectionInformation(requestVo);
    	result.push("informationList", JsonUtil.list2jsonToArray(informations));
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	return result;
    }

    /**
     * 查询咨询详情
     * @param request
     * @param response
     * @param pageNum
     * @param pageSize
     * @param infoId
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/query/information/details.ddxj")
    public ResponseBase queryInformationDetails(HttpServletRequest request, HttpServletResponse response,Integer userId,int infoId) throws Exception{
    	ResponseBase result=ResponseBase.getInitResponse();

    	String ip = RequestUtils.getIpAddr(request);
		if(!CmsUtils.isNullOrEmpty(ip))
		{
			InformationRecordIp record = informationService.queryInforRecordIpByIP(ip, infoId);
			if(CmsUtils.isNullOrEmpty(record))
			{
				InformationRecordIp newIp = new InformationRecordIp();
				newIp.setInfoId(infoId);
				newIp.setUserIp(ip);
				newIp.setCreateTime(new Date());
				newIp.setUpdateTime(new Date());
				informationService.addInformationRecordIp(newIp);
			}
		}

    	Information information = informationService.selectByPrimaryKey(infoId);
		result.push("information", JsonUtil.object2jsonToObject(information));

		UserCollection collection = null;
		if(userId != null && userId > 0)
		{
			collection = userCollectionService.queryInformationCollection(userId, infoId);
		}

		if(collection != null)
    	{
			result.push("followStatus", collection.getFlag());
    	}
    	else
    	{
			result.push("followStatus", Constants.Number.TWO_INT);
    	}

		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	return result;
    }

    /**
     * 查询个人用户信息
     * @param request
     * @param response
     * @param pageNum
     * @param pageSize
     * @param userId
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/query/user/info.ddxj")
    public ResponseBase queryUserInfo(HttpServletRequest request, HttpServletResponse response, int userId) throws Exception{
    	ResponseBase result=ResponseBase.getInitResponse();
    	User user = userService.selectByPrimaryKey(userId);
    	RealAuth auth = userService.queryUserRealAuth(userId);
    	if(!CmsUtils.isNullOrEmpty(user))
    	{
    		if(!CmsUtils.isNullOrEmpty(auth))
			{
				if(auth.getRealStatus() == Constants.Number.THREE_INT)
				{
					//实名认证成功
					result.push("isRealAuth", Constants.Number.ONE_INT);
				}else
				{
					result.push("isRealAuth", Constants.Number.TWO_INT);
				}
			}else
			{
				result.push("isRealAuth", Constants.Number.TWO_INT);
			}
    	}
    	result.push("user", JsonUtil.bean2jsonObject(user));
    	//result.push("personDesc", newString);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	return result;
    }

    /**
     * 修改支付密码
     * @param request
     * @param response
     * @param userId
     * @param newPassword
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/update/payment/password.ddxj")
    public ResponseBase updatePaymentPassword(HttpServletRequest request, HttpServletResponse response, int userId, String newPassword) throws Exception
    {
    	ResponseBase result=ResponseBase.getInitResponse();
    	String oldPassword = request.getParameter("oldPassword");
    	UserPassword password = userService.selectPasswordByUserId(userId);
    	if(!CmsUtils.isNullOrEmpty(password))
    	{
    		if(!CmsUtils.isNullOrEmpty(oldPassword))
        	{
        		//判断原支付密码是否匹配
        		if(!CmsUtils.isNullOrEmpty(password.getPayPassword()))
        		{
        			if(oldPassword.equals(password.getPayPassword()))
        			{
        				//修改支付密码
        				password.setPayPassword(newPassword);
        				int count = userService.updatePaymentPassword(password);
        				if(count == Constants.Number.ZERO_INT)
        				{
        					result.setResponse(Constants.FALSE);
                    		result.setResponseCode(Constants.SUCCESS_200);
                    		result.setResponseMsg(Constants.Prompt.UPDATE_FAILURE);
        				}else
        				{
        					result.setResponse(Constants.TRUE);
                    		result.setResponseCode(Constants.SUCCESS_200);
                    		result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
        				}

        			}else
        			{
            			result.setResponse(Constants.FALSE);
                		result.setResponseCode(Constants.SUCCESS_200);
                		result.setResponseMsg(Constants.Prompt.PAY_PASSWORD_ERROR);
        			}
        		}else
        		{
        			result.setResponse(Constants.FALSE);
            		result.setResponseCode(Constants.SUCCESS_200);
            		result.setResponseMsg(Constants.Prompt.NOT_SETTING_PAY_PASSWORD);
        		}
        	}
    		else
        	{
        		//验证码修改支付密码
        		if(!CmsUtils.isNullOrEmpty(password.getPayPassword()))
        		{
        			password.setPayPassword(newPassword);
        			userService.updatePaymentPassword(password);
        			result.setResponse(Constants.TRUE);
            		result.setResponseCode(Constants.SUCCESS_200);
            		result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);

        		}
        		else
        		{
        			//无支付密码，设置支付密码
        			password.setPayPassword(newPassword);
        			userService.updatePaymentPassword(password);
        			result.setResponse(Constants.TRUE);
            		result.setResponseCode(Constants.SUCCESS_200);
            		result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
        		}
        	}
    	}else
    	{
    		//未设置密码
    		UserPassword userPassword = new UserPassword();
    		userPassword.setUserId(userId);
    		userPassword.setPayPassword(newPassword);
    		userPassword.setCreateTime(new Date());
    		userPassword.setUpdateTime(new Date());
    		userService.insertUserPassword(userPassword);
			result.setResponse(Constants.TRUE);
    		result.setResponseCode(Constants.SUCCESS_200);
    		result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
    	}
    	return result;
    }
	/**
    * 发放工资
    * @param request
    * @param response
    * @param fromUserId
    * @param toUserId
    * @param recruitId
    * @param workerName
    * @param unit
    * @param count
    * @param price
    * @param transferDesc
    * @param money
    * @param transferWay
    * @return
    * @author ddxj
    */
    @RequestMapping(value="/add/recruit/wages.ddxj")
    public ResponseBase addRecruitWages(HttpServletRequest request, HttpServletResponse response,Integer fromUserId,Integer toUserId,Integer recruitId,String workerName ,String unit,String count,String price,String transferDesc,String money,Integer transferWay)
    {
    	User toUser=userService.queryUserDetail(toUserId);
    	UserPassword userPassword = userService.selectPasswordByUserId(fromUserId);
    	ResponseBase result=ResponseBase.getInitResponse();
    	if(!CmsUtils.isNullOrEmpty(userPassword))
    	{
    		if(!CmsUtils.isNullOrEmpty(userPassword.getPayPassword()))
    		{
    	    	if(!toUser.getRealName().equals(workerName)){
    	    		result.push("payStatus", Constants.Number.ONE_INT);
    	    		result.setResponse(Constants.FALSE);
    	        	result.setResponseCode(Constants.SUCCESS_200);
    	        	result.setResponseMsg(Constants.Prompt.REALNAME_NOT_MATCHING);
    	        	return result;
    	    	}
    	    	else
    	    	{
    	    		//判断是余额支付还是授信支付
    	    		if(transferWay == Constants.Number.TWO_INT)//余额支付
    	    		{
    	    			UserTransfer userTransfer=new UserTransfer();
    	    			userTransfer.setFromUserId(fromUserId);
    	    			userTransfer.setToUserId(toUserId);
    	    			userTransfer.setRecruitId(recruitId);
    	    			userTransfer.setUnit(unit);
    	    			userTransfer.setPrice(new BigDecimal(price));
    	    			userTransfer.setCount(count);
    	    			userTransfer.setTransferDesc(transferDesc);
    	    			userTransfer.setTransferType(Constants.Number.TWO_INT);
    	    			userTransfer.setMoney(new BigDecimal(money));
    	    			userTransfer.setTransferWay(transferWay);
    	    			userTransfer.setOrderNo(OrderNumUtils.getOredrNum());
    	    			userTransfer.setCreateTime(new Date());
    	    			userTransfer.setUpdateTime(new Date());
    	    			result= userTransferService.addRecruitWages(userTransfer);
    	    			if(result.isResponse())
    	    			{
    	    				result.push("payStatus", Constants.Number.ONE_INT);
    	    				result.setResponse(Constants.TRUE);
    	    				result.setResponseCode(Constants.SUCCESS_200);
    	    				result.setResponseMsg(Constants.Prompt.PAY_SUCCESSFUL);
    	    			}
    	    		}else if(transferWay == Constants.Number.ONE_INT)
    	    		{
    	    			//判断发放金额与授信金额
    	    			RecruitCredit recruitCredit = recruitCreditService.queryRecruitCredit(fromUserId, recruitId);
    	    			//可用额度
    	    			BigDecimal availableAmount = recruitCredit.getUsableMoney();
    	    			//发放额度
    	    			BigDecimal settlementAmount = new BigDecimal(money);
    	    			//剩余额度
    	    			BigDecimal surplusAmount = availableAmount.subtract(settlementAmount);
    	    			if(CmsUtils.isNullOrEmpty(recruitCredit))
    	    			{
    	    				result.push("payStatus", Constants.Number.ONE_INT);
    	    				result.setResponse(Constants.FALSE);
    	    				result.setResponseCode(Constants.SUCCESS_200);
    	    				result.setResponseMsg(Constants.Prompt.NOT_CREDIT);
    	    			}else
    	    			{
    	    				if(availableAmount.compareTo(settlementAmount) < 0)
    	    				{
    	    					result.push("payStatus", Constants.Number.ONE_INT);
    	    					result.setResponse(Constants.FALSE);
    	        				result.setResponseCode(Constants.SUCCESS_200);
    	        				result.setResponseMsg(Constants.Prompt.CREDIT_BALANCE_INSUFFICIENT);
    	        				return result;
    	    				}
    	    				//更新用户的可用额度
    	    				recruitCreditService.updateUsableMoney(fromUserId, recruitId, surplusAmount);
    	    				//授信支付
    	    				SalaryRecord record = new SalaryRecord();
    	    				record.setRecruitId(recruitId);
    	    				record.setAssignUserId(fromUserId);
    	    				record.setSendeeUserId(toUserId);
    	    				record.setCount(count);
    	    				record.setUnit(unit);
    	    				record.setPrice(new BigDecimal(price));
    	    				record.setTransferDesc(transferDesc);
    	    				record.setMoney(new BigDecimal(money));
    	    				record.setTransferType(Constants.Number.TWO_INT);
    	    				record.setTransferWay(transferWay);
    	    				//审核中暂时没有结算ID
    	    				record.setCreateTime(new Date());
    	    				record.setUpdateTime(new Date());
    	    				record.setAuditStatus(Constants.Number.ONE_INT);
    	    				salaryRecordService.insertSelective(record);
    	    				result.setResponse(Constants.TRUE);
    	    				result.setResponseCode(Constants.SUCCESS_200);
    	    				result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
    	    			}
    	    		}
    	    	}
    		}else
    		{
    			result.push("payStatus", Constants.Number.ZERO_INT);
    			result.setResponse(Constants.FALSE);
            	result.setResponseCode(Constants.SUCCESS_200);
            	result.setResponseMsg(Constants.Prompt.NOT_SETTING_PAY_PASSWORD);
            	return result;
    		}
    	}else
    	{
    		result.push("payStatus", Constants.Number.ZERO_INT);
    		result.setResponse(Constants.FALSE);
        	result.setResponseCode(Constants.SUCCESS_200);
        	result.setResponseMsg(Constants.Prompt.NOT_SETTING_PAY_PASSWORD);
        	return result;
    	}
    	return result;

    }

    /**
     * 转账
     * @param request
     * @param response
     * @param userId
     * @param toPhone
     * @param transferMoney
     * @param payType
     * @return
     * @author ddxj
     */
    @RequestMapping(value="/add/transfer/to/user.ddxj")
    public ResponseBase addTransferToUser(HttpServletRequest request, HttpServletResponse response,Integer userId,String toPhone,String transferMoney,Integer payType)
    {
    	ResponseBase result=ResponseBase.getInitResponse();
    	//收款人详细信息
    	User toUser=userService.SelectUser(toPhone);
    	if(CmsUtils.isNullOrEmpty(toUser)){
    		result.setResponse(Constants.FALSE);
        	result.setResponseCode(Constants.SUCCESS_200);
        	result.setResponseMsg(Constants.Prompt.NOT_USER);
        	return result;
    	}else{
    		//查询转账人角色
    		Integer role = userService.queryUserDetail(userId).getRole();
    		if(role==Constants.Number.ONE_INT)//工人
    		{
    			//单笔2W
    			BigDecimal tranMoney=new BigDecimal(transferMoney);
    			if(tranMoney.compareTo(new BigDecimal(Constants.Number.WORKER_SINGLE_MONEY))>0 )
    			{
					result.setResponse(Constants.FALSE);
    	        	result.setResponseCode(Constants.SUCCESS_200);
    	        	result.setResponseMsg(Constants.Prompt.SINGLE_TRANSFER_AMOUNT+Constants.Prompt.WORKER_SINGLE_AMOUNT);
    	        	return result;
    			}
    			//单日3W
    			BigDecimal money = userService.totalUserTransferMoney(userId,Constants.Number.ONE_INT);
    			if(!CmsUtils.isNullOrEmpty(money))
    			{
    				if((tranMoney.add(money)).compareTo(new BigDecimal(Constants.Number.WORKER_SINGLE_AMOUNT))>0)
        			{
        				result.setResponse(Constants.FALSE);
        	        	result.setResponseCode(Constants.SUCCESS_200);
        	        	result.setResponseMsg(Constants.Prompt.TRANSFER_BOUND_AMOUNT+Constants.Prompt.WORKER_ONE_DAY_AMOUNT);
        	        	return result;
        			}
    			}
    			//次数3次
    			int total = userService.countUserTransfer(userId,Constants.Number.ONE_INT);
    			if(total>=Constants.Number.THREE_INT)
    			{
    				result.setResponse(Constants.FALSE);
    	        	result.setResponseCode(Constants.SUCCESS_200);
    	        	result.setResponseMsg(Constants.Prompt.TRANSFER_NUM+Constants.Prompt.WORKER_NUM);
    	        	return result;
    			}
    		}
    		else if(role==Constants.Number.TWO_INT)//工头
    		{
    			//单笔10W
    			BigDecimal tranMoney=new BigDecimal(transferMoney);
    			if(tranMoney.compareTo(new BigDecimal(Constants.Number.BOSS_SINGLE_MONEY))>0 )
    			{
					result.setResponse(Constants.FALSE);
    	        	result.setResponseCode(Constants.SUCCESS_200);
    	        	result.setResponseMsg(Constants.Prompt.SINGLE_TRANSFER_AMOUNT+Constants.Prompt.GANGER_SINGLE_AMOUNT);
    	        	return result;
    			}
    			//单日50W
    			BigDecimal money = userService.totalUserTransferMoney(userId,Constants.Number.ONE_INT);
    			if(!CmsUtils.isNullOrEmpty(money))
    			{
    				if((tranMoney.add(money)).compareTo(new BigDecimal(Constants.Number.BOSS_SINGLE_AMOUNT))>0)
        			{
        				result.setResponse(Constants.FALSE);
        	        	result.setResponseCode(Constants.SUCCESS_200);
        	        	result.setResponseMsg(Constants.Prompt.TRANSFER_BOUND_AMOUNT+Constants.Prompt.GANGER_ONE_DAY_AMOUNT);
        	        	return result;
        			}
    			}
    			//次数5次
    			int total = userService.countUserTransfer(userId,Constants.Number.ONE_INT);
    			if(total>=Constants.Number.FIVE_INT)
    			{
    				result.setResponse(Constants.FALSE);
    	        	result.setResponseCode(Constants.SUCCESS_200);
    	        	result.setResponseMsg(Constants.Prompt.TRANSFER_NUM+Constants.Prompt.GANGER_NUM);
    	        	return result;
    			}
    		}

    		UserTransfer userTransfer=new UserTransfer();
    		Integer toUserId=toUser.getId();
	    	userTransfer.setFromUserId(userId);
	    	userTransfer.setToUserId(toUserId);
	    	userTransfer.setMoney(new BigDecimal(transferMoney));
    		userTransfer.setOrderNo(OrderNumUtils.getOredrNum());
    		userTransfer.setTransferType(Constants.Number.ONE_INT);
    		userTransfer.setCreateTime(new Date());
    		userTransfer.setUpdateTime(new Date());
    		userTransfer.setTransferDesc("余额转账");
    		userTransfer.setTransferWay(payType);
	    	result=userTransferService.addRecruitWages(userTransfer);
    	}
    	return result;
    }
	/**
	 * 更新申请成为工头
	 * @param request
	 * @param response
	 * @param userId
	 * @param reason
	 * @return
	 */
	@RequestMapping(value="update/apply/foreman/record.ddxj")
	public ResponseBase updateApplyForemanRecord(HttpServletRequest request, HttpServletResponse response,Integer userId,String reason,Integer applyId)
	{
		ResponseBase result=ResponseBase.getInitResponse();
		ApplyForeman applyForeman = userService.selectByUserId(userId);
		if(CmsUtils.isNullOrEmpty(applyForeman))
		{
			ApplyForeman newApplyForeman = new ApplyForeman();
			newApplyForeman.setUserId(userId);
			newApplyForeman.setReason(reason);
			newApplyForeman.setValidateStatus(Constants.Number.ONE_INT);
			newApplyForeman.setValidateTime(new Date());
			newApplyForeman.setCreateTime(new Date());
			newApplyForeman.setUpdateTime(new Date());
			int count = userService.addApplyForeman(newApplyForeman);
			if(count == Constants.Number.ZERO_INT)
			{
				result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.ADD_FAILURE);
			}else
			{
				result.setResponse(Constants.TRUE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.APPLY_SUBMIT_WAIT);
			}
		}else
		{
			if(!CmsUtils.isNullOrEmpty(applyId) && applyId > Constants.Number.ZERO_INT)
			{
				applyForeman.setId(Integer.valueOf(applyId));
				applyForeman.setReason(reason);
				applyForeman.setValidateStatus(Constants.Number.ONE_INT);
				applyForeman.setUpdateTime(new Date());
				applyForeman.setValidateTime(new Date());
				int count = userService.updateForman(applyForeman);
				if(count == Constants.Number.ZERO_INT)
				{
					result.setResponse(Constants.FALSE);
					result.setResponseCode(Constants.SUCCESS_200);
					result.setResponseMsg(Constants.Prompt.UPDATE_FAILURE);
				}else
				{
					result.setResponse(Constants.TRUE);
					result.setResponseCode(Constants.SUCCESS_200);
					result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
				}
			}else
			{
				result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.APPLY_SUBMIT_WAIT);
			}
		}
		/*ApplyForeman applyForeman = new ApplyForeman();
		if(!CmsUtils.isNullOrEmpty(applyId) && applyId > Constants.Number.ZERO_INT)
		{
			//修改操作
			applyForeman.setId(Integer.valueOf(applyId));
			applyForeman.setReason(reason);
			applyForeman.setValidateStatus(Constants.Number.ONE_INT);
			applyForeman.setUpdateTime(new Date());
			applyForeman.setValidateTime(new Date());
			int count = userService.updateForman(applyForeman);
			if(count == Constants.Number.ZERO_INT)
			{
				result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.UPDATE_FAILURE);
			}else
			{
				result.setResponse(Constants.TRUE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
			}
		}else
		{
			ApplyForeman applyForeman1 = userService.selectByUserId(userId);
			if(!CmsUtils.isNullOrEmpty(applyForeman1))
			{
				result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.APPLY_SUBMIT_WAIT);
				return result;
			}
			//增加操作
			applyForeman.setUserId(userId);
			applyForeman.setReason(reason);
			applyForeman.setValidateStatus(Constants.Number.ONE_INT);
			applyForeman.setValidateTime(new Date());
			applyForeman.setCreateTime(new Date());
			applyForeman.setUpdateTime(new Date());
			int count = userService.addApplyForeman(applyForeman);
			if(count == Constants.Number.ZERO_INT)
			{
				result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.UPDATE_FAILURE);
			}else
			{
				result.setResponse(Constants.TRUE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
			}
		}*/
		return result;
	}
	
	/**
	 * 取消申请成为工头
	 * @param request
	 * @param response
	 * @param userId
	 * @author fancunxin
	 * @return
	 */
	@RequestMapping(value="/delete/apply/foreman/record.ddxj")
	public ResponseBase deleteApplyForemanRecord(HttpServletRequest request, HttpServletResponse response,Integer userId)
	{
		ResponseBase result=ResponseBase.getInitResponse();
		
		ApplyForeman applyForeman = userService.selectByUserId(userId);
		if(CmsUtils.isNullOrEmpty(applyForeman))
		{
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.NO_APPLICATION);
		}
		userService.delApplyForemanRecord(userId);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
		return result;
	}

    /**
     * 查看申请工头详情
     * @param request
     * @param response
     * @param userId
     * @return
     */
    @RequestMapping(value="/query/apply/foreman/record.ddxj")
    public ResponseBase addRechargeInfo(HttpServletRequest request, HttpServletResponse response,Integer userId)
    {
    	ResponseBase result=ResponseBase.getInitResponse();
    	ApplyForeman applyForeman = userService.selectByUserId(userId);
    	if(!CmsUtils.isNullOrEmpty(applyForeman))
    	{
    		result.push("applyForemanRecord", JsonUtil.object2jsonToObject(applyForeman));
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	}else
    	{
    		result.push("applyForemanRecord", JsonUtil.object2jsonToObject(applyForeman));
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	}
    	return result;
    }

    /**
     * 查看资金变动记录
     * @param request
     * @param response
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     * @throws IllegalAccessException
     * @author ddxj
     */
    @RequestMapping(value="/query/capital/change/record.ddxj")
    public ResponseBase queryCapitalChangeRecord(HttpServletRequest request, HttpServletResponse response,Integer userId,Integer pageNum,Integer pageSize,Integer transferType) throws IllegalAccessException
    {
    	ResponseBase result=ResponseBase.getInitResponse();
    	PageHelper.startPage(pageNum, pageSize);
    	List<UserTransfer> transferList = null;
    	if(transferType != null && transferType == 0)
    	{
    		transferList = userTransferService.queryCapitalChangeRecord(userId);
    	}
    	else
    	{
    		transferList = userTransferService.queryCapitalChangeRecordByType(userId, transferType);
    	}
		result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	result.push("transferList", JsonUtil.list2jsonToArray(transferList));
		return result;
    }
    /**
     * 资金变动详情
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException
     */
    @RequestMapping(value = "/query/transfer/details.ddxj")
    public ResponseBase queryTransferDetails(HttpServletRequest request, HttpServletResponse response,Integer transferId ,Integer transferType) throws ClientException, IllegalAccessException
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	//根据不同的转账类型显示不同的资金变动详情
    	if(CmsUtils.isNullOrEmpty(transferType)){
    		result.setResponse(Constants.FALSE);
	    	result.setResponseCode(Constants.SUCCESS_200);
	    	result.setResponseMsg(Constants.Prompt.TRANSFER_TYPE_NOT_NULL);
	    	return result;
    	}
		UserTransfer transfer = userTransferService.querySettlementAndTransfer(transferId, transferType);
		result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	result.push("transfer", JsonUtil.object2jsonToObject(transfer));
    	return result;


    }

    /**
     * 查询转账用户信息
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException
     */
    @RequestMapping(value = "/query/transfer/user.ddxj")
    public ResponseBase queryTransferUser(HttpServletRequest request, HttpServletResponse response,String phone) throws ClientException, IllegalAccessException
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	User user = userService.SelectUser(phone);
    	if(CmsUtils.isNullOrEmpty(user))
    	{
    		result.setResponse(Constants.FALSE);
        	result.setResponseCode(Constants.SUCCESS_200);
        	result.setResponseMsg(Constants.Prompt.NOT_USER);
        	return result;
    	}
    	result.push("user", JsonUtil.bean2jsonObject(user));
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	log.info("#####################查询转账用户信息成功#####################");
    	return result;
    }

    /**
     * 查询个人中心
     * @param request
     * @param response
     * @param phone
     * @return
     * @throws ClientException
     * @throws IllegalAccessException
     */
    @RequestMapping(value = "/query/center/user/info.ddxj",method=RequestMethod.POST)
    public ResponseBase queryCenterUserInfo(HttpServletRequest request, HttpServletResponse response,Integer userId) throws ClientException, IllegalAccessException
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	User user = userService.queryUserDetail(userId);
    	Map<String, Object> totalCount = new HashMap<String, Object>();
    	//查询消息标识
    	int count = userCenterService.queryIsRead(userId);
    	if(count > 0)
    	{
    		result.push("messageIsRead", 1);
    	}else
    	{
    		result.push("messageIsRead", 2);
    	}
    	if(!CmsUtils.isNullOrEmpty(user))
    	{
        	//查询推送标识
        	if(!CmsUtils.isNullOrEmpty(user.getAppUserToken()))
        	{
        		result.push("pushIsOpen", 1);
        	}else
        	{
        		result.push("pushIsOpen", 2);
        	}
    		if(user.getRole() == Constants.Number.ONE_INT)//工人端
    		{
    			totalCount.put("collectionRecruitCount", collectionService.selectRecruitCount(userId));//收藏的活动数量
    			totalCount.put("followUserCount", collectionService.selectUserCount(userId));//关注的工头数量
    			totalCount.put("jobActivityCount", recruitRecordService.selectByUserId(userId));//报名的活动数量
    			result.push("totalCount", totalCount);
    		}
    		else if(user.getRole() == Constants.Number.TWO_INT)//工头端
    		{

    			totalCount.put("recruitNotAdoptCount", recruitService.selectRecruitCount(userId, Constants.Number.ONE_INT));//招聘未通过的数量
    			totalCount.put("recruitValidateCount", recruitService.selectRecruitCount(userId, Constants.Number.TWO_INT));//招聘审核中的数量
    			totalCount.put("recruitActivityCount", recruitService.selectRecruitStatusCount(userId, Constants.Number.ONE_INT));//招聘进行中的数量
    			totalCount.put("recruitSuccessCount", recruitService.selectRecruitStatusCount(userId, Constants.Number.TWO_INT));//招聘已完成的数量
    			result.push("totalCount", totalCount);
    		}
    		result.push("user", JsonUtil.bean2jsonObject(user));
    		result.setResponse(Constants.TRUE);
    		result.setResponseCode(Constants.SUCCESS_200);
    		result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	}else
    	{
    		result.push("user",null);
    		result.setResponse(Constants.FALSE);
    		result.setResponseCode(Constants.SUCCESS_200);
    		result.setResponseMsg(Constants.Prompt.QUERY_FAILURE);
    	}
    	return result;
    }

    @RequestMapping(value = "/test/send/message.ddxj")
    public ResponseBase testSendMessage(HttpServletRequest request, HttpServletResponse response) throws ClientException, IllegalAccessException
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	redisUtils.set(Constants.VALIDATE_MESSAGE_NOTIC, true);
    	messageService.sendMessage(Constants.Number.ONE_INT,"您有一条新的招聘审核信息，请及时处理。<a href='../recruit/recruit_list.html'>点击此处进入</a>", "您有一条新的招聘审核信息，请及时处理。", Constants.NULL_CHAR);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.SEND_SUCCESSFUL);
    	return result;
    }

    /**
     * 查询报名用户详情
     * @param request
     * @param response
     * @param userId
     * @param recruitId
     * @return
     */
    @RequestMapping(value = "/query/enlist/user/details.ddxj")
    public ResponseBase queryEnlistUserDetails(HttpServletRequest request, HttpServletResponse response,Integer recruitId,Integer userId)
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	RecruitRecord recruitUser = recruitRecordService.queryAllWorkerDeliver(recruitId, userId);
    	Recruit recruit=recruitService.selectByPrimaryKey(recruitId);
    	//查询是否关注过该用户
    	int count = userCollectionService.selectByUserIdAndToUserId(recruit.getUserId(), userId);
    	if(count == Constants.Number.ZERO_INT){
    		//未关注该用户
    		result.push("followStatus", Constants.Number.TWO_INT);
    	}
    	else
    	{
    		//已经关注该用户
    		result.push("followStatus", Constants.Number.ONE_INT);
    	}
    	if(!CmsUtils.isNullOrEmpty(recruitUser))
    	{
    		if(recruitUser.getFlag() == Constants.Number.ONE_INT)
    		{
    			//未取消报名
    			if(recruitUser.getEnlistStatus() == Constants.Number.ONE_INT)//已报名
    			{
    				result.push("enlistStatus", Constants.Number.ONE_INT);//待确认报名
    				result.push("recruitUser", JsonUtil.bean2jsonObject(recruitUser));
    				result.setResponse(Constants.TRUE);
    				result.setResponseCode(Constants.SUCCESS_200);
    				result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    				return result;
    			}
    			if(recruitUser.getEnlistStatus() == Constants.Number.THREE_INT)//报名未通过
    			{
    				result.push("enlistStatus", Constants.Number.TWO_INT);//拒绝报名
    				result.push("recruitUser", JsonUtil.bean2jsonObject(recruitUser));
    				result.setResponse(Constants.TRUE);
    				result.setResponseCode(Constants.SUCCESS_200);
    				result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    				return result;
    			}
    			if(recruitUser.getEnlistStatus() == Constants.Number.TWO_INT)//报名通过
    			{
    				if(recruitUser.getWorkerStatus() == Constants.Number.ZERO_INT && recruitUser.getForemanStatus() == Constants.Number.ZERO_INT)//工人,工头未开始
    				{
    					result.push("enlistStatus", Constants.Number.THREE_INT);//待确认录用
    					result.push("recruitUser", JsonUtil.bean2jsonObject(recruitUser));
    					result.setResponse(Constants.TRUE);
    					result.setResponseCode(Constants.SUCCESS_200);
    					result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    					return result;
    				}
    				if(recruitUser.getWorkerStatus() == Constants.Number.ONE_INT && recruitUser.getForemanStatus() == Constants.Number.ZERO_INT)
    				{
    					//工人点击确认到达工地，工头未确认录用
    					result.push("enlistStatus", Constants.Number.THREE_INT);//待确认录用
    					result.push("recruitUser", JsonUtil.bean2jsonObject(recruitUser));
    					result.setResponse(Constants.TRUE);
    					result.setResponseCode(Constants.SUCCESS_200);
    					result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    					return result;
    				}
    				if(recruitUser.getWorkerStatus() == Constants.Number.FOUR_INT)//未录用
    				{
    					result.push("enlistStatus", Constants.Number.FOUR_INT);//拒绝录用
    					result.push("recruitUser", JsonUtil.bean2jsonObject(recruitUser));
    					result.setResponse(Constants.TRUE);
    					result.setResponseCode(Constants.SUCCESS_200);
    					result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    					return result;
    				}
    				if(recruitUser.getForemanStatus() == Constants.Number.ONE_INT && recruitUser.getWorkerStatus() == Constants.Number.FIVE_INT )//工头已确认,工人已录用
    				{
    					result.push("enlistStatus", Constants.Number.FIVE_INT);//工人未确认
    					result.push("recruitUser", JsonUtil.bean2jsonObject(recruitUser));
    					result.setResponse(Constants.TRUE);
    					result.setResponseCode(Constants.SUCCESS_200);
    					result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    					return result;
    				}
    				if(recruitUser.getForemanStatus() == Constants.Number.TWO_INT && recruitUser.getWorkerStatus() == Constants.Number.TWO_INT)//工人,工头进行中
    				{
    					if(recruitUser.getBalanceStatus() == Constants.Number.ZERO_INT)//未结算
    					{
    						result.push("enlistStatus", Constants.Number.SIX_INT);//待结算工资
    						result.push("recruitUser", JsonUtil.bean2jsonObject(recruitUser));
    						result.setResponse(Constants.TRUE);
    						result.setResponseCode(Constants.SUCCESS_200);
    						result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    						return result;
    					}
    					if(recruitUser.getBalanceStatus() == Constants.Number.ONE_INT)//已结算
    					{
    						result.push("enlistStatus", Constants.Number.SEVEN_INT);//待结束项目
    						result.push("recruitUser", JsonUtil.bean2jsonObject(recruitUser));
    						result.setResponse(Constants.TRUE);
    						result.setResponseCode(Constants.SUCCESS_200);
    						result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    						return result;
    					}
    				}
    				if(recruitUser.getForemanStatus() == Constants.Number.THREE_INT && recruitUser.getWorkerStatus() == Constants.Number.THREE_INT)//工人,工头都结束
    				{
    					if(recruitUser.getForemanCommentStatus() == Constants.Number.ZERO_INT)//工头未评论
    					{
    						result.push("enlistStatus", Constants.Number.EIGHT_INT);//待评价工人
    						result.push("recruitUser", JsonUtil.bean2jsonObject(recruitUser));
    						result.setResponse(Constants.TRUE);
    						result.setResponseCode(Constants.SUCCESS_200);
    						result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    						return result;
    					}
    					if(recruitUser.getForemanCommentStatus() == Constants.Number.ONE_INT)//工头已评论
    					{
    						result.push("enlistStatus", Constants.Number.NINE_INT);//已完成
    						result.push("recruitUser", JsonUtil.bean2jsonObject(recruitUser));
    						result.setResponse(Constants.TRUE);
    						result.setResponseCode(Constants.SUCCESS_200);
    						result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    						return result;
    					}
    				}
    			}
    		}else if(recruitUser.getFlag() == Constants.Number.TWO_INT)
    		{
    			//已取消报名
    			result.push("enlistStatus", Constants.Number.TEN_INT);
				result.push("recruitUser", JsonUtil.bean2jsonObject(recruitUser));
				result.setResponse(Constants.TRUE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
				return result;
    		}
    	}
    	else
    	{
    		result.setResponse(Constants.FALSE);
    		result.setResponseCode(Constants.SUCCESS_200);
    		result.setResponseMsg(Constants.Prompt.QUERY_FAILURE);
    	}
    	return result;
    }

    /**
     * 验证支付密码
     * @param request
     * @param response
     * @param userId
     * @param payPassword
     * @return
     */
    @RequestMapping(value = "/validate/user/paypassword.ddxj")
    public ResponseBase validatePaymentPassword(HttpServletRequest request, HttpServletResponse response,Integer userId,String payPassword)
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	UserPassword passwordByUserId = userService.selectPasswordByUserId(userId);
    	if(!CmsUtils.isNullOrEmpty(passwordByUserId))
    	{
    		if(!CmsUtils.isNullOrEmpty(passwordByUserId.getPayPassword()))
    		{
    			if(passwordByUserId.getPayPassword().equals(payPassword))
    			{
    				result.setResponse(Constants.TRUE);
    				result.setResponseCode(Constants.SUCCESS_200);
    				result.setResponseMsg(Constants.Prompt.VALIDATE_SUCCESSFUL);
    			}
    			else
    			{
    				result.setResponse(Constants.FALSE);
    				result.setResponseCode(Constants.SUCCESS_200);
    				result.setResponseMsg(Constants.Prompt.PAY_PASSWORD_ERROR);
    			}
    		}
    		else
    		{
    			result.setResponse(Constants.FALSE);
    			result.setResponseCode(Constants.SUCCESS_200);
    			result.setResponseMsg(Constants.Prompt.NOT_SETTING_PAY_PASSWORD);
    		}
    	}
    	else
    	{
    		result.setResponse(Constants.FALSE);
    		result.setResponseCode(Constants.SUCCESS_200);
    		result.setResponseMsg(Constants.Prompt.NOT_SETTING_PAY_PASSWORD);
    	}
    	return result;
    }

    /**
     * 查询工头列表
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException
     */
    @RequestMapping(value = "/query/foreman/user/list.ddxj")
    public ResponseBase queryForemanUserList(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo) throws IllegalAccessException
    {
    	ResponseBase result = ResponseBase.getInitResponse();

    	PageHelper.startPage(requestVo.getPageNum(), requestVo.getPageSize());
    	List<User> userList = userService.queryForemanUserList(requestVo);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
		result.push("userList",JsonUtil.list2jsonToArray(userList));
    	return result;
    }
	    /**
	     * 测试验证敏感文字
	    * @Title: MemberController.java
	    * @param @param request
	    * @param @param response
	    * @param @param content
	    * @param @return
	    * @param @throws IOException参数
	    * @return ResponseBase    返回类型
	    * @throws
	    * @Package net.zn.ddxj.api
	    * @author 上海众宁网络科技有限公司-何俊辉
	    * @date 2018年5月12日
	    * @version V1.0
	     */
	    @RequestMapping(value = "/validate/sensitive/keyword.ddxj")
	    public ResponseBase addSensitiveKeyword(HttpServletRequest request, HttpServletResponse response,String content) throws IOException{
	    	ResponseBase result = ResponseBase.getInitResponse();
	    	Set<String> sensitiveWord = validateKeywordsService.validateKeywords(content);
	    	if(!CmsUtils.isNullOrEmpty(sensitiveWord))
	    	{
	    		result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.TEXT_ILLEGALITY+ sensitiveWord);
				return result;
	    	}
	    	else
	    	{
	    		result.setResponse(Constants.TRUE);
	    		result.setResponseCode(Constants.SUCCESS_200);
	    		result.setResponseMsg(Constants.Prompt.VALIDATE_SUCCESSFUL);
	    	}
	    	return result;
	    }
	    /**
	     * 上传图片
	    * @Title: MemberController.java
	    * @param @param request
	    * @param @param response
	    * @param @param base64
	    * @param @return
	    * @param @throws QiniuException
	    * @param @throws IOException参数
	    * @return ResponseBase    返回类型
	    * @throws
	    * @Package net.zn.ddxj.api
	    * @author 上海众宁网络科技有限公司-何俊辉
	    * @date 2018年5月18日
	    * @version V1.0
	     */
	    @RequestMapping(value = "/file/images/upload.ddxj", method = RequestMethod.POST)
	    public ResponseBase uploadWeixinFile(HttpServletRequest request, HttpServletResponse response,String base64) throws QiniuException, IOException
	    {
	    	ResponseBase result = ResponseBase.getInitResponse();
	    	if(CmsUtils.isNullOrEmpty(base64))
	    	{
	    		result.setResponse(Constants.FALSE);
	    		result.setResponseCode(Constants.SUCCESS_200);
	    		result.setResponseMsg(Constants.Prompt.IMAGE_NOT_NULL);
	    		return result;
	    	}
	    	if(CmsUtils.imageSize(base64) / 1024 > 5120)//文件不能大于5M
	        {
	    		result.setResponse(Constants.FALSE);
	    		result.setResponseCode(Constants.SUCCESS_200);
	    		result.setResponseMsg(Constants.Prompt.IMAGE_SIZE_NOT_EXCEED_5M);
	    		return result;
	        }
	        // 生成文件名 每个月用一个文件夹
	        Calendar cal = Calendar.getInstance();
	        String fileName = CmsUtils.generateStr(32);

	        String filePath = Constants.UPLOAD_IMAGE_PATH + cal.get(Calendar.YEAR) + Constants.SPT + (cal.get(Calendar.MONTH) + Constants.Number.ONE_INT) + Constants.SPT +(cal.get(Calendar.DATE) + Constants.Number.ONE_INT) + Constants.SPT + fileName;
	        // 上传文件
        	QiNiuUploadManager.uploadFile(filePath, CmsUtils.getImageIo(base64));
	        // 鉴黄
	        if(!QiNiuUploadManager.checkImage(filePath))
			{
	        	result.setResponse(Constants.FALSE);
	    		result.setResponseCode(Constants.SUCCESS_200);
	    		result.setResponseMsg(Constants.Prompt.IMAGE_ILLEGALITY);
	    		return result;
			}
        	result.push("url", PropertiesUtils.getPropertiesByName("static_url")+filePath);
	    	result.setResponse(Constants.TRUE);
    		result.setResponseCode(Constants.SUCCESS_200);
    		result.setResponseMsg(Constants.Prompt.UOLOAD_SUCCESSFUL);
    		return result;
	    }
	    /**
	     * 退出登录
	     * @param request
	     * @param response
	     * @param userId
	     * @return
	     */
	    @RequestMapping(value="/member/logout.ddxj")
	    public ResponseBase logoutSystem(HttpServletRequest request, HttpServletResponse response,Integer userId){
	    	ResponseBase result=ResponseBase.getInitResponse();
	    	User userInfo=userService.queryUserDetail(userId);
	    	if(!CmsUtils.isNullOrEmpty(userInfo))
	    	{
	    		userService.updateOutLogin(userId);
	    		redisUtils.remove(Constants.LOGIN_USER_INFO + userInfo.getPhone());
	    	}
	    	result.setResponse(Constants.TRUE);
    		result.setResponseCode(Constants.SUCCESS_200);
    		result.setResponseMsg(Constants.Prompt.VALIDATE_SUCCESSFUL);
	    	return result;
	    }
	    /**
	     * 推送开关
	     * @param request
	     * @param response
	     * @param userId
	     * @param appUserToken
	     * @param switchStatus
	     * @return
	     */
	    @RequestMapping(value="/update/push/appUsertoken.ddxj")
	    public ResponseBase updatePushAppUserToken(HttpServletRequest request, HttpServletResponse response,Integer userId,String appUserToken,Integer switchStatus){
	    	ResponseBase result=ResponseBase.getInitResponse();
	    	User userInfo=userService.queryUserDetail(userId);
	    	if(!CmsUtils.isNullOrEmpty(userInfo))
	    	{
	    		if(!CmsUtils.isNullOrEmpty(switchStatus))
		    	{
		    		if(switchStatus == Constants.Number.ONE_INT)//打开
		    		{
		    			asycService.updateUserAppUserToken(userInfo.getPhone(), appUserToken);//更新用户TOKEN
		    		}
		    		else if(switchStatus == Constants.Number.TWO_INT)//关闭
		    		{
		    			asycService.updateUserAppUserToken(userInfo.getPhone(), Constants.NULL_CHAR);//更新用户TOKEN
		    		}
		    	}
	    	}
	    	result.setResponse(Constants.TRUE);
    		result.setResponseCode(Constants.SUCCESS_200);
    		result.setResponseMsg(Constants.Prompt.VALIDATE_SUCCESSFUL);
	    	return result;
	    }
	    /**
	     * 查询用户余额
	     * @param request
	     * @param response
	     * @param userId
	     * @param recruitId
	     * @return
	     */
	    @RequestMapping(value="/query/user/money.ddxj")
	    public ResponseBase queryUserMoney(HttpServletRequest request, HttpServletResponse response,Integer userId,Integer recruitId){
	    	ResponseBase result=ResponseBase.getInitResponse();
	    	BigDecimal userMoney=userService.queryUserDetail(userId).getRemainderMoney();
	    	RecruitCredit queryRecruitCredit = recruitCreditService.queryRecruitCredit(userId, recruitId);
	    	BigDecimal creditMoney= new BigDecimal("0.00");
	    	if(!CmsUtils.isNullOrEmpty(queryRecruitCredit))
	    	{
	    		if(queryRecruitCredit.getCreditStatus() == Constants.Number.TWO_INT)
	    		{
	    			creditMoney=recruitCreditService.queryRecruitCredit(userId, recruitId).getUsableMoney();
	    			result.push("creditStatus", 2);

	    		}
	    		else if(queryRecruitCredit.getCreditStatus()==Constants.Number.ONE_INT)
	    		{
	    			result.push("creditStatus", Constants.Number.ONE_INT);
	    		}
	    		else if(queryRecruitCredit.getCreditStatus()==Constants.Number.THREE_INT)
	    		{
	    			result.push("creditStatus", Constants.Number.THREE_INT);
	    		}
	    		else
	    		{
	    			result.push("creditStatus", Constants.Number.ZERO_INT);
	    		}

	    	}
	    	else
	    	{
	    		result.push("creditStatus", Constants.Number.ZERO_INT);
	    	}

	    	result.setResponse(Constants.TRUE);
    		result.setResponseCode(Constants.SUCCESS_200);
    		result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    		result.push("userMoney",userMoney);
    		result.push("creditMoney", creditMoney);
	    	return result;
	    }
	    /**
		 * app 版本查询
		 * @param request
		 * @param response
		 * @param model
		 * @param endUserPassword
		 */
		@RequestMapping(value="/check/version.ddxj")
		public ResponseBase selVersion(HttpServletRequest request,HttpServletResponse response,String currentVersion,String mobileSource,String phone,String appUserToken)
		{
			ResponseBase result=ResponseBase.getInitResponse();
			Version version = userService.queryLastVersion(mobileSource);//最后一次版本号，已经发布过的
			Version notExamineMaxVersion = userService.queryNotExamineMaxVersion(mobileSource);//最后一次版本号
			if(!CmsUtils.isNullOrEmpty(phone) && !CmsUtils.isNullOrEmpty(appUserToken)){
				asycService.updateUserAppUserToken(phone, appUserToken);//更新用户TOKEN
				asycService.updateUserInfoAsync(phone,CmsUtils.getIpAddr(request),mobileSource);//线程启动-更新用户登录信息
			}
			if(!CmsUtils.isNullOrEmpty(version))
			{
				int versionStatus = version.getMaxVersion().compareTo(currentVersion);
				if(!CmsUtils.isNullOrEmpty(notExamineMaxVersion))//说明有新版本更新，但是未审核通过
				{
					int notExamineMaxVersionStatus = notExamineMaxVersion.getMaxVersion().compareTo(currentVersion);
					if(notExamineMaxVersionStatus  == Constants.Number.ZERO_INT)//系统正在审核中
					{
						result.push("examineStatus",Constants.N);//examineStatus 审核已通过-Y 审核未通过-N
						if(versionStatus > Constants.Number.ZERO_INT)
						{
							result.push("isDownLoad",Constants.Y);//isDownLoad 更新-Y 不更新-N
						}
						else
						{
							result.push("isDownLoad",Constants.N);//isDownLoad 更新-Y 不更新-N
						}
						result.push("dowloadAddress", notExamineMaxVersion.getDowloadAddress());//下载地址
						result.push("forceUpdate", notExamineMaxVersion.getForceUpdate());//是否强制更新（Y-强制更新,N-不强制更新）
						result.push("updateContent", notExamineMaxVersion.getUpdateContent());// 更新内容
					}
					else
					{
						result.push("isDownLoad",Constants.N);//isDownLoad 更新-Y 不更新-N
						if(notExamineMaxVersionStatus < Constants.Number.ZERO_INT)
						{
							result.push("examineStatus",Constants.N);//examineStatus 审核已通过-Y 审核未通过-N
						}
						else
						{
							result.push("examineStatus",Constants.Y);//examineStatus 审核已通过-Y 审核未通过-N
						}
						result.push("dowloadAddress", version.getDowloadAddress());//下载地址
						result.push("forceUpdate", version.getForceUpdate());//是否强制更新（Y-强制更新,N-不强制更新）
						result.push("updateContent", version.getUpdateContent());// 更新内容
					}

				}
				else // 数据库中没有查询到正在审核的数据
				{
					if(versionStatus > Constants.Number.ZERO_INT)
					{
						result.push("isDownLoad",Constants.Y);//isDownLoad 更新-Y 不更新-N
					}
					else
					{
						result.push("isDownLoad",Constants.N);//isDownLoad 更新-Y 不更新-N
					}
					result.push("examineStatus",Constants.Y);//examineStatus 审核已通过-Y 审核未通过-N
					result.push("dowloadAddress", version.getDowloadAddress());//下载地址
					result.push("forceUpdate", version.getForceUpdate());//是否强制更新（Y-强制更新,N-不强制更新）
					result.push("updateContent", version.getUpdateContent());// 更新内容
				}
				result.setResponse(Constants.TRUE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
			}
			else
			{
				result.push("isDownLoad",Constants.N);
				result.push("dowloadAddress",null);//下载地址
				result.push("forceUpdate",null);//是否强制更新（Y-强制更新,N-不强制更新）
				result.push("updateContent",null);// 更新内容
				result.push("examineStatus",null);//examineStatus 审核已通过-Y 审核未通过-N
				result.setResponse(Constants.TRUE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.Prompt.NOT_VERSION_UPDATE);
			}
	    	return result;
		}
		/**
		 * 获取语音验证码
		 * @param request
		 * @param responseR
		 * @param phone
		 * @return
		 * @throws ClientException
		 */
		 @RequestMapping(value = "/member/getVoiceCode.ddxj")
		 public ResponseBase getVoiceCode(HttpServletRequest request, HttpServletResponse response,String phone ,Integer type) throws ClientException {
			 ResponseBase result=ResponseBase.getInitResponse();
			 User user=userService.SelectUser(phone);
			 SingleCallByTtsResponse singleCallByTtsResponse=null;
			 if(type==Constants.Number.ONE_INT)//1.注册验证码
			 {
				 String code = RandomCode.getCode();
				 singleCallByTtsResponse = VmsUtil.singleCallByTts(phone, code,type,!CmsUtils.isNullOrEmpty(user) ? Constants.Number.ONE_INT: 2);
				 redisUtils.set(Constants.LOGIN_IDEN_CODE + phone, code, Constants.Number.THIRTY_SECOND);// 验证码30分钟有效时间
			 }
			 else if(type==Constants.Number.TWO_INT)//2.修改密码
			 {
				 if(CmsUtils.isNullOrEmpty(user))
				{
					result.setResponse(Constants.FALSE);
					result.setResponseCode(Constants.SUCCESS_200);
					result.setResponseMsg(Constants.Prompt.NOT_USER);
					return result;
				}
				 String code = RandomCode.getCode();
				 singleCallByTtsResponse = VmsUtil.singleCallByTts(phone, code,type,!CmsUtils.isNullOrEmpty(user) ? Constants.Number.ONE_INT: 2);
				 redisUtils.set(Constants.CHANGE_PASS_CODE + phone, code, Constants.Number.THIRTY_SECOND);// 验证码30分钟有效时间
			 }
			 else if(type==Constants.Number.THREE_INT)//3.修改支付密码
			 {
				 String code = RandomCode.getCode();
				 singleCallByTtsResponse = VmsUtil.singleCallByTts(phone, code,type,!CmsUtils.isNullOrEmpty(user) ? Constants.Number.ONE_INT: 2);
				 redisUtils.set(Constants.CHANGE_PAY_CODE + phone, code, Constants.Number.THIRTY_SECOND);// 验证码30分钟有效时间
			 }
			 else if(type==Constants.Number.FOUR_INT)//4.绑定银行卡
			 {
				 String code = RandomCode.getCode();
				 singleCallByTtsResponse = VmsUtil.singleCallByTts(phone, code,type,!CmsUtils.isNullOrEmpty(user) ? Constants.Number.ONE_INT: 2);
				 redisUtils.set(Constants.BOND_BANK_CARD_CODE + phone, code, Constants.Number.THIRTY_SECOND);// 验证码30分钟有效时间
			 }
			 else if(type==Constants.Number.FIVE_INT)//5.授信验证码
			 {
				 String code = RandomCode.getCode();
				 singleCallByTtsResponse = VmsUtil.singleCallByTts(phone, code,type,!CmsUtils.isNullOrEmpty(user) ? Constants.Number.ONE_INT: 2);
				 redisUtils.set(Constants.CREDIT_VERIFY_CODE + phone, code, Constants.Number.THIRTY_SECOND);// 验证码30分钟有效时间
			 }
			 else if(type==Constants.Number.SIX_INT)//6.更改手机号码
			 {
				if(!CmsUtils.isNullOrEmpty(user)){
					result.setResponse(Constants.FALSE);
					result.setResponseCode(Constants.SUCCESS_200);
					result.setResponseMsg(Constants.Prompt.PHONE_EXISTENCE);
					return result;
				}
				String code = RandomCode.getCode();
				singleCallByTtsResponse = VmsUtil.singleCallByTts(phone, code,type,!CmsUtils.isNullOrEmpty(user) ? Constants.Number.ONE_INT: 2);
				redisUtils.set(Constants.CHANGE_PHONE_CODE + phone, code, Constants.Number.THIRTY_SECOND);// 验证码30分钟有效时间
			 }
			 else if(type==Constants.Number.SEVEN_INT)//忘记密码
			 {
				if(CmsUtils.isNullOrEmpty(user))
				{
					result.setResponse(Constants.FALSE);
					result.setResponseCode(Constants.SUCCESS_200);
					result.setResponseMsg(Constants.Prompt.NOT_USER);
					return result;
				}
				String code = RandomCode.getCode();
				singleCallByTtsResponse = VmsUtil.singleCallByTts(phone, code,type,!CmsUtils.isNullOrEmpty(user) ? Constants.Number.ONE_INT: 2);
				redisUtils.set(Constants.FORGET_PASSWORD + phone, code, Constants.Number.THIRTY_SECOND);// 验证码30分钟有效时间
			 }
			 if(Constants.OK.equals(singleCallByTtsResponse.getCode()))
			 {
				 result.setResponse(Constants.TRUE);
	    		 result.setResponseCode(Constants.SUCCESS_200);
	    		 result.setResponseMsg(Constants.Prompt.SEND_SUCCESSFUL);
			 }
			 else
			 {
				 result.setResponse(Constants.FALSE);
	    		 result.setResponseCode(Constants.SUCCESS_200);
	    		 result.setResponseMsg(Constants.Prompt.SEND_FAILURE);
			 }
			return result;

		 }

	/**
	 * 查询邀请记录
	 * @param userId
	 * @return
	 * @throws IllegalAccessException
	 */
	@RequestMapping("/user/invite/record.ddxj")
	public ResponseBase queryInvitaRecord(HttpServletRequest request, HttpServletResponse response,Integer userId) throws IllegalAccessException{
		ResponseBase result=ResponseBase.getInitResponse();
		List<InviteRecord> list = inviteRecordService.queryInviteRecord(userId);
		BigDecimal totalMoney = new BigDecimal(0.00);
		result.push("recordList", JsonUtil.list2jsonToArray(list));
		if(!CmsUtils.isNullOrEmpty(list))
		{
			for (InviteRecord inviteRecord : list) {
				totalMoney = totalMoney.add(inviteRecord.getInviterBonus());
			}
			result.push("totalMoney", totalMoney);
			result.push("successCount", list.size());
		}else
		{
			result.push("totalMoney", Constants.Number.ZERO_INT);
			result.push("successCount", Constants.Number.ZERO_INT);
		}
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.SUCCESS);
		return result;
	}
	@RequestMapping("/query/extend/user/info.ddxj")
	public ResponseBase queryExtendUserInfo(HttpServletRequest request, HttpServletResponse response,String currentTime,String endTime,Integer extendCode,Integer pageNum,
			Integer pageSize){
		ResponseBase result=ResponseBase.getInitResponse();
		PageHelper.startPage(pageNum, pageSize);
		List<User> list = userService.queryExtendUserInfo(currentTime, extendCode, endTime);
		long currentCount = new PageInfo<User>(list).getTotal();
		result.push("todayCount", userService.queryTodayUserCount(2));
		result.push("totalCount", userService.queryTodayUserCount(1));
		result.push("monthCount", userService.queryTodayUserCount(3));
		result.push("userList", JsonUtil.list2jsonToArray(list));
		result.push("currentCount", currentCount);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.SUCCESS);
		return result;
	}

	/**
     * 分销设置-查询
     *
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException
     */
    @RequestMapping(value="/invite/record/setting/find.ddxj")
    public ResponseBase findInviteRecordSetting(HttpServletRequest request, HttpServletResponse response)
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	InviteSetting wxInfo = inviteSettingService.getInviteInfo(Constants.INVITE_WECHAT_KEY);
    	InviteSetting frdInfo = inviteSettingService.getInviteInfo(Constants.INVITE_FRIEND_KEY);
    	result.push("wxInfo", wxInfo);
    	result.push("frdInfo", frdInfo);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	return result;
    }
	/**
	 * 将数据添加到缓存
	 * @param request
	 * @param response
	 * @param key
	 * @param value
	 * @param group
	 * @param time
	 * @return
	 */
    @RequestMapping(value="/add/redis.ddxj")
    public ResponseBase addRedis(HttpServletRequest request, HttpServletResponse response,String key,String value,String group,String time)
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	if(!CmsUtils.isNullOrEmpty(group))
		{
    		key = group + ":"+ key;
		}
    	if(!CmsUtils.isNullOrEmpty(time))
    	{
    		redisUtils.set(key, value,Long.valueOf(time));
    	}
    	else
    	{
    		redisUtils.set(key, value);
    	}
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.ADD_SUCCESSFUL);
    	log.info("###"+key+"成功入库###");
    	return result;
    }
    /**
     * 从缓存中移除
     * @param request
     * @param response
     * @param key
     * @param group
     * @return
     */
    @RequestMapping(value="/remove/redis.ddxj")
    public ResponseBase removeRedis(HttpServletRequest request, HttpServletResponse response,String key,String group)
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	if(!CmsUtils.isNullOrEmpty(group))
		{
    		key = group + ":"+ key;
		}
		redisUtils.remove(key);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
    	log.info("###"+key+"成功删除###");
    	return result;
    }
	/**
	 * 获取redis中的数据
	 * @param request
	 * @param response
	 * @param key
	 * @param group
	 * @return
	 */
    @RequestMapping(value="/get/redis.ddxj")
    public ResponseBase getRedis(HttpServletRequest request, HttpServletResponse response,String key,String group)
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	String oldKey = key;
    	if(!CmsUtils.isNullOrEmpty(group))
		{
    		oldKey = group + ":"+ key;
		}
		result.push(key, String.valueOf(redisUtils.get(oldKey)));
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	return result;
    }

    /**
     * 更新消息中心状态
     * @param request
     * @param response
     * @param messageId
     * @param userId
     * @return
     */
    @RequestMapping(value="/messagecenter/update.ddxj")
    public ResponseBase updateMessageCenter(HttpServletRequest request, HttpServletResponse response,Integer messageId,Integer userId)
    {
    	ResponseBase result = ResponseBase.getInitResponse();
    	int messageCount = userCenterService.updateMessageStuatus(messageId, userId);
    	List<Map<String, Object>> selectIsRead = userCenterService.selectIsRead(userId, 1);
    	if(!CmsUtils.isNullOrEmpty(selectIsRead))
		{
			for (Map<String, Object> map : selectIsRead)
			{

				Integer type = (Integer) map.get("type");
				Long count = (Long) map.get("count");
				if(type ==Constants.Number.ONE_INT)
				{
					if(count != Constants.Number.ZERO_INT)
					{
						result.push("informIsRead", Constants.Number.ONE_INT);
					}
					else if(count == Constants.Number.ZERO_INT)
					{
						result.push("informIsRead", Constants.Number.TWO_INT);
					}
				}
				else if(type ==Constants.Number.TWO_INT)
				{
					if(count != Constants.Number.ZERO_INT)
					{
						result.push("activityIsRead", Constants.Number.ONE_INT);
					}
					else if(count == Constants.Number.ZERO_INT)
					{
						result.push("activityIsRead", Constants.Number.TWO_INT);
					}

				}else if(type ==Constants.Number.THREE_INT)
				{
					if(count != Constants.Number.ZERO_INT)
					{
						result.push("evaluateIsRead", Constants.Number.ONE_INT);
					}
					else if(count == Constants.Number.ZERO_INT)
					{
						result.push("evaluateIsRead", Constants.Number.TWO_INT);
					}

				}else if(type == Constants.Number.FOUR_INT)
				{
					if(count != Constants.Number.ZERO_INT)
					{
						result.push("transactionIsRead", Constants.Number.ONE_INT);
					}
					else if(count == Constants.Number.ZERO_INT)
					{
						result.push("transactionIsRead", Constants.Number.TWO_INT);
					}

				}
			}
		}else
		{
			result.push("informIsRead", Constants.Number.TWO_INT);
			result.push("activityIsRead", Constants.Number.TWO_INT);
			result.push("evaluateIsRead", Constants.Number.TWO_INT);
			result.push("transactionIsRead", Constants.Number.TWO_INT);
		}
    	if(messageCount > 0)
    	{
    		result.setResponse(Constants.TRUE);
        	result.setResponseCode(Constants.SUCCESS_200);
        	result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
        	return result;
    	}else
    	{
    		result.setResponse(Constants.FALSE);
    		result.setResponseCode(Constants.SUCCESS_200);
    		result.setResponseMsg(Constants.Prompt.UPDATE_FAILURE);
    		return result;
    	}
    }
 /*
    //**
     * 查询消息中心列表
     * @param pageSize
     * @param pageNum
     * @return
     *//*
    @RequestMapping("/user/query/messages.ddxj")
    public ResponseBase queryMessageList(Integer userId,Integer messageTypeId, Integer pageSize,Integer pageNum){
    	ResponseBase result = ResponseBase.getInitResponse();
    	PageHelper.startPage(pageNum, pageSize);
    	List<MessageCenter> list = userCenterService.queryMessageList(userId,messageTypeId);
		result.push("messageList", JsonUtil.list2jsonToArray(list));
		result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
		return result;
    }

    //**
     * 查询
     * @return
     *//*
    @RequestMapping("/user/query/isread.ddxj")
    public ResponseBase queryMessageStatus(Integer userId){
    	ResponseBase result = ResponseBase.getInitResponse();
    	int count = userCenterService.queryIsRead(userId);
    	result.push("count", count);
		result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	return result;
    }

    @RequestMapping("/user/update/message/status.ddxj")
    public ResponseBase updateMessageStuatus(Integer MessageId,Integer userId){
    	ResponseBase result = ResponseBase.getInitResponse();

    	userCenterService.updateMessageStuatus(MessageId, userId);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
    	return result;
    }*/
    /**
     * 查询消息中心
     * @param request
     * @param response
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value="/query/messagecenter/list.ddxj")
    public ResponseBase queryMessageCenter(HttpServletRequest request, HttpServletResponse response,int userId,int typeId,int pageNum,int pageSize){
		ResponseBase result=ResponseBase.getInitResponse();
		PageHelper.startPage(pageNum, pageSize);
		List<MessageCenter> messageCenterList = userCenterService.queryMessageCenter(userId,typeId);
		List<Map<String, Object>> selectIsRead = userCenterService.selectIsRead(userId, 1);
		if(!CmsUtils.isNullOrEmpty(selectIsRead))
		{
			for (Map<String, Object> map : selectIsRead)
			{

				Integer type = (Integer) map.get("type");
				Long count = (Long) map.get("count");
				if(type == Constants.Number.ONE_INT)
				{
					if(count != Constants.Number.ZERO_INT)
					{
						result.push("informIsRead", Constants.Number.ONE_INT);
					}
					else if(count == Constants.Number.ZERO_INT)
					{
						result.push("informIsRead", Constants.Number.TWO_INT);
					}
				}
				else if(type == Constants.Number.TWO_INT)
				{
					if(count != Constants.Number.ZERO_INT)
					{
						result.push("activityIsRead", Constants.Number.ONE_INT);
					}
					else if(count == Constants.Number.ZERO_INT)
					{
						result.push("activityIsRead", Constants.Number.TWO_INT);
					}

				}else if(type == Constants.Number.THREE_INT)
				{
					if(count != Constants.Number.ZERO_INT)
					{
						result.push("evaluateIsRead", Constants.Number.ONE_INT);
					}
					else if(count == Constants.Number.ZERO_INT)
					{
						result.push("evaluateIsRead", Constants.Number.TWO_INT);
					}

				}else if(type == Constants.Number.FOUR_INT)
				{
					if(count != Constants.Number.ZERO_INT)
					{
						result.push("transactionIsRead", Constants.Number.ONE_INT);
					}
					else if(count == Constants.Number.ZERO_INT)
					{
						result.push("transactionIsRead", Constants.Number.TWO_INT);
					}

				}
			}
		}else
		{
			result.push("informIsRead", Constants.Number.TWO_INT);
			result.push("activityIsRead", Constants.Number.TWO_INT);
			result.push("evaluateIsRead", Constants.Number.TWO_INT);
			result.push("transactionIsRead", Constants.Number.TWO_INT);
		}
		result.push("messageCenterList", JsonUtil.list2jsonToString(messageCenterList));
		result.setResponse(Constants.TRUE);
     	result.setResponseCode(Constants.SUCCESS_200);
     	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	return result;

    }
    /**
     * 查询最新消息中心
     * @param request
     * @param response
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value="/query/messagecenter/detail.ddxj")
    public ResponseBase queryMessageCenterDetail(HttpServletRequest request, HttpServletResponse response,Integer userId){
		ResponseBase result=ResponseBase.getInitResponse();
		User user = userService.queryUserDetail(userId);
		if(!CmsUtils.isNullOrEmpty(user))
		{
			//查询推送标识
        	if(!CmsUtils.isNullOrEmpty(user.getAppUserToken()))
        	{
        		result.push("pushIsOpen", 1);
        	}else
        	{
        		result.push("pushIsOpen", 2);
        	}
		}else
		{
			result.push("pushIsOpen", 2);
		}
		Integer typeId = Constants.Number.TWO_INT;//项目类型
		Integer isRead = Constants.Number.ONE_INT;//未读
		MessageCenter messageCenter = userCenterService.queryNewMessageCenter(userId, typeId);
		if(!CmsUtils.isNullOrEmpty(messageCenter))
		{
			if(!CmsUtils.isNullOrEmpty(messageCenter.getMessageTypeId()) && messageCenter.getMessageTypeId() != 0 && messageCenter.getIsRead() == isRead)
			{
				Integer messageTypeId = messageCenter.getTypeId();
//				if(messageTypeId == 6)
//				{
//					4_工头_项目_报名成功_查看详情
//					5_工头_项目_工人已到达工地_查看详情
//					22_工头_项目_工人已开工_查看详情
//					6_工头_项目_评价工人_查看详情
//					result.push("messageCenter", JsonUtil.bean2jsonToString(messageCenter));
//				}else 
				if(messageTypeId == 16 || messageTypeId == 17 || messageTypeId == 18 || messageTypeId == 19 || messageTypeId == 23 || messageTypeId == 24)
				{
					//16_工人_活动_报名提醒_查看详情
					//17_工人_活动_到达提醒_查看详情
					//18_工人_活动_开工提醒_查看详情
					//19_工人_活动_开工提示_查看详情
					//23_工人_项目_最新招聘信息_查看详情
					//24_工人_项目_评价工头_查看详情
					result.push("messageCenter", JsonUtil.bean2jsonToString(messageCenter));
				}
				else
				{
					result.push("messageCenter", null);
				}
			}
			else
			{
				result.push("messageCenter", null);
			}
		}
		else
		{
			result.push("messageCenter", null);
		}
		result.setResponse(Constants.TRUE);
     	result.setResponseCode(Constants.SUCCESS_200);
     	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	return result;

    }
    /**
     * @Title: queryIndexBanner
     * @Description: TODO(查询闪屏广告图片)
     * @param: @param request
     * @param: @param response
     * @param: @return
     * @return: ResponseBase
     * @throws
     */
    @RequestMapping(value="/query/screen_advert.ddxj")
    public ResponseBase queryIndexBanner(HttpServletRequest request, HttpServletResponse response,String pushPlatform){
    	ResponseBase result=ResponseBase.getInitResponse();
    	ScreenAdvert screenAdvert = screenAdvertService.queryScreenAdvert(pushPlatform);
    	if(!CmsUtils.isNullOrEmpty(screenAdvert))
    	{
    		result.setResponse(Constants.TRUE);
        	result.setResponseCode(Constants.SUCCESS_200);
        	result.push("screenAdvert", JsonUtil.bean2jsonObject(screenAdvert));
        	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	}
    	else
    	{
    		result.setResponse(Constants.TRUE);
        	result.setResponseCode(Constants.SUCCESS_200);
        	result.push("screenAdvert", JsonUtil.bean2jsonObject(screenAdvert));
        	result.setResponseMsg(Constants.Prompt.QUERY_FAILURE);
    	}

    	return result;

    }
    /**
     * @Title: userLogin
     * @Description: TODO(用户登陆)
     * @param: @param user
     * @param: @param result
     * @param: @param ipAddress
     * @param: @param loginChannel
     * @param: @return
     * @return: ResponseBase
     * @throws
     */
    private ResponseBase userLogin(User user,ResponseBase result,String ipAddress,String loginChannel)
    {
		if(!CmsUtils.isNullOrEmpty(user.getRole()))
		{
			if(user.getRole()==Constants.Number.ONE_INT ||user.getRole()==Constants.Number.TWO_INT)
			{
				result.setResponse(Constants.TRUE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.SUCCESS);
				result.push("loginType",Constants.Number.ONE_INT);
				result.push("user", JsonUtil.bean2jsonObject(user));
			}
			else if(!(user.getRole()==Constants.Number.ONE_INT ||user.getRole()==Constants.Number.TWO_INT))
			{
				result.setResponse(Constants.TRUE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg(Constants.SUCCESS);
				result.push("loginType",Constants.Number.TWO_INT);
				result.push("user", JsonUtil.bean2jsonObject(user));
			}
		}
		else
		{
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.SUCCESS);
			result.push("loginType",Constants.Number.TWO_INT);
			result.push("user", JsonUtil.bean2jsonObject(user));
		}
		redisUtils.set(Constants.LOGIN_USER_INFO + user.getPhone(), JsonUtil.bean2jsonToString(user), Constants.Number.MONTH_SECOND);//用户信息1个月有效时间
		asycService.updateUserInfoAsync(user.getPhone(),ipAddress,loginChannel);//线程启动-更新用户登录信息
		return result;
    }
    /**
     * @Title: addLoginInfo
     * @Description: TODO(登陆添加默认用户信息)
     * @param: @param result
     * @param: @param user
     * @param: @param phone
     * @param: @param appUserToken
     * @param: @param loginChannel
     * @param: @param ipAddress
     * @param: @return
     * @return: ResponseBase
     * @throws
     */
    private ResponseBase addLoginInfo(ResponseBase result,User user,String phone,String appUserToken,String loginChannel,String ipAddress)
    {
		user = new User();
		user.setPhone(phone);
		user.setLastIp(ipAddress);
		user.setCurrentIp(ipAddress);
		user.setLastTime(new Date());
		user.setBirthDate(new Date());
		user.setAge(CmsUtils.getAgeByBirth(user.getBirthDate()));
		user.setCurrentDateTime(new Date());
		user.setCreateTime(new Date());
		user.setUpdateTime(new Date());
		user.setAppUserToken(appUserToken);
		user.setCurrentDevice(loginChannel);
		user.setLastDevice(loginChannel);
		userService.insertSelective(user);
		//创建环信账号
		insertEasemobIMUsers(phone);
		User user1 = userService.SelectUser(phone);
		result.push("user",JsonUtil.bean2jsonObject(user1));
		redisUtils.set(Constants.LOGIN_USER_INFO + phone, JsonUtil.bean2jsonToString(userService.SelectUser(phone)), Constants.Number.MONTH_SECOND);// 验证码1个月有效时间
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.push("loginType",Constants.Number.TWO_INT);
		result.setResponseMsg(Constants.Prompt.REGISTER_SUCCESSFUL);
		return result;

    }
    /**
     * @Title: changeUserToken
     * @Description: TODO(改变用户TOKEN、绑定微信授权信息)
     * @param: @param loginChannel
     * @param: @param phone
     * @param: @param appUserToken
     * @param: @param wechatInfo
     * @param: @param loginType
     * @return: void
     * @throws
     */
    private void changeUserToken(String loginChannel,String phone,String appUserToken,String wechatInfo,Integer loginType)
    {
    	if(!CmsUtils.isNullOrEmpty(loginChannel) && !Constants.JSAPI.equals(loginChannel))//APP登录
		{
			asycService.updateUserAppUserToken(phone, appUserToken);//更新用户TOKEN
		}
		else
		{
			if(!CmsUtils.isNullOrEmpty(wechatInfo))//获取微信用户授权
			{
				if(!CmsUtils.isNullOrEmpty(loginType) && loginType != Constants.Number.THREE_INT)//不等于微信登陆，如果是密码或者验证码登陆，将微信的信息进行更换
				{
					asycService.changeUserWechatInfo(phone, wechatInfo);//用OPENID去查询用户信息如果查出来的openId与当前手机号不匹配，则把之前的账号的微信信息删除，重新更新到新的的账户中
				}
				asycService.updateUserWechatInfo(phone, wechatInfo,loginChannel);
			}
		}
		if(!CmsUtils.isNullOrEmpty(loginType) && loginType != Constants.Number.THREE_INT)//不等于微信登陆，如果是密码或者验证码登陆，将微信的信息进行更换
		{
			asycService.updateUserWechatInfo(phone, wechatInfo,loginChannel);
		}
    }

    /**
     * @Title: insertEasemobIMUsers
     * @Description: TODO(更改信息后同步修改环信推送昵称)
     * @param: String realName
     * @param: String phone
     * @throws
     */
    private void updateEasemobNickName(String realName,int userId)
    {
    	EasemobUser user = userService.queryEasemobUserByPhone(userId);
    	if(user != null)
    	{
    		user.setNickName(realName);
    		user.setUpdateTime(new Date());
    		userService.updateEasemobUser(user);
    	}

    	EasemobIMUsers api = new EasemobIMUsers();
		String token = userService.queryEasemobToken();
		Nickname name = new Nickname().nickname(realName);
		api.modifyIMUserNickNameWithAdminToken(String.valueOf(userId), name, token);
    }

    /**
     * @Title: insertEasemobIMUsers
     * @Description: TODO(登陆时判断是否有环信账号并创建)
     * @param: String phone
     * @throws
     */
    private void insertEasemobIMUsers(String phone)
	{
		User user = userService.SelectUser(phone);
		if(!CmsUtils.isNullOrEmpty(user))//当用户存在时
		{
			EasemobIMUsers api = new EasemobIMUsers();
			Object userNode = api.getIMUserByUserName(String.valueOf(user.getId()));//检查用户是否存在
			if(CmsUtils.isNullOrEmpty(userNode))//null表示用户未注册
			{
				String token = userService.queryEasemobToken();
				try
				{
					List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
					Map<String,Object> data = new HashMap<String, Object>();
					data.put("username",user.getId());
					data.put("password",user.getId());
					if(!CmsUtils.isNullOrEmpty(user.getRealName()))
					{
						data.put("nickname",user.getRealName());
					}
					else if(!CmsUtils.isNullOrEmpty(user.getUserName()))
					{
						data.put("nickname",user.getUserName());
					}
					else
					{
						data.put("nickname",user.getPhone());
					}
					list.add(data);
					net.sf.json.JSONObject obj = WechatUtils.addEasemobUser(list, token);//注册新用户
					if(obj.get("error") == null)
					{
						EasemobUser easemob = userService.queryEasemobUserByPhone(user.getId());
						if(easemob == null)//是否存在
						{
							EasemobUser u1 = new EasemobUser();
							u1.setUserId(user.getId());
							u1.setUserName(String.valueOf(user.getId()));
							u1.setPassword(String.valueOf(user.getId()));
							if(!CmsUtils.isNullOrEmpty(user.getRealName()))
							{
								u1.setNickName(user.getRealName());
							}
							else if(!CmsUtils.isNullOrEmpty(user.getUserName()))
							{
								u1.setNickName(user.getUserName());
							}
							else
							{
								u1.setNickName(String.valueOf(user.getId()));
							}
							u1.setActivated(obj.getJSONArray("entities").getJSONObject(0).getBoolean("activated"));
							u1.setUpdateTime(new Date());
							u1.setCreateTime(new Date());
							userService.insertEasemobUser(u1);
						}
					}
					else if(obj.get("error").equals("duplicate_unique_property_exists"))
					{
						//用户名存在   不再执行
					}
					else if(obj.get("error") != null && obj.get("error_description").equals("Unable to authenticate due to expired access token"))
					{
						//token过期或未传token
						EasemobToken t = new EasemobToken();
						t.setAccessToken(TokenUtil.getAccessToken());
						t.setExpiresIn(5184000);
						t.setCreateTime(new Date());
						userService.insertEasemobToken(t);
						insertEasemobIMUsers(phone);//重新执行
					}
					else
					{
						insertEasemobIMUsers(phone);//注册失败后重新执行
					}
				} catch (WXException e) {}
			}
		}
    }

    /**
     * @Title: batchInsertEasemobIMUsers
     * @Description: TODO(批量注册环信账号)
     * @param: List<User> userList
     * @throws
     */
    private void batchInsertEasemobIMUsers(List<User> userList)
	{
		EasemobIMUsers api = new EasemobIMUsers();
		String token = userService.queryEasemobToken();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for(User user : userList)
		{
			//检查用户是否存在
			Object userNode = api.getIMUserByUserName(user.getPhone());
			if(CmsUtils.isNullOrEmpty(userNode))//null表示用户未注册
			{
				Map<String,Object> data = new HashMap<String, Object>();
				data.put("username",user.getId());
				data.put("password",user.getId());
				if(!CmsUtils.isNullOrEmpty(user.getRealName()))
				{
					data.put("nickname",user.getRealName());
				}
				else if(!CmsUtils.isNullOrEmpty(user.getUserName()))
				{
					data.put("nickname",user.getUserName());
				}
				else
				{
					data.put("nickname",user.getPhone());
				}
				list.add(data);
			}
		}

		try {
			net.sf.json.JSONObject obj = WechatUtils.addEasemobUser(list, token);//批量添加新用户
			if(obj.getString("error") == null)
			{
				JSONArray array = obj.getJSONArray("entities");

				for(int i=0;i<array.size();i++)
				{
					net.sf.json.JSONObject json = array.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
					User user = userService.selectByPrimaryKey(json.getInt("username"));

					EasemobUser easemob = userService.queryEasemobUserByPhone(user.getId());
					if(easemob == null)
					{
						EasemobUser u1 = new EasemobUser();
						u1.setUserId(user.getId());
						u1.setUserName(String.valueOf(user.getId()));
						u1.setPassword(String.valueOf(user.getId()));
						if(!CmsUtils.isNullOrEmpty(user.getRealName()))
						{
							u1.setNickName(user.getRealName());
						}
						else if(!CmsUtils.isNullOrEmpty(user.getUserName()))
						{
							u1.setNickName(user.getUserName());
						}
						else
						{
							u1.setNickName(String.valueOf(user.getId()));
						}
						u1.setActivated(json.getBoolean("activated"));
						u1.setUpdateTime(new Date());
						u1.setCreateTime(new Date());
						userService.insertEasemobUser(u1);
					}
				}
			}
		} catch (WXException e) {}
    }

    /**
	 * 根据环信ID查询用户头像和姓名
	 * @param request
	 * @param response
	 * @author fancunxin
	 * @return
	 *
	 */
    @RequestMapping(value="/query/userby/easemob/detail.ddxj")
    public ResponseBase queryUserEasemobName(HttpServletRequest request, HttpServletResponse response, String easemobId, String userId)
    {
    	ResponseBase result=ResponseBase.getInitResponse();
    	User user = userService.selectByPrimaryKey(Integer.valueOf(easemobId));
		insertEasemobIMUsers(user.getPhone());
		user = userService.queryUserByEasemobName(easemobId,userId);
		result.push("user", JsonUtil.bean2jsonToString(user));
		result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg(Constants.Prompt.QUERY_SUCCESSFUL);
    	return result;
    }
}
