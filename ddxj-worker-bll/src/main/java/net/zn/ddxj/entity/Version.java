package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author wcyong
 * 
 * @date 2018-06-01
 */
public class Version implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1194554931929833389L;

	private Integer id;

    /**
     * 最大版本号
     */
    private String maxVersion;

    /**
     * 更新内容
     */
    private String updateContent;

    /**
     * 是否强制更新（Y-强制更新,N-不强制更新）
     */
    private String forceUpdate;

    /**
     * 下载地址
     */
    private String dowloadAddress;

    /**
     * 手机来源（IOS,Android）
     */
    private String mobileSource;

    /**
     * 发布时间
     */
    private Date announceTime;

    /**
     * 创建时间
     */
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMaxVersion() {
        return maxVersion;
    }

    public void setMaxVersion(String maxVersion) {
        this.maxVersion = maxVersion == null ? null : maxVersion.trim();
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent == null ? null : updateContent.trim();
    }

    public String getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(String forceUpdate) {
        this.forceUpdate = forceUpdate == null ? null : forceUpdate.trim();
    }

    public String getDowloadAddress() {
        return dowloadAddress;
    }

    public void setDowloadAddress(String dowloadAddress) {
        this.dowloadAddress = dowloadAddress == null ? null : dowloadAddress.trim();
    }

    public String getMobileSource() {
        return mobileSource;
    }

    public void setMobileSource(String mobileSource) {
        this.mobileSource = mobileSource == null ? null : mobileSource.trim();
    }

    public Date getAnnounceTime() {
        return announceTime;
    }

    public void setAnnounceTime(Date announceTime) {
        this.announceTime = announceTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}