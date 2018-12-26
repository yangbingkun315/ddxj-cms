package net.zn.ddxj.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 授信资金变动记录
 * 
 * @author wcyong
 * 
 * @date 2018-05-14
 */
public class CreditRepayment implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3655131672262569776L;

	/**
     * 主键ID
     */
    private Integer id;

    /**
     * 授信记录ID
     */
    private Integer recruitCreditId;

    /**
     * 交易金额
     */
    private BigDecimal businessMoney;

    /**
     * 用途
     */
    private String application;

    /**
     * 类型（1-发放，2-还款）
     */
    private Integer type;

    /**
     * 交易状态 (1审核中,2审核成功,3审核失败)
     */
    private Integer businessStatus;

    /**
     * 收款人姓名
     */
    private String payeeName;
    
    /**
     * 收款人ID
     */
    private Integer userId;


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
    

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRecruitCreditId() {
        return recruitCreditId;
    }

    public void setRecruitCreditId(Integer recruitCreditId) {
        this.recruitCreditId = recruitCreditId;
    }

    public BigDecimal getBusinessMoney() {
        return businessMoney;
    }

    public void setBusinessMoney(BigDecimal businessMoney) {
        this.businessMoney = businessMoney;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application == null ? null : application.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(Integer businessStatus) {
        this.businessStatus = businessStatus;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName == null ? null : payeeName.trim();
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