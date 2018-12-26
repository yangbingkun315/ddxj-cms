package net.zn.ddxj.service.impl;

import java.util.List;

import net.zn.ddxj.entity.ScreenAdvert;
import net.zn.ddxj.mapper.ScreenAdvertMapper;
import net.zn.ddxj.service.ScreenAdvertService;
import net.zn.ddxj.vo.CmsRequestVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class ScreenAdvertServiceImpl implements ScreenAdvertService {
	@Autowired 
	private ScreenAdvertMapper screenAdvertMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return screenAdvertMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(ScreenAdvert record) {
		// TODO Auto-generated method stub
		return screenAdvertMapper.insert(record);
	}

	@Override
	public int insertSelective(ScreenAdvert record) {
		// TODO Auto-generated method stub
		return screenAdvertMapper.insertSelective(record);
	}

	@Override
	public ScreenAdvert selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return screenAdvertMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(ScreenAdvert record) {
		// TODO Auto-generated method stub
		return screenAdvertMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(ScreenAdvert record) {
		// TODO Auto-generated method stub
		return screenAdvertMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<ScreenAdvert> queryScreenAdvertList(CmsRequestVo requestVo) {
		// TODO Auto-generated method stub
		return screenAdvertMapper.queryScreenAdvertList(requestVo);
	}

	@Override
	public int updateScreenAdvertFlag(int id) {
		// TODO Auto-generated method stub
		return screenAdvertMapper.deleteByPrimaryKey(id);
	}

	@Override
	public ScreenAdvert queryScreenAdvert(String pushPlatform) {
		// TODO Auto-generated method stub
		return screenAdvertMapper.queryScreenAdvert(pushPlatform);
	}

	@Override
	public List<ScreenAdvert> queryEndTime(String pushPlatform) {
		// TODO Auto-generated method stub
		return screenAdvertMapper.queryScreenAdvertEndTime(pushPlatform);
	}

	@Override
	public List<ScreenAdvert> queryAndroidAndIOS(String pushPlatform) {
		// TODO Auto-generated method stub
		return screenAdvertMapper.queryAndroidAndIOS(pushPlatform);
	}

	@Override
	public List<ScreenAdvert> validateTimeIsNotScreeAdvert(String currentTime) {
		// TODO Auto-generated method stub
		return screenAdvertMapper.validateTimeIsNotScreeAdvert(currentTime);
	}

	@Override
	public ScreenAdvert queryScreenAdvertById(int id) {
		// TODO Auto-generated method stub
		return screenAdvertMapper.selectByPrimaryKey(id);
	}

	

}
