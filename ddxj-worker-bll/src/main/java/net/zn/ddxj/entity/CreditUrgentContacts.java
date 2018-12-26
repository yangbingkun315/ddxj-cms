package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 授信紧急联系人
 * 
 * @author wcyong
 * 
 * @date 2018-05-03
 */
public class CreditUrgentContacts implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1043191767019354269L;

	/**
     * 主键ID
     */
    private Integer id;

    /**
     * 授信记录ID
     */
    private Integer recruitCreditId;

    /**
     * 联系人姓名
     */
    private String contactsName;

    /**
     * 联系人电话
     */
    private String contactsPhone;

    /**
     * 联系人与本人关系
     */
    private String contactsRelation;

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

    public String getContactsName() {
        return contactsName;
    }

    public void setContactsName(String contactsName) {
        this.contactsName = contactsName == null ? null : contactsName.trim();
    }

    public String getContactsPhone() {
        return contactsPhone;
    }

    public void setContactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone == null ? null : contactsPhone.trim();
    }

    public String getContactsRelation() {
        return contactsRelation;
    }

    public void setContactsRelation(String contactsRelation) {
        this.contactsRelation = contactsRelation == null ? null : contactsRelation.trim();
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