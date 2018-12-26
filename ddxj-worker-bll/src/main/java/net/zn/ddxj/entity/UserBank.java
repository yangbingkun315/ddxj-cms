package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 绑定银行卡
 * 
 * @author wcyong
 * 
 * @date 2018-04-28
 */
public class UserBank implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -743907644064499549L;

	/**
     * 主键ID
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 银行卡号
     */
    private String bankCard;

    /**
     * 银行卡正面
     */
    private String bankFront;

    /**
     * 银行卡反面
     */
    private String bankOpposite;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 银行卡类型
     */
    private String bankType;

    /**
     * 持卡人姓名
     */
    private String cardholderName;

    /**
     * 持卡人身份证
     */
    private String cardholderIdNumber;

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
     * 预留手机号
     */
    private String phone;

    /**
     * 银行开户行
     */
    private String address;

    /**
     * 第三方验证JSON
     */
    private String validateJson;
    /**
     * 背景颜色
     */
    private String rgb;
    
    private String bankCode;
    
    public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getRgb() {
		return rgb;
	}

	public void setRgb(String rgb) {
		this.rgb = rgb;
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

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard == null ? null : bankCard.trim();
    }

    public String getBankFront() {
        return bankFront;
    }

    public void setBankFront(String bankFront) {
        this.bankFront = bankFront == null ? null : bankFront.trim();
    }

    public String getBankOpposite() {
        return bankOpposite;
    }

    public void setBankOpposite(String bankOpposite) {
        this.bankOpposite = bankOpposite == null ? null : bankOpposite.trim();
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

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName == null ? null : cardholderName.trim();
    }

    public String getCardholderIdNumber() {
        return cardholderIdNumber;
    }

    public void setCardholderIdNumber(String cardholderIdNumber) {
        this.cardholderIdNumber = cardholderIdNumber == null ? null : cardholderIdNumber.trim();
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getValidateJson() {
        return validateJson;
    }

    public void setValidateJson(String validateJson) {
        this.validateJson = validateJson == null ? null : validateJson.trim();
    }
}