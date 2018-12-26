package net.zn.ddxj.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.common.PageHelperModel;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.Category;
import net.zn.ddxj.entity.Circle;
import net.zn.ddxj.entity.CmsUser;
import net.zn.ddxj.entity.Credit;
import net.zn.ddxj.entity.CreditRepayment;
import net.zn.ddxj.entity.Notice;
import net.zn.ddxj.entity.Recruit;
import net.zn.ddxj.entity.RecruitBanner;
import net.zn.ddxj.entity.RecruitCategory;
import net.zn.ddxj.entity.RecruitCredit;
import net.zn.ddxj.entity.RecruitRecord;
import net.zn.ddxj.entity.SalaryRecord;
import net.zn.ddxj.entity.User;
import net.zn.ddxj.entity.UserTransfer;
import net.zn.ddxj.service.CategoryService;
import net.zn.ddxj.service.CircleService;
import net.zn.ddxj.service.CmsService;
import net.zn.ddxj.service.RecruitCreditService;
import net.zn.ddxj.service.RecruitRecordService;
import net.zn.ddxj.service.RecruitService;
import net.zn.ddxj.service.SalaryRecordService;
import net.zn.ddxj.service.UserService;
import net.zn.ddxj.service.UserTransferService;
import net.zn.ddxj.tool.AsycService;
import net.zn.ddxj.tool.WechatService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.DateUtils;
import net.zn.ddxj.utils.FrontUtils;
import net.zn.ddxj.utils.OrderNumUtils;
import net.zn.ddxj.utils.RedisUtils;
import net.zn.ddxj.utils.ResponseUtils;
import net.zn.ddxj.utils.aliyun.SmsUtil;
import net.zn.ddxj.utils.json.JsonUtil;
import net.zn.ddxj.utils.wechat.WXException;
import net.zn.ddxj.utils.wechat.WechatUtils;
import net.zn.ddxj.vo.CmsRequestVo;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aliyuncs.exceptions.ClientException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiniu.common.QiniuException;

@Controller
@Slf4j
public class RecruitController extends BaseController{
	private static final String RECRUUIT_LIST = "/recruit/recruit_list.html";
	private static final String RECRUUIT_LIST_TPL = "/recruit/recruit_list_tpl.html";
	private static final String RECRUUIT_EDIT = "/recruit/recruit_edit.html";
	private static final String RECRUUIT_APPLY_LIST = "/recruit/recruit_apply_list.html";
	private static final String RECRUUIT_APPLY_LIST_TPL = "/recruit/recruit_apply_list_tpl.html";
	private static final String CREDIT_ORGANIZATION_LIST="/recruit/credit_institution_list.html";
	private static final String CREDIT_ORGANIZATION_LIST_TPL="/recruit/credit_institution_list_tpl.html";
	private static final String CREDIT_ORGANIZATION_EDIT= "/recruit/credit_institution_edit.html";
	private static final String CREDIT_RECORD_LIST="/recruit/credit_record_list.html";
	private static final String CREDIT_RECORD_LIST_TPL= "/recruit/credit_record_list_tpl.html";
	private static final String CIRCLE_RECORD_LIST="/circle/circle_record_list.html";
	private static final String CIRCLE_RECORD_LIST_TPL= "/circle/circle_record_list_tpl.html";
	private static final String CREDIT_RECORD_REPAYMENT="/recruit/credit_record_repayment.html";
	private static final String RECRUUIT_NOTICE_LIST= "/recruit/recruit_notice_list.html";
	private static final String RECRUIT_ADDRESS_MAP= "/recruit/recruit_address_map.html";
	private static final String RECRUUIT_NOTICE_LIST_TPL= "/recruit/recruit_notice_list_tpl.html";
	private static final String RECRUUIT_NOTICE_EDIT= "/recruit/recruit_notice_edit.html";
	private static final String RECRUUIT_SALARY_LIST= "/recruit/recruit_salary_list.html";
	private static final String RECRUUIT_SALARY_LIST_TPL= "/recruit/recruit_salary_list_tpl.html";
	
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
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private WechatService wechatService;
	@Autowired
	public RedisUtils redisUtils;
	@Autowired
	private SalaryRecordService salaryRecordService;
	@Autowired
	private UserTransferService userTransferService;
	@Autowired
	private CmsService cmsService;
	
	/**
	 * 发放管理GET
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/recruit/salary/manager/list.htm", method = RequestMethod.GET)
	public String recruitSalaryManagerListGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		
		return FrontUtils.findFrontTpl(request, response, model, RECRUUIT_SALARY_LIST);
	}
	/**
	 * 发放管理POST
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/recruit/salary/manager/list.htm", method = RequestMethod.POST)
	public String recruitSalaryManagerListPost(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) {
		PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
		List<SalaryRecord> salaryList = salaryRecordService.querySalaryRecordCms(requestVo);
		PageInfo<SalaryRecord> pageInfo = new PageInfo<SalaryRecord>(salaryList);
		PageHelperModel.responsePageModel(pageInfo,model);
		model.addAttribute("salaryList",pageInfo.getList());
		return FrontUtils.findFrontTpl(request, response, model, RECRUUIT_SALARY_LIST_TPL);
	}
	/**
	 * 删除发放记录POST
	 * @param request
	 * @param response
	 * @param model
	 * @param salaryId
	 * @return
	 * @throws IllegalAccessException
	 */
	@RequestMapping(value = "/recruit/salary/manager/delete.htm", method = RequestMethod.POST)
	public String recruitSalaryManagerDeletePOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer salaryId) throws IllegalAccessException
	{
		ResponseBase result = ResponseBase.getInitResponse();
		SalaryRecord salaryRecord = salaryRecordService.selectByPrimaryKey(salaryId);
		salaryRecordService.deleteByPrimaryKey(salaryId);
		
		//日志记录
	    CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
	    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "招聘管理-授信发放记录-删除发放记录-（"+salaryRecord.getAssignUserId()+"）",sessionUser.getId());

		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	/**
	 * 授信发放审核POST
	 * @param request
	 * @param response
	 * @param model
	 * @param salaryId
	 * @return
	 * @throws IllegalAccessException
	 */
	@RequestMapping(value = "/recruit/salary/manager/update.htm", method = RequestMethod.POST)
	public String recruitSalaryManagerupdatePOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer salaryId,Integer auditStatus) throws IllegalAccessException
	{
		ResponseBase result = ResponseBase.getInitResponse();
		salaryRecordService.updateAuditStatus(salaryId,auditStatus);
		SalaryRecord salaryRecord = salaryRecordService.selectByPrimaryKey(salaryId);
		
		//发放人ID
		Integer fromUserId=salaryRecord.getAssignUserId();
		Integer toUserId=salaryRecord.getSendeeUserId();
		//项目ID
		Integer recruitId=salaryRecord.getRecruitId();
		//通过发放人ID和招聘ID查询授信额度
		RecruitCredit recruitCredit = recruitCreditService.queryRecruitCredit(fromUserId, recruitId);
		//授信可用金额
		BigDecimal availableAmount=recruitCredit.getUsableMoney();
		//发放金额
		BigDecimal settlementAmount=salaryRecord.getMoney();
		UserTransfer transfer =new UserTransfer();
		if(auditStatus == 2)
		{		
			//查询授信额度
			transfer.setRecruitId(recruitId);
			transfer.setFromUserId(fromUserId);
			transfer.setMoney(settlementAmount);
			transfer.setToUserId(toUserId);
			transfer.setOrderNo(OrderNumUtils.getOredrNum());
			transfer.setTransferWay(salaryRecord.getTransferWay());
			transfer.setTransferDesc(salaryRecord.getTransferDesc());
			transfer.setTransferType(salaryRecord.getTransferType());
			transfer.setUnit(salaryRecord.getUnit());
			transfer.setCount(salaryRecord.getCount());
			transfer.setPrice(salaryRecord.getPrice());
			transfer.setFromOverplusBalance(availableAmount);
			transfer.setToOverplusBalance(salaryRecord.getToUser().getRemainderMoney().add(salaryRecord.getMoney()));
			transfer.setCreateTime(new Date());
			transfer.setUpdateTime(new Date());
			ResponseBase responseBase = userTransferService.addRecruitWages(transfer);
			if(responseBase.isResponse())
			{
				salaryRecord.setTransferId(transfer.getId());
				salaryRecordService.updateByPrimaryKeySelective(salaryRecord);
				//asycService.pushTransferAccounts(salaryRecord.getAssignUserId(), salaryRecord.getSendeeUserId(),transfer);
				//推送
				asycService.pushPayMoney(salaryRecord.getSendeeUserId(), recruitCredit.getRecruitId(), salaryRecord, transfer);
			}else
			{
				result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg("更新失败");
				return null;
			}
		}
		if(auditStatus == 3)
		{
			//推送
			asycService.pushPayMoney(salaryRecord.getSendeeUserId(), recruitCredit.getRecruitId(), salaryRecord, transfer);
			//授信额度退还
			recruitCreditService.updateUsableMoney(fromUserId, recruitId, availableAmount.add(settlementAmount));
		}
		//日志记录
	    CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
	    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "招聘管理-授信发放记录-发放审核-（"+salaryRecord.getAssignUserId()+"）",sessionUser.getId());
    	result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	/**
	 * 招聘管理GET
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/recruit/recruit/list.htm", method = RequestMethod.GET)
	public String recruitRecordListGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		
		return FrontUtils.findFrontTpl(request, response, model, RECRUUIT_LIST);
	}
	/**
     * 招聘管理POST
     *
     * @param request
     * @param response
     * @throws ClientException 
     */
	@RequestMapping(value = "/recruit/recruit/list.htm", method = RequestMethod.POST)
	public String recruitRecordListPOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) throws IllegalAccessException
	{
		PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
		List<Recruit> recruitList = recruitService.findRecruitList(requestVo);
		PageInfo<Recruit> pageInfo = new PageInfo<Recruit>(recruitList);
		PageHelperModel.responsePageModel(pageInfo,model);
		model.addAttribute("recruitList",pageInfo.getList());
		
		return FrontUtils.findFrontTpl(request, response, model, RECRUUIT_LIST_TPL);
	}
	/**
	 * 删除招聘记录POST
	 * @param request
	 * @param response
	 * @param model
	 * @param recruitId
	 * @return
	 * @throws IllegalAccessException
	 */
	@RequestMapping(value = "/recruit/recruit/delete.htm", method = RequestMethod.POST)
	public String recruitRecordDeletePOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer recruitId) throws IllegalAccessException
	{
		ResponseBase result = ResponseBase.getInitResponse();
		
		Recruit recruitDetail = recruitService.queryRecruitDetail(recruitId);
		recruitService.deleteByRecruitId(recruitId);
		
		//日志记录
	    CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
	    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "招聘管理-招聘列表-删除-（"+recruitDetail.getRecruitTitle()+"）",sessionUser.getId());
	    
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	/**
	 * 更新招聘记录POST
	 * @param request
	 * @param response
	 * @param recruitId
	 * @param validateStatus
	 * @param validateCause
	 * @param recruitStatus
	 * @return
	 */
    @RequestMapping(value="/recruit/recruit/update/status.htm")
    public String updateRecruit(HttpServletRequest request, HttpServletResponse response,Integer recruitId, Integer validateStatus, String validateCause,
			Integer recruitStatus  ){
    	ResponseBase result = ResponseBase.getInitResponse();
    	int updateByRecruitId = recruitService.updateByRecruitId(recruitId, validateStatus, validateCause, recruitStatus);
		Recruit recruitDetail = recruitService.queryRecruitDetail(recruitId);
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
    	}
    	//日志记录
	    CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
	    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "招聘管理-招聘列表-审核-（"+recruitDetail.getRecruitTitle()+"）",sessionUser.getId());
	    
    	result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
    	
    }
    
    /**
	 * 招聘管理-新增修改GET
	 * @param request
	 * @param response
	 * @param model
	 * @author FanCunXin
	 * @return
	 */
	@RequestMapping(value = "/recruit/recruit/edit.htm", method = RequestMethod.GET)
	public String recruitRecordEditGet(HttpServletRequest request,HttpServletResponse response,ModelMap model,int id) 
	{
		List<Category> categoryList = categoryService.getCategoryByType(2);
		model.addAttribute("categoryUserList", categoryList);
		
		Recruit recruit = recruitService.selectByPrimaryKey(id);
		if(!CmsUtils.isNullOrEmpty(recruit))
		{
			model.addAttribute("recruit",recruit);
		}
		return FrontUtils.findFrontTpl(request, response, model, RECRUUIT_EDIT);
	}
	
	/**
     * 招聘管理-新增修改POST
     * @param request
	 * @param response
	 * @param model
	 * @author FanCunXin
	 * @return
	 */
	@RequestMapping(value = "/recruit/recruit/edit.htm", method = RequestMethod.POST)
	public String recruitRecordEditPOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		Recruit recruit = requestVo.getRecruit();
		if(recruit == null)
		{
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("招聘信息不存在");
			ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		}
		recruit.setStartTime(DateUtils.getDate(requestVo.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
		recruit.setEndTime(DateUtils.getDate(requestVo.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
		recruit.setStopTime(DateUtils.getDate(requestVo.getStopTime(), "yyyy-MM-dd HH:mm:ss"));
		recruitService.updateRecruit(recruit);
		
		if(!CmsUtils.isNullOrEmpty(requestVo.getBannerImageList()))
		{
			recruitService.deleteRecruitBanner(recruit.getId());//删除招聘图片
			
			for(String img : requestVo.getBannerImageList())
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
		
		if(!CmsUtils.isNullOrEmpty(requestVo.getCategoryIdList()))
		{
			recruitService.deleteRecruitCategory(recruit.getId());//删除招聘分类
			
			for(Integer id : requestVo.getCategoryIdList())
			{
    			RecruitCategory cay = new RecruitCategory();
    			cay.setRecruitId(recruit.getId());
    			cay.setCategoryId(id);
    			cay.setUpdateTime(new Date());
    			cay.setCreateTime(new Date());
    			recruitService.addRecruitCategory(cay);
			}
		}
		//日志记录
	    CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
	    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "招聘管理-招聘列表-修改-（"+recruit.getRecruitTitle()+"）",sessionUser.getId());
		
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	/**
	 * 查看项目地址地图
	* @Title: recruitAddressMapGet
	* @Description: TODO()
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @param lat
	* @param @param lng
	* @param @return    参数
	* @return String    返回类型
	* @author  上海众宁网络科技有限公司-何俊辉
	* @throws
	 */
	@RequestMapping(value = "/recruit/address/map.htm", method = RequestMethod.GET)
	public String recruitAddressMapGet(HttpServletRequest request,HttpServletResponse response,ModelMap model,String lat,String lng) {
		
		model.addAttribute("lat",lat);
		model.addAttribute("lng",lng);
		return FrontUtils.findFrontTpl(request, response, model, RECRUIT_ADDRESS_MAP);
	}
	/**
	 * 最新公告GET
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/recruit/notice/list.htm", method = RequestMethod.GET)
	public String recruitNoticeListGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		
		return FrontUtils.findFrontTpl(request, response, model, RECRUUIT_NOTICE_LIST);
	}
	
	/**
     * 最新公告POST
     *
     * @param request
     * @param response
     * @throws ClientException 
     */
	@RequestMapping(value = "/recruit/notice/list.htm", method = RequestMethod.POST)
	public String recruitNoticeListPost(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) throws IllegalAccessException
	{
		PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
		List<Notice> noticeList = recruitService.findNoticeList(requestVo);
		PageInfo<Notice> pageInfo = new PageInfo<>(noticeList);
		PageHelperModel.responsePageModel(pageInfo,model);
		model.addAttribute("noticeList",pageInfo.getList());
		
		return FrontUtils.findFrontTpl(request, response, model, RECRUUIT_NOTICE_LIST_TPL);
	}
	
	/**
	 * 删除公告POST
	 * @param request
	 * @param response
	 * @param model
	 * @param noticeId
	 * @return
	 * @throws IllegalAccessException
	 */
	@RequestMapping(value = "/recruit/notice/delete.htm", method = RequestMethod.POST)
	public String recruitNoticeDeletePOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer noticeId) throws IllegalAccessException
	{
		ResponseBase result = ResponseBase.getInitResponse();
		Notice notice = recruitService.queryByNoticeId(noticeId);
		recruitService.deleteByNoticeId(noticeId);
		
		//日志记录
	    CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
	    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "招聘管理-公告管理-删除公告-（"+notice.getNoticeContent()+"）",sessionUser.getId());
		
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	
    /**
	 * 最新公告-新增修改GET
	 * @param request
	 * @param response
	 * @param model
	 * @author FanCunXin
	 * @return
	 */
	@RequestMapping(value = "/recruit/notice/edit.htm", method = RequestMethod.GET)
	public String recruitNoticeEditGet(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer id) 
	{
		Notice notice = recruitService.findNoticeById(id);
		if(!CmsUtils.isNullOrEmpty(notice))
		{
			model.addAttribute("notice",notice);
		}
		return FrontUtils.findFrontTpl(request, response, model, RECRUUIT_NOTICE_EDIT);
	}
	
	/**
     * 最新公告-新增修改POST
     * @param request
	 * @param response
	 * @param model
	 * @author FanCunXin
	 * @return
	 */
	@RequestMapping(value = "/recruit/notice/edit.htm", method = RequestMethod.POST)
	public String recruitNoticeEditPost(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		Notice notice = requestVo.getNotice();
		if(!CmsUtils.isNullOrEmpty(notice.getId()) && notice.getId() > 0)
		{
			notice.setStartTime(DateUtils.getDate(requestVo.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
			notice.setEndTime(DateUtils.getDate(requestVo.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
			notice.setUpdateTime(new Date());
			recruitService.updateNotice(notice);
			//日志记录
		    CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "招聘管理-公告管理-编辑公告-（"+notice.getNoticeContent()+"）",sessionUser.getId());
			

		}else
		{
			notice.setStartTime(DateUtils.getDate(requestVo.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
			notice.setEndTime(DateUtils.getDate(requestVo.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
			notice.setCreateTime(new Date());
			notice.setUpdateTime(new Date());
			recruitService.insertNoticeSelective(notice);
			//日志记录
		    CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "招聘管理-公告管理-添加公告-（"+notice.getNoticeContent()+"）",sessionUser.getId());
			
		}
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	
	/**
	 * 查看报名列表POST
	 * @param request
	 * @param response
	 * @param model
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/recruit/apply/user/list.htm", method = RequestMethod.POST)
	public String recruitapplyRecordListPost(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo)
	{
		PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
		List<RecruitRecord> recruitApplyList = recruitRecordService.queryRecruitUserList(requestVo.getRecruitId());
		PageInfo<RecruitRecord> pageInfo = new PageInfo<RecruitRecord>(recruitApplyList);
		model.addAttribute("recruitApplyList", pageInfo.getList());
		
		return FrontUtils.findFrontTpl(request, response, model, RECRUUIT_APPLY_LIST_TPL);
		
	}
	/**
	 * 查查报名列表GET
	 * @param request
	 * @param response
	 * @param model
	 * @param requestVo
	 * @return
	 */
	@RequestMapping(value="/recruit/apply/user/list.htm", method = RequestMethod.GET)
	public String recruitapplyRecordListGet(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo)
	{
		model.addAttribute("recruitId",requestVo.getRecruitId());
		return FrontUtils.findFrontTpl(request, response, model, RECRUUIT_APPLY_LIST);
	}
	 /**
     * 更新报名工人状态
     * 
     * @param request
     * @param response
     * @author fancunxin
     * @throws ClientException 
     */
    @RequestMapping(value="/recruit/apply/user/update/status.htm", method = RequestMethod.POST)
    public String updateRecordStatus(HttpServletRequest request, HttpServletResponse response,Integer recordId,Integer enlistStatus,String refuseCause)
    {
    	ResponseBase result=ResponseBase.getInitResponse();
    	RecruitRecord record = recruitRecordService.queryRecruitRecord(recordId);
    	recruitRecordService.updateByRecordId(recordId, enlistStatus, refuseCause);
    	
    	//日志记录
	    CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
	    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "招聘管理-报名列表-报名状态-（"+record.getUser().getRealName()+"）",sessionUser.getId());
		
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
    }
    /**
     * 删除报名记录POST
     * @param request
     * @param response
     * @param model
     * @param id
     * @return
     */
	@RequestMapping(value = "/recruit/apply/user/delete.htm", method = RequestMethod.POST)
	public String updateRecordDelete(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer recordId) {
		ResponseBase result = ResponseBase.getInitResponse();
    	RecruitRecord record = recruitRecordService.queryRecruitRecord(recordId);
		recruitRecordService.updateRecordFlag(recordId);

    	//日志记录
	    CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
	    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "招聘管理-报名列表-删除报名-（"+record.getUser().getRealName()+"）",sessionUser.getId());
		
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	/**
	 * 授信机构GET
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/recruit/credit/list.htm", method = RequestMethod.GET)
	public String creditOrganizationListGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		
		return FrontUtils.findFrontTpl(request, response, model, CREDIT_ORGANIZATION_LIST);
	}
	/**
     * 授信机构POST
     *
     * @param request
     * @param response
     * @throws ClientException 
     */
	@RequestMapping(value = "/recruit/credit/list.htm", method = RequestMethod.POST)
	public String creditOrganizationListPOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) throws IllegalAccessException
	{
		PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
		List<Credit> creditList = recruitCreditService.findCreditList(requestVo);
		PageInfo<Credit> pageInfo=new PageInfo<Credit>(creditList);
		PageHelperModel.responsePageModel(pageInfo,model);
		model.addAttribute("creditList",pageInfo.getList());
		
		return FrontUtils.findFrontTpl(request, response, model, CREDIT_ORGANIZATION_LIST_TPL);
	}
	/**
	 * 授信机构删除POST
	 * @param request
	 * @param response
	 * @param model
	 * @param recordId
	 * @return
	 */
	@RequestMapping(value = "/recruit/credit/delete.htm", method = RequestMethod.POST)
	public String creditOrganizationDeletePOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer creditId) {
		ResponseBase result = ResponseBase.getInitResponse();
		Credit creditIdDetail = recruitCreditService.queryCreditIdDetail(creditId);
		recruitCreditService.deleteByCreditId(creditId);

    	//日志记录
	    CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
	    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "招聘管理-授信机构-删除机构-（"+creditIdDetail.getCreditName()+"）",sessionUser.getId());
		
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.DELETE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	
	/**
	 * 修改、新增机构-GET
	 * @param request
	 * @param response
	 * @param model
	 * @author FanCunXin
	 * @return
	 */
	@RequestMapping(value = "/recruit/credit/org/edit.htm", method = RequestMethod.GET)
	public String cmsUserEditGET(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer creditId) 
	{
		if(!CmsUtils.isNullOrEmpty(creditId) && creditId>0)
		{
			Credit credit = recruitCreditService.queryCreditIdDetail(creditId);
			model.addAttribute("credit",credit);
		}
		return FrontUtils.findFrontTpl(request, response, model, CREDIT_ORGANIZATION_EDIT);
	}
	
	/**
	 * 修改、新增机构-POST
	 * @param request
	 * @param response
	 * @param model
	 * @author FanCunXin
	 * @return
	 * @throws IOException 
	 * @throws QiniuException 
	 */
	@RequestMapping(value = "/recruit/credit/org/edit.htm", method = RequestMethod.POST)
	public String cmsUserEditPOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) throws QiniuException, IOException 
	{
		ResponseBase result = ResponseBase.getInitResponse();
		Credit credit = requestVo.getCredit();
		//修改
		if(!CmsUtils.isNullOrEmpty(credit.getId()) && credit.getId() > 0)
		{
			credit.setUpdateTime(new Date());
			result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
			recruitCreditService.updateByPrimaryKeySelective(credit);
			//日志记录
		    CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "招聘管理-授信机构-编辑机构-（"+credit.getCreditName()+"）",sessionUser.getId());
			
		}
		else
		{
			credit.setUpdateTime(new Date());
			credit.setCreateTime(new Date());
			result.setResponseMsg(Constants.Prompt.ADD_SUCCESSFUL);
			recruitCreditService.insertSelective(credit);
			//日志记录
		    CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "招聘管理-授信机构-添加机构-（"+credit.getCreditName()+"）",sessionUser.getId());

		}
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	/**
	 * 项目授信GET
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/recruit/credit/record/list.htm", method = RequestMethod.GET)
	public String creditRecordListGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		
		return FrontUtils.findFrontTpl(request, response, model, CREDIT_RECORD_LIST);
	}
	/**
     * 项目授信POST
     *
     * @param request
     * @param response
     * @throws ClientException 
     */
	@RequestMapping(value = "/recruit/credit/record/list.htm", method = RequestMethod.POST)
	public String creditRecordListPOST(HttpServletRequest request,HttpServletResponse response,ModelMap model,CmsRequestVo requestVo) throws IllegalAccessException
	{
		PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
		List<RecruitCredit> creditRecordList = recruitCreditService.findCreditRecord(requestVo);
		PageInfo<RecruitCredit> pageInfo=new PageInfo<RecruitCredit>(creditRecordList);
		PageHelperModel.responsePageModel(pageInfo,model);
		model.addAttribute("creditRecordList",pageInfo.getList());
		
		return FrontUtils.findFrontTpl(request, response, model, CREDIT_RECORD_LIST_TPL);
	}
	/**
	 * 授信记录删除POST
	 * @param request
	 * @param response
	 * @param creditRecordId
	 * @return
	 */
	@RequestMapping(value="/recruit/credit/record/delete.htm", method = RequestMethod.POST)
	public String deleteCreditRecord(HttpServletRequest request, HttpServletResponse response ,Integer creditRecordId){
		ResponseBase result=ResponseBase.getInitResponse();
		RecruitCredit recruitCredit = recruitCreditService.queryRecruitCreditById(creditRecordId);
		recruitCreditService.deleteCreditRecordById(creditRecordId);
		
		//日志记录
	    CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
	    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "招聘管理-授信记录-删除授信记录-（"+recruitCredit.getRecruit().getUser().getRealName()+'-'+recruitCredit.getRecruit().getRecruitTitle()+"）",sessionUser.getId());

		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("删除成功");
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	/**
	 * 授信记录审核POST
	 * @param request
	 * @param response
	 * @param id
	 * @param totalMoney
	 * @param creditStatus
	 * @param interestRate
	 * @param validateCause
	 * @return
	 */
	@RequestMapping(value="/recruit/credit/record/update/status.htm", method = RequestMethod.POST)
	public String updateCreditRecord(HttpServletRequest request, HttpServletResponse response,Integer id ,String totalMoney,Integer creditStatus,String interestRate, String validateCause){
		ResponseBase result=ResponseBase.getInitResponse();
		RecruitCredit recruitCredit=null;
		if(id != null && id > 0){
			recruitCredit=recruitCreditService.selectCreditRecord(id);
			recruitCredit.setUpdateTime(new Date() );
			if(!CmsUtils.isNullOrEmpty(totalMoney)){
				recruitCredit.setTotalMoney(new BigDecimal(totalMoney));
				recruitCredit.setUsableMoney(new BigDecimal(totalMoney));
				recruitCredit.setInterestRate(interestRate);
			}else{
				recruitCredit.setTotalMoney(new BigDecimal(0));
				recruitCredit.setUsableMoney(new BigDecimal(0));
				recruitCredit.setValidateCause(validateCause);
				recruitCredit.setInterestRate("0");
			}
			
			recruitCredit.setCreditStatus(creditStatus);
			recruitCreditService.updateByPrimaryKeySelective(recruitCredit);
			asycService.pushUserCreditStatus(recruitCredit.getUserId(), recruitCredit.getId());
			
			Recruit recruit = recruitService.selectByPrimaryKey(recruitCredit.getRecruitId());
			User user = userService.selectByPrimaryKey(recruitCredit.getUserId());
			if(!CmsUtils.isNullOrEmpty(user.getOpenid()))
			{
				//发送微信模板消息推送
				try {
					Map<String,Object> template = new HashMap<String,Object>();
					template.put("touser", user.getOpenid());
					template.put("template_id", "UN7Ck04wQL18SmjiHVDZNt-HzbLWQ_7yfLQeAQLy_Ac");
					template.put("url", "www.baidu.com");
					template.put("topcolor", "#44b549");
					
					Map<String,Object> data = new HashMap<String,Object>();
					
					Map<String,String> data1 = new HashMap<String,String>();
					String msg = "恭喜您，您发布的项目已通过授信";
					if(creditStatus == 3)//审核失败
					{
						msg = "非常抱歉，您发布项目未通过授信";
					}
					data1.put("value", msg);
					data.put("first", data1);
					
					Map<String,String> data2 = new HashMap<String,String>();
					data2.put("value", recruit.getRecruitTitle());
					data.put("keyword1", data2);
					
					Map<String,String> data3 = new HashMap<String,String>();
					String msgt = "已通过";
					if(creditStatus == 3)//审核失败
					{
						msgt = "未通过";
					}
					data3.put("value", msgt);
					data.put("keyword2", data3);
					
					Map<String,String> data4 = new HashMap<String,String>();
					data4.put("value", DateUtils.getStringDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
					data.put("keyword3", data4);
					
					Map<String,String> remark = new HashMap<String,String>();
					remark.put("value", "感谢您的使用");
					data.put("remark", remark);
					
					template.put("data",data);
					
					WechatUtils.sendWechatTemplateMassage(wechatService.queryWechatToken(), JsonUtil.map2jsonToString(template));
				} catch (WXException e) {}
			}
			
			log.info("#####################更改状态成功#####################");
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("修改成功");
			ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
			
			//日志记录
		    CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "招聘管理-授信记录-审核-（"+recruitCredit.getRecruit().getUser().getRealName()+'-'+recruitCredit.getRecruit().getRecruitTitle()+"）",sessionUser.getId());

		}
		else
		{
			log.info("#####################更改状态失败#####################");
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("修改失败");
			ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		}
		
		return null;
		
	}
	/**
	 * 查询圈子GET
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/circle/record/list.htm", method = RequestMethod.GET)
	public String queryCircleListGet(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		
		return FrontUtils.findFrontTpl(request, response, model, CIRCLE_RECORD_LIST);
	}
	/**
	 * 查询圈子POST
	 * @param request
	 * @param response
	 * @param model
	 * @param requestVo
	 * @return
	 */
    @RequestMapping(value = "/circle/record/list.htm")
    public String queryCircleListPost(HttpServletRequest request, HttpServletResponse response,ModelMap model,CmsRequestVo requestVo)
    {
    	PageHelper.startPage(requestVo.getCurrentPage(), requestVo.getPageSize());
		List<Circle> circleList = circleService.queryCircleList(requestVo);
		PageInfo<Circle> pageInfo = new PageInfo<Circle>(circleList);
		PageHelperModel.responsePageModel(pageInfo,model);
		model.addAttribute("circleList",pageInfo.getList());
		
		return FrontUtils.findFrontTpl(request, response, model, CIRCLE_RECORD_LIST_TPL);
    	
    }
    
   	@RequestMapping(value="/circle/record/delete.htm", method = RequestMethod.POST)
   	public String deleteCircleRecord(HttpServletRequest request, HttpServletResponse response ,Integer id){
   		ResponseBase result=ResponseBase.getInitResponse();
   		Circle circle = circleService.queryCircleById(id);
   		circleService.deleteCricleById(id);
   		try {
   			asycService.sendAppTemplateFromCircle(id);
		} catch (Exception e) {
			// TODO: handle exception
		} 
   		result.setResponse(Constants.TRUE);
   		result.setResponseCode(Constants.SUCCESS_200);
   		result.setResponseMsg("删除成功");

		//日志记录
	    CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
	    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "营销管理-圈子列表-删除-（"+circle.getUser().getRealName()+"）",sessionUser.getId());

   		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
   		return null;
   	}
   	/**
	 * 授信还款-GET
	 * @param request
	 * @param response
	 * @param model
	 * @param userId
	 * @return
   	 * @throws ParseException 
	 */
	@RequestMapping(value="/recruit/credit/record/repayment.htm", method = RequestMethod.GET)
	public String creditRepaymentGet(HttpServletRequest request,HttpServletResponse response,ModelMap model,Integer id) throws ParseException
	{
		RecruitCredit recruitCredit = recruitCreditService.selectByPrimaryKey(id);
		CmsUser user = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		
		int loanDay=0;
		Date loanTime=recruitCredit.getUpdateTime();
		Date currentTime=new Date();
		loanDay=(int) ((currentTime.getTime()-loanTime.getTime())/1000/60/60/24);
		BigDecimal repaymentAmount=new BigDecimal(0.0);
		BigDecimal interestRate=new BigDecimal(0.0);
		if(loanDay==0)
		{
			repaymentAmount=recruitCredit.getTotalMoney();
		}
		else
		{
			interestRate = new BigDecimal(recruitCredit.getInterestRate());
			repaymentAmount = recruitCredit.getTotalMoney().add(recruitCredit.getTotalMoney().multiply(interestRate).multiply(new BigDecimal(String.valueOf(loanDay))));
		}

		model.addAttribute("recruitCredit",recruitCredit);
		model.addAttribute("loanDay",loanDay);
		model.addAttribute("user",user);
		model.addAttribute("repaymentAmount",repaymentAmount);
		return FrontUtils.findFrontTpl(request, response, model, CREDIT_RECORD_REPAYMENT);
		
	}
	@RequestMapping(value="/recruit/credit/record/repayment.htm", method = RequestMethod.POST)
	public String creditRepaymentPost(HttpServletRequest request,HttpServletResponse response,ModelMap model)
	{
		ResponseBase result = ResponseBase.getInitResponse();
		CreditRepayment reditRepayment = new CreditRepayment();
		String money = request.getParameter("repaymentAmount");
		BigDecimal repMoney=new BigDecimal(money);
		Integer recruitId=Integer.parseInt(request.getParameter("recruitId"));
		CmsUser user = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		String phone=user.getTelphone();
		String idCode=request.getParameter("idCode");
		String sumMoney=request.getParameter("sumMoney");
		BigDecimal totalMoney=new BigDecimal(sumMoney);
		if(totalMoney.compareTo(repMoney)<0)
		{
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("输入金额不能小于还款金额");
			ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
			return null;
		}
		if(idCode.equals(redisUtils.get(Constants.PAYMENT_VERIFIED_CODE + phone)))
		{
			reditRepayment.setBusinessMoney(new BigDecimal(money));
			reditRepayment.setApplication("授信还款");
			reditRepayment.setType(2);
			reditRepayment.setRecruitCreditId(recruitId);
			reditRepayment.setBusinessStatus(2);
			reditRepayment.setUserId(user.getId());
			reditRepayment.setPayeeName(user.getNickName());
			reditRepayment.setCreateTime(new Date());
			reditRepayment.setUpdateTime(new Date());
			recruitCreditService.insertSelective(reditRepayment);
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg(Constants.Prompt.ADD_SUCCESSFUL);
			ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
			
			
		}
		else
		{
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("验证码错误");
			ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		}
		return null;
		
	}
	/**
	 * 获取验证码
	 * @param request
	 * @param response
	 * @param phone
	 * @param type
	 * @return
	 * @throws ClientException
	 */
	@RequestMapping(value = "/get/verification/code.htm",method=RequestMethod.POST)
	public String queryIdenCode(HttpServletRequest request, HttpServletResponse response, ModelMap model,String phone ,Integer type) throws ClientException
	{
		ResponseBase result = ResponseBase.getInitResponse();
		CmsUser user = (CmsUser)SecurityUtils.getSubject().getPrincipal();
		phone=user.getTelphone();
		String code = null;
		if(type==Constants.Number.TEN_INT)
		{
			code = SmsUtil.getSmsCode(phone,  type,!CmsUtils.isNullOrEmpty(user) ? Constants.Number.ONE_INT : Constants.Number.TWO_INT);
			redisUtils.set(Constants.PAYMENT_VERIFIED_CODE + phone, code, Constants.Number.THIRTY_SECOND);// 验证码30分钟有效时间
		}
		result.push("code", code);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg(Constants.Prompt.SEND_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
	}
	/**
	 * 咨询置顶POST
	 * @param request
	 * @param response
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/recruit/recruit/record/stick.htm", method = RequestMethod.POST)
	public String changeRecruitStick(HttpServletRequest request, HttpServletResponse response,ModelMap model,Integer id,Integer type){
		
		ResponseBase result = ResponseBase.getInitResponse();
		Recruit recruit = recruitService.selectByPrimaryKey(id);
		recruitService.changeRecruitStick(id,type);
		
		//日志记录
	    CmsUser sessionUser = (CmsUser)SecurityUtils.getSubject().getPrincipal();
	    cmsService.addOperateLogs(CmsUtils.getIpAddr(request), "招聘管理-招聘列表-置顶-（"+recruit.getRecruitTitle()+"）",sessionUser.getId());

		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponse(Constants.TRUE);
		result.setResponseMsg(Constants.Prompt.UPDATE_SUCCESSFUL);
		ResponseUtils.renderJson(response, JsonUtil.bean2jsonToString(result));
		return null;
		
	}
}
