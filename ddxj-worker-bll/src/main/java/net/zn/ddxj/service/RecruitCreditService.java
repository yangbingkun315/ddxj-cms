package net.zn.ddxj.service;

import java.math.BigDecimal;
import java.util.List;

import net.zn.ddxj.entity.Credit;
import net.zn.ddxj.entity.CreditRepayment;
import net.zn.ddxj.entity.RecruitCredit;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

public interface RecruitCreditService {
	
	RecruitCredit queryRecruitCredit(Integer userId,Integer recruitId);//根据用户编号查询授信记录
	
	int deleteByPrimaryKey(Integer id);

	int insert(RecruitCredit record);
	
	int insertSelective(RecruitCredit record);
	
	RecruitCredit selectByPrimaryKey(Integer id);
	
	int updateByPrimaryKeySelective(RecruitCredit record);
	
	int updateByPrimaryKey(RecruitCredit record);
	
	List<Credit> queryCreditMCN();//查询授信机构
	
	List<CreditRepayment> queryCreditRepaymentRecord(int recruitId);//查询授信发放记录

	RecruitCredit queryUserIsCredit(Integer userId, Integer recruitId);

	int updateUsableMoney(Integer userId,Integer recruitId ,BigDecimal usableMoney);//更新授信金额

	List<Credit> findCreditList(CmsRequestVo requestVo);//查询授信机构
	
	int deleteByCreditId(Integer id);//删除授信机构
	
	int insertSelective(Credit record);//添加授信机构
	
	int updateByPrimaryKeySelective(Credit record);//修改授信机构

	Credit queryCreditIdDetail(Integer creditId);

	List<RecruitCredit> findCreditRecord(CmsRequestVo requestVo);

	RecruitCredit selectCreditRecord(Integer creditRecordId);//查询授信记录详情

	int deleteCreditRecordById(Integer creditRecordId);//删除授信记录

	int delRecruitCreditRecord(Integer userId);

	int delCreditRepayMentRecord(Integer userId);
	
	RecruitCredit findRecruitCredit(Integer recruitId);//根据recruitId统计授信记录

	int updateRecruitCreditRecrod(RecruitCredit record);//更新授信记录

	CreditRepayment queryRecruitRepmentRecord(int recruitId);//查询授信还款记录
	
	int insertSelective(CreditRepayment record );

	RecruitCredit queryRecruitCreditById(Integer id);
	


}
