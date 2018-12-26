package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author wcyong
 * 
 * @date 2018-05-12
 */
public class SensitiveKeywords implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5265285392075197270L;

	private Integer id;

    /**
     * 关键字类型
     */
    private String keywordsType;

    /**
     * 关键字内容
     */
    private String keywordsContent;

    private Date createTime;

    private Integer flag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeywordsType() {
        return keywordsType;
    }

    public void setKeywordsType(String keywordsType) {
        this.keywordsType = keywordsType == null ? null : keywordsType.trim();
    }

    public String getKeywordsContent() {
        return keywordsContent;
    }

    public void setKeywordsContent(String keywordsContent) {
        this.keywordsContent = keywordsContent == null ? null : keywordsContent.trim();
    }


    public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}