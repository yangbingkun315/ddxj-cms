package net.zn.ddxj.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.InviteSetting;
import net.zn.ddxj.vo.CmsRequestVo;

public interface InviteSettingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(InviteSetting record);

    int insertSelective(InviteSetting record);

    InviteSetting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InviteSetting record);

    int updateByPrimaryKey(InviteSetting record);
    
    InviteSetting queryByFlag(@Param("type")Integer type,@Param("flag")Integer flag);
    
    List<InviteSetting> queryInviteSetting(@Param("title")String title,@Param("type")String type,@Param("startTime")String startTime,@Param("endTime")String endTime);
    
    List<InviteSetting> queryInviteSettingList(CmsRequestVo requestVo);
}