package net.zn.ddxj.tool.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import net.zn.ddxj.constants.Constants;
import net.zn.ddxj.entity.Message;
import net.zn.ddxj.mapper.MessageMapper;
import net.zn.ddxj.tool.MessageService;
import net.zn.ddxj.utils.RedisUtils;

@Service
public class MessageServiceImpl implements MessageService{
	@Autowired
	private MessageMapper messageMapper;
	@Autowired
	private RedisUtils redisUtils;
	@Override
	@Async
	public void sendMessage(Integer messageType, String messageContent, String messageTitle, String messageParameter) {
			Message message = new Message();
			message.setMessageTitle(messageTitle);
			message.setMessageContent(messageContent);
			message.setMessageType(messageType);
			message.setMessageParameter(messageParameter);
			message.setCreateTime(new Date());
			messageMapper.insertSelective(message);
			redisUtils.set(Constants.VALIDATE_MESSAGE_NOTIC, messageMapper.queryMessageTotalCount(1));//重置redis数量
			
	}

	@Override
	public List<Message> queryMessageList(Integer type) {
		return messageMapper.queryReadMessageList(type);
	}

	@Override
	public int updateSignMessageRead(Integer id) {
		redisUtils.set(Constants.VALIDATE_MESSAGE_NOTIC, messageMapper.queryMessageTotalCount(1));//重置redis数量
		return messageMapper.updateSignMessageRead(id);
	}

	@Override
	public int queryMessageTotalCount(Integer type) {
		// TODO Auto-generated method stub
		return messageMapper.queryMessageTotalCount(type);
	}

	@Override
	public Message queryMessage(Integer id) {
		// TODO Auto-generated method stub
		return messageMapper.selectByPrimaryKey(id);
	}
	
	

}
