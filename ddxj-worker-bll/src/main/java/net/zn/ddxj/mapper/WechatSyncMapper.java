package net.zn.ddxj.mapper;

import net.zn.ddxj.entity.WechatSync;

public interface WechatSyncMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WechatSync record);

    int insertSelective(WechatSync record);

    WechatSync selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WechatSync record);

    int updateByPrimaryKey(WechatSync record);
    
    int queryWechatSyncById(String wechatId);//根据素材id查询是否有同步记录
}