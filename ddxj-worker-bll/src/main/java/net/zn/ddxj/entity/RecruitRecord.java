package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 招聘记录
 * 
 * @author wcyong
 * 
 * @date 2018-04-18
 */
public class RecruitRecord implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4948030665013057910L;

	/**
     * 主键ID
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 招聘ID
     */
    private Integer recruitId;

    /**
     * 报名状态( 0:未报名1:已报名2:报名通过3:报名未通过)
     */
    private Integer enlistStatus;

    /**
     * 工头开工状态（0：未开始，1：工头确认，2：进行中，3：已结束）
     */
    private Integer foremanStatus;

    /**
     * 工人开工状态（0：未开始，1：工人确认，2：进行中，3：已结束）
     */
    private Integer workerStatus;

    /**
     * 工人评论状态（0-未评论，1-已评论）
     */
    private Integer workerCommentStatus;

    /**
     * 工头评论（0-未评论，1-已评论）
     */
    private Integer foremanCommentStatus;

    /**
     * 结算状态:(0-未结算 1-已结算)
     */
    private Integer balanceStatus;

    /**
     * 拒绝原因
     */
    private String refuseCause;

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
     * user实体类
     * @return
     */
    private User user;
    /**
     * Recruit实体类
     * @return
     */
    private Recruit recruit;
    
	public Recruit getRecruit() {
		return recruit;
	}

	public void setRecruit(Recruit recruit) {
		this.recruit = recruit;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
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

    public Integer getRecruitId() {
        return recruitId;
    }

    public void setRecruitId(Integer recruitId) {
        this.recruitId = recruitId;
    }

    public Integer getEnlistStatus() {
        return enlistStatus;
    }

    public void setEnlistStatus(Integer enlistStatus) {
        this.enlistStatus = enlistStatus;
    }

    public Integer getForemanStatus() {
        return foremanStatus;
    }

    public void setForemanStatus(Integer foremanStatus) {
        this.foremanStatus = foremanStatus;
    }

    public Integer getWorkerStatus() {
        return workerStatus;
    }

    public void setWorkerStatus(Integer workerStatus) {
        this.workerStatus = workerStatus;
    }

    public Integer getWorkerCommentStatus() {
        return workerCommentStatus;
    }

    public void setWorkerCommentStatus(Integer workerCommentStatus) {
        this.workerCommentStatus = workerCommentStatus;
    }

    public Integer getForemanCommentStatus() {
        return foremanCommentStatus;
    }

    public void setForemanCommentStatus(Integer foremanCommentStatus) {
        this.foremanCommentStatus = foremanCommentStatus;
    }

    public Integer getBalanceStatus() {
        return balanceStatus;
    }

    public void setBalanceStatus(Integer balanceStatus) {
        this.balanceStatus = balanceStatus;
    }

    public String getRefuseCause() {
        return refuseCause;
    }

    public void setRefuseCause(String refuseCause) {
        this.refuseCause = refuseCause == null ? null : refuseCause.trim();
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