package net.zn.ddxj.mapper;

import java.util.List;

import net.zn.ddxj.entity.SiteBespeakConsult;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

public interface SiteBespeakConsultMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SiteBespeakConsult record);

    int insertSelective(SiteBespeakConsult record);

    SiteBespeakConsult selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SiteBespeakConsult record);

    int updateByPrimaryKey(SiteBespeakConsult record);
    
    List<SiteBespeakConsult> siteBespeakConsultList(CmsRequestVo requestVo);//多条件查询预约咨询列表
    
    int updateSiteBespeakConsult(int id);//删除预约资讯
}