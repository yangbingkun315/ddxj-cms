package net.zn.ddxj.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户提现
 * 
 * @author wcyong
 * 
 * @date 2018-04-19
 */
public class UserWithdraw implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4048826521750013052L;

	/**
     * 主键ID
     */
    private Integer id;

    /**
     * 提现金额
     */
    private BigDecimal money;

    /**
     * 提现用户ID
     */
    private Integer userId;

    /**
     * 银行卡号
     */
    private String bankOn;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 银行卡类型
     */
    private String bankType;

    /**
     * 审核流程（1、客服审核 2、财务审核 3、财经审核 4、boss审核（大于一万的）5、出纳打款6、打款成功）
     */
    private Integer withdrawProcess;
    
    /**
     * 审核状态（0、审核中 1、客服审核成功 2、客服审核失败 3、财务审核成功 4、财务审核失败 5、财经审核成功 6、财经审核失败 7、boss审核成功 8、boss审核失败9、出纳打款）
     */
    private Integer withdrawStatus;

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
    
    public Integer getWithdrawProcess() {
		return withdrawProcess;
	}

	public void setWithdrawProcess(Integer withdrawProcess) {
		this.withdrawProcess = withdrawProcess;
	}

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

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getBankOn() {
        return bankOn;
    }

    public void setBankOn(String bankOn) {
        this.bankOn = bankOn == null ? null : bankOn.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType == null ? null : bankType.trim();
    }

    public Integer getWithdrawStatus() {
        return withdrawStatus;
    }

    public void setWithdrawStatus(Integer withdrawStatus) {
        this.withdrawStatus = withdrawStatus;
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