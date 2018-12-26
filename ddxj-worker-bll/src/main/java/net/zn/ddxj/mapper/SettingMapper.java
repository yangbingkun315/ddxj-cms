package net.zn.ddxj.mapper;

import net.zn.ddxj.entity.Setting;

public interface SettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Setting record);

    int insertSelective(Setting record);

    Setting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Setting record);

    int updateByPrimaryKey(Setting record);
    
    Setting querySettingByKey(String key);
}