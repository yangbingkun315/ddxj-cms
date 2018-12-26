package net.zn.ddxj.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.zn.ddxj.entity.SalaryRecord;
import net.zn.ddxj.mapper.SalaryRecordMapper;
import net.zn.ddxj.service.SalaryRecordService;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

@Service
@Component
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
public class SalaryRecordServiceImpl implements SalaryRecordService{
	@Autowired
	private SalaryRecordMapper salaryRecordMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return salaryRecordMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(SalaryRecord record) {
		// TODO Auto-generated method stub
		return salaryRecordMapper.insert(record);
	}

	@Override
	public int insertSelective(SalaryRecord record) {
		// TODO Auto-generated method stub
		return salaryRecordMapper.insertSelective(record);
	}

	@Override
	public SalaryRecord selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return salaryRecordMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(SalaryRecord record) {
		// TODO Auto-generated method stub
		return salaryRecordMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(SalaryRecord record) {
		// TODO Auto-generated method stub
		return salaryRecordMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<SalaryRecord> selectByUserIdAndRecruitId(Integer userId, Integer recruitId) {
		return salaryRecordMapper.selectByUserIdAndRecruitId(userId, recruitId);
	}

	@Override
	public int selectCount(Integer userId, Integer recruitId) {
		return salaryRecordMapper.selectCount(userId, recruitId);
	}

	@Override
	public List<SalaryRecord> querySalaryRecord(RequestVo requestVo) {
		// TODO Auto-generated method stub
		return salaryRecordMapper.querySalaryRecord(requestVo);
	}

	@Override
	public int updateAuditStatus(Integer salaryId, Integer auditStatus) {
		// TODO Auto-generated method stub
		return salaryRecordMapper.updateAuditStatus(salaryId,auditStatus);
	}

	@Override
	public int deleteSalaryRecord(Integer userId) {
		// TODO Auto-generated method stub
		return salaryRecordMapper.deleteSalaryRecord(userId);
	}

	@Override
	public BigDecimal totalMoney(Integer userId, Integer recruitId) {
		// TODO Auto-generated method stub
		return salaryRecordMapper.totalMoeny(userId, recruitId);
	}

	@Override
	public int delSalaryRecord(Integer userId) {
		// TODO Auto-generated method stub
		return salaryRecordMapper.delSalaryRecord(userId);
	}

	@Override
	public List<SalaryRecord> querySalaryRecordCms(CmsRequestVo requestVo) {
		return salaryRecordMapper.querySalaryRecordCms(requestVo);
	}
	

}
