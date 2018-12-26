package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 实名认证
 * 
 * @author wcyong
 * 
 * @date 2018-04-18
 */
public class RealAuth implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8785473951288331490L;

	/**
     * 主键ID
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 身份证-正面
     */
    private String idCardFront;

    /**
     * 身份证-反面
     */
    private String idCardOpposite;

    /**
     * 身份证号
     */
    private String idCardNumber;

    /**
     * 手持身份证
     */
    private String idCardHand;

    /**
     * 审核状态（1：审核中，2：审核失败，3审核成功）
     */
    private Integer realStatus;

    /**
     * 身份证所在地
     */
    private String idCardAddress;

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
     * 实名认证json数据
     */
    private String validateData;
    
    private User user;
    
    public String getValidateData() {
		return validateData;
	}

	public void setValidateData(String validateData) {
		this.validateData = validateData;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getIdCardFront() {
        return idCardFront;
    }

    public void setIdCardFront(String idCardFront) {
        this.idCardFront = idCardFront == null ? null : idCardFront.trim();
    }

    public String getIdCardOpposite() {
        return idCardOpposite;
    }

    public void setIdCardOpposite(String idCardOpposite) {
        this.idCardOpposite = idCardOpposite == null ? null : idCardOpposite.trim();
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber == null ? null : idCardNumber.trim();
    }

    public String getIdCardHand() {
        return idCardHand;
    }

    public void setIdCardHand(String idCardHand) {
        this.idCardHand = idCardHand == null ? null : idCardHand.trim();
    }

    public Integer getRealStatus() {
        return realStatus;
    }

    public void setRealStatus(Integer realStatus) {
        this.realStatus = realStatus;
    }

    public String getIdCardAddress() {
        return idCardAddress;
    }

    public void setIdCardAddress(String idCardAddress) {
        this.idCardAddress = idCardAddress == null ? null : idCardAddress.trim();
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