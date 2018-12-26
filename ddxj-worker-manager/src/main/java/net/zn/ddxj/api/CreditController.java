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
import net.zn.ddxj.entity.Credit;
import net.zn.ddxj.entity.Recruit;
import net.zn.ddxj.entity.RecruitCredit;
import net.zn.ddxj.entity.User;
import net.zn.ddxj.service.RecruitCreditService;
import net.zn.ddxj.service.RecruitService;
import net.zn.ddxj.service.UserService;
import net.zn.ddxj.tool.AsycService;
import net.zn.ddxj.tool.WechatService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.DateUtils;
import net.zn.ddxj.utils.json.JsonUtil;
import net.zn.ddxj.utils.wechat.WXException;
import net.zn.ddxj.utils.wechat.WechatUtils;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@RestController
@Slf4j
/**
 * 授信管理
 * @ClassName CreditController
 * @author ddxj
 * @date 2018年5月3日16:23:35
 */
public class CreditController {
	@Autowired
	private RecruitCreditService recruitCreditService;
	@Autowired
	private AsycService asycService;
	@Autowired
	private UserService userService;
	@Autowired
	private RecruitService recruitService;
	@Autowired
	private WechatService wechatService;

	/**
	 * 查询机构列表
	 * @param request
	 * @param response
	 * @param requestVo
	 * @return
	 */
	@RequestMapping(value="/manager/credit/list.ddxj")
	public ResponseBase queryCreditList(HttpServletRequest request, HttpServletResponse response,CmsRequestVo requestVo){
		ResponseBase result=ResponseBase.getInitResponse();
		PageHelper.startPage(requestVo.getPageNum(), requestVo.getPageSize());
		List<Credit> creditList = recruitCreditService.findCreditList(requestVo);
		PageInfo<Credit> pageInfo=new PageInfo<Credit>(creditList);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("查询授信机构列表成功");
		result.push("creditList", pageInfo);
		
		return result;
		
	}
	/**
	 * 通过id查询机构详情
	 * @param request
	 * @param response
	 * @param creditId
	 * @return
	 */
	@RequestMapping(value = "/manager/credit/details.ddxj")
	public ResponseBase queryCreditDetail(HttpServletRequest request, HttpServletResponse response,Integer  creditId){
		ResponseBase result=ResponseBase.getInitResponse();
		Credit credit = recruitCreditService.queryCreditIdDetail(creditId);
		result.push("credit", credit);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("查询成功");
		return result;
		
	}
	/**
	 * 通过ID删除授信机构
	 * @param request
	 * @param response
	 * @param creditId
	 * @return
	 */
	@RequestMapping(value="/manager/credit/delete.ddxj")
	public ResponseBase deleteCredit(HttpServletRequest request, HttpServletResponse response ,Integer creditId){
		ResponseBase result=ResponseBase.getInitResponse();
		recruitCreditService.deleteByCreditId(creditId);
		result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("删除成功");
		return result;
		
	}
	/**
	 * 增加修改
	 * @param request
	 * @param response
	 * @param credit
	 * @return
	 */
	@RequestMapping(value="/manager/credit/update.ddxj")
	public ResponseBase addOrUpdate(HttpServletRequest request, HttpServletResponse response, Integer id,String creditName,String creditCode,String creditLogo,String creditDesc,String creditAddress){
		ResponseBase result=ResponseBase.getInitResponse();
		Credit credit=null;
		if(id != null && id > 0){
			credit = recruitCreditService.queryCreditIdDetail(id);
			credit.setCreditName(creditName);
			credit.setCreditCode(creditCode);
			credit.setCreditLogo(creditLogo);
			credit.setCreditDesc(creditDesc);
			credit.setCreditAddress(creditAddress);
			credit.setUpdateTime(new Date());
			recruitCreditService.updateByPrimaryKeySelective(credit);
			result.setResponse(Constants.TRUE);
	    	result.setResponseCode(Constants.SUCCESS_200);
	    	result.setResponseMsg("修改成功");
			
		}else{
			credit = new Credit();
			credit.setCreditName(creditName);
			credit.setCreditCode(creditCode);
			credit.setCreditLogo(creditLogo);
			credit.setCreditDesc(creditDesc);
			credit.setCreditAddress(creditAddress);
			credit.setCreateTime(new Date());
			credit.setUpdateTime(new Date());
			recruitCreditService.insertSelective(credit);			
			result.setResponse(Constants.TRUE);
	    	result.setResponseCode(Constants.SUCCESS_200);
	    	result.setResponseMsg("增加成功");
		}
		return result;
	}
	/**
	 * 查询授信记录
	 * @param request
	 * @param response
	 * @param requestVo
	 * @return
	 */
	@RequestMapping(value="/manager/query/creditRecord.ddxj")
	public ResponseBase queryCreditRecord(HttpServletRequest request, HttpServletResponse response,CmsRequestVo requestVo){
		ResponseBase result=ResponseBase.getInitResponse();
		PageHelper.startPage(requestVo.getPageNum(), requestVo.getPageSize());
		List<RecruitCredit> creditRecordList = recruitCreditService.findCreditRecord(requestVo);
		PageInfo<RecruitCredit> pageInfo=new PageInfo<RecruitCredit>(creditRecordList);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("查询授信记录成功");
		result.push("creditRecordList", pageInfo);
		return result;
		
	}
	/**
	 * 查询授信记录详情
	 * @param request
	 * @param response
	 * @param creditRecordId
	 * @return
	 */
	@RequestMapping(value="/manager/query/creditRecord/details.ddxj")
	public ResponseBase findCreditRecord(HttpServletRequest request, HttpServletResponse response ,Integer id){
		ResponseBase result=ResponseBase.getInitResponse();
		RecruitCredit record = recruitCreditService.selectCreditRecord(id);
		result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.push("record", JsonUtil.bean2jsonObject(record));
    	result.setResponseMsg("查询成功");
		return result;
		
	}
	/**
	 * 更新授信记录
	 * @param request
	 * @param response
	 * @param record
	 * @return
	 */
	@RequestMapping(value="/manager/update/creditRecord.ddxj")
	public ResponseBase updateCreditRecord(HttpServletRequest request, HttpServletResponse response,Integer id ,String totalMoney,Integer creditStatus,String interestRate, String validateCause){
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
		}else{
			log.info("#####################更改状态失败#####################");
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("修改失败");
		}
		
		return result;
		
	}
	/**
	 * 删除授信记录
	 * @param request
	 * @param response
	 * @param creditRecordId
	 * @return
	 */
	@RequestMapping(value="/manager/delete/creditRecord.ddxj")
	public ResponseBase deleteCreditRecord(HttpServletRequest request, HttpServletResponse response ,Integer creditRecordId){
		ResponseBase result=ResponseBase.getInitResponse();
		recruitCreditService.deleteCreditRecordById(creditRecordId);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("删除成功");
		return result;
	}

	

}
