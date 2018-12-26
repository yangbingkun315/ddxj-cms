package net.zn.ddxj.entity.manager;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author wcyong
 * 
 * @date 2018-04-28
 */
public class ManagerRole implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6432693306023274493L;

	private Integer id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色简介，作用
     */
    private String roleDesc;

    private Date createTime;

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

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc == null ? null : roleDesc.trim();
    }

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}