package net.zn.ddxj.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.CreditRepayment;

public interface CreditRepaymentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CreditRepayment record);

    int insertSelective(CreditRepayment record);

    CreditRepayment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CreditRepayment record);

    int updateByPrimaryKey(CreditRepayment record);
    
    List<CreditRepayment> queryCreditRepaymentRecord(@Param("recruitId")int recruitId);//查询授信发放记录

    int updateCreditRepaymentRecord(int id);//查询授信还款记录

	int delCreditRepayMentRecord(Integer userId);

	CreditRepayment queryRecruitRepmentRecord(int recruitId); //查询授信还款记录
}