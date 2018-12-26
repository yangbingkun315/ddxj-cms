package net.zn.ddxj.service;

import java.util.List;
import java.util.Map;

import net.zn.ddxj.entity.MessageCenter;

public interface UserCenterService {
	
	/**
	 * 查询是否有未读消息
	 * @param userId
	 * @return
	 */
	int queryIsRead(Integer userId);
	
	/**
	 * 查询消息列表
	 * @return
	 */
	List<MessageCenter> queryMessageList(Integer userId,Integer messageTypeId);
	
	/**
	 * 更新消息状态
	 * @param messageTypeId
	 * @param MessageId
	 * @param userId
	 */
	int updateMessageStuatus(Integer messageId,Integer userId);

	List<MessageCenter> queryMessageCenter(int userId ,int typeId);//查询消息中心
	
	/**
	 * 查询最新的消息
	 * @param userId
	 * @param typeId
	 * @param isRead
	 * @return
	 */
	MessageCenter queryNewMessageCenter(int userId,int typeId);
	
	List<MessageCenter> queryInformIsRead(int userId,int isRead,int typeId);//通知
	
	List<MessageCenter> queryActivityIsRead(int userId,int isRead,int typeId);//活动
	
	List<MessageCenter> queryEvaluateIsRead(int userId,int isRead,int typeId);//评价
	
	List<MessageCenter> queryTransactionIsRead(int userId,int isRead,int typeId);//交易

	List<Map<String, Object>> selectIsRead(int userId, int isRead); //查询是否有未读消息

	int deleteMessageRecord(Integer userId); //删除消息记录
	
}
