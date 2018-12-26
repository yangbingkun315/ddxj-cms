package net.zn.ddxj.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.common.PageHelperModel;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.ApplyForeman;
import net.zn.ddxj.entity.Category;
import net.zn.ddxj.entity.CmsUser;
import net.zn.ddxj.entity.RealAuth;
import net.zn.ddxj.entity.Recruit;
import net.zn.ddxj.entity.RecruitRecord;
import net.zn.ddxj.entity.User;
import net.zn.ddxj.entity.UserTransfer;
import net.zn.ddxj.entity.UserWithdraw;
import net.zn.ddxj.service.AppMessageService;
import net.zn.ddxj.service.CategoryService;
import net.zn.ddxj.service.CircleService;
import net.zn.ddxj.service.CmsService;
import net.zn.ddxj.service.PayService;
import net.zn.ddxj.service.RecruitCreditService;
import net.zn.ddxj.service.RecruitRecordService;
import net.zn.ddxj.service.RecruitService;
import net.zn.ddxj.service.SalaryRecordService;
import net.zn.ddxj.service.UserCenterService;
import net.zn.ddxj.service.UserCollectionService;
import net.zn.ddxj.service.UserCommentService;
import net.zn.ddxj.service.UserService;
import net.zn.ddxj.service.UserTransferService;
import net.zn.ddxj.service.UserWithdrawService;
import net.zn.ddxj.tool.AsycService;
import net.zn.ddxj.tool.WechatService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.DateUtils;
import net.zn.ddxj.utils.FrontUtils;
import net.zn.ddxj.utils.RedisUtils;
import net.zn.ddxj.utils.ResponseUtils;
import net.zn.ddxj.utils.json.JsonUtil;
import net.zn.ddxj.utils.wechat.WXException;
import net.zn.ddxj.utils.wechat.WechatUtils;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiniu.common.QiniuException;

@Controller
public class UserController extends BaseController{
	private static final String CUSTOMER_USER_AUTH = "/customer/customer_auth.html";
	private static final String CUSTOMER_USER__AUTH_TPL = "/customer/customer_auth_tpl.html";
	private static final String CUSTOMER_USER = "/customer/customer_user.html";
	private static final String CUSTOMER_USER_TPL = "/customer/customer_user_tpl.html";
	private static final String CUSTOMER_USER_DETAIL = "/customer/customer_user_detail.html";
	private static final String CUSTOMER_USER_EDIT = "/customer/customer_user_edit.html";
	private static final String CUSTOMER_USER_CATEGORY_LIST = "/customer/customer_category.html";
	private static final String CUSTOMER_USER_CATEGORY_LIST_TPL = "/customer/customer_category_tpl.html";
	private static final String CUSTOMER_USER_CATEGORY_EDIT = "/customer/customer_category_edit.html";
	private static final String CUSTOMER_APPLY_FOREMAN = "/customer/applicant_user.html";
	private static final String CUSTOMER_APPLY_FOREMAN_TPL = "/customer/applicant_user_tpl.html";
	private static final String CUSTOMER_CAPITAL_CHANGE = "/customer/customer_capital_change.html";
	private static final String CUSTOMER_CAPITAL_CHANGE_TPL = "/customer/customer_capital_change_tpl.html";
	private static final String USER_WITHDRAW_LIST = "/customer/userWithdrawList.html";
	private static final String USER_WITHDRAW_LIST_TPL = "/customer/userWithdrawListTpl.html";
	private static final String USER_TRANSFER_LIST = "/fund/transfer_list.html";
	private static final String USER_TRANSFER_LIST_TPL = "/fund/transfer_list_tpl.html";
	
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
	@Autowired
	private UserCenterService userCenterService;
	@Autowired
	private CmsService cmsService;
	
	/**
	 * 客户管理GET
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/customer/user/list.htm", method = RequestMethod.GET)
	public String customerUserGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		
		return FrontUtils.findFrontTpl(request, response, model, CUSTOMER_USER);
	}
	
	/**
	 * 客户管理POST
	 * @param request
	 * @param response
	 * @param model
	 * @param requestVo
	 * @return
	 */
	@RequestMapping(value = "/customer/user/list.htm", method = RequestMethod.POST)
	public String  customerUserPost(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) {
		PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
		Map<String,Object> param = new HashMap<String,Object>();
	    param.put("realName", requestVo.getRealName());
	    param.put("phone", requestVo.getPhone());
	    param.put("role", requestVo.getRole());
	    param.put("startTime", requestVo.getStartTime());
	    param.put("endTime", requestVo.getEndTime());
	    param.put("staffNum", requestVo.getStaffNum());
	    param.put("isAuth", requestVo.getIsAuth());
		List<User> customerUserList = userService.queryManagerUserList(param);
		PageInfo<User> page = new PageInfo<User>(customerUserList);
		PageHelperModel.responsePageModel(page,model);
		model.addAttribute("customerUserList",page.getList());
		
		return FrontUtils.findFrontTpl(request, response, model, CUSTOMER_USER_TPL);
	}
	
	/**
	 * 查询用户详情-GET
	 * @param request
	 * @param response
	 * @param model
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/customer/user/detail.htm", method = RequestMethod.GET)
	public String customerUserDetailGet(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer userId)
	{
		model.addAttribute("userDetail", userService.queryUserDetail(userId));
		return FrontUtils.findFrontTpl(request, response, model, CUSTOMER_USER_DETAIL);
		
	}
	
	/**
	 * 客户管理-新增修改GET
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/customer/user/edit.htm", method = RequestMethod.GET)
	public String customerUserEditGet(HttpServletRequest request,HttpServletResponse response,ModelMap model,int userId) 
	{
		User user = userService.queryUserDetail(userId);
		if(user == null)
		{
			return null;
		}
		model.addAttribute("user", user);
		
		CmsRequestVo requestVo = new CmsRequestVo();
		List<Category> categoryList = categoryService.queryCategoryList(requestVo);
		model.addAttribute("categoryUserList",categoryList);
		
		return FrontUtils.findFrontTpl(request, response, model, CUSTOMER_USER_EDIT);
	}
	
	/**
	 * 客户管理-新增修改POST
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException 
	 * @throws QiniuException 
	 */
	@RequestMapping(value = "/customer/user/edit.htm", method = RequestMethod.POST)
	public String customerUserEditPOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) throws QiniuException, IOException 
	{
		ResponseBase result = ResponseBase.getInitResponse();
		User user = requestVo.getUser();
		if(user == null)
		{
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("用户信息不存在");
			ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		}
		user.setBirthDate(DateUtils.getDate(requestVo.getBirthDate(), "yyyy-MM-dd HH:mm:ss"));
		userService.updateByPrimaryKeySelective(user);
		
		if(!CmsUtils.isNullOrEmpty(requestVo.getCategoryIdList()))
		{
			userService.deleteUserCategory(user.getId());//删除用户工种
			
			for(Integer id : requestVo.getCategoryIdList())
			{
				Map<String, Object> date = new HashMap<String, Object>();
				date.put("userId", user.getId());
				date.put("categoryId", id);
				date.put("createTime", new Date());
				date.put("updateTime", new Date());
				userService.insertUserCategory(date);
			}
			CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "客户管理-客户管理-修改客户-（"+user.getRealName()+"）",sessionUser.getId());
		}
		
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	
	/**
	 * 工种列表
	* @Title: 
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-饶开宇
	* @throws
	 */
	@RequestMapping(value = "/customer/user/category/list.htm", method = RequestMethod.GET)
	public String cmsUserCategoryListGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		
		return FrontUtils.findFrontTpl(request, response, model, CUSTOMER_USER_CATEGORY_LIST);
	}
	
	/**
	 * 工种列表
	* @Title: 
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @param requestVo
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-饶开宇
	* @throws
	 */
	@RequestMapping(value = "/customer/user/category/list.htm", method = RequestMethod.POST)
	public String cmsUserCategoryListPost(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) {
		
		PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
		//List<CmsUser> findUserList = cmsService.findUserList();
		List<Category> categoryList = categoryService.queryCategoryList(requestVo);
		PageInfo<Category> page = new PageInfo<Category>(categoryList);
		PageHelperModel.responsePageModel(page,model);
		model.addAttribute("categoryList",page.getList());
		return FrontUtils.findFrontTpl(request, response, model, CUSTOMER_USER_CATEGORY_LIST_TPL);
	}
	
	/**
	 * 工种删除
	* @Title: cmsRoleDeletePost
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @param id
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-饶开宇
	* @throws
	 */
	@RequestMapping(value = "/customer/user/category/delete.htm", method = RequestMethod.POST)
	public String cmsRoleDeletePost(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer id) {
		ResponseBase result = ResponseBase.getInitResponse();
		Category detail = categoryService.queryCategoryDetail(id);
		categoryService.deleteCategory(id);
		//日志记录
		CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "客户管理-工种管理-删除工种-（"+detail.getCategoryName()+"）",sessionUser.getId());
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	
	/**
	 * 修改、新增工种-GET
	 * @param request
	 * @param response
	 * @param model
	 * @author Rao
	 * @return
	 */
	@RequestMapping(value = "/customer/user/category/edit.htm", method = RequestMethod.GET)
	public String cmsUserCategoryEditGET(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer categoryId) 
	{
		Category category = null;
		Category categoryParent = null;
		if(!CmsUtils.isNullOrEmpty(categoryId) && categoryId > 0)
		{
			category = categoryService.queryCategoryDetail(categoryId);
			if(!CmsUtils.isNullOrEmpty(category.getParentId()))
			{
				categoryParent = categoryService.queryCategoryDetail(category.getParentId());
				model.addAttribute("categoryParent", categoryParent);
			}
			
		}
		model.addAttribute("category", category);
		
		List<Category> categoryParentList = categoryService.queryParentCategory();
		model.addAttribute("categoryParentList", categoryParentList);
		return FrontUtils.findFrontTpl(request, response, model, CUSTOMER_USER_CATEGORY_EDIT);
	}
	
	/**
	 * 修改、新增工种-POST
	 * @param request
	 * @param response
	 * @param model
	 * @author Rao
	 * @return
	 */
	@RequestMapping(value = "/customer/user/category/edit.htm", method = RequestMethod.POST)
	public String cmsUserCategoryEditPOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) 
	{
		ResponseBase result = ResponseBase.getInitResponse();
		Category category = null;
	    CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		if(!CmsUtils.isNullOrEmpty(requestVo.getId()) && requestVo.getId() > 0)
		{
			category = categoryService.queryCategoryDetail(requestVo.getId());
		    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "客户管理-工种管理-修改工种-（"+category.getCategoryName()+"）",sessionUser.getId());

		}
		categoryService.updateCategoryEdit(requestVo);
		model.addAttribute("category", category);
		List<Category> categoryParentList = categoryService.queryParentCategory();
		model.addAttribute("categoryParentList", categoryParentList);
		ResponseUtils.renderHtml(response, 0);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		return FrontUtils.findFrontTpl(request, response, model, CUSTOMER_USER_CATEGORY_LIST);
	}
	
	/**
	 * 实名认证GET
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/customer/user/real/auth/list.htm", method = RequestMethod.GET)
	public String customerUserAuthGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		
		return FrontUtils.findFrontTpl(request, response, model, CUSTOMER_USER_AUTH);
	}
	
	/**
	 * 实名认证POST
	 * @param request
	 * @param response
	 * @param model
	 * @param requestVo
	 * @return
	 */
	@RequestMapping(value = "/customer/user/real/auth/list.htm", method = RequestMethod.POST)
	public String  customerUserAuthPost(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) {
		PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
		List<RealAuth> realAuthList = userService.queryRealAuthList(requestVo);
		PageInfo<RealAuth> page = new PageInfo<RealAuth>(realAuthList);
		PageHelperModel.responsePageModel(page,model);
		model.addAttribute("realAuthList",page.getList());
		return FrontUtils.findFrontTpl(request, response, model, CUSTOMER_USER__AUTH_TPL);
	}
	
	/**
	 * 删除实名认证-POST
	 * @param request
	 * @param response
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/customer/user/real/auth/delete.htm", method = RequestMethod.POST)
	public String customerUserAuthDeletePOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,int id) 
	{
		ResponseBase result = ResponseBase.getInitResponse();
		RealAuth auth = userService.queryRealAuthById(id);
		userService.deleteRealAuth(id);
		//日志记录
		CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "客户管理-实名认证-删除实名认证-（"+auth.getRealName()+"）",sessionUser.getId());
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	
	/**
	 * 实名认证审核
	 * @param request
	 * @param response
	 * @param model
	 * @param id
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/customer/user/real/auth/update.htm", method = RequestMethod.POST)
	public String updateRealAuthStatusPOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,int id,Integer status) 
	{
		ResponseBase result = ResponseBase.getInitResponse();
		RealAuth auth = userService.queryRealAuthById(id);
		String checkStatus="";
		if(status==2)
		{
			checkStatus="审核失败";
		}
		else if(status==3)
		{
			checkStatus="审核成功";
		}
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
				template.put("url", "www.baidu.com");
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
    	//日志记录
		CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "客户管理-实名认证-实名认证-审核（"+auth.getRealName()+checkStatus+"）",sessionUser.getId());
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
		
	}
	
	/**
	 * 锁定与解锁用户-POST
	 * @param request
	 * @param response
	 * @param userId
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/customer/user/forbidden/user.htm", method = RequestMethod.POST)
	public String forbiddenUserPOST(HttpServletRequest request, HttpServletResponse response,Integer userId,Integer type)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		userService.forbiddenUser(userId,type);
		User userDetail = userService.queryUserDetail(userId);
		Integer loginStatus = userDetail.getLoginStatus();
		String status="";
		if(loginStatus==1)
		{
			status="正常";
		}
		else if(loginStatus==2)
		{
			status="禁用";
		}
		else if(loginStatus==3)
		{
			status="冻结";
		}
		//日志记录
		CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "客户管理-客户管理-锁定与解锁用户-（"+status+'-'+userDetail.getRealName()+"）",sessionUser.getId());
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
		
	}
	
	/**
	 * 注销用户-POST
	 * @param request
	 * @param response
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/customer/user/logout/user.htm", method = RequestMethod.POST)
	public String logoutUserInfo(HttpServletRequest request, HttpServletResponse response,Integer userId)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		User userDetail = userService.queryUserDetail(userId);
		Integer role=userDetail.getRole();
		String position="";
		if(role==1)
		{
			position="工人";

		}
		else if(role==2)
		{
			position="工头";
		}
		if(!CmsUtils.isNullOrEmpty(role)&& role==1)//工人
		{
			RecruitRecord workerConduct = recruitRecordService.queryWorkerConduct(userId);//查询正在进行的活动
			if(CmsUtils.isNullOrEmpty(workerConduct))
			{
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
				userService.delValidateManager(userId);//删除认证message
				//userCenterService.deleteMessageRecord(userId);//删除消息中心所有记录
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponse(Constants.TRUE);
				result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
				ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
				//日志记录
				CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
				cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "客户管理-客户管理-注销用户-（"+position+'-'+userDetail.getRealName()+"-注销"+"）",sessionUser.getId());
				return null;
			 }
			 else
			 {
				 result.setResponse(Constants.FALSE);
	    	     result.setResponseCode(Constants.SUCCESS_200);
	    	     result.setResponseMsg("该用户在职，删除失败！");
	    	     ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
	    	     return null;
			 }
		}
		else if(!CmsUtils.isNullOrEmpty(role)&& role==2)//工头
		{
			List<Recruit> recruitUnderway = recruitService.selectRecruitUnderway(userId);//活动正在进行也不能删除
			if(CmsUtils.isNullOrEmpty(recruitUnderway))
			{
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
				userService.delValidateManager(userId);//删除认证message
				//userCenterService.deleteMessageRecord(userId);//删除消息中心所有记录
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponse(Constants.TRUE);
				result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
				ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
				//日志记录
				CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
				cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "客户管理-客户管理-注销用户-（"+position+'-'+userDetail.getRealName()+"-注销"+"）",sessionUser.getId());
				return null;
			}
			else
			{
				result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg("删除失败，有在进行的活动！");
				ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
				return null;
			}
		}
		else if(!CmsUtils.isNullOrEmpty(role)&& role==0)
		{
			userService.delUserInfo(userId);//删除用户信息
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponse(Constants.TRUE);
			result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
			ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
			//日志记录
			CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
			cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "客户管理-客户管理-注销用户-（"+position+'-'+userDetail.getRealName()+"-注销"+"）",sessionUser.getId());
			return null;
		}
		else if(CmsUtils.isNullOrEmpty(role))
		{
			userService.delUserInfo(userId);//删除用户信息
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponse(Constants.TRUE);
			result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
			ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
			//日志记录
			CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
			cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "客户管理-客户管理-注销用户-（"+position+'-'+userDetail.getRealName()+"-注销"+"）",sessionUser.getId());
			return null;
		}
		return null;
	}
	
	/**
	 * 申请成为工头GET
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/customer/user/foreman/list.htm", method = RequestMethod.GET)
	public String applyForemanGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		return FrontUtils.findFrontTpl(request, response, model, CUSTOMER_APPLY_FOREMAN);
	}
	
	/**
	 * 申请成为工头POST
	 * @param request
	 * @param response
	 * @param model
	 * @param requestVo
	 * @return
	 */
	@RequestMapping(value="/customer/user/foreman/list.htm", method = RequestMethod.POST)
	public String  applyForemanPost(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) {
		PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
		List<ApplyForeman> foremanList = userService.queryApplyForeman(requestVo);
		PageInfo<ApplyForeman> pageInfo = new PageInfo<ApplyForeman>(foremanList);
		PageHelperModel.responsePageModel(pageInfo,model);
		model.addAttribute("foremanList",pageInfo.getList());
		return FrontUtils.findFrontTpl(request, response, model, CUSTOMER_APPLY_FOREMAN_TPL);
	}
	 
	/**
	 * 更新申请状态POST
	 * @param request
	 * @param response
	 * @param requestVo
	 * @return
	 */
	@RequestMapping(value = "/customer/user/foreman/update.htm", method = RequestMethod.POST)
	public String updateApplyForemanStatus(HttpServletRequest request, HttpServletResponse response,Integer foremanId,Integer validateStatus,String validateCause){
		ResponseBase result=ResponseBase.getInitResponse();	
		ApplyForeman applyForemanInfo=userService.selectByForemanId(foremanId);
		Integer userId=applyForemanInfo.getUserId();
		if(validateStatus==2)//审核失败
		{
			applyForemanInfo.setValidateCause(validateCause);
			userService.updateApplyForemanStatus(foremanId,validateStatus,validateCause);
			//日志记录
			CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
			cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "客户管理-申请工头-审核-（"+applyForemanInfo.getUser().getRealName()+"）",sessionUser.getId());
			result.setResponse(Constants.TRUE);
		   	result.setResponseCode(Constants.SUCCESS_200);
		   	result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
		   	ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		   	return null;
		}
		else if(validateStatus==3) //审核成功
		{
			String cause="";
			//审核成功
			userService.updateApplyForemanStatus(foremanId,validateStatus,cause);
			//更新用户角色
			userService.updateUserRole(userId);
			//更新用户的承包方式
			userService.updateUservalidateMessage(userId);
			//删除用户的工种
			userService.deleteUserCategory(userId);
			//删除用户报名表
			recruitRecordService.updateUserSignStatus(userId);
			//授信收工资记录 
			salaryRecordService.deleteSalaryRecord(userId);
			//删除用户收藏
			userCollectionService.deleteUserCollection(userId);
			//删除职位邀请表
			userService.deleteUserRequest(userId);
			//删除用户评论标签
			userCommentService.delUserCommentLabel(userId);
			//删除评论			
			userCommentService.deleteUserComment(userId);
			//删除转账记录表
			userTransferService.deleteUserTransfer(userId);
			//删除圈子点赞
			circleService.deleteCricleLaud(userId);
			//删除圈子评论
			circleService.deleteCricleComment(userId);
			//删除圈子图片
			circleService.deleteCricleImage(userId);
			//删除圈子
			circleService.deleteCricle(userId);
			//删除消息中心所有记录
			userCenterService.deleteMessageRecord(userId);
			//日志记录
			CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
			cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "客户管理-申请工头-审核-（"+applyForemanInfo.getUser().getRealName()+"）",sessionUser.getId());
			result.setResponse(Constants.TRUE);
		   	result.setResponseCode(Constants.SUCCESS_200);
		   	result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
		   	ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		   	return null;
			
		}
		result.setResponse(Constants.FALSE);
	   	result.setResponseCode(Constants.SUCCESS_200);
	   	result.setResponseMsg(Constants.Prompt.UPDATE_FAILURE);
	   	ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
		
	}
		
	/**
	 * 删除申请列表POST
	 * @param request
	 * @param response
	 * @param applyId
	 * @return
	 */
	@RequestMapping(value="/customer/user/foreman/delete.htm", method = RequestMethod.POST)
	public String deleteApplyForemanRecord(HttpServletRequest request, HttpServletResponse response,Integer id){
		ResponseBase result=ResponseBase.getInitResponse();
		ApplyForeman foreman = userService.selectByForemanId(id);
		userService.deleteForemanByApplyId(id);
		//日志记录
		CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "客户管理-申请工头-删除-（"+foreman.getUser().getRealName()+"）",sessionUser.getId());
		result.setResponse(Constants.TRUE);
	   	result.setResponseCode(Constants.SUCCESS_200);
	   	result.setResponseMsg("删除成功");
	   	ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	
	/**
	 * 提现列表GET
	 * @param request
	 * @param response
	 * @param requestVo
	 * @return
	 */
	@RequestMapping(value="/query/userWithdraw/list.htm", method = RequestMethod.GET)
	public String queryUserWithdrawListGET(HttpServletRequest request, HttpServletResponse response,ModelMap model,RequestVo requestVo)
	{
		model.addAttribute("requestVo",requestVo);
		return FrontUtils.findFrontTpl(request, response, model, USER_WITHDRAW_LIST);
	}
	
	/**
	 * 提现列表POST
	 * @param request
	 * @param response
	 * @param requestVo
	 * @return
	 */
	@RequestMapping(value="/query/userWithdraw/list.htm", method = RequestMethod.POST)
	public String queryUserWithdrawListPOST(HttpServletRequest request, HttpServletResponse response,ModelMap model,RequestVo requestVo)
	{
		PageHelper.startPage(requestVo.getCurrentPage(), 20);
		List<UserWithdraw> withdrawList = userWithdrawService.queryWithdrawRecord(requestVo);
		PageInfo<UserWithdraw> pageInfo = new PageInfo<UserWithdraw>(withdrawList);
		PageHelperModel.responsePageModel(pageInfo,model);
		model.addAttribute("userWithdrawList",pageInfo.getList());
		return FrontUtils.findFrontTpl(request, response, model, USER_WITHDRAW_LIST_TPL);
	}
	
	/**
	 * 提现更新状态
	 * @param request
	 * @param response
	 * @param id
	 * @param withdrawStatus
	 * @param withdrawProcess
	 * @return
	 */
	@RequestMapping(value = "/update/withdraw/status.htm", method = RequestMethod.POST)
	public String updateStatus(HttpServletRequest request, HttpServletResponse response,Integer id,Integer withdrawStatus ,Integer withdrawProcess){
		ResponseBase result=ResponseBase.getInitResponse();
		//获取提现记录
		UserWithdraw withdraw = userWithdrawService.selectByPrimaryKey(id);
		//获取提现人的编号
		Integer userId = withdraw.getUserId();
		
		if(withdrawStatus == 2 || withdrawStatus == 4 || withdrawStatus == 6 || withdrawStatus == 8)
		{
			//获取提现人的金额
			BigDecimal userMoney = userService.queryUserDetail(userId).getRemainderMoney();
			userService.updateFromUserMoney(userMoney.add(withdraw.getMoney()),userId);
		}
		
		userWithdrawService.updateStatus(id,withdrawStatus,withdrawProcess);//更新状态
		
		UserWithdraw userWithdraw = new UserWithdraw();
		userWithdraw.setId(id);
		userWithdraw.setUpdateTime(new Date());
		userWithdrawService.updateWithdrawalsInfo(userWithdraw);//更新时间
		
		asycService.pushUserWithdrawStatus(userId,id);//APP推送
		//日志记录
		CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "资金管理-提现列表-提现审核-（"+withdraw.getUser().getRealName()+"）",sessionUser.getId());
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("更新成功");
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	
	/**
	 * 查询用户转账Echart-POST
	 * @param request
	 * @param response
	 * @param model
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/customer/user/transfer/echart.htm", method = RequestMethod.POST)
	public String queryTransferEchartPost(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer userId)
	{
		ResponseBase result = ResponseBase.getInitResponse();
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
		result.setData(data);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	
	/**
	 * 查询转账列表GET
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/customer/transfer/list.htm", method = RequestMethod.GET)
	public String queryAllTransferListGet(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		return FrontUtils.findFrontTpl(request, response, model, USER_TRANSFER_LIST);
	}
	
	/**
	 * 查询转账列表POST
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/customer/transfer/list.htm", method = RequestMethod.POST)
	public String queryAllTransferListPOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("orderNo", request.getParameter("orderNo"));
		param.put("userName", requestVo.getUserName());
		param.put("phone", requestVo.getPhone());
		param.put("transferWay", requestVo.getTransferWay());
		param.put("transferType", requestVo.getTransferType());
		param.put("startTime", requestVo.getStartTime());
	    param.put("endTime", requestVo.getEndTime());
		PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
		List<UserTransfer> userTransferList = userTransferService.queryUserTransferDetails(param);
	    PageInfo<UserTransfer> pageInfo = new PageInfo<UserTransfer>(userTransferList);
	    PageHelperModel.responsePageModel(pageInfo,model);
		model.addAttribute("userTransferList",pageInfo.getList());
		return FrontUtils.findFrontTpl(request, response, model, USER_TRANSFER_LIST_TPL);
	}
	
	
	
	/**
	 * 查询资金变动列表GET
	 * @param request
	 * @param response
	 * @param model
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/customer/user/transfer/list.htm", method = RequestMethod.GET)
	public String queryTransferListGet(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer userId){
		model.addAttribute("userId",userId);
		return FrontUtils.findFrontTpl(request, response, model, CUSTOMER_CAPITAL_CHANGE);
	}
	/**
	 * 查询资金变动列表POST
	 * @param request
	 * @param response
	 * @param model
	 * @param requestVo
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/customer/user/transfer/list.htm", method = RequestMethod.POST)
	public String queryTransferListPost(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo,Integer userId){
		model.addAttribute("userId",userId);
		    	
    	Map<String,Object> param = new HashMap<String,Object>();
    	if(requestVo.getStatus() == 1)//支出
    	{
    		param.put("fromUserId", userId);
    	}
    	else if(requestVo.getStatus()  == 2)//收入
    	{
    		param.put("toUserId", userId);
    	}
    		
	    param.put("transferWay", requestVo.getTransferWay());
	    param.put("transferType", requestVo.getTransferType());
	    param.put("startTime", requestVo.getStartTime());
	    param.put("endTime", requestVo.getEndTime());
	    PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
	    List<UserTransfer> userTransferList = userTransferService.queryUserTransferDetails(param);
	    PageInfo<UserTransfer> pageInfo = new PageInfo<UserTransfer>(userTransferList);
	    PageHelperModel.responsePageModel(pageInfo,model);
		model.addAttribute("userTransferList",pageInfo.getList());
		model.addAttribute("status",requestVo.getStatus());
		return FrontUtils.findFrontTpl(request, response, model, CUSTOMER_CAPITAL_CHANGE_TPL);
	}
	
	
}
