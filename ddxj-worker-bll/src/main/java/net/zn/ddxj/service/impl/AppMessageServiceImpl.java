package net.zn.ddxj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.zn.ddxj.entity.AppMessage;
import net.zn.ddxj.mapper.AppMessageMapper;
import net.zn.ddxj.service.AppMessageService;
@Service
@Component
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
public class AppMessageServiceImpl implements AppMessageService {
	@Autowired
	private AppMessageMapper appMessageMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return appMessageMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(AppMessage record) {
		// TODO Auto-generated method stub
		return appMessageMapper.insert(record);
	}

	@Override
	public int insertSelective(AppMessage record) {
		// TODO Auto-generated method stub
		return appMessageMapper.insertSelective(record);
	}

	@Override
	public AppMessage selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return appMessageMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(AppMessage record) {
		// TODO Auto-generated method stub
		return appMessageMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(AppMessage record) {
		// TODO Auto-generated method stub
		return appMessageMapper.updateByPrimaryKey(record);
	}

	@Override
	public int delAppMessageRecord(Integer userId) {
		// TODO Auto-generated method stub
		return appMessageMapper.delAppMessageRecord(userId);
	}



}
