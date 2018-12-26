package net.zn.ddxj.mapper;

import net.zn.ddxj.entity.WechatToken;

public interface WechatTokenMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WechatToken record);

    int insertSelective(WechatToken record);

    WechatToken selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WechatToken record);

    int updateByPrimaryKey(WechatToken record);
    
    WechatToken queryLastToken();
}