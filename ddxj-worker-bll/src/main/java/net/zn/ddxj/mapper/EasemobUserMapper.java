package net.zn.ddxj.mapper;

import net.zn.ddxj.entity.EasemobUser;

public interface EasemobUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EasemobUser record);

    int insertSelective(EasemobUser record);

    EasemobUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EasemobUser record);

    int updateByPrimaryKey(EasemobUser record);
    
    EasemobUser queryEasemobUserByUserId(Integer id);//根据UserId查询环信用户信息
}