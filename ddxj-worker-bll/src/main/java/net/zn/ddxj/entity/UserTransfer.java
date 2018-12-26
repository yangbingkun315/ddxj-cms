package net.zn.ddxj.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 转账结算记录
 * 
 * @author wcyong
 * 
 * @date 2018-05-03
 */
public class UserTransfer implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4208162010346618491L;

	/**
     * 主键ID
     */
    private Integer id;

    /**
     * 招聘ID（只有为结算工种的时候才需要填）
     */
    private Integer recruitId;

    /**
     * 提现ID（只有为提现的时候才需要填）
     */
    private Integer withdrawId;

    /**
     * 微信充值订单号（只有为充值的时候才需要填）
     */
    private String wehatPayNo;

    /**
     * 转账人用户ID
     */
    private Integer fromUserId;

    /**
     * 转账金额
     */
    private BigDecimal money;

    /**
     * 收款人用户ID
     */
    private Integer toUserId;

    /**
     * 订单号
     */
    private String orderNo;

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

    private BigDecimal toOverplusBalance;//收款人余额
    
    private BigDecimal fromOverplusBalance;//转账人余额
    
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
     * toUser
     * @return
     */
    private User toUser;
    private User fromUser;
    private Recruit recruit;
    private UserWithdraw withdraw;
    private PaymentRecord payment;
    
    public User getFromUser() {
		return fromUser;
	}

	public void setFromUser(User fromUser) {
		this.fromUser = fromUser;
	}

	public Recruit getRecruit() {
		return recruit;
	}

	public void setRecruit(Recruit recruit) {
		this.recruit = recruit;
	}

	public UserWithdraw getWithdraw() {
		return withdraw;
	}

	public void setWithdraw(UserWithdraw withdraw) {
		this.withdraw = withdraw;
	}

	public PaymentRecord getPayment() {
		return payment;
	}

	public void setPayment(PaymentRecord payment) {
		this.payment = payment;
	}

	public User getToUser() {
		return toUser;
	}

	public void setToUser(User toUser) {
		this.toUser = toUser;
	}

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

    public Integer getWithdrawId() {
        return withdrawId;
    }

    public void setWithdrawId(Integer withdrawId) {
        this.withdrawId = withdrawId;
    }

    public String getWehatPayNo() {
        return wehatPayNo;
    }

    public void setWehatPayNo(String wehatPayNo) {
        this.wehatPayNo = wehatPayNo == null ? null : wehatPayNo.trim();
    }

    public Integer getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
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


    public BigDecimal getToOverplusBalance() {
		return toOverplusBalance;
	}

	public void setToOverplusBalance(BigDecimal toOverplusBalance) {
		this.toOverplusBalance = toOverplusBalance;
	}

	public BigDecimal getFromOverplusBalance() {
		return fromOverplusBalance;
	}

	public void setFromOverplusBalance(BigDecimal fromOverplusBalance) {
		this.fromOverplusBalance = fromOverplusBalance;
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