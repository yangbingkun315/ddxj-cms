package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 发布圈子
 * 
 * @author wcyong
 * 
 * @date 2018-04-24
 */
public class Circle implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 840538151958285898L;

	private Integer id;

    /**
     * 唯一ID
     */
    private String circleNumber;

    /**
     * 发布人ID
     */
    private Integer userId;

    /**
     * 发布内容
     */
    private String content;

    /**
     * 发布位置
     */
    private String address;
    /**
     * 发布城市
     */
    private String city;

    /**
     * 经度
     */
    private String lng;

    /**
     * 纬度
     */
    private String lat;

    /**
     * 发布人IP
     */
    private String ip;

    /**
     * 发布人手机机型
     */
    private String model;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private Integer giveCount;//动态点赞数量
    
    private Integer isGive;//是否点赞过
    
    /**
     * 是否有效
     */
    private Integer flag;
    
    
    private User user;
    
    private List<CircleImage> circleImageList;

    private Integer commentCount;//被评论数
    
    public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	public Integer getGiveCount() {
		return giveCount;
	}

	public void setGiveCount(Integer giveCount) {
		this.giveCount = giveCount;
	}

	public Integer getIsGive() {
		return isGive;
	}

	public void setIsGive(Integer isGive) {
		this.isGive = isGive;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<CircleImage> getCircleImageList() {
		return circleImageList;
	}

	public void setCircleImageList(List<CircleImage> circleImageList) {
		this.circleImageList = circleImageList;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCircleNumber() {
        return circleNumber;
    }

    public void setCircleNumber(String circleNumber) {
        this.circleNumber = circleNumber == null ? null : circleNumber.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng == null ? null : lng.trim();
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat == null ? null : lat.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
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