package net.zn.ddxj.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 发布招聘
 * 
 * @author wcyong
 * 
 * @date 2018-04-26
 */
public class Recruit implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4012991045692869898L;

	/**
     * 主键ID
     */
    private Integer id;

    /**
     * 招聘标题
     */
    private String recruitTitle;
    
    /**
     * 项目编号
     */
    private String projectNumber;

    /**
     * 招聘内容
     */
    private String recruitContent;

    /**
     * 发布者ID
     */
    private Integer userId;

    /**
     * 工期开始时间
     */
    private Date startTime;

    /**
     * 工期结束时间
     */
    private Date endTime;

    /**
     * 省（招聘所在地）
     */
    private String recruitProvince;

    /**
     * 市（招聘所在地）
     */
    private String recruitCity;

    /**
     * 县（招聘所在地）
     */
    private String recruitArea;

    /**
     * 招聘地址经度
     */
    private String recruitLong;

    /**
     * 招聘地址纬度
     */
    private String recruitLat;

    /**
     * 详细地址（招聘所在地）
     */
    private String recruitAddress;

    /**
     * 招聘人数
     */
    private Integer recruitPerson;

    /**
     * 承包方
     */
    private String contractor;

    /**
     * 封面图片
     */
    private String coverImage;

    /**
     * 工资结算方式
     */
    private String balanceWay;

    /**
     * 开始金额
     */
    private BigDecimal startPrice;

    /**
     * 结束金额
     */
    private BigDecimal endPrice;

    /**
     * 审核状态（1：审核中，2：审核失败，3审核成功）
     */
    private Integer validateStatus;

    /**
     * 结算方式（0-一次性结，1-月结）
     */
    private Integer balanceType;

    /**
     * 招聘状态（0：未开始，1：进行中，2：已结束）
     */
    private Integer recruitStatus;

    /**
     * 是否置顶(1-置顶，2-不置顶)
     */
    private Integer stick;

    /**
     * 招聘截止时间
     */
    private Date stopTime;

    /**
     * 审核原因
     */
    private String validateCause;

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
     * 是否被邀请过
     */
    private Integer isRequest;
    
    
    /**
     * 招聘轮播图片
     */
    private List<RecruitBanner> bannerList;
    
    /**	
     * 已报名人数
     */
    private Integer enlistNumber;
    
    /**
     * 工种分类
     */
    private List<Category> categoryList;
    
    /**
     * user
     * @return
     */
    private User user;
    
    /**
     * underway 正在进行中的人数

     * @return
     */
    public Integer underwayNum;
    
    /**
     * 项目状态
     */
    private Integer projectStatus;
    
    /**
     * 项目距离
     */
    private String distance;
     

	public Integer getUnderwayNum() {
		return underwayNum;
	}

	public Integer getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(Integer projectStatus) {
		this.projectStatus = projectStatus;
	}

	public void setUnderwayNum(Integer underwayNum) {
		this.underwayNum = underwayNum;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getProjectNumber() {
		return projectNumber;
	}

	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}

	public List<Category> getCategoryList() {
    	if(categoryList == null)
    	{
    		categoryList = new ArrayList<>();
    	}
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}

	public List<RecruitBanner> getBannerList() {
    	if(bannerList == null)
    	{
    		bannerList = new ArrayList<>();
    	}
		return bannerList;
	}

	public void setBannerList(List<RecruitBanner> bannerList) {
		this.bannerList = bannerList;
	}

	public Integer getIsRequest() {
		return isRequest;
	}

	public void setIsRequest(Integer isRequest) {
		this.isRequest = isRequest;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRecruitTitle() {
        return recruitTitle;
    }

    public void setRecruitTitle(String recruitTitle) {
        this.recruitTitle = recruitTitle == null ? null : recruitTitle.trim();
    }

    public String getRecruitContent() {
        return recruitContent;
    }

    public void setRecruitContent(String recruitContent) {
        this.recruitContent = recruitContent == null ? null : recruitContent.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getRecruitProvince() {
        return recruitProvince;
    }

    public void setRecruitProvince(String recruitProvince) {
        this.recruitProvince = recruitProvince == null ? null : recruitProvince.trim();
    }

    public String getRecruitCity() {
        return recruitCity;
    }

    public void setRecruitCity(String recruitCity) {
        this.recruitCity = recruitCity == null ? null : recruitCity.trim();
    }

    public String getRecruitArea() {
        return recruitArea;
    }

    public void setRecruitArea(String recruitArea) {
        this.recruitArea = recruitArea == null ? null : recruitArea.trim();
    }

    public String getRecruitLong() {
        return recruitLong;
    }

    public void setRecruitLong(String recruitLong) {
        this.recruitLong = recruitLong == null ? null : recruitLong.trim();
    }

    public String getRecruitLat() {
        return recruitLat;
    }

    public void setRecruitLat(String recruitLat) {
        this.recruitLat = recruitLat == null ? null : recruitLat.trim();
    }

    public String getRecruitAddress() {
        return recruitAddress;
    }

    public void setRecruitAddress(String recruitAddress) {
        this.recruitAddress = recruitAddress == null ? null : recruitAddress.trim();
    }

    public Integer getRecruitPerson() {
        return recruitPerson;
    }

    public void setRecruitPerson(Integer recruitPerson) {
        this.recruitPerson = recruitPerson;
    }

    public String getContractor() {
        return contractor;
    }

    public void setContractor(String contractor) {
        this.contractor = contractor == null ? null : contractor.trim();
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage == null ? null : coverImage.trim();
    }

    public String getBalanceWay() {
        return balanceWay;
    }

    public void setBalanceWay(String balanceWay) {
        this.balanceWay = balanceWay == null ? null : balanceWay.trim();
    }

    public BigDecimal getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(BigDecimal startPrice) {
        this.startPrice = startPrice;
    }

    public BigDecimal getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(BigDecimal endPrice) {
        this.endPrice = endPrice;
    }

    public Integer getValidateStatus() {
        return validateStatus;
    }

    public void setValidateStatus(Integer validateStatus) {
        this.validateStatus = validateStatus;
    }

    public Integer getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(Integer balanceType) {
        this.balanceType = balanceType;
    }

    public Integer getRecruitStatus() {
        return recruitStatus;
    }

    public void setRecruitStatus(Integer recruitStatus) {
        this.recruitStatus = recruitStatus;
    }

    public Integer getStick() {
        return stick;
    }

    public void setStick(Integer stick) {
        this.stick = stick;
    }

    public Date getStopTime() {
        return stopTime ;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public String getValidateCause() {
        return validateCause;
    }

    public void setValidateCause(String validateCause) {
        this.validateCause = validateCause == null ? null : validateCause.trim();
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
	public Integer getEnlistNumber() {
		return enlistNumber;
	}

	public void setEnlistNumber(Integer enlistNumber) {
		this.enlistNumber = enlistNumber;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}
	
	
}