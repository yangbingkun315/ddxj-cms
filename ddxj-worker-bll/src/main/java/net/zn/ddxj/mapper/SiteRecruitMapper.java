package net.zn.ddxj.mapper;

import java.util.List;

import net.zn.ddxj.entity.SiteRecruit;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

public interface SiteRecruitMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SiteRecruit record);

    int insertSelective(SiteRecruit record);

    SiteRecruit selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SiteRecruit record);

    int updateByPrimaryKey(SiteRecruit record);

	List<SiteRecruit> queryAllSiteRecruit(CmsRequestVo requestVo);//查询招贤纳士

	SiteRecruit querySiteRecruitDetail(Integer recruitId);

	int delSiteRecruit(Integer id);
}