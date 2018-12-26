package net.zn.ddxj.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * 招聘授信记录
 * 
 * @author wcyong
 * 
 * @date 2018-05-03
 */
public class RecruitCredit implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4175806516187542432L;

	/**
     * 主键ID
     */
    private Integer id;

    /**
     * 授信ID
     */
    private Integer creditId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 招聘ID
     */
    private Integer recruitId;

    /**
     * 总额度
     */
    private BigDecimal totalMoney;

    /**
     * 授信状态(0未授信,2已授信1,授信中,3-授信失败)
     */
    private Integer creditStatus;

    /**
     * 可用额度
     */
    private BigDecimal usableMoney;

    /**
     * 利率
     */
    private String interestRate;

    /**
     * 审核原因
     */
    private String validateCause;

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
     * 授信机构实体类
     * @return
     */
    private Credit credit;
    /**
     * 招聘实体类
     */
    private Recruit recruit;
	/**
     * 授信紧急联系人
     * @return
     */
    private List<CreditUrgentContacts> creditUrgentContactsList;
    
    /**
     * 授信合同照片
     * @return
     */
    private List<CreditContactsImage> creditContactsImageList;
    /**
     * 还款记录
     * @return
     */
    private CreditRepayment creditRepayment;
	public CreditRepayment getCreditRepayment() {
		return creditRepayment;
	}
	public void setCreditRepayment(CreditRepayment creditRepayment) {
		this.creditRepayment = creditRepayment;
	}

	public Recruit getRecruit() {
		return recruit;
	}

	public void setRecruit(Recruit recruit) {
		this.recruit = recruit;
	}

    public List<CreditContactsImage> getCreditContactsImageList() {
		return creditContactsImageList;
	}

	public void setCreditContactsImageList(List<CreditContactsImage> creditContactsImageList) {
		this.creditContactsImageList = creditContactsImageList;
	}


	public List<CreditUrgentContacts> getCreditUrgentContactsList() {
		return creditUrgentContactsList;
	}

	public void setCreditUrgentContactsList(List<CreditUrgentContacts> creditUrgentContactsList) {
		this.creditUrgentContactsList = creditUrgentContactsList;
	}

	public Credit getCredit() {
		return credit;
	}

	public void setCredit(Credit credit) {
		this.credit = credit;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCreditId() {
        return creditId;
    }

    public void setCreditId(Integer creditId) {
        this.creditId = creditId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRecruitId() {
        return recruitId;
    }

    public void setRecruitId(Integer recruitId) {
        this.recruitId = recruitId;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Integer getCreditStatus() {
        return creditStatus;
    }

    public void setCreditStatus(Integer creditStatus) {
        this.creditStatus = creditStatus;
    }

    public BigDecimal getUsableMoney() {
        return usableMoney;
    }

    public void setUsableMoney(BigDecimal usableMoney) {
        this.usableMoney = usableMoney;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate == null ? null : interestRate.trim();
    }


	public String getValidateCause() {
		return validateCause;
	}

	public void setValidateCause(String validateCause) {
		this.validateCause = validateCause;
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