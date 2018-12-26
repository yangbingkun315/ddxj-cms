package net.zn.ddxj.mapper;

import java.util.List;

import net.zn.ddxj.entity.WechatSendMessage;
import net.zn.ddxj.vo.RequestVo;

public interface WechatSendMessageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WechatSendMessage record);

    int insertSelective(WechatSendMessage record);

    WechatSendMessage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WechatSendMessage record);

    int updateByPrimaryKey(WechatSendMessage record);
    
    List<WechatSendMessage> queryWechatSendMessageList(RequestVo requestVo);//多条件查询群发记录
}