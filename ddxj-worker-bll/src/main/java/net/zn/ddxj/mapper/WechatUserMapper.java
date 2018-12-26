package net.zn.ddxj.mapper;

import java.util.List;

import net.zn.ddxj.entity.WechatUser;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

public interface WechatUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WechatUser record);

    int insertSelective(WechatUser record);

    WechatUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WechatUser record);

    int updateByPrimaryKey(WechatUser record);
    
    List<String> queryWechatUserOpenId();
    
    int totalWechatUserCount();
    
    List<WechatUser> queryWechatUserList(RequestVo requestVo);//多条件查询微信用户列表
    
    List<WechatUser> queryWechatUserListCms(CmsRequestVo requestVo);//多条件查询微信用户列表
}