package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 群发工种分类表
 * 
 * @author wcyong
 * 
 * @date 2018-07-20
 */
public class SendMessageCategory implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2946708713867840534L;

	/**
     * 主键ID
     */
    private Integer id;

    /**
     * 群发记录ID
     */
    private Integer messageId;

    /**
     * 工种ID
     */
    private Integer categoryId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否有效
     */
    private Integer flag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
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