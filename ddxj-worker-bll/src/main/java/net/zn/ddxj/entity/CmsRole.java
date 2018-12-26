package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * 
 * @author wcyong
 * 
 * @date 2018-08-09
 */
public class CmsRole  implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4301025515587636029L;

	/**
     * 角色id
     */
    private Integer id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色key
     */
    private String roleKey;

    /**
     *  角色描述
     */
    private String roleDescription;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private String flag;
    /**
     * 使用角色数量
     */
    private Integer roleCount;
    
    List<CmsResource> resourceList;


	public Integer getRoleCount() {
		return roleCount;
	}

	public void setRoleCount(Integer roleCount) {
		this.roleCount = roleCount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<CmsResource> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<CmsResource> resourceList) {
		this.resourceList = resourceList;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    public String getRoleKey() {
        return roleKey;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey == null ? null : roleKey.trim();
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription == null ? null : roleDescription.trim();
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