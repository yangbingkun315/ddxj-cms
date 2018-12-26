package net.zn.ddxj.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.PaymentRecord;
import net.zn.ddxj.vo.RequestVo;

public interface PaymentRecordMapper {
    int insert(PaymentRecord record);

    int insertSelective(PaymentRecord record);
    
    PaymentRecord selectPayOutTradeNo(@Param("outTradeNo")String outTradeNo); //根据id查询
    
    int updateByPrimaryKeySelective(PaymentRecord paymentRecord);

	List<PaymentRecord> queryPayRecord(RequestVo requestVo);//查询充值记录

	int delPayMentRecord(Integer userId);
}