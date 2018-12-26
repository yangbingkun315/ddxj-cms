package net.zn.ddxj.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.zn.ddxj.entity.Credit;
import net.zn.ddxj.entity.CreditRepayment;
import net.zn.ddxj.entity.RecruitCredit;
import net.zn.ddxj.mapper.CreditMapper;
import net.zn.ddxj.mapper.CreditRepaymentMapper;
import net.zn.ddxj.mapper.RecruitCreditMapper;
import net.zn.ddxj.service.RecruitCreditService;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;
@Service
@Component
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
public class RecruitCreditServiceImpl implements RecruitCreditService {
	@Autowired
	private RecruitCreditMapper recruitCreditMapper;
	@Autowired
	private CreditMapper creditMapper;
	@Autowired
	private CreditRepaymentMapper repaymentMapper;

	@Override
	public RecruitCredit queryRecruitCredit(Integer userId,Integer recruitId) {
		// TODO Auto-generated method stub
		return recruitCreditMapper.queryRecruitCredit(userId,recruitId);
	}
	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return recruitCreditMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(RecruitCredit record) {
		// TODO Auto-generated method stub
		return recruitCreditMapper.insert(record);
	}

	@Override
	public int insertSelective(RecruitCredit record) {
		// TODO Auto-generated method stub
		return recruitCreditMapper.insertSelective(record);
	}

	@Override
	public RecruitCredit selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return recruitCreditMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(RecruitCredit record) {
		// TODO Auto-generated method stub
		return recruitCreditMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(RecruitCredit record) {
		// TODO Auto-generated method stub
		return recruitCreditMapper.updateByPrimaryKey(record);
	}
	
	@Override
	public List<Credit> queryCreditMCN() {
		// TODO Auto-generated method stub
		return creditMapper.queryCreditMCN();
	}
	
	@Override
	public List<CreditRepayment> queryCreditRepaymentRecord( int recruitId) {
		// TODO Auto-generated method stub
		return repaymentMapper.queryCreditRepaymentRecord( recruitId);
	}
	
	@Override
	public RecruitCredit queryUserIsCredit(Integer userId,Integer recruitId) {
		// TODO Auto-generated method stub
		return recruitCreditMapper.queryUserIsCredit(userId,recruitId);
	}
	@Override
	public int updateUsableMoney(Integer userId, Integer recruitId, BigDecimal usableMoney) {
		// TODO Auto-generated method stub
		return recruitCreditMapper.updateUsableMoney(userId, recruitId, usableMoney);
	}
	@Override
	public List<Credit> findCreditList(CmsRequestVo requestVo) {
		// TODO Auto-generated method stub
		return creditMapper.findCreditList(requestVo);
	}
	@Override
	public int deleteByCreditId(Integer id) {
		// TODO Auto-generated method stub
		return creditMapper.deleteByCreditId(id);
	}
	@Override
	public int insertSelective(Credit record) {
		// TODO Auto-generated method stub
		return creditMapper.insertSelective(record);
	}
	@Override
	public int updateByPrimaryKeySelective(Credit record) {
		// TODO Auto-generated method stub
		return creditMapper.updateByPrimaryKeySelective(record);
	}
	@Override
	public Credit queryCreditIdDetail(Integer creditId) {
		// TODO Auto-generated method stub
		return creditMapper.selectByPrimaryKey(creditId);
	}
	@Override
	public List<RecruitCredit> findCreditRecord(CmsRequestVo requestVo) {
		// TODO Auto-generated method stub
		return recruitCreditMapper.findCreditRecord(requestVo);
	}
	@Override
	public RecruitCredit selectCreditRecord(Integer id) {
		// TODO Auto-generated method stub
		return recruitCreditMapper.selectByPrimaryKey(id);
	}
	@Override
	public int deleteCreditRecordById(Integer id) {
		// TODO Auto-generated method stub
		return recruitCreditMapper.deleteByPrimaryKey(id);
	}
	@Override
	public int delRecruitCreditRecord(Integer userId) {
		// TODO Auto-generated method stub
		return recruitCreditMapper.delRecruitCreditRecord(userId);
	}
	@Override
	public int delCreditRepayMentRecord(Integer userId) {
		// TODO Auto-generated method stub
		return repaymentMapper.delCreditRepayMentRecord(userId);
	}
	@Override
	public RecruitCredit findRecruitCredit(Integer recruitId) {
		// TODO Auto-generated method stub
		return recruitCreditMapper.findRecruitCredit(recruitId);
	}
	@Override
	public int updateRecruitCreditRecrod(RecruitCredit record) {
		// TODO Auto-generated method stub
		return recruitCreditMapper.updateByPrimaryKeySelective(record);
	}
	@Override
	public CreditRepayment queryRecruitRepmentRecord(int recruitId) {
		// TODO Auto-generated method stub
		return repaymentMapper.queryRecruitRepmentRecord(recruitId);
	}
	@Override
	public int insertSelective(CreditRepayment record) {
		// TODO Auto-generated method stub
		return repaymentMapper.insertSelective(record);
	}
	@Override
	public RecruitCredit queryRecruitCreditById(Integer id) {
		// TODO Auto-generated method stub
		return recruitCreditMapper.selectByPrimaryKey(id);
	}
}
