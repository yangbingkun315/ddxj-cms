package net.zn.ddxj.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.zn.ddxj.entity.AppMenu;
import net.zn.ddxj.entity.AppMessage;
import net.zn.ddxj.mapper.AppMenuMapper;
import net.zn.ddxj.mapper.AppMessageMapper;
import net.zn.ddxj.service.AppMenuService;
import net.zn.ddxj.service.AppMessageService;
import net.zn.ddxj.vo.CmsRequestVo;
@Service
@Component
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
public class AppMenuServiceImpl implements AppMenuService {
	@Autowired
	private AppMenuMapper appMenuMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return appMenuMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(AppMenu record) {
		// TODO Auto-generated method stub
		return appMenuMapper.insert(record);
	}

	@Override
	public int insertSelective(AppMenu record) {
		// TODO Auto-generated method stub
		return appMenuMapper.insertSelective(record);
	}

	@Override
	public AppMenu selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return appMenuMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(AppMenu record) {
		// TODO Auto-generated method stub
		return appMenuMapper.updateByPrimaryKey(record);
	}

	@Override
	public int updateByPrimaryKey(AppMenu record) {
		// TODO Auto-generated method stub
		return appMenuMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<AppMenu> queryAppMenuList(CmsRequestVo cmsRequestVo) {
		// TODO Auto-generated method stub
		return appMenuMapper.queryAppMenuList(cmsRequestVo);
	}




}
