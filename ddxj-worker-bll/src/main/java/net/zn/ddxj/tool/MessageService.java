package net.zn.ddxj.tool;

import java.util.List;

import net.zn.ddxj.entity.Message;

public interface MessageService {
	
	public void sendMessage(Integer messageType,String messageContent,String messageTitle,String messageParameter);
	
	public List<Message> queryMessageList(Integer type);
	public int updateSignMessageRead(Integer id);
	
	int queryMessageTotalCount(Integer type);
	Message queryMessage(Integer id);//根据ID查询消息
}
