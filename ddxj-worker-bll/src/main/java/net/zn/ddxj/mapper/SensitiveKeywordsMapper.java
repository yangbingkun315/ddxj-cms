package net.zn.ddxj.mapper;

import java.util.List;

import net.zn.ddxj.entity.SensitiveKeywords;

public interface SensitiveKeywordsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SensitiveKeywords record);

    int insertSelective(SensitiveKeywords record);

    SensitiveKeywords selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SensitiveKeywords record);

    int updateByPrimaryKey(SensitiveKeywords record);
    
    List<SensitiveKeywords> querySensitiveKeywordsList();
    
}