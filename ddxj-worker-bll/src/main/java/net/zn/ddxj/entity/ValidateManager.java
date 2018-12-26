package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author wcyong
 * 
 * @date 2018-05-09
 */
public class ValidateManager implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1772822650092959819L;

	private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 类型(BANK-银行卡)
     */
    private String validateType;

    /**
     * 验证错误消息
     */
    private String validateMsg;

    private Date createTime;

    private Integer flag;

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

    public String getValidateType() {
        return validateType;
    }

    public void setValidateType(String validateType) {
        this.validateType = validateType == null ? null : validateType.trim();
    }

    public String getValidateMsg() {
        return validateMsg;
    }

    public void setValidateMsg(String validateMsg) {
        this.validateMsg = validateMsg == null ? null : validateMsg.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}