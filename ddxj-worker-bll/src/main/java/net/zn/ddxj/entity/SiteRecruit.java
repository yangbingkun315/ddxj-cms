package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 招贤纳士
 * 
 * @author wcyong
 * 
 * @date 2018-06-12
 */
public class SiteRecruit implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 184538983152019079L;

	private Integer id;

    /**
     * 招聘图片
     */
    private String recruitImg;

    /**
     * 职位名称
     */
    private String recruitName;

    /**
     * 岗位要求
     */
    private String recruitDemand;

    /**
     * 岗位职责
     */
    private String recuritDuties;

    /**
     * 任职要求
     */
    private String recruitTenure;

    /**
     * 联系人
     */
    private String name;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 联系邮箱
     */
    private String email;

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

    public String getRecruitImg() {
        return recruitImg;
    }

    public void setRecruitImg(String recruitImg) {
        this.recruitImg = recruitImg == null ? null : recruitImg.trim();
    }

    public String getRecruitName() {
        return recruitName;
    }

    public void setRecruitName(String recruitName) {
        this.recruitName = recruitName == null ? null : recruitName.trim();
    }

    public String getRecruitDemand() {
        return recruitDemand;
    }

    public void setRecruitDemand(String recruitDemand) {
        this.recruitDemand = recruitDemand == null ? null : recruitDemand.trim();
    }

    public String getRecuritDuties() {
        return recuritDuties;
    }

    public void setRecuritDuties(String recuritDuties) {
        this.recuritDuties = recuritDuties == null ? null : recuritDuties.trim();
    }

    public String getRecruitTenure() {
        return recruitTenure;
    }

    public void setRecruitTenure(String recruitTenure) {
        this.recruitTenure = recruitTenure == null ? null : recruitTenure.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
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