package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户工作收藏表
 * 
 * @author wcyong
 * 
 * @date 2018-04-16
 */
public class UserCollection implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -3119296675054434701L;

	/**
     * 主键ID
     */
    private Integer id;

    /**
     * 关注用户ID
     */
    private Integer fromUserId;

    /**
     * 招聘ID
     */
    private Integer recruitId;

    /**
     * 被关注用户ID
     */
    private Integer toUserId;

    /**
     * 关注类型（1-关注用户，2-收藏工作，3-收藏资讯）
     */
    private Integer collectionType;

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
    
    /**
     * 资讯ID
     */
    private Integer infoId;
    
	public Integer getInfoId() {
		return infoId;
	}

	public void setInfoId(Integer infoId) {
		this.infoId = infoId;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Integer getRecruitId() {
        return recruitId;
    }

    public void setRecruitId(Integer recruitId) {
        this.recruitId = recruitId;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public Integer getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(Integer collectionType) {
        this.collectionType = collectionType;
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