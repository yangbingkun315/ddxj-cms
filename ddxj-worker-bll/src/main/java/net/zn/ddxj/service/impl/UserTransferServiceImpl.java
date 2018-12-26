package net.zn.ddxj.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.CreditRepayment;
import net.zn.ddxj.entity.RecruitCredit;
import net.zn.ddxj.entity.SalaryRecord;
import net.zn.ddxj.entity.User;
import net.zn.ddxj.entity.UserTransfer;
import net.zn.ddxj.mapper.CreditRepaymentMapper;
import net.zn.ddxj.mapper.RecruitCreditMapper;
import net.zn.ddxj.mapper.RecruitRecordMapper;
import net.zn.ddxj.mapper.SalaryRecordMapper;
import net.zn.ddxj.mapper.UserMapper;
import net.zn.ddxj.mapper.UserTransferMapper;
import net.zn.ddxj.service.UserTransferService;
import net.zn.ddxj.tool.AsycService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.OrderNumUtils;
import net.zn.ddxj.vo.RequestVo;

@Service
@Component
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
public class UserTransferServiceImpl implements UserTransferService {
	@Autowired
	private UserTransferMapper userTransferMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private RecruitCreditMapper recruitCreditMapper;
	@Autowired
	private RecruitRecordMapper recruitRecordMapper;
	@Autowired
	private CreditRepaymentMapper creditRepaymentMapper;
	@Autowired
	private SalaryRecordMapper salaryRecordMapper;
	@Autowired
	private AsycService asycService;
	@Override
	public ResponseBase addRecruitWages(UserTransfer userTransfer) {
		ResponseBase result=ResponseBase.getInitResponse();
		//转账人编号
		Integer fromUserId=userTransfer.getFromUserId();
		//获取转账人的信息
		User fromUser=userMapper.queryUserDetail(fromUserId);
		//转账人的金额
		BigDecimal fromUserMoney=fromUser.getRemainderMoney();
		//收款人的编号
		Integer toUserId=userTransfer.getToUserId();
		//收款人的信息
		User toUser=userMapper.queryUserDetail(toUserId);
		//收款人的金额
		BigDecimal toUserMoney=toUser.getRemainderMoney();
		//招聘ID
		Integer recruitId=userTransfer.getRecruitId();
		//转账金额
		BigDecimal transferMoney=userTransfer.getMoney();

		if(transferMoney.compareTo(BigDecimal.ZERO) == 0 || transferMoney.compareTo(BigDecimal.ZERO) == -1)
		{
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("金额不能小于0元");
			return result;
		}
		if(!CmsUtils.isNullOrEmpty(userTransfer.getRecruitId()))//结算工资
		{
			//转账方式
			if(userTransfer.getTransferWay()==1)//信用结算
			{
				RecruitCredit recruitCreditRecord=recruitCreditMapper.queryRecruitCredit(fromUserId, recruitId);
				if(CmsUtils.isNullOrEmpty(recruitCreditRecord))
				{
					result.setResponse(Constants.FALSE);
					result.setResponseCode(Constants.SUCCESS_200);
					result.setResponseMsg("该用户未授信");
					return result;
				}
				else
				{
					if(recruitCreditRecord.getCreditStatus() == 2)//如果授信状态为已经授信
					{
						//可用金额
						BigDecimal usableMoney=recruitCreditRecord.getUsableMoney();
						if(usableMoney.compareTo(BigDecimal.ZERO) >= 0)//可用金额大于等于0
						{
							//设置剩余金额
							userTransfer.setFromOverplusBalance(usableMoney);
							userTransfer.setToOverplusBalance(toUserMoney.add(transferMoney));
							userTransferMapper.insertSelective(userTransfer);
							//更新转账人的可用金额
							//recruitCreditMapper.updateUsableMoney(fromUserId, recruitId, usableMoney);
							//更新收款人的金额
							userMapper.updateToUserMoney(toUserMoney.add(transferMoney), toUserId);
							//更新结算状态
							recruitRecordMapper.updateBlanceStatus(toUserId, recruitId, 1);
							//向授信还款记录表添加记录
							CreditRepayment record=new CreditRepayment();
							record.setRecruitCreditId(recruitId);
							record.setUserId(toUserId);
							record.setPayeeName(toUser.getRealName());
							record.setCreateTime(new Date());
							record.setUpdateTime(new Date());
							record.setApplication(userTransfer.getTransferDesc());
							record.setBusinessMoney(transferMoney);
							record.setType(1);
							creditRepaymentMapper.insertSelective(record);
							creditRepaymentMapper.updateCreditRepaymentRecord(record.getId());//更改授信记录状态
							result.setResponse(Constants.TRUE);
							result.setResponseCode(Constants.SUCCESS_200);
							result.setResponseMsg("审核成功");
							return result;
						}
						else
						{
							result.setResponse(Constants.FALSE);
							result.setResponseCode(Constants.SUCCESS_200);
							result.setResponseMsg("该用户已授信,可用额度不够结算工资");
							return result;
						}
					}
					else if(recruitCreditRecord.getCreditStatus() == 3)//授信未通过
					{
						result.setResponse(Constants.FALSE);
						result.setResponseCode(Constants.SUCCESS_200);
						result.setResponseMsg("授信未通过");
						return result;
					}
					else
					{
						result.setResponse(Constants.FALSE);
						result.setResponseCode(Constants.SUCCESS_200);
						result.setResponseMsg("授信审核中");
						return result;
					}
					
				}
			}
			else if(userTransfer.getTransferWay()==2)//余额结算
			{
				if((fromUserMoney.compareTo(transferMoney)>0) ||(fromUserMoney.compareTo(transferMoney))==0){
					//设置剩余金额
					userTransfer.setFromOverplusBalance(fromUserMoney.subtract(transferMoney));
					userTransfer.setToOverplusBalance(toUserMoney.add(transferMoney));
					userTransferMapper.insertSelective(userTransfer);					
					userMapper.updateFromUserMoney(fromUserMoney.subtract(transferMoney), fromUserId);
					userMapper.updateToUserMoney(toUserMoney.add(transferMoney), toUserId);
					recruitRecordMapper.updateBlanceStatus(toUserId, recruitId, 1);
					//向结算表中添加记录
					SalaryRecord salaryRecord=new SalaryRecord();
					salaryRecord.setRecruitId(recruitId);
					salaryRecord.setAssignUserId(fromUserId);
					salaryRecord.setSendeeUserId(toUserId);
					salaryRecord.setMoney(transferMoney);
					salaryRecord.setPrice(userTransfer.getPrice());
					salaryRecord.setCount(userTransfer.getCount());
					salaryRecord.setUnit(userTransfer.getUnit());
					salaryRecord.setTransferDesc(userTransfer.getTransferDesc());
					salaryRecord.setTransferWay(userTransfer.getTransferWay());
					salaryRecord.setTransferType(userTransfer.getTransferType());
					salaryRecord.setTransferId(userTransfer.getId());
					salaryRecord.setCreateTime(new Date());
					salaryRecord.setUpdateTime(new Date());
					salaryRecord.setAuditStatus(2);
					salaryRecordMapper.insertSelective(salaryRecord);
					//发放工资推送
					asycService.pushPayMoney(toUserId, recruitId, salaryRecord, userTransfer);
					result.setResponse(Constants.TRUE);
					result.setResponseCode(Constants.SUCCESS_200);
					result.setResponseMsg("结算成功");
					return result;
				}else{
					result.setResponse(Constants.FALSE);
					result.setResponseCode(Constants.SUCCESS_200);
					result.setResponseMsg("结算失败,余额不足");
					return result;
				}
				
			}
		}
		else //转账
		{
			//支付方式		
			if(userTransfer.getTransferWay()==2)//余额支付
			{
				if(!fromUser.getPhone().equals(toUser.getPhone()))
				{
					if(fromUserMoney.compareTo(transferMoney)>0 ||fromUserMoney.compareTo(transferMoney)==0){
						//设置剩余金额
						userTransfer.setFromOverplusBalance(fromUserMoney.subtract(transferMoney));
						userTransfer.setToOverplusBalance(toUserMoney.add(transferMoney));
						userTransferMapper.insertSelective(userTransfer);
						userMapper.updateFromUserMoney((fromUser.getRemainderMoney()).subtract(transferMoney),userTransfer.getFromUserId());
						userMapper.updateToUserMoney((toUser.getRemainderMoney()).add(transferMoney),userTransfer.getToUserId());
						asycService.pushTransferAccounts(fromUserId, toUserId,userTransfer);//发送资金变动
						result.setResponse(Constants.TRUE);
						result.setResponseCode(Constants.SUCCESS_200);
						result.setResponseMsg("转账成功");
						
						return result;
					}else{
						result.setResponse(Constants.FALSE);
						result.setResponseCode(Constants.SUCCESS_200);
						result.setResponseMsg("转账失败,余额不足");
						return result;
					}
				}
				else
				{
					result.setResponse(Constants.FALSE);
					result.setResponseCode(Constants.SUCCESS_200);
					result.setResponseMsg("不能给自己转账");
					return result;
				}
				
				
			}
			else if(userTransfer.getTransferWay()==3)//银行卡支付
			{
				
			}
		}
		return result;
	}
	@Override
	public void addTransferRecord(Integer userId, BigDecimal bnous,String desc){
		//收款人的信息
		User toUser=userMapper.queryUserDetail(userId);
		//收款人的金额
		BigDecimal toUserMoney=toUser.getRemainderMoney();

		UserTransfer userTransfer = new UserTransfer();
    	userTransfer.setFromUserId(1);
    	userTransfer.setToUserId(userId);
    	userTransfer.setMoney(bnous);
		userTransfer.setTransferType(5);
		userTransfer.setCreateTime(new Date());
		userTransfer.setUpdateTime(new Date());
		userTransfer.setTransferDesc(desc);
		userTransfer.setTransferWay(2);
		userTransfer.setToOverplusBalance(toUserMoney.add(bnous));
		userTransfer.setOrderNo(OrderNumUtils.getOredrNum());
    	userTransferMapper.insertSelective(userTransfer);
	}
	
	@Override
	public int addUserWithdraw(UserTransfer userTransfer) {
		// TODO Auto-generated method stub
		return userTransferMapper.addUserWithdraw(userTransfer);
	}
	@Override
	public List<UserTransfer> queryCapitalChangeRecord(Integer userId) {
		// TODO Auto-generated method stub
		return userTransferMapper.queryCapitalChangeRecord(userId);
	}
	@Override
	public UserTransfer querySettlementAndTransfer(Integer transferId, Integer transferType) {
		// TODO Auto-generated method stub
		return userTransferMapper.querySettlementAndTransfer(transferId, transferType);
	}
	@Override
	public List<UserTransfer> queryAllTransfer(RequestVo requestVo) {
		// TODO Auto-generated method stub
		return userTransferMapper.queryAllTransfer(requestVo);
	}
	@Override
	public List<UserTransfer> queryPayrollList(RequestVo requestVo) {
		// TODO Auto-generated method stub
		return userTransferMapper.queryPayrollList(requestVo);
	}
	@Override
	public List<UserTransfer> queryTransferLists(RequestVo requestVo) {
		// TODO Auto-generated method stub
		return userTransferMapper.queryTransferLists(requestVo);
	}
	@Override
	public List<UserTransfer> selectWithdrawList(RequestVo requestVo) {
		// TODO Auto-generated method stub
		return userTransferMapper.selectWithdrawList(requestVo);
	}
	@Override
	public List<UserTransfer> querypaymentRecordList(RequestVo requestVo) {
		// TODO Auto-generated method stub
		return userTransferMapper.querypaymentRecordList(requestVo);
	}
	@Override
	public List<UserTransfer> queryUserTransferDetails(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return userTransferMapper.queryUserTransferDetails(param);
	}
	@Override
	public int deleteUserTransfer(Integer userId) {
		// TODO Auto-generated method stub
		return userTransferMapper.deleteUserTransfer(userId);
	}
	@Override
	public int delUserTransferRecord(Integer userId) {
		// TODO Auto-generated method stub
		return userTransferMapper.delUserTransferRecord(userId);
	}

	@Override
	public UserTransfer selectByWithdrawId(Integer withdrawId) {
		return userTransferMapper.selectByWithdrawId(withdrawId);
	}
	@Override
	public List<UserTransfer> queryCapitalChangeRecordByType(Integer userId, Integer transferType) {
		return userTransferMapper.queryCapitalChangeRecordByType(userId, transferType);
	}
	@Override
	public List<UserTransfer> findUserTransferRecord(Integer userId) {
		// TODO Auto-generated method stub
		return userTransferMapper.findUserTransferRecord(userId);
	}

}
