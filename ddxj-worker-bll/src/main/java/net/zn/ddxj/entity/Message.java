package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author wcyong
 * 
 * @date 2018-05-03
 */
public class Message implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2128238733646478769L;

	private Integer id;

    /**
     * 1-发布审核，2-实名认证，3-提现通知，4-申请工头,5-项目报名
     */
    private Integer messageType;

    /**
     * 消息内容
     */
    private String messageContent;

    /**
     * 消息标题
     */
    private String messageTitle;

    /**
     * 传入参数（?xxx=1&xxx=2）
     */
    private String messageParameter;

    /**
     * 1-未读，2-已读
     */
    private Integer messageRead;

    private Date createTime;

    private Integer flag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent == null ? null : messageContent.trim();
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle == null ? null : messageTitle.trim();
    }

    public String getMessageParameter() {
        return messageParameter;
    }

    public void setMessageParameter(String messageParameter) {
        this.messageParameter = messageParameter == null ? null : messageParameter.trim();
    }

    public Integer getMessageRead() {
        return messageRead;
    }

    public void setMessageRead(Integer messageRead) {
        this.messageRead = messageRead;
    }


    public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}