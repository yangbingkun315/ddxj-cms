package net.zn.ddxj.mapper;

import java.util.List;

import net.zn.ddxj.entity.SiteSlide;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

public interface SiteSlideMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SiteSlide record);

    int insertSelective(SiteSlide record);

    SiteSlide selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SiteSlide record);

    int updateByPrimaryKey(SiteSlide record);
    
    List<SiteSlide> querySiteSlideList(CmsRequestVo requestVo);//多条件查询幻灯片列表
    
    int updateSiteSlideFlag(int id);//删除幻灯片
}