package net.zn.ddxj.mapper;

import java.util.List;

import net.zn.ddxj.entity.KeyWords;
import net.zn.ddxj.vo.CmsRequestVo;

public interface KeyWordsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(KeyWords record);

    int insertSelective(KeyWords record);

    KeyWords selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(KeyWords record);

    int updateByPrimaryKey(KeyWords record);
    
    List<KeyWords> findKeyWordsList(CmsRequestVo requestVo);
    
    int updateKeywordsFlagById(Integer keywordsId);
    
    List<KeyWords> findKeyWordsByKeyWords(String keyWords);
}