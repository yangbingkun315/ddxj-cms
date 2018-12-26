package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 招聘职位邀请表
 * 
 * @author wcyong
 * 
 * @date 2018-04-16
 */
public class UserRequest implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1211167676380385310L;

	/**
     * 主键ID
     */
    private Integer id;

    /**
     * 招聘ID
     */
    private Integer recruitId;

    /**
     * 接受邀请用户ID
     */
    private Integer acceptUserId;

    /**
     * 发送邀请用户ID
     */
    private Integer sendUserId;

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

    public Integer getRecruitId() {
        return recruitId;
    }

    public void setRecruitId(Integer recruitId) {
        this.recruitId = recruitId;
    }

    public Integer getAcceptUserId() {
        return acceptUserId;
    }

    public void setAcceptUserId(Integer acceptUserId) {
        this.acceptUserId = acceptUserId;
    }

    public Integer getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(Integer sendUserId) {
        this.sendUserId = sendUserId;
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