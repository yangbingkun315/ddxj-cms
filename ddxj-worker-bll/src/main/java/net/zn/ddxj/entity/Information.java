package net.zn.ddxj.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import net.zn.ddxj.utils.CmsUtils;

/**
 * 资讯
 * 
 * @author wcyong
 * 
 * @date 2018-05-24
 */
public class Information implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1948232316039011702L;

	/**
     * 主键ID
     */
    private Integer id;

    /**
     * 资讯标题
     */
    private String infoTitle;

    /**
     * 资讯类型（1-纯文字，2-多图模式，3-视频模式,4-一图）
     */
    private Integer infoType;

    /**
     * 是否置顶(1-置顶，2-不置顶)
     */
    private Integer stick;

    /**
     * 资讯简介
     */
    private String infoSummary;

    /**
     * 资讯内容
     */
    private byte[] stringContent;

    /**
     * 作者
     */
    private String author;

    /**
     * 图一
     */
    private String imageOne;

    /**
     * 图二
     */
    private String imageTwo;

    /**
     * 图三
     */
    private String imageThree;

    /**
     * 资讯标注（用,隔开）
     */
    private String infoLabel;

    /**
     * 上架时间
     */
    private Date startTime;

    /**
     * 下架时间
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
    
    private String infoContent;
    
    /**
     * 资讯分类
     */
    private List<InformationType> categoryList;
    
    /**
     * 阅读人数
     */
    private Integer readPeople;
    
    /**
     * 资讯是否过期（true=已过期，false=未过期）
     */
    private Boolean isLower;
    
    /**
     * 收藏时间
     */
    private Date collectionTime;
    
    public Date getCollectionTime() {
		return collectionTime;
	}

	public void setCollectionTime(Date collectionTime) {
		this.collectionTime = collectionTime;
	}

	public Boolean getIsLower() {
		return isLower;
	}

	public void setIsLower(Boolean isLower) {
		this.isLower = isLower;
	}

	public Integer getReadPeople() {
		return readPeople;
	}

	public void setReadPeople(Integer readPeople) {
		this.readPeople = readPeople;
	}

	public List<InformationType> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<InformationType> categoryList) {
		this.categoryList = categoryList;
	}

	public String getInfoContent() 
    {
    	if(!CmsUtils.isNullOrEmpty(stringContent) && stringContent.length > 0)
    	{
    		try {
    			infoContent = new String(stringContent,"UTF-8");
			} 
    		catch (UnsupportedEncodingException e) 
    		{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
		return infoContent;
	}

	public void setInfoContent(String infoContent) {
		this.infoContent = infoContent;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInfoTitle() {
        return infoTitle;
    }

    public void setInfoTitle(String infoTitle) {
        this.infoTitle = infoTitle == null ? null : infoTitle.trim();
    }

    public Integer getInfoType() {
        return infoType;
    }

    public void setInfoType(Integer infoType) {
        this.infoType = infoType;
    }

    public Integer getStick() {
        return stick;
    }

    public void setStick(Integer stick) {
        this.stick = stick;
    }

    public String getInfoSummary() {
        return infoSummary;
    }

    public void setInfoSummary(String infoSummary) {
        this.infoSummary = infoSummary == null ? null : infoSummary.trim();
    }

    public byte[] getStringContent() {
        return stringContent;
    }

    public void setStringContent(byte[] stringContent) {
        this.stringContent = stringContent;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    public String getImageOne() {
        return imageOne;
    }

    public void setImageOne(String imageOne) {
        this.imageOne = imageOne == null ? null : imageOne.trim();
    }

    public String getImageTwo() {
        return imageTwo;
    }

    public void setImageTwo(String imageTwo) {
        this.imageTwo = imageTwo == null ? null : imageTwo.trim();
    }

    public String getImageThree() {
        return imageThree;
    }

    public void setImageThree(String imageThree) {
        this.imageThree = imageThree == null ? null : imageThree.trim();
    }

    public String getInfoLabel() {
        return infoLabel;
    }

    public void setInfoLabel(String infoLabel) {
        this.infoLabel = infoLabel == null ? null : infoLabel.trim();
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