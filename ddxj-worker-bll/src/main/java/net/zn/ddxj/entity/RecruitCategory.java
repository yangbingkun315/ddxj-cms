package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 招聘工种分类
 * 
 * @author wcyong
 * 
 * @date 2018-04-26
 */
public class RecruitCategory implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3908351650019618327L;

	/**
     * 主键ID
     */
    private Integer id;

    /**
     * 招聘ID
     */
    private Integer recruitId;

    /**
     * 工种ID
     */
    private Integer categoryId;

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

    public Integer getRecruitId() {
        return recruitId;
    }

    public void setRecruitId(Integer recruitId) {
        this.recruitId = recruitId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
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