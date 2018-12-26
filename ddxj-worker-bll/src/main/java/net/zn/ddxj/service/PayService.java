package net.zn.ddxj.service;

import java.util.List;

import net.zn.ddxj.entity.PaymentRecord;
import net.zn.ddxj.vo.RequestVo;

public interface PayService {
	int addPayMentRecord(PaymentRecord paymentRecord);
	PaymentRecord selectPayOutTradeNo(String outTradeNo); //根据id查询
	int updatePayMentRecord(PaymentRecord paymentRecord);
	List<PaymentRecord> queryPayRecord(RequestVo requestVo);
	int delPayMentRecord(Integer userId);
}
