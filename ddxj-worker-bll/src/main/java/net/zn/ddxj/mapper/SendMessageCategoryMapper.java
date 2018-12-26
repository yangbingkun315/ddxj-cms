package net.zn.ddxj.mapper;

import net.zn.ddxj.entity.SendMessageCategory;

public interface SendMessageCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SendMessageCategory record);

    int insertSelective(SendMessageCategory record);

    SendMessageCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SendMessageCategory record);

    int updateByPrimaryKey(SendMessageCategory record);
}