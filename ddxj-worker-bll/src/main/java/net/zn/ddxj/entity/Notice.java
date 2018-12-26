package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 最新公告表
 * 
 * @author wcyong
 * 
 * @date 2018-09-03
 */
public class Notice implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 571001765747066548L;

	/**
     * 主键ID
     */
    private Integer id;

    /**
     * 公告类型(1-工人，2-工头，3-工人和工头)
     */
    private Integer noticeType;

    /**
     * 公告内容
     */
    private String noticeContent;

    /**
     * 公告上架时间
     */
    private Date startTime;

    /**
     * 公告下架时间
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

    public Integer getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(Integer noticeType) {
        this.noticeType = noticeType;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent == null ? null : noticeContent.trim();
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