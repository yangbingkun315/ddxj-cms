package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 申请工头
 * 
 * @author wcyong
 * 
 * @date 2018-04-25
 */
public class ApplyForeman implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3880825767513592826L;

	/**
     * 主键ID
     */
    private Integer id;

    /**
     * 申请人用户ID
     */
    private Integer userId;

    /**
     * 申请理由
     */
    private String reason;

    /**
     * 审核时间
     */
    private Date validateTime;

    /**
     * 审核失败原因
     */
    private String validateCause;

    /**
     * 审核状态（1：审核中，2：审核失败，3审核成功）
     */
    private Integer validateStatus;

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
    /**
     * user
     * @return
     */
    private User user;
    
    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public Date getValidateTime() {
        return validateTime;
    }

    public void setValidateTime(Date validateTime) {
        this.validateTime = validateTime;
    }

    public String getValidateCause() {
        return validateCause;
    }

    public void setValidateCause(String validateCause) {
        this.validateCause = validateCause == null ? null : validateCause.trim();
    }

    public Integer getValidateStatus() {
        return validateStatus;
    }

    public void setValidateStatus(Integer validateStatus) {
        this.validateStatus = validateStatus;
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