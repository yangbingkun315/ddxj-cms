package net.zn.ddxj.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.zn.ddxj.entity.InviteLink;
import net.zn.ddxj.mapper.InviteLinkMapper;
import net.zn.ddxj.service.InviteLinkService;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

/**
 * @author GZF
 */
@Service
@Component
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
public class InviteLinkServiceImpl implements InviteLinkService {

    @Autowired
    private InviteLinkMapper inviteLinkMapper;

    @Override
    public void insertInviteLink(Integer userId,String ticket,String url) {
    	
        InviteLink inviteLink = new InviteLink();
        inviteLink.setUserId(userId);
        inviteLink.setTicket(ticket);
        inviteLink.setUrl(url);
        inviteLink.setCreateTime(new Date());
        inviteLink.setUpdateTime(new Date());
        inviteLink.setFlag(1);
        inviteLinkMapper.insert(inviteLink);
    }

	@Override
	public InviteLink findByUserId(Integer userId) {
		return inviteLinkMapper.findByUserId(userId);
	}

	@Override
	public List<InviteLink> queryInviteLinkRecord(RequestVo requestVo) {
		// TODO Auto-generated method stub
		return inviteLinkMapper.queryInviteLinkRecord(requestVo);
	}

	@Override
	public List<InviteLink> queryInviteLinkRecordCms(CmsRequestVo requestVo) {
		return inviteLinkMapper.queryInviteLinkRecordCms(requestVo);
	}
}
