package net.zn.ddxj.service;

import net.zn.ddxj.entity.AppMessage;

public interface AppMessageService {
    int deleteByPrimaryKey(Integer id);

    int insert(AppMessage record);

    int insertSelective(AppMessage record);

    AppMessage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppMessage record);

    int updateByPrimaryKey(AppMessage record);

	int delAppMessageRecord(Integer userId);

	
}
