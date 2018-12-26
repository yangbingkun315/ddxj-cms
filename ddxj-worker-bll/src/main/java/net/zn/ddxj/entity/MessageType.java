package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息类型表
 * 
 * @author wcyong
 * 
 * @date 2018-07-13
 */
public class MessageType implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7389949774531027238L;

	private Integer id;

    /**
     * 上级id
     */
    private Integer upId;

    /**
     * 类型名称
     */
    private String name;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否有效(1有效 2无效)
     */
    private Integer flag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUpId() {
        return upId;
    }

    public void setUpId(Integer upId) {
        this.upId = upId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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