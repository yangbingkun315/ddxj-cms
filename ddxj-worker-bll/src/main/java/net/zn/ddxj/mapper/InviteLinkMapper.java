package net.zn.ddxj.mapper;

import java.util.List;

import net.zn.ddxj.entity.InviteLink;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

public interface InviteLinkMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(InviteLink record);

    int insertSelective(InviteLink record);

    InviteLink selectByPrimaryKey(Integer id);
    
    InviteLink findByUserId(Integer userId);

    int updateByPrimaryKeySelective(InviteLink record);

    int updateByPrimaryKey(InviteLink record);

	List<InviteLink> queryInviteLinkRecord(RequestVo requestVo);
	
	List<InviteLink> queryInviteLinkRecordCms(CmsRequestVo requestVo);
}