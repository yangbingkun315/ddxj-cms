package net.zn.ddxj.entity;

import java.util.Date;

/**
 * 
 * 
 * @author wcyong
 * 
 * @date 2018-08-13
 */
public class CmsLogs {
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 操作内容
     */
    private String logContent;

    /**
     * 操作人ID地址
     */
    private String logIpAddress;

    /**
     * 操作时间
     */
    private Date logTime;
    
    private CmsUser user;

    public CmsUser getUser() {
		return user;
	}

	public void setUser(CmsUser user) {
		this.user = user;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLogContent() {
        return logContent;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent == null ? null : logContent.trim();
    }

    public String getLogIpAddress() {
        return logIpAddress;
    }

    public void setLogIpAddress(String logIpAddress) {
        this.logIpAddress = logIpAddress == null ? null : logIpAddress.trim();
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }
}