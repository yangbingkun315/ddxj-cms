package net.zn.ddxj.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * 
 * @author wcyong
 * 
 * @date 2018-05-22
 */
public class SalaryRecord implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 9057300752055952266L;

	private Integer id;

    /**
     * 结算ID
     */
    private Integer transferId;

    /**
     * 招聘ID
     */
    private Integer recruitId;

    /**
     * 发放工资人用户ID
     */
    private Integer assignUserId;

    /**
     * 接受工资人用户ID
     */
    private Integer sendeeUserId;

    /**
     * 转账金额
     */
    private BigDecimal money;

    /**
     * 转账方式（1-授信支付，2-余额支付，3-银行卡）
     */
    private Integer transferWay;

    /**
     * 转账描述
     */
    private String transferDesc;

    /**
     * 转账类型（1-转账，2-结算，3-充值，4-提现，5-系统发放）
     */
    private Integer transferType;

    /**
     * 单位
     */
    private String unit;

    /**
     * 数量
     */
    private String count;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 审核状态(1-审核中 2-审核成功 3-审核失败)
     */
    private Integer auditStatus;

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
    
    private User toUser;
    
    private Recruit recruit;
    
    

	public User getToUser() {
		return toUser;
	}

	public void setToUser(User toUser) {
		this.toUser = toUser;
	}

	public Recruit getRecruit() {
		return recruit;
	}

	public void setRecruit(Recruit recruit) {
		this.recruit = recruit;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTransferId() {
        return transferId;
    }

    public void setTransferId(Integer transferId) {
        this.transferId = transferId;
    }

    public Integer getRecruitId() {
        return recruitId;
    }

    public void setRecruitId(Integer recruitId) {
        this.recruitId = recruitId;
    }

    public Integer getAssignUserId() {
        return assignUserId;
    }

    public void setAssignUserId(Integer assignUserId) {
        this.assignUserId = assignUserId;
    }

    public Integer getSendeeUserId() {
        return sendeeUserId;
    }

    public void setSendeeUserId(Integer sendeeUserId) {
        this.sendeeUserId = sendeeUserId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getTransferWay() {
        return transferWay;
    }

    public void setTransferWay(Integer transferWay) {
        this.transferWay = transferWay;
    }

    public String getTransferDesc() {
        return transferDesc;
    }

    public void setTransferDesc(String transferDesc) {
        this.transferDesc = transferDesc == null ? null : transferDesc.trim();
    }

    
    public Integer getTransferType() {
		return transferType;
	}

	public void setTransferType(Integer transferType) {
		this.transferType = transferType;
	}

	public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count == null ? null : count.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
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