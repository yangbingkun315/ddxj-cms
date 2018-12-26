package net.zn.ddxj.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 工程需求表
 * 
 * @author wcyong
 * 
 * @date 2018-06-12
 */
public class SiteJobDemand implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6435747358721819416L;

	private Integer id;

    /**
     * 工程地点
     */
    private String address;

    /**
     * 工种名称
     */
    private String categoryName;

    /**
     * 工期开始时间
     */
    private Date startTime;

    /**
     * 工期结束时间
     */
    private Date endTime;

    /**
     * 需求描述
     */
    private String demandDesc;

    /**
     * 预发工资
     */
    private BigDecimal estimateWages;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 用户IP地址
     */
    private String ip;

    /**
     * 受理状态 1-未受理 2-已受理
     */
    private Integer status;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName == null ? null : categoryName.trim();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getDemandDesc() {
        return demandDesc;
    }

    public void setDemandDesc(String demandDesc) {
        this.demandDesc = demandDesc == null ? null : demandDesc.trim();
    }

    public BigDecimal getEstimateWages() {
        return estimateWages;
    }

    public void setEstimateWages(BigDecimal estimateWages) {
        this.estimateWages = estimateWages;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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