package net.zn.ddxj.service;

import java.util.List;
import java.util.Map;

import net.zn.ddxj.entity.SiteBespeakConsult;
import net.zn.ddxj.entity.SiteJobDemand;
import net.zn.ddxj.entity.SiteRecruit;
import net.zn.ddxj.entity.SiteSlide;
import net.zn.ddxj.entity.WebRecruit;
import net.zn.ddxj.vo.CmsRequestVo;

public interface SiteService {
	int deleteByPrimaryKey(Integer id);

    int insert(SiteSlide record);

    int insertSelective(SiteSlide record);

    SiteSlide selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SiteSlide record);

    int updateByPrimaryKey(SiteSlide record);
    
    List<SiteSlide> querySiteSlideList(CmsRequestVo requestVo);//多条件查询幻灯片列表
    
    List<SiteBespeakConsult> siteBespeakConsultList(CmsRequestVo requestVo);//多条件查询预约咨询列表
    
    int addSiteBespeakConsult(SiteBespeakConsult record);//新增预约咨询
    
    int updateSiteBespeakConsult(SiteBespeakConsult record);
    
    int updateSiteSlideFlag(int id);//删除幻灯片
    
    int updateSiteBespeakConsult(int id);//删除预约资讯

	List<SiteRecruit>queryAllSiteRecruit(CmsRequestVo requestVo);

	SiteRecruit querySiteRecruitDetail(Integer recruitId);

	int addSiteJobDemand(SiteJobDemand record);

	int addSiteRecruit(SiteRecruit record);

	int delSiteRecruit(Integer id);

	int updateSiteRecruit(SiteRecruit siteRecruit);

	SiteJobDemand querySiteJobDemandDetail(Integer id);

	List<SiteJobDemand> querySiteJobDemandList(CmsRequestVo requestVo);

	int delSiteJobDemand(Integer id);

	int updateSiteJobDemand(SiteJobDemand siteJobDemand);

	int updateSiteJobDemandStatus(SiteJobDemand siteJobDemand);

	SiteBespeakConsult querybespeakById(int id);//查询预约咨询列表
	
	List<WebRecruit> queryWebRecruitList(CmsRequestVo requestVo);//多条件查询官网招聘列表
	
	WebRecruit queryWebRecruitById(Integer id);//根据ID查询官网招聘信息
	
	int updateWebRecruit(WebRecruit webRecruit);//修改招聘信息
	
	int insertWebRecruit(WebRecruit webRecruit);//新增招聘信息
	
	int deleteWebRecruitById(Integer recruitId);//删除招聘信息
	
	int changeWebRecruitStick(Integer type,Integer id);//置顶

	List<WebRecruit> queryMultipleConditional(Map<String, Object> params);//多条件查询招聘列表
	
	List<WebRecruit> queryWebRecruitListForStick();//查询置顶的招聘

	WebRecruit queryWebSiteRecruitDetail(Integer recruitId);//查询招聘详情
}
