package net.zn.ddxj.service.impl;

import java.util.List;
import java.util.Map;

import net.zn.ddxj.entity.SiteBespeakConsult;
import net.zn.ddxj.entity.SiteJobDemand;
import net.zn.ddxj.entity.SiteRecruit;
import net.zn.ddxj.entity.SiteSlide;
import net.zn.ddxj.entity.WebRecruit;
import net.zn.ddxj.mapper.SiteBespeakConsultMapper;
import net.zn.ddxj.mapper.SiteJobDemandMapper;
import net.zn.ddxj.mapper.SiteRecruitMapper;
import net.zn.ddxj.mapper.SiteSlideMapper;
import net.zn.ddxj.mapper.WebRecruitMapper;
import net.zn.ddxj.service.SiteService;
import net.zn.ddxj.vo.CmsRequestVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
@Service
@Component
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
public class SiteServiceImpl implements SiteService{
	
	@Autowired
	private SiteSlideMapper siteSlideMapper;
	@Autowired
	private SiteBespeakConsultMapper siteBespeakConsultMapper;
	@Autowired
	private SiteRecruitMapper siteRecruitMapper;
	@Autowired
	private SiteJobDemandMapper siteJobDemandMapper;
	@Autowired
	private WebRecruitMapper webRecruitMapper;
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return siteSlideMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(SiteSlide record) {
		// TODO Auto-generated method stub
		return siteSlideMapper.insert(record);
	}

	@Override
	public int insertSelective(SiteSlide record) {
		// TODO Auto-generated method stub
		return siteSlideMapper.insertSelective(record);
	}

	@Override
	public SiteSlide selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return siteSlideMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(SiteSlide record) {
		// TODO Auto-generated method stub
		return siteSlideMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(SiteSlide record) {
		// TODO Auto-generated method stub
		return siteSlideMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<SiteSlide> querySiteSlideList(CmsRequestVo requestVo) {
		// TODO Auto-generated method stub
		return siteSlideMapper.querySiteSlideList(requestVo);
	}

	@Override
	public int addSiteBespeakConsult(SiteBespeakConsult record) {
		// TODO Auto-generated method stub
		return siteBespeakConsultMapper.insertSelective(record);
	}

	@Override
	public List<SiteRecruit> queryAllSiteRecruit(CmsRequestVo requestVo) {
		// TODO Auto-generated method stub
		return siteRecruitMapper.queryAllSiteRecruit(requestVo);
	}

	@Override
	public SiteRecruit querySiteRecruitDetail(Integer recruitId) {
		// TODO Auto-generated method stub
		return siteRecruitMapper.querySiteRecruitDetail(recruitId);
	}

	@Override
	public int addSiteJobDemand(SiteJobDemand record) {
		// TODO Auto-generated method stub
		return siteJobDemandMapper.insertSelective(record);
	}

	@Override
	public int addSiteRecruit(SiteRecruit record) {
		// TODO Auto-generated method stub
		return siteRecruitMapper.insertSelective(record);
	}

	@Override
	public List<SiteBespeakConsult> siteBespeakConsultList(CmsRequestVo requestVo) {
		// TODO Auto-generated method stub
		return siteBespeakConsultMapper.siteBespeakConsultList(requestVo);
	}

	@Override
	public int updateSiteSlideFlag(int id) {
		// TODO Auto-generated method stub
		return siteSlideMapper.updateSiteSlideFlag(id);
	}

	@Override
	public int updateSiteBespeakConsult(int id) {
		// TODO Auto-generated method stub
		return siteBespeakConsultMapper.updateSiteBespeakConsult(id);
	}

	@Override
	public int updateSiteBespeakConsult(SiteBespeakConsult record) {
		// TODO Auto-generated method stub
		return siteBespeakConsultMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int delSiteRecruit(Integer id) {
		// TODO Auto-generated method stub
		return siteRecruitMapper.delSiteRecruit(id);
	}

	@Override
	public int updateSiteRecruit(SiteRecruit siteRecruit) {
		// TODO Auto-generated method stub
		return siteRecruitMapper.updateByPrimaryKeySelective(siteRecruit);
	}

	@Override
	public SiteJobDemand querySiteJobDemandDetail(Integer id) {
		// TODO Auto-generated method stub
		return siteJobDemandMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<SiteJobDemand> querySiteJobDemandList(CmsRequestVo requestVo) {
		// TODO Auto-generated method stub
		return siteJobDemandMapper.querySiteJobDemandList(requestVo);
	}

	@Override
	public int delSiteJobDemand(Integer id) {
		// TODO Auto-generated method stub
		return siteJobDemandMapper.delSiteJobDemand(id);
	}

	@Override
	public int updateSiteJobDemand(SiteJobDemand siteJobDemand) {
		// TODO Auto-generated method stub
		return siteJobDemandMapper.updateByPrimaryKeySelective(siteJobDemand);
	}

	@Override
	public int updateSiteJobDemandStatus(SiteJobDemand siteJobDemand) {
		// TODO Auto-generated method stub
		return siteJobDemandMapper.updateByPrimaryKeySelective(siteJobDemand);
	}

	@Override
	public SiteBespeakConsult querybespeakById(int id) {
		// TODO Auto-generated method stub
		return siteBespeakConsultMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<WebRecruit> queryWebRecruitList(CmsRequestVo requestVo) {
		// TODO Auto-generated method stub
		return webRecruitMapper.queryWebRecruitList(requestVo);
	}

	@Override
	public WebRecruit queryWebRecruitById(Integer id) {
		// TODO Auto-generated method stub
		return webRecruitMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateWebRecruit(WebRecruit webRecruit) {
		// TODO Auto-generated method stub
		return webRecruitMapper.updateByPrimaryKeySelective(webRecruit);
	}

	@Override
	public int insertWebRecruit(WebRecruit webRecruit) {
		// TODO Auto-generated method stub
		return webRecruitMapper.insertSelective(webRecruit);
	}

	@Override
	public int deleteWebRecruitById(Integer recruitId) {
		// TODO Auto-generated method stub
		return webRecruitMapper.deleteWebRecruitById(recruitId);
	}

	@Override
	public List<WebRecruit> queryMultipleConditional(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return webRecruitMapper.queryMultipleConditional(params);
	}

	@Override
	public List<WebRecruit> queryWebRecruitListForStick() {
		return webRecruitMapper.queryWebRecruitListForStick();
	}
	
	@Override
	public int changeWebRecruitStick(Integer type,Integer id) {
		// TODO Auto-generated method stub
		return webRecruitMapper.changeWebRecruitStick(type, id);
	}

	@Override
	public WebRecruit queryWebSiteRecruitDetail(Integer recruitId) {
		// TODO Auto-generated method stub
		return webRecruitMapper.queryWebSiteRecruitDetail(recruitId);
	}
}
