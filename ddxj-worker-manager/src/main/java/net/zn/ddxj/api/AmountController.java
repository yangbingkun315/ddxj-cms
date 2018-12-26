package net.zn.ddxj.api;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.PaymentRecord;
import net.zn.ddxj.entity.RecruitCredit;
import net.zn.ddxj.entity.SalaryRecord;
import net.zn.ddxj.entity.UserTransfer;
import net.zn.ddxj.entity.UserWithdraw;
import net.zn.ddxj.service.PayService;
import net.zn.ddxj.service.RecruitCreditService;
import net.zn.ddxj.service.SalaryRecordService;
import net.zn.ddxj.service.UserService;
import net.zn.ddxj.service.UserTransferService;
import net.zn.ddxj.service.UserWithdrawService;
import net.zn.ddxj.tool.AsycService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.OrderNumUtils;
import net.zn.ddxj.vo.RequestVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@RestController
@Slf4j
public class AmountController {
	@Autowired
	private UserTransferService userTransferService;//转账,结算
	@Autowired
	private UserWithdrawService userWithdrawService;//提现
	@Autowired
	private PayService payService;//充值
	@Autowired
	private UserService userService;
	@Autowired
	private AsycService asycService;
	@Autowired
	private SalaryRecordService salaryRecordService;
	@Autowired
	private RecruitCreditService recruitCreditService;
	
	/**
	 * 转账
	 * @param request
	 * @param response
	 * @param requestVo
	 * @return
	 */
	@RequestMapping(value = "/manager/query/transfer/list.ddxj")
	public ResponseBase transferList(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo){
		ResponseBase result=ResponseBase.getInitResponse();
		PageHelper.startPage(requestVo.getPageNum(), requestVo.getPageSize());
		List<UserTransfer> transferList = userTransferService.queryAllTransfer(requestVo);
		PageInfo<UserTransfer> pageInfo = new PageInfo<UserTransfer>(transferList);
		if(!CmsUtils.isNullOrEmpty(pageInfo)){
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.push("transferList", pageInfo);
			result.setResponseMsg("查询成功");
		}else{
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.push("transferList", pageInfo);
			result.setResponseMsg("查询失败");
		}
		return result;
		
	}
	/**
	 * 提现
	 * @param request
	 * @param response
	 * @param requestVo
	 * @return
	 */
	@RequestMapping(value = "/manager/query/withdraw/list.ddxj")
	public ResponseBase withdrawList(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo){
		ResponseBase result=ResponseBase.getInitResponse();
		PageHelper.startPage(requestVo.getPageNum(), requestVo.getPageSize());
		List<UserWithdraw> withdrawList = userWithdrawService.queryWithdrawRecord(requestVo);
		PageInfo<UserWithdraw> pageInfo = new PageInfo<UserWithdraw>(withdrawList);
		if(!CmsUtils.isNullOrEmpty(pageInfo)){
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.push("withdrawList", pageInfo);
			result.setResponseMsg("查询成功");
		}else{
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.push("withdrawList", pageInfo);
			result.setResponseMsg("查询失败");
		}
		return result;
		
	}
	/**
	 * 提现详情
	 * @param request
	 * @param response
	 * @param requestVo
	 * @return
	 */
	@RequestMapping(value = "/manager/query/withdraw/details.ddxj")
	public ResponseBase withdrawDetails(HttpServletRequest request, HttpServletResponse response,int withdrawId){
		ResponseBase result=ResponseBase.getInitResponse();
		UserWithdraw withdraw = userWithdrawService.selectByPrimaryKey(withdrawId);
		result.push("withdraw", withdraw);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("查询提现详情成功");
    	log.info("#####################查询提现详情成功#####################");
    	return result;
		
	}
	/**
	 * 提现更新
	 * @param request
	 * @param response
	 * @param id
	 * @param status
	 * @param money
	 * @return
	 */
	@RequestMapping(value = "/manager/update/withdraw/status.ddxj")
	public ResponseBase updateStatus(HttpServletRequest request, HttpServletResponse response,Integer id,Integer withdrawStatus ,Integer withdrawProcess){
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
		
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("更新成功");
		return result;
	}
	/**
	 * 充值
	 * @param request
	 * @param response
	 * @param requestVo
	 * @return
	 */
	@RequestMapping(value = "/manager/query/payment/list.ddxj")
	public ResponseBase paymentList(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo){
		ResponseBase result=ResponseBase.getInitResponse();
		PageHelper.startPage(requestVo.getPageNum(), requestVo.getPageSize());
		List<PaymentRecord> payRecordList = payService.queryPayRecord(requestVo);
		PageInfo<PaymentRecord> pageInfo = new PageInfo<PaymentRecord>(payRecordList);
		if(!CmsUtils.isNullOrEmpty(pageInfo)){
			result.setResponse(Constants.TRUE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.push("payRecordList", pageInfo);
			result.setResponseMsg("查询成功");
		}else{
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.push("payRecordList", pageInfo);
			result.setResponseMsg("查询失败");
		}
		return result;
		
	}
	/**
	 * 查询充值详情
	 * @param request
	 * @param response
	 * @param outTradeNo
	 * @return
	 */
	@RequestMapping(value="/manager/query/payment/details.ddxj")
	public ResponseBase payMentDetails(HttpServletRequest request, HttpServletResponse response,String outTradeNo){
		ResponseBase result=ResponseBase.getInitResponse();
		PaymentRecord paymentRecord=payService.selectPayOutTradeNo(outTradeNo);
		result.push("paymentRecord", paymentRecord);
    	result.setResponse(Constants.TRUE);
    	result.setResponseCode(Constants.SUCCESS_200);
    	result.setResponseMsg("查询充值详情成功");
    	log.info("#####################查询充值详情成功#####################");
    	return result;
	}
	/**
	 * 查询结算
	 * @param request
	 * @param response
	 * @param requestVo
	 * @return
	 */
	@RequestMapping(value="/manager/query/transfer/payroll.ddxj")
	public ResponseBase payroll(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo){
		ResponseBase result=ResponseBase.getInitResponse();
		PageHelper.startPage(requestVo.getPageNum(), requestVo.getPageSize());
		List<UserTransfer> payRollList = userTransferService.queryPayrollList(requestVo);
		PageInfo<UserTransfer> pageInfo = new PageInfo<UserTransfer>(payRollList);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.push("payRollList", pageInfo);
		result.setResponseMsg("查询成功");
		return result;
	}
	/**
	 * 查询转账
	 * @param request
	 * @param response
	 * @param requestVo
	 * @return
	 */
	@RequestMapping(value="/manager/query/transfer/record.ddxj")
	public ResponseBase queryTransferLists(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo){
		ResponseBase result=ResponseBase.getInitResponse();
		PageHelper.startPage(requestVo.getPageNum(), requestVo.getPageSize());
		List<UserTransfer> transferLists = userTransferService.queryTransferLists(requestVo);
		PageInfo<UserTransfer> pageInfo = new PageInfo<UserTransfer>(transferLists);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.push("transferLists", pageInfo);
		result.setResponseMsg("查询成功");
		return result;
	}
	/**
	 * 查询提现
	 * @param request
	 * @param response
	 * @param requestVo
	 * @return
	 */
	@RequestMapping(value="/manager/query/withdraw/record.ddxj")
	public ResponseBase selectWithdrawList(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo){
		ResponseBase result=ResponseBase.getInitResponse();
		PageHelper.startPage(requestVo.getPageNum(), requestVo.getPageSize());
		List<UserTransfer> withdrawLists = userTransferService.selectWithdrawList(requestVo);
		PageInfo<UserTransfer> pageInfo = new PageInfo<UserTransfer>(withdrawLists);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.push("withdrawLists", pageInfo);
		result.setResponseMsg("查询成功");
		return result;
	}
	/**
	 * 查询充值
	 * @param request
	 * @param response
	 * @param requestVo
	 * @return
	 */
	@RequestMapping(value="/manager/query/payment/record.ddxj")
	public ResponseBase querypaymentRecordList(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo){
		ResponseBase result=ResponseBase.getInitResponse();
		PageHelper.startPage(requestVo.getPageNum(), requestVo.getPageSize());
		List<UserTransfer> recordLists = userTransferService.querypaymentRecordList(requestVo);
		PageInfo<UserTransfer> pageInfo = new PageInfo<UserTransfer>(recordLists);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.push("recordLists", pageInfo);
		result.setResponseMsg("查询成功");
		return result;
	}
	/**
	 * 查询salary记录
	 * @param request
	 * @param response
	 * @param requestVo
	 * @return
	 */
	@RequestMapping(value="/manager/query/salary/list.ddxj")
	public ResponseBase querySalaryRecord(HttpServletRequest request, HttpServletResponse response,RequestVo requestVo){
		ResponseBase result=ResponseBase.getInitResponse();
		PageHelper.startPage(requestVo.getPageNum(), requestVo.getPageSize());
		List<SalaryRecord> salaryList = salaryRecordService.querySalaryRecord(requestVo);
		PageInfo<SalaryRecord> pageInfo = new PageInfo<SalaryRecord>(salaryList);
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.push("salaryList", pageInfo);
		result.setResponseMsg("查询成功");
		return result;
	}
	/**
	 * 授信发放审核
	 * @param request
	 * @param response
	 * @param salaryId
	 * @param auditStatus
	 * @return
	 */
	@RequestMapping(value="/manager/query/salary/update.ddxj")
	public ResponseBase updateSalaryRecord(HttpServletRequest request, HttpServletResponse response,Integer salaryId,Integer auditStatus){
		ResponseBase result=ResponseBase.getInitResponse();
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
				return result;
			}
		}
		if(auditStatus == 3)
		{
			//推送
			asycService.pushPayMoney(salaryRecord.getSendeeUserId(), recruitId, salaryRecord, transfer);
			recruitCreditService.updateUsableMoney(fromUserId, recruitId, availableAmount.add(settlementAmount));

		}
		result.setResponse(Constants.TRUE);
		result.setResponseCode(Constants.SUCCESS_200);
		result.setResponseMsg("更新成功");
		return result;
	}


}
