package net.zn.ddxj.vo;

import java.math.BigDecimal;

public class RequestVo {
	
	private Integer userId;
	private Integer fromUserId;
	private Integer toUserId;
	private Integer categoryId;
	private Integer id;
	private Integer categoryType;
	private Integer categorySort;
	
	private Integer pageNum;
	private Integer currentPage;
	private Integer pageSize;
	
	private String menuName;
	private String categoryName;
	private String parentId;
	private String categoryParentId;
	private	String userName;
	private String phone;
	private String createTime;

	private Integer recruitId;
	private Integer recruitStatus;
	private Integer recruitPerson;
	private String recruitTitle;
	private String recruitContent;
	private String startTime;
	private String endTime;
	private String recruitProvince;
	private String recruitCity;
	private String recruitArea;
	private String recruitLong;
	private String recruitLat;
	private String recruitAddress;
	private String contractor;
	private String coverImage;
	private String balanceWay;
	private Integer balanceType;
	private String startPrice;
	private String endPrice;
	private String stopTime;
	private String banners;
	private String categorys;
	private String realName;
	//机构相关
	private String creditName;
	private	String creditCode;
	private String creditAddress;
	private Integer flag;
	
	private String workerName;
	private String city;
	private String scoreSort;
	private Integer contractType;
	/**
	 * 授信记录相关
	 * @return
	 */
	private Integer creditRecordId;
    private BigDecimal totalMoney;
    private Integer creditStatus;
    /**
     * 提现
     * @return
     */
    private Integer withdrawStatus;
    private Integer withdrawProcess;
    private String bankOn;
    private String bankName;    
    private String bankType;
    /**
     * 充值
     * @return
     */
    public Integer payStatus;
    
    private String bannerUrl;
    private String bannerLink;
    private Integer bannerType;
    
    private String infoTitle;
    private String infoSummary;
    private String author;
    private Integer infoType;
    private Integer inforId;
    private String infoLabel;
    private String imageOne;
    private String imageTwo;
    private String imageThree;
    private String infoContent;
    
    private String withdrawName;
    private String withdrawPhone;
    private String payMentName;
    private String payMentPhone;
    private String fromName;
    private String fromPhone;
    private Integer transferWay;
    private Integer transferType;
    
    private Integer libId;
    private String problemTitle;
    private Integer problemDifficulty;
    private String problemContent;
    private String nickName;
    private String mediaId;

    private Integer validateStatus;
    private Integer stick;
    private String validateCause;
    private String isAuth;
    private Integer status;
    private String staffNum;
    private Integer auditStatus;
   
    private String idCardNumber;
    private Integer realStatus;//审核状态（1：审核中，2：审核失败，3审核成功）
    
    private String imgName;
    private String imgUrl;
    private String imgDesc;
    
    private String companyName;
    private String name;
    
    private String recruitImg;
    private String recruitName;
    private String recruitDemand;
    private String recuritDuties;
    private String recruitTenure;
    private String email;
    
    private String address;
    private String demandDesc;
    private BigDecimal estimateWages;
    private String ip;
    
    private String circleNumber;
    
    private Integer massType;
    private Integer sendType;
    private String massContent;
    private String province;
    private Integer categoryId1;
    private Integer categoryId2;
    private Integer categoryId3;
    private String previewOpenId;
    private Integer tagId;
    private String sex;
    private String startAge;
    private String endAge;
    private Integer msgSex;
    private Integer massPlatform;
    private String massTitle;
    private String massLink;
    private Integer massObject;
    private String timingTime;
    private Integer openType;
    private Integer msgId;
    private Integer role;
    //用户导出
    //private List<Integer> userIdList;
    private String userIdList;
    
    private String pushPlatform;
    private String timer;
    private Integer sort;
    private Integer typeId;     
    private Integer type;
    private Integer wechatId;
    
	public Integer getWechatId() {
		return wechatId;
	}

	public void setWechatId(Integer wechatId) {
		this.wechatId = wechatId;
	}

	public Integer getCurrentPage() {
		if(currentPage == null || currentPage <= 0)
		{
			currentPage = 1;
		}
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Integer getWithdrawProcess() {
		return withdrawProcess;
	}

	public void setWithdrawProcess(Integer withdrawProcess) {
		this.withdrawProcess = withdrawProcess;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getPushPlatform() {
		return pushPlatform;
	}

	public void setPushPlatform(String pushPlatform) {
		this.pushPlatform = pushPlatform;
	}

	public String getTimer() {
		return timer;
	}

	public void setTimer(String timer) {
		this.timer = timer;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public Integer getMsgId() {
		return msgId;
	}

	public void setMsgId(Integer msgId) {
		this.msgId = msgId;
	}

	public Integer getOpenType() {
		return openType;
	}

	public void setOpenType(Integer openType) {
		this.openType = openType;
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
		this.massTitle = massTitle;
	}

	public String getMassLink() {
		return massLink;
	}

	public void setMassLink(String massLink) {
		this.massLink = massLink;
	}

	public Integer getMassObject() {
		return massObject;
	}

	public void setMassObject(Integer massObject) {
		this.massObject = massObject;
	}

	public String getTimingTime() {
		return timingTime;
	}

	public void setTimingTime(String timingTime) {
		this.timingTime = timingTime;
	}

	public Integer getMsgSex() {
		return msgSex;
	}

	public void setMsgSex(Integer msgSex) {
		this.msgSex = msgSex;
	}

	public String getStartAge() {
		return startAge;
	}

	public void setStartAge(String startAge) {
		this.startAge = startAge;
	}

	public String getEndAge() {
		return endAge;
	}

	public void setEndAge(String endAge) {
		this.endAge = endAge;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Integer getSendType() {
		return sendType;
	}

	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}

	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	public String getPreviewOpenId() {
		return previewOpenId;
	}

	public void setPreviewOpenId(String previewOpenId) {
		this.previewOpenId = previewOpenId;
	}

	public Integer getCategoryId1() {
		return categoryId1;
	}

	public void setCategoryId1(Integer categoryId1) {
		this.categoryId1 = categoryId1;
	}

	public Integer getCategoryId2() {
		return categoryId2;
	}

	public void setCategoryId2(Integer categoryId2) {
		this.categoryId2 = categoryId2;
	}

	public Integer getCategoryId3() {
		return categoryId3;
	}

	public void setCategoryId3(Integer categoryId3) {
		this.categoryId3 = categoryId3;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getMassContent() {
		return massContent;
	}

	public void setMassContent(String massContent) {
		this.massContent = massContent;
	}

	public Integer getMassType() {
		return massType;
	}

	public void setMassType(Integer massType) {
		this.massType = massType;
	}

	public String getCircleNumber() {
		return circleNumber;
	}

	public void setCircleNumber(String circleNumber) {
		this.circleNumber = circleNumber;
	}

	public Integer getBalanceType() {
		return balanceType;
	}

	public void setBalanceType(Integer balanceType) {
		this.balanceType = balanceType;
	}

	public String getIdCardNumber() {
		return idCardNumber;
	}

	public void setIdCardNumber(String idCardNumber) {
		this.idCardNumber = idCardNumber;
	}

	public Integer getRealStatus() {
		return realStatus;
	}

	public void setRealStatus(Integer realStatus) {
		this.realStatus = realStatus;
	}
	   
    public String getStaffNum() {
		return staffNum;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDemandDesc() {
		return demandDesc;
	}

	public void setDemandDesc(String demandDesc) {
		this.demandDesc = demandDesc;
	}

	public BigDecimal getEstimateWages() {
		return estimateWages;
	}

	public void setEstimateWages(BigDecimal estimateWages) {
		this.estimateWages = estimateWages;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
    public String getRecruitImg() {
		return recruitImg;
	}

	public void setRecruitImg(String recruitImg) {
		this.recruitImg = recruitImg;
	}

	public String getRecruitName() {
		return recruitName;
	}

	public void setRecruitName(String recruitName) {
		this.recruitName = recruitName;
	}

	public String getRecruitDemand() {
		return recruitDemand;
	}

	public void setRecruitDemand(String recruitDemand) {
		this.recruitDemand = recruitDemand;
	}

	public String getRecuritDuties() {
		return recuritDuties;
	}

	public void setRecuritDuties(String recuritDuties) {
		this.recuritDuties = recuritDuties;
	}

	public String getRecruitTenure() {
		return recruitTenure;
	}

	public void setRecruitTenure(String recruitTenure) {
		this.recruitTenure = recruitTenure;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getImgDesc() {
		return imgDesc;
	}

	public void setImgDesc(String imgDesc) {
		this.imgDesc = imgDesc;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public void setStaffNum(String staffNum) {
		this.staffNum = staffNum;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getIsAuth() {
		return isAuth;
	}

	public void setIsAuth(String isAuth) {
		this.isAuth = isAuth;
	}

	public String getValidateCause() {
		return validateCause;
	}

	public void setValidateCause(String validateCause) {
		this.validateCause = validateCause;
	}

	public Integer getValidateStatus() {
		return validateStatus;
	}

	public void setValidateStatus(Integer validateStatus) {
		this.validateStatus = validateStatus;
	}

    public Integer getStick() {
		return stick;
	}

	public void setStick(Integer stick) {
		this.stick = stick;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getProblemDifficulty() {
		return problemDifficulty;
	}

	public void setProblemDifficulty(Integer problemDifficulty) {
		this.problemDifficulty = problemDifficulty;
	}

	public String getProblemContent() {
		return problemContent;
	}

	public void setProblemContent(String problemContent) {
		this.problemContent = problemContent;
	}

	public Integer getLibId() {
		return libId;
	}

	public void setLibId(Integer libId) {
		this.libId = libId;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getProblemTitle() {
		return problemTitle;
	}

	public void setProblemTitle(String problemTitle) {
		this.problemTitle = problemTitle;
	}

	public String getWithdrawName() {
		return withdrawName;
	}

	public void setWithdrawName(String withdrawName) {
		this.withdrawName = withdrawName;
	}

	public String getWithdrawPhone() {
		return withdrawPhone;
	}

	public void setWithdrawPhone(String withdrawPhone) {
		this.withdrawPhone = withdrawPhone;
	}

	public String getPayMentName() {
		return payMentName;
	}

	public void setPayMentName(String payMentName) {
		this.payMentName = payMentName;
	}

	public String getPayMentPhone() {
		return payMentPhone;
	}

	public void setPayMentPhone(String payMentPhone) {
		this.payMentPhone = payMentPhone;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getFromPhone() {
		return fromPhone;
	}

	public void setFromPhone(String fromPhone) {
		this.fromPhone = fromPhone;
	}

	public Integer getTransferWay() {
		return transferWay;
	}

	public void setTransferWay(Integer transferWay) {
		this.transferWay = transferWay;
	}

	public Integer getTransferType() {
		return transferType;
	}

	public void setTransferType(Integer transferType) {
		this.transferType = transferType;
	}

	public Integer getInfoType() {
		return infoType;
	}

	public void setInfoType(Integer infoType) {
		this.infoType = infoType;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getInfoSummary() {
		return infoSummary;
	}

	public void setInfoSummary(String infoSummary) {
		this.infoSummary = infoSummary;
	}

	public String getInfoLabel() {
		return infoLabel;
	}

	public void setInfoLabel(String infoLabel) {
		this.infoLabel = infoLabel;
	}

	public String getImageOne() {
		return imageOne;
	}

	public void setImageOne(String imageOne) {
		this.imageOne = imageOne;
	}

	public String getImageTwo() {
		return imageTwo;
	}

	public void setImageTwo(String imageTwo) {
		this.imageTwo = imageTwo;
	}

	public String getImageThree() {
		return imageThree;
	}

	public void setImageThree(String imageThree) {
		this.imageThree = imageThree;
	}

	public String getInfoContent() {
		return infoContent;
	}

	public void setInfoContent(String infoContent) {
		this.infoContent = infoContent;
	}

	public Integer getInforId() {
		return inforId;
	}

	public void setInforId(Integer inforId) {
		this.inforId = inforId;
	}

	public String getInfoTitle() {
		return infoTitle;
	}

	public void setInfoTitle(String infoTitle) {
		this.infoTitle = infoTitle;
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	public String getBannerLink() {
		return bannerLink;
	}

	public void setBannerLink(String bannerLink) {
		this.bannerLink = bannerLink;
	}

	public Integer getBannerType() {
		return bannerType;
	}

	public void setBannerType(Integer bannerType) {
		this.bannerType = bannerType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Integer getWithdrawStatus() {
		return withdrawStatus;
	}

	public void setWithdrawStatus(Integer withdrawStatus) {
		this.withdrawStatus = withdrawStatus;
	}

	public String getBankOn() {
		return bankOn;
	}

	public void setBankOn(String bankOn) {
		this.bankOn = bankOn;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public Integer getContractType() {
		return contractType;
	}

	public void setContractType(Integer contractType) {
		this.contractType = contractType;
	}

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getScoreSort() {
		return scoreSort;
	}

	public void setScoreSort(String scoreSort) {
		this.scoreSort = scoreSort;
	}

	public Integer getCreditRecordId() {
		return creditRecordId;
	}

	public void setCreditRecordId(Integer creditRecordId) {
		this.creditRecordId = creditRecordId;
	}

	public BigDecimal getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
	}

	public Integer getCreditStatus() {
		return creditStatus;
	}

	public void setCreditStatus(Integer creditStatus) {
		this.creditStatus = creditStatus;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getCreditName() {
		return creditName;
	}

	public void setCreditName(String creditName) {
		this.creditName = creditName;
	}

	public String getCreditCode() {
		return creditCode;
	}

	public void setCreditCode(String creditCode) {
		this.creditCode = creditCode;
	}

	public String getCreditAddress() {
		return creditAddress;
	}

	public void setCreditAddress(String creditAddress) {
		this.creditAddress = creditAddress;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Integer getRecruitId() {
		return recruitId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setRecruitId(Integer recruitId) {
		this.recruitId = recruitId;
	}

	public Integer getRecruitStatus() {
		return recruitStatus;
	}

	public void setRecruitStatus(Integer recruitStatus) {
		this.recruitStatus = recruitStatus;
	}

	public Integer getRecruitPerson() {
		return recruitPerson;
	}

	public void setRecruitPerson(Integer recruitPerson) {
		this.recruitPerson = recruitPerson;
	}

	public String getRecruitTitle() {
		return recruitTitle;
	}

	public void setRecruitTitle(String recruitTitle) {
		this.recruitTitle = recruitTitle;
	}

	public String getRecruitContent() {
		return recruitContent;
	}

	public void setRecruitContent(String recruitContent) {
		this.recruitContent = recruitContent;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getRecruitProvince() {
		return recruitProvince;
	}

	public void setRecruitProvince(String recruitProvince) {
		this.recruitProvince = recruitProvince;
	}

	public String getRecruitCity() {
		return recruitCity;
	}

	public void setRecruitCity(String recruitCity) {
		this.recruitCity = recruitCity;
	}

	public String getRecruitArea() {
		return recruitArea;
	}

	public void setRecruitArea(String recruitArea) {
		this.recruitArea = recruitArea;
	}

	public String getRecruitLong() {
		return recruitLong;
	}

	public void setRecruitLong(String recruitLong) {
		this.recruitLong = recruitLong;
	}

	public String getRecruitLat() {
		return recruitLat;
	}

	public void setRecruitLat(String recruitLat) {
		this.recruitLat = recruitLat;
	}

	public String getRecruitAddress() {
		return recruitAddress;
	}

	public void setRecruitAddress(String recruitAddress) {
		this.recruitAddress = recruitAddress;
	}

	public String getContractor() {
		return contractor;
	}

	public void setContractor(String contractor) {
		this.contractor = contractor;
	}

	public String getCoverImage() {
		return coverImage;
	}

	public void setCoverImage(String coverImage) {
		this.coverImage = coverImage;
	}

	public String getBalanceWay() {
		return balanceWay;
	}

	public void setBalanceWay(String balanceWay) {
		this.balanceWay = balanceWay;
	}

	public String getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(String startPrice) {
		this.startPrice = startPrice;
	}

	public String getEndPrice() {
		return endPrice;
	}

	public void setEndPrice(String endPrice) {
		this.endPrice = endPrice;
	}

	public String getStopTime() {
		return stopTime;
	}

	public void setStopTime(String stopTime) {
		this.stopTime = stopTime;
	}

	public String getBanners() {
		return banners;
	}

	public void setBanners(String banners) {
		this.banners = banners;
	}

	public String getCategorys() {
		return categorys;
	}

	public void setCategorys(String categorys) {
		this.categorys = categorys;
	}

	public Integer getCategorySort() {
		return categorySort;
	}

	public void setCategorySort(Integer categorySort) {
		this.categorySort = categorySort;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(Integer categoryType) {
		this.categoryType = categoryType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPageNum() {
		if(pageNum == null || pageNum <= 0)
		{
			pageNum = 1;
		}
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		if(pageSize == null || pageSize <= 0)
		{
			pageSize = 10;
		}
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUserIdList() {
		return userIdList;
	}

	public void setUserIdList(String userIdList) {
		this.userIdList = userIdList;
	}

	public String getCategoryParentId() {
		return categoryParentId;
	}

	public void setCategoryParentId(String categoryParentId) {
		this.categoryParentId = categoryParentId;
	}

/*	public List<Integer> getUserIdList() {
		return userIdList;
	}

	public void setUserIdList(List<Integer> userIdList) {
		this.userIdList = userIdList;
	}*/
	
}
