package net.zn.ddxj.service.impl;

import java.util.List;

import net.zn.ddxj.entity.Information;
import net.zn.ddxj.entity.InformationCategory;
import net.zn.ddxj.entity.InformationRecordIp;
import net.zn.ddxj.entity.InformationType;
import net.zn.ddxj.mapper.InformationCategoryMapper;
import net.zn.ddxj.mapper.InformationMapper;
import net.zn.ddxj.mapper.InformationRecordIpMapper;
import net.zn.ddxj.mapper.InformationTypeMapper;
import net.zn.ddxj.service.InformationService;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Component
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
public class InformationServiceImpl implements InformationService{
	@Autowired
	private InformationMapper informationMapper;
	@Autowired
	private InformationTypeMapper informationTypeMapper;
	@Autowired
	private InformationCategoryMapper informationCategoryMapper;
	@Autowired
	private InformationRecordIpMapper informationRecordIpMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return informationMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(Information record) {
		return informationMapper.insert(record);
	}

	@Override
	public int insertSelective(Information record) {
		return informationMapper.insertSelective(record);
	}

	@Override
	public Information selectByPrimaryKey(Integer id) {
		return informationMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(Information record) {
		return informationMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(Information record) {
		return informationMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<Information> selectInformation(RequestVo requestVo) {
		return informationMapper.selectInformation(requestVo);
	}

	@Override
	public List<Information> queryInformationList(CmsRequestVo requestVo) {
		// TODO Auto-generated method stub
		return informationMapper.queryInformationList(requestVo);
	}

	@Override
	public int deleteInformation(Integer inforId) {
		// TODO Auto-generated method stub
		return informationMapper.deleteInformation(inforId);
	}

	@Override
	public List<InformationType> queryInformationTypeList(RequestVo requestVo) {
		// TODO Auto-generated method stub
		return informationTypeMapper.queryInformationTypeList(requestVo);
	}

	@Override
	public int deleteInformationCategory(Integer categoryId) {
		// TODO Auto-generated method stub
		return informationTypeMapper.deleteByPrimaryKey(categoryId);
	}

	@Override
	public InformationType InformationCategoryDetail(Integer categoryId) {
		// TODO Auto-generated method stub
		return informationTypeMapper.selectByPrimaryKey(categoryId);
	}

	@Override
	public int insertInformationCategory(InformationType type) {
		// TODO Auto-generated method stub
		return informationTypeMapper.insertSelective(type);
	}

	@Override
	public int updateInformationCategory(InformationType type) {
		// TODO Auto-generated method stub
		return informationTypeMapper.updateByPrimaryKeySelective(type);
	}

	@Override
	public int addInformationCategory(InformationCategory category) {
		// TODO Auto-generated method stub
		return informationCategoryMapper.insertSelective(category);
	}

	@Override
	public int deleteInformationCategoryByInfoId(Integer inforId) {
		// TODO Auto-generated method stub
		return informationCategoryMapper.deleteInformationCategoryByInfoId(inforId);
	}

	@Override
	public InformationRecordIp queryInforRecordIpByIP(String ip,int infoId) {
		// TODO Auto-generated method stub
		return informationRecordIpMapper.queryInforRecordIpByIP(ip, infoId);
	}

	@Override
	public int addInformationRecordIp(InformationRecordIp ip) {
		// TODO Auto-generated method stub
		return informationRecordIpMapper.insertSelective(ip);
	}

	@Override
	public int changeInfoStick(Integer id,Integer type) {
		// TODO Auto-generated method stub
		return informationMapper.changeInfoStick(id,type);
	}

	@Override
	public List<InformationType> queryInfoTypeList(CmsRequestVo requestVo) {
		// TODO Auto-generated method stub
		return informationTypeMapper.queryInfoTypeList(requestVo);
	}

	@Override
	public InformationType selectInfoTypeByPrimaryKey(Integer typeId) {
		// TODO Auto-generated method stub
		return informationTypeMapper.selectByPrimaryKey(typeId);
	}

}
