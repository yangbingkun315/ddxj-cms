package net.zn.ddxj.service;

import java.util.List;

import net.zn.ddxj.entity.InviteLink;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;


/**
 * @author GZF
 */
public interface InviteLinkService {

    /**
     * 插入数据
     * @param userId
     * @param ticket
     * @param url
     */
    void insertInviteLink(Integer userId,String ticket,String url);
    
    InviteLink findByUserId(Integer userId);

	List<InviteLink> queryInviteLinkRecord(RequestVo requestVo);
	
	List<InviteLink> queryInviteLinkRecordCms(CmsRequestVo requestVo);
}
