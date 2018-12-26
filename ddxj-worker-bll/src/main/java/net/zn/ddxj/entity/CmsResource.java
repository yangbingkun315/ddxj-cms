package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author wcyong
 * 
 * @date 2018-08-09
 */
public class CmsResource  implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6563659926447461325L;

	private Integer id;

    /**
     * 上级ID
     */
    private Integer parentId;
    
    private CmsResource resourceParent;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源唯一标识
     */
    private String resourceKey;

    /**
     * 资源类型,0:目录;1:菜单;2:按钮
     */
    private Integer resourceType;

    /**
     * 资源url
     */
    private String resourceUrl;

    /**
     * 层级
     */
    private Integer resourceLevel;

    /**
     * 排序
     */
    private Integer resourceSort;

    /**
     * 图标
     */
    private String icon;

    /**
     * 描述
     */
    private String resourceDescription;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private Integer flag;
    
	public CmsResource getResourceParent() {
		return resourceParent;
	}

	public void setResourceParent(CmsResource resourceParent) {
		this.resourceParent = resourceParent;
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

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName == null ? null : resourceName.trim();
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey == null ? null : resourceKey.trim();
    }

    public Integer getResourceType() {
        return resourceType;
    }

    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl == null ? null : resourceUrl.trim();
    }

    public Integer getResourceLevel() {
        return resourceLevel;
    }

    public void setResourceLevel(Integer resourceLevel) {
        this.resourceLevel = resourceLevel;
    }


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    public String getResourceDescription() {
        return resourceDescription;
    }

    public void setResourceDescription(String resourceDescription) {
        this.resourceDescription = resourceDescription == null ? null : resourceDescription.trim();
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

	public Integer getResourceSort() {
		return resourceSort;
	}

	public void setResourceSort(Integer resourceSort) {
		this.resourceSort = resourceSort;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + ((flag == null) ? 0 : flag.hashCode());
		result = prime * result + ((icon == null) ? 0 : icon.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((parentId == null) ? 0 : parentId.hashCode());
		result = prime
				* result
				+ ((resourceDescription == null) ? 0 : resourceDescription
						.hashCode());
		result = prime * result
				+ ((resourceKey == null) ? 0 : resourceKey.hashCode());
		result = prime * result
				+ ((resourceLevel == null) ? 0 : resourceLevel.hashCode());
		result = prime * result
				+ ((resourceName == null) ? 0 : resourceName.hashCode());
		result = prime * result
				+ ((resourceParent == null) ? 0 : resourceParent.hashCode());
		result = prime * result
				+ ((resourceType == null) ? 0 : resourceType.hashCode());
		result = prime * result
				+ ((resourceUrl == null) ? 0 : resourceUrl.hashCode());
		result = prime * result + ((resourceSort == null) ? 0 : resourceSort.hashCode());
		result = prime * result
				+ ((updateTime == null) ? 0 : updateTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CmsResource other = (CmsResource) obj;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (flag == null) {
			if (other.flag != null)
				return false;
		} else if (!flag.equals(other.flag))
			return false;
		if (icon == null) {
			if (other.icon != null)
				return false;
		} else if (!icon.equals(other.icon))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (parentId == null) {
			if (other.parentId != null)
				return false;
		} else if (!parentId.equals(other.parentId))
			return false;
		if (resourceDescription == null) {
			if (other.resourceDescription != null)
				return false;
		} else if (!resourceDescription.equals(other.resourceDescription))
			return false;
		if (resourceKey == null) {
			if (other.resourceKey != null)
				return false;
		} else if (!resourceKey.equals(other.resourceKey))
			return false;
		if (resourceLevel == null) {
			if (other.resourceLevel != null)
				return false;
		} else if (!resourceLevel.equals(other.resourceLevel))
			return false;
		if (resourceName == null) {
			if (other.resourceName != null)
				return false;
		} else if (!resourceName.equals(other.resourceName))
			return false;
		if (resourceParent == null) {
			if (other.resourceParent != null)
				return false;
		} else if (!resourceParent.equals(other.resourceParent))
			return false;
		if (resourceType == null) {
			if (other.resourceType != null)
				return false;
		} else if (!resourceType.equals(other.resourceType))
			return false;
		if (resourceUrl == null) {
			if (other.resourceUrl != null)
				return false;
		} else if (!resourceUrl.equals(other.resourceUrl))
			return false;
		if (resourceSort == null) {
			if (other.resourceSort != null)
				return false;
		} else if (!resourceSort.equals(other.resourceSort))
			return false;
		if (updateTime == null) {
			if (other.updateTime != null)
				return false;
		} else if (!updateTime.equals(other.updateTime))
			return false;
		return true;
	}
    
}