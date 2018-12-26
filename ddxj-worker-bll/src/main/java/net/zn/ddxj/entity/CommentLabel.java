package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 评论标签
 * 
 * @author wcyong
 * 
 * @date 2018-04-17
 */
public class CommentLabel implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6373922408818530757L;

	/**
     * 主键ID
     */
    private Integer id;

    /**
     * 职位类型 （1-工人，2-包工头）
     */
    private Integer workType;

    /**
     * 标签
     */
    private String label;

    /**
     * 是否选中（1-选中，2-不选中）
     */
    private Integer checked;

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

    public Integer getWorkType() {
        return workType;
    }

    public void setWorkType(Integer workType) {
        this.workType = workType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label == null ? null : label.trim();
    }

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
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