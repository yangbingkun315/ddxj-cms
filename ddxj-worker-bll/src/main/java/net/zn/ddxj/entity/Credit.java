package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 授信机构列表
 * 
 * @author wcyong
 * 
 * @date 2018-04-18
 */
public class Credit implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -166798271543600421L;

	/**
     * 主键ID
     */
    private Integer id;

    /**
     * 授信公司名称
     */
    private String creditName;

    /**
     * 授信公司代码
     */
    private String creditCode;

    /**
     * 授信公司logo
     */
    private String creditLogo;

    /**
     * 授信简介
     */
    private String creditDesc;

    /**
     * 详细地址
     */
    private String creditAddress;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreditName() {
        return creditName;
    }

    public void setCreditName(String creditName) {
        this.creditName = creditName == null ? null : creditName.trim();
    }

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode == null ? null : creditCode.trim();
    }

    public String getCreditLogo() {
        return creditLogo;
    }

    public void setCreditLogo(String creditLogo) {
        this.creditLogo = creditLogo == null ? null : creditLogo.trim();
    }

    public String getCreditDesc() {
        return creditDesc;
    }

    public void setCreditDesc(String creditDesc) {
        this.creditDesc = creditDesc == null ? null : creditDesc.trim();
    }

    public String getCreditAddress() {
        return creditAddress;
    }

    public void setCreditAddress(String creditAddress) {
        this.creditAddress = creditAddress == null ? null : creditAddress.trim();
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