package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author wcyong
 * 
 * @date 2018-09-28
 */
public class KeyWords implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6578330948808433142L;

	private Integer id;

    /**
     * 关键字
     */
    private String keyWords;

    /**
     * 关键字内容
     */
    private String replayWords;

    /**
     * 是否完全匹配  1-完全匹配，2-不完全匹配
     */
    private Integer isMatch;

    /**
     * 回复类型
     */
    private Integer replyType;

    private Date createTime;

    private Date updateTime;

    private Integer flag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords == null ? null : keyWords.trim();
    }

    public String getReplayWords() {
        return replayWords;
    }

    public void setReplayWords(String replayWords) {
        this.replayWords = replayWords == null ? null : replayWords.trim();
    }

    public Integer getIsMatch() {
        return isMatch;
    }

    public void setIsMatch(Integer isMatch) {
        this.isMatch = isMatch;
    }

    public Integer getReplyType() {
        return replyType;
    }

    public void setReplyType(Integer replyType) {
        this.replyType = replyType;
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