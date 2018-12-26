package net.zn.ddxj.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.entity.UserTransfer;
import net.zn.ddxj.vo.RequestVo;

public interface UserTransferService {
	ResponseBase addRecruitWages(UserTransfer userTransfer);

	int addUserWithdraw(UserTransfer userTransfer);

	List<UserTransfer> queryCapitalChangeRecord(Integer userId);
	
	List<UserTransfer> queryCapitalChangeRecordByType(Integer userId,Integer transferType);
	
	UserTransfer querySettlementAndTransfer(Integer transferId,Integer transferType);//查询转账,结算记录

	List<UserTransfer> queryAllTransfer(RequestVo requestVo);
	List<UserTransfer> queryPayrollList(RequestVo requestVo);//查询结算记录

	List<UserTransfer> queryTransferLists(RequestVo requestVo);//查询转账记录

	List<UserTransfer> selectWithdrawList(RequestVo requestVo);//查询提现记录

	List<UserTransfer> querypaymentRecordList(RequestVo requestVo);//查询充值记录

	List<UserTransfer> queryUserTransferDetails(Map<String, Object> param);

	int deleteUserTransfer(Integer userId);//删除转账记录

	int delUserTransferRecord(Integer userId);
	
	/**
	 * 转邀请奖励金
	 * @param userId 用户ID
	 * @param bnous 奖励金
	 * @param desc 转账描述
	 */
	void addTransferRecord(Integer userId, BigDecimal bnous,String desc);

	/**
	 * 通过withdrawId查询UserTransfer
	 * @param whthdrawId
	 * @return
	 */
	UserTransfer selectByWithdrawId(Integer withdrawId);

	List<UserTransfer> findUserTransferRecord(Integer userId);
}
