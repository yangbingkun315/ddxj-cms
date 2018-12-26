package net.zn.ddxj.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.User;
import net.zn.ddxj.entity.UserBank;
import net.zn.ddxj.entity.UserTransfer;
import net.zn.ddxj.entity.UserWithdraw;
import net.zn.ddxj.mapper.UserBankMapper;
import net.zn.ddxj.mapper.UserMapper;
import net.zn.ddxj.mapper.UserTransferMapper;
import net.zn.ddxj.mapper.UserWithdrawMapper;
import net.zn.ddxj.service.UserWithdrawService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.utils.OrderNumUtils;
import net.zn.ddxj.vo.RequestVo;

@Service
@Component
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
public class UserWithdrawServiceImpl implements UserWithdrawService {
	@Autowired
	private UserWithdrawMapper userWithdrawMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired 
	private UserBankMapper userBankMapper;
	@Autowired
	private UserTransferMapper userTransferMapper;
	@Override
	public ResponseBase addWithdrawalsInfo(UserWithdraw userWithdraw) {
		ResponseBase result=ResponseBase.getInitResponse();		
		Integer userId=userWithdraw.getUserId();
		//通过userId查询用户信息
		User user=userMapper.queryUserDetail(userId);
		//获取用户的提现金额
		BigDecimal withdrawMoney=userWithdraw.getMoney();
		if(CmsUtils.isNullOrEmpty(user)){
			result.setResponse(Constants.FALSE);
			result.setResponseCode(Constants.SUCCESS_200);
			result.setResponseMsg("查询不到该用户信息");
			return result;
		}else{
			//获取用户的金额
			BigDecimal money=user.getRemainderMoney();
			if(withdrawMoney.compareTo(BigDecimal.ZERO) == 0 || withdrawMoney.compareTo(BigDecimal.ZERO) == -1)
			{
				result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg("提现金额不能小于0元");
				return result;
			}
			
			if((money.compareTo(withdrawMoney)>0) ||(money.compareTo(withdrawMoney)==0)){
				List<UserBank> userBankLists = userBankMapper.findUserBankList(userId);
				if(CmsUtils.isNullOrEmpty(userBankLists)){
					result.setResponse(Constants.FALSE);
					result.setResponseCode(Constants.SUCCESS_200);
					result.setResponseMsg("该用户未绑卡");
					return result;
				}else{
					result.setResponse(Constants.TRUE);
					result.setResponseCode(Constants.SUCCESS_200);
					result.setResponseMsg("该用户已绑卡");
				}
				//获取用户的银行卡号
				String bankCard=userWithdraw.getBankOn();
				UserBank userBank = userBankMapper.findUserBank(userId, bankCard);
				if(CmsUtils.isNullOrEmpty(userBank)){
					result.setResponse(Constants.FALSE);
					result.setResponseCode(Constants.SUCCESS_200);
					result.setResponseMsg("查询银行卡失败");
					return result;
				}else{
					result.setResponse(Constants.TRUE);
					result.setResponseCode(Constants.SUCCESS_200);
					result.setResponseMsg("查询银行卡成功");
				}
				userWithdraw.setUserId(userId);
				userWithdraw.setWithdrawStatus(0);
				userWithdraw.setWithdrawProcess(1);
				userWithdraw.setMoney(withdrawMoney);
				userWithdraw.setCreateTime(new Date());
				userWithdraw.setUpdateTime(new Date());
				userWithdraw.setBankOn(userBank.getBankCard());
				userWithdraw.setBankName(userBank.getBankName());
				userWithdraw.setBankType(userBank.getBankType());
				userWithdrawMapper.insertSelective(userWithdraw);
				userMapper.updateFromUserMoney(money.subtract(withdrawMoney), userId);
				//添加记录到转账记录表中
				UserTransfer userTransfer=new UserTransfer();
				userTransfer.setWithdrawId(userWithdraw.getId());
				userTransfer.setMoney(userWithdraw.getMoney());
				userTransfer.setTransferType(4);
				userTransfer.setCreateTime(new Date());
				userTransfer.setUpdateTime(new Date());
				userTransfer.setFromUserId(userWithdraw.getUserId());
				userTransfer.setToUserId(userWithdraw.getUserId());
				userTransfer.setOrderNo(OrderNumUtils.getOredrNum());
				userTransfer.setToOverplusBalance(money.subtract(withdrawMoney));
				userTransfer.setFromOverplusBalance(money.subtract(withdrawMoney));
				userTransfer.setTransferDesc("预计72小时内到账，请耐心等待...");
				userTransferMapper.addUserWithdraw(userTransfer);
				result.setResponse(Constants.TRUE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg("申请提现成功");
			}else{
				result.setResponse(Constants.FALSE);
				result.setResponseCode(Constants.SUCCESS_200);
				result.setResponseMsg("账户余额不足");
			}
		}				
		return result;
		
	}
	@Override
	public UserWithdraw selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return userWithdrawMapper.selectByPrimaryKey(id);
	}
	@Override
	public List<UserWithdraw> queryWithdrawRecord(RequestVo requestVo) {
		// TODO Auto-generated method stub
		return userWithdrawMapper.queryWithdrawRecord(requestVo);
	}
	@Override
	public int updateStatus(Integer id, Integer withdrawStatus, Integer withdrawProcess) {
		// TODO Auto-generated method stub
		return userWithdrawMapper.updateStatus(id, withdrawStatus, withdrawProcess);
	}
	@Override
	public int delUserWithdrawRecord(Integer userId) {
		// TODO Auto-generated method stub
		return userWithdrawMapper.delUserWithdrawRecord(userId);
	}
	@Override
	public int updateWithdrawalsInfo(UserWithdraw userWithdraw) {
		// TODO Auto-generated method stub
		return userWithdrawMapper.updateByPrimaryKeySelective(userWithdraw);
	}
	@Override
	public BigDecimal totlaFrozenAmount(int userId) {
		// TODO Auto-generated method stub
		return userWithdrawMapper.totlaFrozenAmount(userId);
	}
}
		
			
