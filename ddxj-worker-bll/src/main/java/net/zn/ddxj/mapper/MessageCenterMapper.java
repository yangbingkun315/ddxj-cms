package net.zn.ddxj.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.zn.ddxj.entity.MessageCenter;

public interface MessageCenterMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MessageCenter record);

    int insertSelective(MessageCenter record);

    MessageCenter selectByPrimaryKey(Integer id);

    int updateByIdAndUserIdSelective(MessageCenter record);

    int updateByUserIdKey(@Param("isRead")Integer isRead,@Param("updateTime")Date updateTime,@Param("userId") Integer userId);
    
    int findByUserIdAndFlag(@Param("userId") Integer userId,@Param("isRead") Integer isRead);
    
    List<MessageCenter> queryMessage(@Param("userId")Integer userId,@Param("messageTypeId") Integer messageTypeId);
    
    MessageCenter selectByIdAndUserId(@Param("id")Integer id,@Param("userId")Integer userId);

	List<MessageCenter> queryMessageCenter(@Param("userId")Integer userId,@Param("typeId") Integer typeId);//查询消息中心
	
	List<MessageCenter> queryInformIsRead(@Param("userId")Integer userId,@Param("typeId") Integer typeId,@Param("isRead")Integer isRead);
	
	List<MessageCenter> queryActivityIsRead(@Param("userId")Integer userId,@Param("typeId") Integer typeId,@Param("isRead")Integer isRead);
	
	List<MessageCenter> queryEvaluateIsRead(@Param("userId")Integer userId,@Param("typeId") Integer typeId,@Param("isRead")Integer isRead);
	
	List<MessageCenter> queryTransactionIsRead(@Param("userId")Integer userId,@Param("typeId") Integer typeId,@Param("isRead")Integer isRead);

	List<Map<String, Object>> selectIsRead(@Param("userId") int userId, @Param("isRead") int isRead);//查询是否有未读消息

	int updateMessageFlag(Integer userId);
	
	MessageCenter queryNewMessageCenter(@Param("userId") int userId,@Param("typeId") int typeId);
	
}