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

import net.zn.ddxj.entity.InviteRecord;
import net.zn.ddxj.mapper.InviteRecordMapper;
import net.zn.ddxj.service.InviteRecordService;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

@Service
@Component
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
public class InviteRecordServiceImpl implements InviteRecordService {
	
	@Autowired
	private InviteRecordMapper inviteRecordMapper;

	@Override
	public void inserRecord(Integer inviterId,String openId) {
		InviteRecord record = new InviteRecord();
		record.setInviterId(inviterId);
		record.setOpenId(openId);
		record.setCreateTime(new Date());
		record.setUpdateTime(new Date());
		record.setStatus(1);
		inviteRecordMapper.insertSelective(record);
	}
	
	@Override
	public InviteRecord findRecordByOpenId(String openId,Integer status) {
		return inviteRecordMapper.selectByOpenId(openId,status);
	}

	@Override
	public void updateInviteRecord(InviteRecord inviteRecord) {
		inviteRecordMapper.updateByPrimaryKeySelective(inviteRecord);
	}

	@Override
	public List<InviteRecord> queryInviteRecord(Integer userId) {
		return inviteRecordMapper.queryInviteRecord(userId);
	}

	@Override
	public BigDecimal queryInviteBonusCount(Integer userId) {
		return inviteRecordMapper.queryInviteBonusCount(userId);
	}

	@Override
	public List<InviteRecord> selectInviteRecord(RequestVo requestVo) {
		// TODO Auto-generated method stub
		return inviteRecordMapper.selectInviteRecord(requestVo);
	}

	@Override
	public List<InviteRecord> queryInviteRecordCms(CmsRequestVo requestVo) {
		return inviteRecordMapper.queryInviteRecordCms(requestVo);
	}

}
