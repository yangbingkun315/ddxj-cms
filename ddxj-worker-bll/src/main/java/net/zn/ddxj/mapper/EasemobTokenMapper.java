package net.zn.ddxj.mapper;

import net.zn.ddxj.entity.EasemobToken;

public interface EasemobTokenMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EasemobToken record);

    int insertSelective(EasemobToken record);

    EasemobToken selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EasemobToken record);

    int updateByPrimaryKey(EasemobToken record);
    
    EasemobToken queryLastToken();
}