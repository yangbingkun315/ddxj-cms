package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 圈子评论
 * 
 * @author wcyong
 * 
 * @date 2018-04-23
 */
public class CircleComment implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 373239601345908289L;

	/**
     * 主键ID
     */
    private Integer id;

    /**
     * 圈子ID
     */
    private Integer circleId;

    /**
     * 评论父类ID
     */
    private Integer parentId;

    /**
     * 评论者用户ID
     */
    private Integer fromUserId;

    /**
     * 被评论者ID
     */
    private Integer toUserId;

    /**
     * 内容
     */
    private String content;

    /**
     * 评论者IP
     */
    private String ip;

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
    
    private User toUser;
    private User fromUser;
    private List<CircleComment> subCircleCommentList;
    
	public User getToUser() {
		return toUser;
	}

	public void setToUser(User toUser) {
		if(toUser == null)
		{
			toUser = new User();
		}
		this.toUser = toUser;
	}

	public User getFromUser() {
		return fromUser;
	}

	public void setFromUser(User fromUser) {
		if(fromUser == null)
		{
			fromUser = new User();
		}
		this.fromUser = fromUser;
	}

	public List<CircleComment> getSubCircleCommentList() {
		return subCircleCommentList;
	}

	public void setSubCircleCommentList(List<CircleComment> subCircleCommentList) {
		if(subCircleCommentList == null)
		{
			subCircleCommentList = new ArrayList<CircleComment>();
		}
		this.subCircleCommentList = subCircleCommentList;
	}

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

    public Integer getParentId() {
    	if(parentId == null)
    	{
    		parentId = 0;
    	}
        return parentId;
    }

    public void setParentId(Integer parentId) {
    	if(parentId == null)
    	{
    		parentId = 0;
    	}
        this.parentId = parentId;
    }

    public Integer getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
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