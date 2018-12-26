package net.zn.ddxj.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 分销奖励记录表
 * 
 * @author wcyong
 * 
 * @date 2018-06-13
 */
public class InviteRecord implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4540520844468990199L;

	private Integer id;

    /**
     * 邀请者ID
     */
    private Integer inviterId;

    /**
     * 受邀者微信oppenid
     */
    private String openId;

    /**
     * 受邀人手机号
     */
    private String inviteesPhone;

    /**
     * 邀请人奖励金
     */
    private BigDecimal inviterBonus;

    /**
     * 受邀人奖励金
     */
    private BigDecimal inviteesBonus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 注册状态: 1.已关注 2.未关注 3.已注册
     */
    private Integer status;
    
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

    public Integer getInviterId() {
        return inviterId;
    }

    public void setInviterId(Integer inviterId) {
        this.inviterId = inviterId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    public String getInviteesPhone() {
        return inviteesPhone;
    }

    public void setInviteesPhone(String inviteesPhone) {
        this.inviteesPhone = inviteesPhone == null ? null : inviteesPhone.trim();
    }

    public BigDecimal getInviterBonus() {
        return inviterBonus;
    }

    public void setInviterBonus(BigDecimal inviterBonus) {
        this.inviterBonus = inviterBonus;
    }

    public BigDecimal getInviteesBonus() {
        return inviteesBonus;
    }

    public void setInviteesBonus(BigDecimal inviteesBonus) {
        this.inviteesBonus = inviteesBonus;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}