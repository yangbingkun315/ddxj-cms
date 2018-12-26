package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户评论标签
 * 
 * @author wcyong
 * 
 * @date 2018-04-19
 */
public class UserCommentLabel implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1757862778689283572L;

	/**
     * 评论ID
     */
    private Integer commentId;

    /**
     * 评论标签ID
     */
    private Integer labelId;

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

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getLabelId() {
        return labelId;
    }

    public void setLabelId(Integer labelId) {
        this.labelId = labelId;
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