package net.zn.ddxj.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.zn.ddxj.entity.CreditContactsImage;
import net.zn.ddxj.entity.CreditUrgentContacts;
import net.zn.ddxj.entity.RecruitRecord;
import net.zn.ddxj.mapper.CreditContactsImageMapper;
import net.zn.ddxj.mapper.CreditUrgentContactsMapper;
import net.zn.ddxj.mapper.RecruitRecordMapper;
import net.zn.ddxj.service.RecruitRecordService;

@Service
@Component
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
public class RecruitRecordServiceImpl implements RecruitRecordService{
	@Autowired
	private RecruitRecordMapper recruitRecordMapper;
	@Autowired
	private CreditContactsImageMapper creditContactsImageMapper;
	@Autowired
	private CreditUrgentContactsMapper creditUrgentContactsMapper;

	@Override
	public RecruitRecord queryWorkerDeliver(Integer recruitId, Integer userId) {
		// TODO Auto-generated method stub
		return recruitRecordMapper.queryWorkerDeliver(recruitId, userId);
	}

	@Override
	public int updateWorkerStatus(Integer userId, Integer recruitId, Integer enlistStatus) {
		return recruitRecordMapper.updateWorkerStatus(userId, recruitId, enlistStatus);
	}

	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return recruitRecordMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(RecruitRecord record) {
		// TODO Auto-generated method stub
		return recruitRecordMapper.insert(record);
	}

	@Override
	public int insertSelective(RecruitRecord record) {
		// TODO Auto-generated method stub
		return recruitRecordMapper.insertSelective(record);
	}

	@Override
	public RecruitRecord selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return recruitRecordMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(RecruitRecord record) {
		// TODO Auto-generated method stub
		return recruitRecordMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(RecruitRecord record) {
		// TODO Auto-generated method stub
		return recruitRecordMapper.updateByPrimaryKey(record);
	}

	@Override
	public int selectByUserId(Integer userId) {
		return recruitRecordMapper.selectByUserId(userId);
	}

	@Override
	public int updateBlanceStatus(Integer userId, Integer recruitId, Integer balanceStatus) {
		// TODO Auto-generated method stub
		return recruitRecordMapper.updateBlanceStatus(userId, recruitId, balanceStatus);
	}

	@Override
	public RecruitRecord queryWorker(Integer recruitId, Integer userId) {
		return recruitRecordMapper.queryWorker(recruitId, userId);
	}

	@Override
	public int insertImages(CreditContactsImage creditContactsImage) {
		return creditContactsImageMapper.insertSelective(creditContactsImage);
	}

	@Override
	public int insertUrgent(CreditUrgentContacts contacts) {
		return creditUrgentContactsMapper.insertSelective(contacts);
	}

	@Override
	public List<RecruitRecord> queryRecruitUserList(Integer recruitId) {
		// TODO Auto-generated method stub
		return recruitRecordMapper.queryRecruitUserList(recruitId);
	}

	@Override
	public int updateRecordFlag(int recordId) {
		// TODO Auto-generated method stub
		return recruitRecordMapper.updateRecordFlag(recordId);
	}

	@Override
	public int updateByRecordId(Integer recordId, Integer enlistStatus, String refuseCause) {
		// TODO Auto-generated method stub
		return recruitRecordMapper.updateByRecordId(recordId, enlistStatus, refuseCause);
	}

	@Override
	public RecruitRecord queryEnlistUserDetails(Integer recruitId,Integer userId) {
		// TODO Auto-generated method stub
		return recruitRecordMapper.queryEnlistUserDetails(recruitId,userId);
	}

	@Override
	public RecruitRecord queryAllWorkerDeliver(Integer recruitId, Integer userId) {
		return recruitRecordMapper.queryAllWorkerDeliver(recruitId, userId);
	}

	@Override
	public int selectByUserFlag(Integer userId) {
		return recruitRecordMapper.selectByUserFlag(userId);
	}

	@Override
	public int countSingUp(Integer recruitId) {
		// TODO Auto-generated method stub
		return recruitRecordMapper.countSingUp(recruitId);
	}

	@Override
	public int updateUserSignStatus(Integer userId) {
		// TODO Auto-generated method stub
		return recruitRecordMapper.updateUserSignStatus(userId);
	}

	@Override
	public int delRecruitRecord(Integer userId) {
		// TODO Auto-generated method stub
		return recruitRecordMapper.delRecruitRecord(userId);
	}

	@Override
	public int delCreditContactsImageRecord(Integer userId) {
		// TODO Auto-generated method stub
		return creditContactsImageMapper.delCreditContactsImageRecord(userId);
	}

	@Override
	public int delCreditUrgentContactRecord(Integer userId) {
		// TODO Auto-generated method stub
		return creditUrgentContactsMapper.delCreditUrgentContactRecord(userId);
	}

	@Override
	public RecruitRecord queryWorkerConduct(Integer userId) {
		// TODO Auto-generated method stub
		return recruitRecordMapper.queryWorkerConduct(userId);
	}

	@Override
	public int deleteImages(Integer id) {
		// TODO Auto-generated method stub
		return creditContactsImageMapper.deleteImages(id);
	}

	@Override
	public int deleteUrgent(Integer id) {
		// TODO Auto-generated method stub
		return creditUrgentContactsMapper.deleteUrgent(id);
	}

	@Override
	public RecruitRecord queryRecruitRecord(Integer id) {
		// TODO Auto-generated method stub
		return recruitRecordMapper.selectByPrimaryKey(id);
	}
}
