package net.zn.ddxj.mapper;

import java.util.List;

import net.zn.ddxj.entity.SiteJobDemand;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

public interface SiteJobDemandMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SiteJobDemand record);

    int insertSelective(SiteJobDemand record);

    SiteJobDemand selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SiteJobDemand record);

    int updateByPrimaryKey(SiteJobDemand record);

	List<SiteJobDemand> querySiteJobDemandList(CmsRequestVo requestVo);

	int delSiteJobDemand(Integer id);

}