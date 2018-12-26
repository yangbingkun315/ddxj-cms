package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 招聘轮播图
 * 
 * @author wcyong
 * 
 * @date 2018-04-17
 */
public class RecruitBanner implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6108798272183930628L;

	/**
     * 招聘ID
     */
    private Integer id;

    /**
     * 招聘图片URL
     */
    private String bannerUrl;

    /**
     * 跳转地址
     */
    private String bannerLink;

    /**
     * 类型（1-图片，2-视频）
     */
    private Integer bannerType;

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