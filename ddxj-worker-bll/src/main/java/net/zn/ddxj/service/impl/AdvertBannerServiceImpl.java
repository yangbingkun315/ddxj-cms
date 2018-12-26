package net.zn.ddxj.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.zn.ddxj.entity.AdvertBanner;
import net.zn.ddxj.mapper.AdvertBannerMapper;
import net.zn.ddxj.service.AdvertBannerService;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

@Service
@Component
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
public class AdvertBannerServiceImpl implements AdvertBannerService{
	@Autowired
	private AdvertBannerMapper advertBannerMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(AdvertBanner record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelective(AdvertBanner record) {
		// TODO Auto-generated method stub
		return advertBannerMapper.insertSelective(record);
	}

	@Override
	public AdvertBanner selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return advertBannerMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(AdvertBanner record) {
		// TODO Auto-generated method stub
		return advertBannerMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(AdvertBanner record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<AdvertBanner> selectBanner() {
		return advertBannerMapper.selectBanner();
	}

	@Override
	public List<AdvertBanner> queryAdvertBannerList(RequestVo requestVo) {
		// TODO Auto-generated method stub
		return advertBannerMapper.queryAdvertBannerList(requestVo);
	}
	
	@Override
	public List<AdvertBanner> queryCmsAdvertBannerList(CmsRequestVo requestVo) {
		// TODO Auto-generated method stub
		return advertBannerMapper.queryCmsAdvertBannerList(requestVo);
	}

	@Override
	public int deleteAdvertBanne(int bannerId) {
		// TODO Auto-generated method stub
		return advertBannerMapper.deleteAdvertBanne(bannerId);
	}

	@Override
	public AdvertBanner queryIndexBanner() {
		// TODO Auto-generated method stub
		return advertBannerMapper.queryIndexBanner();
	}

}
