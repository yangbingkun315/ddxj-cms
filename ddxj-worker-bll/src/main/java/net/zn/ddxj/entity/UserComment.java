package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 评论
 * 
 * @author wcyong
 * 
 * @date 2018-04-19
 */
public class UserComment implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 8483875949075969900L;

	/**
     * 主键ID
     */
    private Integer id;

    /**
     * 被评论者ID
     */
    private Integer toUserId;

    /**
     * 评论者ID
     */
    private Integer fromUserId;
    /**
     * 评论者对象
     */
    private User fromUser;
    /**
     * 评论者对象
     */
    private List<CommentLabel> labelList;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 招聘ID
     */
    private Integer recruitId;

    /**
     * 几星好评
     */
    private Integer level;

    /**
     * 是否匿名（1-不匿名，2-匿名）
     */
    private Integer anonymousStatus;

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

    public List<CommentLabel> getLabelList() {
		return labelList;
	}

	public void setLabelList(List<CommentLabel> labelList) {
		this.labelList = labelList;
	}

	public User getFromUser() {
		return fromUser;
	}

	public void setFromUser(User fromUser) {
		this.fromUser = fromUser;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public Integer getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent == null ? null : commentContent.trim();
    }

    public Integer getRecruitId() {
        return recruitId;
    }

    public void setRecruitId(Integer recruitId) {
        this.recruitId = recruitId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getAnonymousStatus() {
        return anonymousStatus;
    }

    public void setAnonymousStatus(Integer anonymousStatus) {
        this.anonymousStatus = anonymousStatus;
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