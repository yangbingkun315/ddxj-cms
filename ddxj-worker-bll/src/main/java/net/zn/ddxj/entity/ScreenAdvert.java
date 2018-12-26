package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 开屏广告
 * 
 * @author wcyong
 * 
 * @date 2018-07-30
 */
public class ScreenAdvert implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4607295925365029046L;

	private Integer id;

    /**
     * 开屏图片URL
     */
    private String bannerUrl;

    /**
     * 开屏图片跳转链接
     */
    private String bannerLink;

    /**
     * 开屏时间
     */
    private String timer;

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
     * 是否开启(1:开启,2:未开启)
     */
    private Integer status;

    /**
     * 推送平台(1:IOS 2:Android )
     */
    private String pushPlatform;

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

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer == null ? null : timer.trim();
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPushPlatform() {
		return pushPlatform;
	}

	public void setPushPlatform(String pushPlatform) {
		this.pushPlatform = pushPlatform;
	}

	public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}