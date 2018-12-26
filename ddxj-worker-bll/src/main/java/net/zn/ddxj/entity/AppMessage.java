package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * APP消息管理
 * @author ddxj
 *
 */
public class AppMessage implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 8183409491515485362L;

	private Integer id;

    private Integer userId;

    /**
     * 消息类型（1-站内信，2-实名认证审核，3-招聘通知，4-招聘邀请，5-招聘审核，6-招聘状态，7-授信审核，8-提现审核，9-系统广播，10-评论推送，11-转账消息，12-结算消息)
     */
    private Integer messageType;

    /**
     * 1-通知类，2-消息类，3-广播类
     */
    private Integer messageCategory;

    /**
     * 消息标题
     */
    private String messageTitle;

    /**
     * 消息简介
     */
    private String messageSummary;

    /**
     * 消息内容
     */
    private String messageContent;

    /**
     * 1-置顶，2-不置顶
     */
    private Integer messageStick;

    /**
     * 1-未读，2-已读
     */
    private Integer messageRead;

    private Date createTime;

    private Date updateTime;

    private Integer flag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public Integer getMessageCategory() {
        return messageCategory;
    }

    public void setMessageCategory(Integer messageCategory) {
        this.messageCategory = messageCategory;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle == null ? null : messageTitle.trim();
    }

    public String getMessageSummary() {
        return messageSummary;
    }

    public void setMessageSummary(String messageSummary) {
        this.messageSummary = messageSummary == null ? null : messageSummary.trim();
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent == null ? null : messageContent.trim();
    }

    public Integer getMessageStick() {
        return messageStick;
    }

    public void setMessageStick(Integer messageStick) {
        this.messageStick = messageStick;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}