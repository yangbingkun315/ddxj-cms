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
public class ManagerMenu implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2125483071732592222L;

	private Integer id;

    /**
     * 上级ID
     */
    private Integer pid;

    /**
     * 访问链接
     */
    private String url;

    /**
     * 图标
     */
    private String icon;

    /**
     * 菜单名称
     */
    private String name;

    private Date createTime;

    private Date updateTime;
    
    private Integer sort;
    
    private ManagerMenu managerMenuParent;


	public ManagerMenu getManagerMenuParent() {
		return managerMenuParent;
	}

	public void setManagerMenuParent(ManagerMenu managerMenuParent) {
		this.managerMenuParent = managerMenuParent;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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
}