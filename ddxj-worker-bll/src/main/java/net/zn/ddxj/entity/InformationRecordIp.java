package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户阅读资讯记录表( zn_infomation_record_ip)
 * 
 * @author wcyong
 * 
 * @date 2018-08-15
 */
public class InformationRecordIp implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -2934267605083633414L;

	private Integer id;

    /**
     * 用户手机ip
     */
    private String userIp;

    /**
     * 资讯id
     */
    private Integer infoId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 是否有效（1有效 2无效）
     */
    private Integer flag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp == null ? null : userIp.trim();
    }

    public Integer getInfoId() {
        return infoId;
    }

    public void setInfoId(Integer infoId) {
        this.infoId = infoId;
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