package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 工种分类
 * 
 * @author wcyong
 * 
 * @date 2018-04-12
 */
public class Category implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1654973372655573799L;

	/**
     * 主键ID
     */
    private Integer id;

    /**
     * 工种上级ID
     */
    private Integer parentId;
    
    /**
     * 工种上级名称
     */
    private String parentName;

    /**
     * 工种名称
     */
    private String categoryName;

    /**
     * 工种排序
     */
    private Integer categorySort;

    /**
     * 包工类型（1-总包，2-分包）
     */
    private Integer categoryType;

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

    public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName == null ? null : categoryName.trim();
    }

    public Integer getCategorySort() {
        return categorySort;
    }

    public void setCategorySort(Integer categorySort) {
        this.categorySort = categorySort;
    }

    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
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