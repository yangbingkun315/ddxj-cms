package net.zn.ddxj.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.zn.ddxj.utils.CmsUtils;

/**
 * 用户表
 * 
 * @author wcyong
 * 
 * @date 2018-04-12
 */
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4532599225824024495L;

	/**
	 * 主键ID
	 */
	private Integer id;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 真实姓名
	 */
	private String realName;

	/**
	 * 年龄
	 */
	private Integer age;

	/**
	 * 联系电话
	 */
	private String phone;

	/**
	 * 性别(M-男，F-女)
	 */
	private String sex;

	/**
	 * 用户头像
	 */
	private String userPortrait;

	/**
	 * 用户角色（1-工人，2-工头）
	 */
	private Integer role;

	/**
	 * 微信OPENID
	 */
	private String openid;

	/**
	 * 微信昵称
	 */
	private String wxName;
	/**
	 * 微信平台用户唯一标识
	 */
	private String unionid;

	/**
	 * 个人钱包
	 */
	private BigDecimal remainderMoney;

	/**
	 * 工龄
	 */
	private String standing;

	/**
	 * 省（户口所在地）
	 */
	private String registeredProvince;

	/**
	 * 市（户口所在地）
	 */
	private String registeredCity;

	/**
	 * 县（户口所在地）
	 */
	private String registeredArea;

	/**
	 * 详细地址（户口所在地）
	 */
	private String registeredAddress;

	/**
	 * 省（工作所在地）
	 */
	private String workProvince;

	/**
	 * 市（工作所在地）
	 */
	private String workCity;

	/**
	 * 县（工作所在地）
	 */
	private String workArea;

	/**
	 * 详细地址（工作所在地）
	 */
	private String workAddress;

	/**
	 * 用户状态（0-空闲，1-在职）
	 */
	private Integer status;

	/**
	 * 个人描述
	 */
	private String personDesc;

	/**
	 * 上次登录IP
	 */
	private String lastIp;

	/**
	 * 当前登陆IP
	 */
	private String currentIp;

	/**
	 * 上次登录时间
	 */
	private Date lastTime;

	/**
	 * 当前登陆时间
	 */
	private Date currentDateTime;

	/**
	 * 注册渠道（1-公众号，2-APP，3-小程序）
	 */
	private Integer registerChannel;

	/**
	 * 登陆状态（1-正常，2-禁用，3-冻结）
	 */
	private Integer loginStatus;

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
	 * 工种分类
	 */
	private List<Category> categoryList;

	/**
	 * 是否认证
	 */
	private Integer isAttestation;
	
	/**
	 * 评分
	 */
	private String score = "5.0";
	
	/**
	 * 评分总数量
	 */
	private Integer count;
	
	/**
	 * 生日
	 */
	private Date birthDate;
    /**
     * 修改验证资料(姓名#性别#年龄#籍贯#工种)
     */
    private String validateMessage;
	
	private Integer loginPasswordStatus;//登录密码状态（0-未设置，1-已设置）
	
	private Integer payPasswordStatus;//支付密码状态（0-未设置，1-已设置）
	
	private Integer contractType;//承包方式 1-总包，2-分包
	
	/**
	 * 在招职位数量
	 */
	private Integer recruitCount;
	/**
	 *银行卡列表
	 * @return
	 */
	private List<UserBank> bankList;
	/**
	 * 用于APP推送唯一标识
	 */
	private String appUserToken;
	
	/**
	 * 最近登录的设备（JSAPI,Android,IOS）
	 */
	private String currentDevice;
	
	/**
	 * 最后一次登录的设备（JSAPI,Android,IOS）
	 */
	private String lastDevice;
	
	/**
	 * 推广人员编号
	 * @return
	 */
	private String staffNum;
	
	/**
	 * 环信用户表
	 * @return
	 */
	private EasemobUser easemobUser;
	
	public EasemobUser getEasemobUser() {
		return easemobUser;
	}

	public void setEasemobUser(EasemobUser easemobUser) {
		this.easemobUser = easemobUser;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getStaffNum() {
		return staffNum;
	}

	public void setStaffNum(String staffNum) {
		this.staffNum = staffNum;
	}

	public String getCurrentDevice() {
		return currentDevice;
	}

	public void setCurrentDevice(String currentDevice) {
		this.currentDevice = currentDevice;
	}

	public String getLastDevice() {
		return lastDevice;
	}

	public void setLastDevice(String lastDevice) {
		this.lastDevice = lastDevice;
	}

	public String getAppUserToken() {
		return appUserToken;
	}

	public void setAppUserToken(String appUserToken) {
		this.appUserToken = appUserToken;
	}

	public List<UserBank> getBankList() {
		if (bankList == null) {
			bankList = new ArrayList<UserBank>();
		}
		return bankList;
	}

	public void setBankList(List<UserBank> bankList) {
		this.bankList = bankList;
	}

	public Integer getRecruitCount() {
		return recruitCount;
	}

	public void setRecruitCount(Integer recruitCount) {
		this.recruitCount = recruitCount == null ? 0 : recruitCount;
	}

	public Integer getContractType() {
		return contractType;
	}

	public void setContractType(Integer contractType) {
		this.contractType = contractType;
	}

	public Integer getLoginPasswordStatus() {
		return loginPasswordStatus;
	}

	public void setLoginPasswordStatus(Integer loginPasswordStatus) {
		this.loginPasswordStatus = loginPasswordStatus;
	}

	public Integer getPayPasswordStatus() {
		return payPasswordStatus;
	}

	public void setPayPasswordStatus(Integer payPasswordStatus) {
		this.payPasswordStatus = payPasswordStatus;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName == null ? null : userName.trim();
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName == null ? null : realName.trim();
	}

	public Integer getAge() {
		if(birthDate != null)
		{
			return CmsUtils.getAgeByBirth(birthDate);
		}
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone == null ? null : phone.trim();
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex == null ? null : sex.trim();
	}

	public String getUserPortrait() {
		return userPortrait;
	}

	public void setUserPortrait(String userPortrait) {
		this.userPortrait = userPortrait == null ? null : userPortrait.trim();
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid == null ? null : openid.trim();
	}

	public String getWxName() {
		return wxName;
	}

	public void setWxName(String wxName) {
		this.wxName = wxName == null ? null : wxName.trim();
	}

	public BigDecimal getRemainderMoney() {
		return remainderMoney;
	}

	public void setRemainderMoney(BigDecimal remainderMoney) {
		this.remainderMoney = remainderMoney;
	}

	public String getStanding() {
		return standing;
	}

	public void setStanding(String standing) {
		this.standing = standing == null ? null : standing.trim();
	}

	public String getRegisteredProvince() {
		return registeredProvince;
	}

	public void setRegisteredProvince(String registeredProvince) {
		this.registeredProvince = registeredProvince == null ? null : registeredProvince.trim();
	}

	public String getRegisteredCity() {
		return registeredCity;
	}

	public void setRegisteredCity(String registeredCity) {
		this.registeredCity = registeredCity == null ? null : registeredCity.trim();
	}

	public String getRegisteredArea() {
		return registeredArea;
	}

	public void setRegisteredArea(String registeredArea) {
		this.registeredArea = registeredArea == null ? null : registeredArea.trim();
	}

	public String getRegisteredAddress() {
		return registeredAddress;
	}

	public void setRegisteredAddress(String registeredAddress) {
		this.registeredAddress = registeredAddress == null ? null : registeredAddress.trim();
	}

	public String getWorkProvince() {
		return workProvince;
	}

	public void setWorkProvince(String workProvince) {
		this.workProvince = workProvince == null ? null : workProvince.trim();
	}

	public String getWorkCity() {
		return workCity;
	}

	public void setWorkCity(String workCity) {
		this.workCity = workCity == null ? null : workCity.trim();
	}

	public String getWorkArea() {
		return workArea;
	}

	public void setWorkArea(String workArea) {
		this.workArea = workArea == null ? null : workArea.trim();
	}

	public String getWorkAddress() {
		return workAddress;
	}

	public void setWorkAddress(String workAddress) {
		this.workAddress = workAddress == null ? null : workAddress.trim();
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		if(status != null && status > 0)
		{
			status = 1;
		}
		this.status = status;
	}

	public String getPersonDesc() {
		return personDesc;
	}

	public void setPersonDesc(String personDesc) {
		this.personDesc = personDesc == null ? null : personDesc.trim();
	}

	public String getLastIp() {
		return lastIp;
	}

	public void setLastIp(String lastIp) {
		this.lastIp = lastIp == null ? null : lastIp.trim();
	}

	public String getCurrentIp() {
		return currentIp;
	}

	public void setCurrentIp(String currentIp) {
		this.currentIp = currentIp == null ? null : currentIp.trim();
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	public Date getCurrentDateTime() {
		return currentDateTime;
	}

	public void setCurrentDateTime(Date currentDateTime) {
		this.currentDateTime = currentDateTime;
	}

	public Integer getRegisterChannel() {
		return registerChannel;
	}

	public void setRegisterChannel(Integer registerChannel) {
		this.registerChannel = registerChannel;
	}

	public Integer getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(Integer loginStatus) {
		this.loginStatus = loginStatus;
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

	public List<Category> getCategoryList() {
		if (categoryList == null) {
			categoryList = new ArrayList<Category>();
		}
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}

	public Integer getIsAttestation() {
		if(isAttestation != null && isAttestation >= 1){
			return 1;
		}else{
			return 0;
		}
	}

	public void setIsAttestation(Integer isAttestation) {
		this.isAttestation = isAttestation;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		if(!CmsUtils.isNullOrEmpty(score))
		{ 
//			 BigDecimal Allcount =  new BigDecimal(score);//总评分
//			 BigDecimal parentCount = new BigDecimal(String.valueOf(count));//总数量
//			 //score = String.valueOf(Allcount.divide(parentCount).setScale(1, BigDecimal.ROUND_HALF_UP));
//			 score = String.valueOf(Allcount.divide(parentCount, 1, BigDecimal.ROUND_HALF_UP));
			BigDecimal parentScore = new BigDecimal(score);
			parentScore = parentScore.setScale(1, BigDecimal.ROUND_HALF_UP);//四舍五入
			score = String.valueOf(parentScore);
		}
		this.score = score;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
    public String getValidateMessage() {
        return validateMessage;
    }

    public void setValidateMessage(String validateMessage) {
        this.validateMessage = validateMessage == null ? null : validateMessage.trim();
    }
}