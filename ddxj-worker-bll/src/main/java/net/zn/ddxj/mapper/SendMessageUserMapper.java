package net.zn.ddxj.mapper;

import java.util.List;

import net.zn.ddxj.entity.SendMessageUser;
import net.zn.ddxj.vo.RequestVo;

public interface SendMessageUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SendMessageUser record);

    int insertSelective(SendMessageUser record);

    SendMessageUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SendMessageUser record);

    int updateByPrimaryKey(SendMessageUser record);
    
    List<SendMessageUser> querySendMessageUserList(RequestVo requestVo);//多条件查询群发用户列表
}