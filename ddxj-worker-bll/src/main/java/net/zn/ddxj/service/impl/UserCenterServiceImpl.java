package net.zn.ddxj.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.zn.ddxj.entity.MessageCenter;
import net.zn.ddxj.mapper.MessageCenterMapper;
import net.zn.ddxj.service.UserCenterService;
import net.zn.ddxj.utils.CmsUtils;

@Service
@Component
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
public class UserCenterServiceImpl implements UserCenterService {

	@Autowired
	private MessageCenterMapper messageCenterMapper;

	@Override
	public int queryIsRead(Integer userId) {
		int count = messageCenterMapper.findByUserIdAndFlag(userId, 1);
		return count;
	}

	@Override
	public List<MessageCenter> queryMessageList(Integer userId, Integer messageTypeId) {
		return messageCenterMapper.queryMessage(userId, messageTypeId);
	}

	@Override
	public int updateMessageStuatus(Integer messageId, Integer userId) {
		int count = 0;
		if (0 == messageId) {
			if (!CmsUtils.isNullOrEmpty(userId)) {
				count = messageCenterMapper.updateByUserIdKey(2, new Date(), userId);
			}
		} else {
			MessageCenter messageCenter = messageCenterMapper.selectByPrimaryKey(messageId);
			if (!CmsUtils.isNullOrEmpty(messageCenter)) {
				messageCenter.setIsRead(2);
				messageCenter.setUpdateTime(new Date());
				count = messageCenterMapper.updateByIdAndUserIdSelective(messageCenter);
			}
		}
		return count;
	}

	@Override
	public List<MessageCenter> queryMessageCenter(int userId,int typeId) {
		// TODO Auto-generated method stub
		return messageCenterMapper.queryMessageCenter(userId,typeId);
	}

	@Override
	public List<MessageCenter> queryInformIsRead(int userId, int isRead, int typeId) {
		// TODO Auto-generated method stub
		return messageCenterMapper.queryInformIsRead(userId, typeId, isRead);
	}

	@Override
	public List<MessageCenter> queryActivityIsRead(int userId, int isRead, int typeId) {
		// TODO Auto-generated method stub
		return messageCenterMapper.queryActivityIsRead(userId, typeId, isRead);
	}

	@Override
	public List<MessageCenter> queryEvaluateIsRead(int userId, int isRead, int typeId) {
		// TODO Auto-generated method stub
		return messageCenterMapper.queryEvaluateIsRead(userId, typeId, isRead);
	}

	@Override
	public List<MessageCenter> queryTransactionIsRead(int userId, int isRead, int typeId) {
		// TODO Auto-generated method stub
		return messageCenterMapper.queryTransactionIsRead(userId, typeId, isRead);
	}

	@Override
	public List<Map<String, Object>> selectIsRead(int userId, int isRead) {
		// TODO Auto-generated method stub
		return messageCenterMapper.selectIsRead(userId,isRead);
	}

	@Override
	public int deleteMessageRecord(Integer userId) {
		// TODO Auto-generated method stub
		return messageCenterMapper.updateMessageFlag(userId);
	}

	@Override
	public MessageCenter queryNewMessageCenter(int userId, int typeId) {
		// TODO Auto-generated method stub
		return messageCenterMapper.queryNewMessageCenter(userId, typeId);
	}
}
