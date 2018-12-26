package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 圈子图片
 * 
 * @author wcyong
 * 
 * @date 2018-04-23
 */
public class CircleImage implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3050023063234931329L;

	/**
     * 主键ID
     */
    private Integer id;

    /**
     * 圈子ID
     */
    private Integer circleId;

    /**
     * 图片路径
     */
    private String pictureUrl;

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

    public Integer getCircleId() {
        return circleId;
    }

    public void setCircleId(Integer circleId) {
        this.circleId = circleId;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl == null ? null : pictureUrl.trim();
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