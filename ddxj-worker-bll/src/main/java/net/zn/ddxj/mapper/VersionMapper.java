package net.zn.ddxj.mapper;

import net.zn.ddxj.entity.Version;

public interface VersionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Version record);

    int insertSelective(Version record);

    Version selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Version record);

    int updateByPrimaryKey(Version record);
    
    Version queryLastVersion(String mobileSource);
    
    Version queryNotExamineMaxVersion(String mobileSource);
}