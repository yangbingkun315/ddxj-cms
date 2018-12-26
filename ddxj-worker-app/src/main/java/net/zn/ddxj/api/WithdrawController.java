package net.zn.ddxj.api;

import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.zn.ddxj.base.BaseController;
import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.UserBank;
import net.zn.ddxj.entity.UserWithdraw;
import net.zn.ddxj.service.UserService;
import net.zn.ddxj.service.UserWithdrawService;
import net.zn.ddxj.tool.AsycService;
import net.zn.ddxj.utils.CmsUtils;
/**
 * 提现模块
 * @author ddxj
 *
 */
@RestController
@Slf4j
public class WithdrawController extends BaseController{
	@Autowired
	private UserWithdrawService userWithdrawService;
	@Autowired
	private UserService userService;
	@Autowired
	private AsycService asycService;
	@RequestMapping(value="/add/withdrawals/info.ddxj")
	public ResponseBase addWithdrawalsInfo(HttpServletRequest request, HttpServletResponse response, Integer userId, String bankCard,String withdrawMoney){
		ResponseBase result=ResponseBase.getInitResponse();
		UserBank userBank=userService.findUserBank(userId,bankCard);
		if(CmsUtils.isNullOrEmpty(userBank)){
			result.setResponse(Constants.FALSE);
        	result.setResponseCode(Constants.SUCCESS_200);
        	result.setResponseMsg(Constants.Prompt.NOT_BOND_CARD);
        	return result;
		}else{
    		//查询提现人角色
    		Integer role = userService.queryUserDetail(userId).getRole();
    		if(role==Constants.Number.ONE_INT)//工人
    		{
    			//单笔2W
    			BigDecimal tranMoney=new BigDecimal(withdrawMoney);
    			if(tranMoney.compareTo(new BigDecimal(Constants.Number.WORKER_SINGLE_MONEY))>0 )
    			{
					result.setResponse(Constants.FALSE);
    	        	result.setResponseCode(Constants.SUCCESS_200);
    	        	result.setResponseMsg(Constants.Prompt.SINGLE_WITHDRAW_AMOUNT+Constants.Prompt.WORKER_SINGLE_AMOUNT);
    	        	return result;
    			}
    			
    			//单日3W
    			BigDecimal money = userService.totalUserTransferMoney(userId,Constants.Number.FOUR_INT);
    			if(!CmsUtils.isNullOrEmpty(money))
    			{
    				if((tranMoney.add(money)).compareTo(new BigDecimal(Constants.Number.WORKER_SINGLE_AMOUNT))>0)
        			{
        				result.setResponse(Constants.FALSE);
        	        	result.setResponseCode(Constants.SUCCESS_200);
        	        	result.setResponseMsg(Constants.Prompt.WITHDRAW_BOUND_AMOUNT+Constants.Prompt.WORKER_ONE_DAY_AMOUNT);
        	        	return result;
        			}
    			}
    			//次数3次
    			int total = userService.countUserTransfer(userId,Constants.Number.FOUR_INT);
    			if(total>=Constants.Number.THREE_INT)
    			{
    				result.setResponse(Constants.FALSE);
    	        	result.setResponseCode(Constants.SUCCESS_200);
    	        	result.setResponseMsg(Constants.Prompt.WITHDRAW_NUM+Constants.Prompt.WORKER_NUM);
    	        	return result;
    			}
    			
    			
    			
    		}
    		else if(role==Constants.Number.TWO_INT)//工头
    		{
    			//单笔10W
    			BigDecimal tranMoney=new BigDecimal(withdrawMoney);
    			if(tranMoney.compareTo(new BigDecimal(Constants.Number.BOSS_SINGLE_MONEY))>0 )
    			{
					result.setResponse(Constants.FALSE);
    	        	result.setResponseCode(Constants.SUCCESS_200);
    	        	result.setResponseMsg(Constants.Prompt.SINGLE_WITHDRAW_AMOUNT+Constants.Prompt.GANGER_SINGLE_AMOUNT);
    	        	return result;
    			}
    			//单日50W
    			BigDecimal money = userService.totalUserTransferMoney(userId,Constants.Number.FOUR_INT);
    			if(!CmsUtils.isNullOrEmpty(money))
    			{
    				if((tranMoney.add(money)).compareTo(new BigDecimal(Constants.Number.BOSS_SINGLE_AMOUNT))>0)
        			{
        				result.setResponse(Constants.FALSE);
        	        	result.setResponseCode(Constants.SUCCESS_200);
        	        	result.setResponseMsg(Constants.Prompt.WITHDRAW_BOUND_AMOUNT+Constants.Prompt.GANGER_ONE_DAY_AMOUNT);
        	        	return result;
        			}
    			}
    			//次数5次
    			int total = userService.countUserTransfer(userId,Constants.Number.FOUR_INT);
    			if(total>=Constants.Number.FIVE_INT)
    			{
    				result.setResponse(Constants.FALSE);
    	        	result.setResponseCode(Constants.SUCCESS_200);
    	        	result.setResponseMsg(Constants.Prompt.WITHDRAW_NUM+Constants.Prompt.GANGER_NUM);
    	        	return result;
    			}
    			
    			
    		}
    		
			UserWithdraw userWithdraw=new UserWithdraw();		
			userWithdraw.setUserId(userId);
			userWithdraw.setWithdrawStatus(0);
			userWithdraw.setWithdrawProcess(1);
			userWithdraw.setMoney(new BigDecimal(withdrawMoney));
			userWithdraw.setCreateTime(new Date());
			userWithdraw.setUpdateTime(new Date());
			userWithdraw.setBankOn(userBank.getBankCard());
			userWithdraw.setBankName(userBank.getBankName());
			userWithdraw.setBankType(userBank.getBankType());
			userWithdrawService.addWithdrawalsInfo(userWithdraw);//提现成功
			asycService.addManagerMessage(Constants.Number.THREE_INT,"用户ID："+userWithdraw.getUserId()+"提现"+withdrawMoney+",卡号："+userBank.getBankCard()+"，开户银行："+userBank.getBankName()+"，开户所在地："+userBank.getAddress(),"您有一条新的提现信息，请及时处理。" , null);
			result.setResponse(Constants.TRUE);
        	result.setResponseCode(Constants.SUCCESS_200);
        	result.setResponseMsg(Constants.Prompt.CASH_WITHDRAWAL_SUCCESSFUL);
		}
		return result;
	}

}
