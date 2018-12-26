package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 广告轮播图
 * 
 * @author wcyong
 * 
 * @date 2018-04-23
 */
public class AdvertBanner implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3707957058293294100L;

	/**
     * 主键ID
     */
    private Integer id;

    /**
     * 图片URL
     */
    private String bannerUrl;

    /**
     * 图片跳转链接
     */
    private String bannerLink;

    /**
     * 类型（1-图片，2-视频）
     */
    private Integer bannerType;

    /**
     * 图片上架时间
     */
    private Date startTime;

    /**
     * 图片下架时间
     */
    private Date endTime;

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

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl == null ? null : bannerUrl.trim();
    }

    public String getBannerLink() {
        return bannerLink;
    }

    public void setBannerLink(String bannerLink) {
        this.bannerLink = bannerLink == null ? null : bannerLink.trim();
    }

    public Integer getBannerType() {
        return bannerType;
    }

    public void setBannerType(Integer bannerType) {
        this.bannerType = bannerType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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