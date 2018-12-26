package net.zn.ddxj.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.zn.ddxj.entity.PaymentRecord;
import net.zn.ddxj.entity.User;
import net.zn.ddxj.entity.UserTransfer;
import net.zn.ddxj.mapper.PaymentRecordMapper;
import net.zn.ddxj.mapper.UserMapper;
import net.zn.ddxj.mapper.UserTransferMapper;
import net.zn.ddxj.service.PayService;
import net.zn.ddxj.utils.OrderNumUtils;
import net.zn.ddxj.vo.RequestVo;

@Service
@Component
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
public class PayServiceImpl implements PayService{

	@Autowired
	private PaymentRecordMapper paymentRecordMapper;
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private UserTransferMapper userTransferMapper;
	
	@Override
	public int addPayMentRecord(PaymentRecord paymentRecord) {
		
		return paymentRecordMapper.insertSelective(paymentRecord);
	}
	@Override
	public PaymentRecord selectPayOutTradeNo(String outTradeNo) {
		// TODO Auto-generated method stub
		return paymentRecordMapper.selectPayOutTradeNo(outTradeNo);
	}
	@Override
	public int updatePayMentRecord(PaymentRecord paymentRecord) {
		int updatePayMentRecord = paymentRecordMapper.updateByPrimaryKeySelective(paymentRecord);
		if(updatePayMentRecord > 0)
		{
			User user=userMapper.queryUserDetail(paymentRecord.getUserId());
			userMapper.updateUserRemainderMoney(user.getRemainderMoney().add(paymentRecord.getTotalFee()), paymentRecord.getUserId());
			//添加记录到转账记录表中
			UserTransfer userTransfer = userTransferMapper.selectByOutTradeNo(paymentRecord.getOutTradeNo());
			if(userTransfer == null)
			{
				userTransfer = new UserTransfer();
				userTransfer.setCreateTime(new Date());
			}
			userTransfer.setWehatPayNo(paymentRecord.getOutTradeNo());
			userTransfer.setMoney(paymentRecord.getTotalFee());
			userTransfer.setUpdateTime(new Date());
			userTransfer.setFromUserId(user.getId());
			userTransfer.setToUserId(user.getId());
			userTransfer.setOrderNo(OrderNumUtils.getOredrNum());
			userTransfer.setToOverplusBalance(user.getRemainderMoney().add(paymentRecord.getTotalFee()));
			userTransfer.setFromOverplusBalance(user.getRemainderMoney().add(paymentRecord.getTotalFee()));
			String title = "";
			if(paymentRecord.getTradeType().equals("ALIPAY"))//支付宝支付
			{
				title = "支付宝充值成功，充值金额：￥";
				userTransfer.setTransferType(3);
			}
			else if(paymentRecord.getTradeType().equals("WECHAT"))//微信支付
			{
				title = "微信充值成功，充值金额：￥";
				userTransfer.setTransferType(3);
			}
			else if(paymentRecord.getTradeType().equals("JSAPI"))//公众号支付
			{
				title = "公众号充值成功，充值金额：￥";
				userTransfer.setTransferType(3);
			}
			userTransfer.setTransferDesc(title + paymentRecord.getTotalFee());
			userTransferMapper.insertSelective(userTransfer);
		}
		return updatePayMentRecord;
	}
	@Override
	public List<PaymentRecord> queryPayRecord(RequestVo requestVo) {
		// TODO Auto-generated method stub
		return paymentRecordMapper.queryPayRecord(requestVo);
	}
	@Override
	public int delPayMentRecord(Integer userId) {
		// TODO Auto-generated method stub
		return paymentRecordMapper.delPayMentRecord(userId);
	}

}
