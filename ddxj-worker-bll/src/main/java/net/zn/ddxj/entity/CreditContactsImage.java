package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 授信合同照片
 * 
 * @author wcyong
 * 
 * @date 2018-05-03
 */
public class CreditContactsImage implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7984573919974768055L;

	/**
     * 主键ID
     */
    private Integer id;

    /**
     * 授信记录ID
     */
    private Integer recruitCreditId;

    /**
     * 合同照片
     */
    private String contractImage;

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
    private String flag;

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

    public String getContractImage() {
        return contractImage;
    }

    public void setContractImage(String contractImage) {
        this.contractImage = contractImage == null ? null : contractImage.trim();
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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag == null ? null : flag.trim();
    }
}