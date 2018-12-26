package net.zn.ddxj.mapper;

import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.WechatUserTag;

public interface WechatUserTagMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WechatUserTag record);

    int insertSelective(WechatUserTag record);

    WechatUserTag selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WechatUserTag record);

    int updateByPrimaryKey(WechatUserTag record);
    
    int addWechatUserTag(@Param("openId")String openId,@Param("tagId")Integer tagId);
}