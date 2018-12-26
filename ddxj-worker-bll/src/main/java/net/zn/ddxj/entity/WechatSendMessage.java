package net.zn.ddxj.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 群发消息表
 * 
 * @author wcyong
 * 
 * @date 2018-07-20
 */
public class WechatSendMessage implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6705974817246901709L;

	/**
     * 主键ID
     */
    private Integer id;

    /**
     * 群发类型1.客服群发 2.高级群发 3.APP群发
     */
    private Integer sendType;

    /**
     * 发送类型 1.文字 2.图文素材 3.图片
     */
    private Integer massType;

    /**
     * 推送平台 1.IOS 2.安卓 3.IOS和安卓 4.公众号
     */
    private Integer massPlatform;

    /**
     * 群发标题
     */
    private String massTitle;

    /**
     * 群发内容
     */
    private String massContent;

    /**
     * 跳转链接
     */
    private String massLink;

    /**
     * 推送对象 1.广播 2.工头端 3.工人端 4.公众号
     */
    private Integer massObject;

    /**
     * 地区-省
     */
    private String province;

    /**
     * 地区-市
     */
    private String city;

    /**
     * 性别 0.全部 1.男 2.女
     */
    private Integer sex;

    /**
     * 开始年龄
     */
    private Date startAge;

    /**
     * 结束年龄
     */
    private Date endAge;

    /**
     * 定时发送时间
     */
    private Date timingTime;

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
     * 群发用户
     */
    private List<SendMessageUser> sendMessageUserList;
    
    /**
     * 群发分类
     */
    private List<Category> categoryList;

    public List<Category> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}

	public List<SendMessageUser> getSendMessageUserList() {
		return sendMessageUserList;
	}

	public void setSendMessageUserList(List<SendMessageUser> sendMessageUserList) {
		this.sendMessageUserList = sendMessageUserList;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public Integer getMassType() {
        return massType;
    }

    public void setMassType(Integer massType) {
        this.massType = massType;
    }

    public Integer getMassPlatform() {
        return massPlatform;
    }

    public void setMassPlatform(Integer massPlatform) {
        this.massPlatform = massPlatform;
    }

    public String getMassTitle() {
        return massTitle;
    }

    public void setMassTitle(String massTitle) {
        this.massTitle = massTitle == null ? null : massTitle.trim();
    }

    public String getMassContent() {
        return massContent;
    }

    public void setMassContent(String massContent) {
        this.massContent = massContent == null ? null : massContent.trim();
    }

    public String getMassLink() {
        return massLink;
    }

    public void setMassLink(String massLink) {
        this.massLink = massLink == null ? null : massLink.trim();
    }

    public Integer getMassObject() {
        return massObject;
    }

    public void setMassObject(Integer massObject) {
        this.massObject = massObject;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getStartAge() {
        return startAge;
    }

    public void setStartAge(Date startAge) {
        this.startAge = startAge;
    }

    public Date getEndAge() {
        return endAge;
    }

    public void setEndAge(Date endAge) {
        this.endAge = endAge;
    }

    public Date getTimingTime() {
        return timingTime;
    }

    public void setTimingTime(Date timingTime) {
        this.timingTime = timingTime;
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