package net.zn.ddxj.service;

import java.math.BigDecimal;
import java.util.List;
import net.zn.ddxj.entity.InviteRecord;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;


public interface InviteRecordService {
	
	/**
	 * 插入邀请记录
	 * @param inviterId 邀请者ID
	 * @param oppenId 受邀者微信oppenid
	 */
	void inserRecord(Integer inviterId,String openId);
	
	/**
	 * 根据OpenId查询状态为已关注的用户
	 * @param openId
	 * @return
	 */
	InviteRecord findRecordByOpenId(String openId,Integer status); 
	
	/**
	 * 更新邀请记录 
	 * @param inviteRecord
	 */
	void updateInviteRecord(InviteRecord inviteRecord);
	
	/**
	 * 查询老用户邀请记录
	 * @param userId
	 */
	public List<InviteRecord> queryInviteRecord(Integer userId);
	
	/**
	 * 查询老用户获取的奖励金总额
	 * @param userId
	 * @return
	 */
	public BigDecimal queryInviteBonusCount(Integer userId);

	List<InviteRecord> selectInviteRecord(RequestVo requestVo);
	
	List<InviteRecord> queryInviteRecordCms(CmsRequestVo requestVo);
	
}	
