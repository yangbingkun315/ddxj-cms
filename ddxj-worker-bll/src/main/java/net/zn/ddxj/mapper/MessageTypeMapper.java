package net.zn.ddxj.mapper;

import net.zn.ddxj.entity.MessageType;

public interface MessageTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MessageType record);

    int insertSelective(MessageType record);

    MessageType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessageType record);

    int updateByPrimaryKey(MessageType record);
}