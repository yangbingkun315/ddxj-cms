package net.zn.ddxj.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import net.zn.ddxj.entity.AdvertBanner;
import net.zn.ddxj.entity.Credit;
import net.zn.ddxj.entity.CreditRepayment;
import net.zn.ddxj.entity.InformationType;
import net.zn.ddxj.entity.InviteSetting;
import net.zn.ddxj.entity.Notice;
import net.zn.ddxj.entity.Recruit;
import net.zn.ddxj.entity.ScreenAdvert;
import net.zn.ddxj.entity.SiteRecruit;
import net.zn.ddxj.entity.SiteSlide;
import net.zn.ddxj.entity.User;
import net.zn.ddxj.entity.WebRecruit;

import org.springframework.web.multipart.MultipartFile;

public class CmsRequestVo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4761241769978091547L;
	private Integer id ;
	private Integer pageSize;
	private Integer totalPage;
	private Integer currentPage;
	private Integer pageNum;
	private Integer userId;
	private String roleName;
	
	private String nickName;
	private String userName;
	private String password;
	private Integer roleId;
	private String sex;
	private String birthday;
	private String birthDate;
	private String telphone;
	private String email;
	private String address;
	private String locked;
	private String description;
	
	private String logContent;
	private String startTime;
	private String endTime;
	private Integer parentId;
	private String categoryParentId;
	private Integer resourceType;
	private Integer groupBtnId;
	private String resourceName;
	
	private Integer parentContentsId;
	private Integer parentMenuId;
	private String resourceKey;
	private String resourceUrl;
	private Integer resourceLevel;
	private Integer resourceSort;
	private String resourceDescription;
	
	private Integer type;
	
	private Integer status;
	private String phone;
	private String createTime;
	
	private String categoryName;
	private Integer categoryId;
	private Integer categoryType;
	private Integer categorySort;
	private String realName;
	private Integer role;
	private String staffNum;
	
	private String idCardNumber;
	private Integer realStatus;//审核状态（1：审核中，2：审核失败，3审核成功）
	private Integer fromUserId;
	private Integer toUserId;
	
	
	private String menuName;

	private Integer recruitId;
	private Integer recruitStatus;
	private Integer recruitPerson;
	private String recruitTitle;
	private String recruitContent;
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
    private String bankOn;
    private String bankName;    
    private String bankType;
    /**
     * 充值
     * @return
     */
    public Integer payStatus;
    
    private String videoUrl;
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
    private String mediaId;

    private Integer validateStatus;
    private Integer stick;
    private String validateCause;
    private String isAuth;
    private Integer auditStatus;
   
    
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
    //用户导出
    //private List<Integer> userIdList;
    
    private String userIdList;
    private String pushPlatform;
    private String timer;
    private Integer sort;
    private Integer typeId;     
    private User user;
    private Credit credit;
    private List<Integer> categoryIdList;
    private MultipartFile imageFile;
    private String storageKey;
    private String storageType;
    private Recruit recruit;
    private List<String> bannerImageList;
    private SiteSlide siteSlide;
    private AdvertBanner advertBanner;
    private SiteRecruit siteRecruit;
    private ScreenAdvert screenAdvert;
    private String icon;
    private CreditRepayment reditRepayment;
    
    private InviteSetting inviteSetting;
    private String title;
	
    private Notice notice;
    private Integer noticeType;
    private String noticeContent;
    
    private InformationType informationType;//咨询分类
    
    private String isMatchAllKeyWords;
    private String keyWords;
    private Integer replyType;
    private String replayWords;
    private String befollowedReplyImg;
    private String imageTextList;
    
    private String registerNickName;
    private String registerUserName;
    private String registerPassword;
    private String registerPhone;
    private String registerBirthday;
    private String registerEmail;
    private String registerAddress;
    
    private String content;
    private String logo;
    private String menuUrl;
    private Integer menuSort;
    
    private WebRecruit webRecruit;
    
	public WebRecruit getWebRecruit() {
		return webRecruit;
	}
	public void setWebRecruit(WebRecruit webRecruit) {
		this.webRecruit = webRecruit;
	}
	public String getRegisterNickName() {
		return registerNickName;
	}
	public void setRegisterNickName(String registerNickName) {
		this.registerNickName = registerNickName;
	}
	public String getRegisterUserName() {
		return registerUserName;
	}
	public void setRegisterUserName(String registerUserName) {
		this.registerUserName = registerUserName;
	}
	public String getRegisterPassword() {
		return registerPassword;
	}
	public void setRegisterPassword(String registerPassword) {
		this.registerPassword = registerPassword;
	}
	public String getRegisterPhone() {
		return registerPhone;
	}
	public void setRegisterPhone(String registerPhone) {
		this.registerPhone = registerPhone;
	}
	public String getRegisterBirthday() {
		return registerBirthday;
	}
	public void setRegisterBirthday(String registerBirthday) {
		this.registerBirthday = registerBirthday;
	}
	public String getRegisterEmail() {
		return registerEmail;
	}
	public void setRegisterEmail(String registerEmail) {
		this.registerEmail = registerEmail;
	}
	public String getRegisterAddress() {
		return registerAddress;
	}
	public void setRegisterAddress(String registerAddress) {
		this.registerAddress = registerAddress;
	}
	public Integer getReplyType() {
		return replyType;
	}
	public void setReplyType(Integer replyType) {
		this.replyType = replyType;
	}
	public String getIsMatchAllKeyWords() {
		return isMatchAllKeyWords;
	}
	public void setIsMatchAllKeyWords(String isMatchAllKeyWords) {
		this.isMatchAllKeyWords = isMatchAllKeyWords;
	}
	public String getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}
	public String getReplayWords() {
		return replayWords;
	}
	public void setReplayWords(String replayWords) {
		this.replayWords = replayWords;
	}
	public String getBefollowedReplyImg() {
		return befollowedReplyImg;
	}
	public void setBefollowedReplyImg(String befollowedReplyImg) {
		this.befollowedReplyImg = befollowedReplyImg;
	}
	public String getImageTextList() {
		return imageTextList;
	}
	public void setImageTextList(String imageTextList) {
		this.imageTextList = imageTextList;
	}
	public InformationType getInformationType() {
		return informationType;
	}
	public void setInformationType(InformationType informationType) {
		this.informationType = informationType;
	}
	public CreditRepayment getReditRepayment() {
		return reditRepayment;
	}
	public void setReditRepayment(CreditRepayment reditRepayment) {
		this.reditRepayment = reditRepayment;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public ScreenAdvert getScreenAdvert() {
		return screenAdvert;
	}

	public void setScreenAdvert(ScreenAdvert screenAdvert) {
		this.screenAdvert = screenAdvert;
	}

	public SiteRecruit getSiteRecruit() {
		return siteRecruit;
	}

	public void setSiteRecruit(SiteRecruit siteRecruit) {
		this.siteRecruit = siteRecruit;
	}

	public SiteSlide getSiteSlide() {
		return siteSlide;
	}

	public void setSiteSlide(SiteSlide siteSlide) {
		this.siteSlide = siteSlide;
	}

	public List<String> getBannerImageList() {
		return bannerImageList;
	}

	public void setBannerImageList(List<String> bannerImageList) {
		this.bannerImageList = bannerImageList;
	}

	public Recruit getRecruit() {
		return recruit;
	}

	public void setRecruit(Recruit recruit) {
		this.recruit = recruit;
	}

	public Credit getCredit() {
		return credit;
	}

	public void setCredit(Credit credit) {
		this.credit = credit;
	}

	public String getStorageKey() {
		return storageKey;
	}

	public void setStorageKey(String storageKey) {
		this.storageKey = storageKey;
	}

	public String getStorageType() {
		return storageType;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}

	public Integer getPageNum() {
		if(pageNum == null)
		{
			pageNum = 1;
		}
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Integer getGroupBtnId() {
		return groupBtnId;
	}

	public void setGroupBtnId(Integer groupBtnId) {
		this.groupBtnId = groupBtnId;
	}
    
	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public List<Integer> getCategoryIdList() {
		return categoryIdList;
	}

	public void setCategoryIdList(List<Integer> categoryIdList) {
		this.categoryIdList = categoryIdList;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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


	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public Integer getRecruitId() {
		return recruitId;
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

	public Integer getBalanceType() {
		return balanceType;
	}

	public void setBalanceType(Integer balanceType) {
		this.balanceType = balanceType;
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

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
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

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
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

	public String getInfoTitle() {
		return infoTitle;
	}

	public void setInfoTitle(String infoTitle) {
		this.infoTitle = infoTitle;
	}

	public String getInfoSummary() {
		return infoSummary;
	}

	public void setInfoSummary(String infoSummary) {
		this.infoSummary = infoSummary;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Integer getInfoType() {
		return infoType;
	}

	public void setInfoType(Integer infoType) {
		this.infoType = infoType;
	}

	public Integer getInforId() {
		return inforId;
	}

	public void setInforId(Integer inforId) {
		this.inforId = inforId;
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

	public Integer getLibId() {
		return libId;
	}

	public void setLibId(Integer libId) {
		this.libId = libId;
	}

	public String getProblemTitle() {
		return problemTitle;
	}

	public void setProblemTitle(String problemTitle) {
		this.problemTitle = problemTitle;
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

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
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

	public String getValidateCause() {
		return validateCause;
	}

	public void setValidateCause(String validateCause) {
		this.validateCause = validateCause;
	}

	public String getIsAuth() {
		return isAuth;
	}

	public void setIsAuth(String isAuth) {
		this.isAuth = isAuth;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
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

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Integer getResourceSort() {
		return resourceSort;
	}

	public void setResourceSort(Integer resourceSort) {
		this.resourceSort = resourceSort;
	}

	public String getRecruitTenure() {
		return recruitTenure;
	}

	public void setRecruitTenure(String recruitTenure) {
		this.recruitTenure = recruitTenure;
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

	public String getCircleNumber() {
		return circleNumber;
	}

	public void setCircleNumber(String circleNumber) {
		this.circleNumber = circleNumber;
	}

	public Integer getMassType() {
		return massType;
	}

	public void setMassType(Integer massType) {
		this.massType = massType;
	}

	public Integer getSendType() {
		return sendType;
	}

	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}

	public String getMassContent() {
		return massContent;
	}

	public void setMassContent(String massContent) {
		this.massContent = massContent;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
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

	public String getPreviewOpenId() {
		return previewOpenId;
	}

	public void setPreviewOpenId(String previewOpenId) {
		this.previewOpenId = previewOpenId;
	}

	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
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

	public Integer getMsgSex() {
		return msgSex;
	}

	public void setMsgSex(Integer msgSex) {
		this.msgSex = msgSex;
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

	public Integer getOpenType() {
		return openType;
	}

	public void setOpenType(Integer openType) {
		this.openType = openType;
	}

	public Integer getMsgId() {
		return msgId;
	}

	public void setMsgId(Integer msgId) {
		this.msgId = msgId;
	}

	public String getUserIdList() {
		return userIdList;
	}

	public void setUserIdList(String userIdList) {
		this.userIdList = userIdList;
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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public String getStaffNum() {
		return staffNum;
	}

	public void setStaffNum(String staffNum) {
		this.staffNum = staffNum;
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentContentsId() {
		return parentContentsId;
	}

	public void setParentContentsId(Integer parentContentsId) {
		this.parentContentsId = parentContentsId;
	}

	public Integer getParentMenuId() {
		return parentMenuId;
	}

	public void setParentMenuId(Integer parentMenuId) {
		this.parentMenuId = parentMenuId;
	}

	public String getResourceKey() {
		return resourceKey;
	}

	public void setResourceKey(String resourceKey) {
		this.resourceKey = resourceKey;
	}

	public String getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}

	public Integer getResourceLevel() {
		return resourceLevel;
	}

	public void setResourceLevel(Integer resourceLevel) {
		this.resourceLevel = resourceLevel;
	}



	public String getResourceDescription() {
		return resourceDescription;
	}

	public void setResourceDescription(String resourceDescription) {
		this.resourceDescription = resourceDescription;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getResourceType() {
		return resourceType;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public void setResourceType(Integer resourceType) {
		this.resourceType = resourceType;
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

	

	public String getLogContent() {
		return logContent;
	}

	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	
	public String getLocked() {
		return locked;
	}

	public void setLocked(String locked) {
		this.locked = locked;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getUserId() {
		return userId;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public String getRoleName() {
		return roleName;
	}
	
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	public Integer getPageSize() {
		if(pageSize == null)
		{
			pageSize = 20;
		}
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public Integer getCurrentPage() {
		if(currentPage == null)
		{
			currentPage = 1;
		}
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(Integer categoryType) {
		this.categoryType = categoryType;
	}

	public Integer getCategorySort() {
		return categorySort;
	}

	public void setCategorySort(Integer categorySort) {
		this.categorySort = categorySort;
	}

	public Integer getContractType() {
		return contractType;
	}

	public void setContractType(Integer contractType) {
		this.contractType = contractType;
	}

	public String getCategoryParentId() {
		return categoryParentId;
	}

	public void setCategoryParentId(String categoryParentId) {
		this.categoryParentId = categoryParentId;
	}

	public AdvertBanner getAdvertBanner() {
		return advertBanner;
	}

	public void setAdvertBanner(AdvertBanner advertBanner) {
		this.advertBanner = advertBanner;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public Notice getNotice() {
		return notice;
	}

	public void setNotice(Notice notice) {
		this.notice = notice;
	}

	public Integer getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(Integer noticeType) {
		this.noticeType = noticeType;
	}

	public String getNoticeContent() {
		return noticeContent;
	}

	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}

	public InviteSetting getInviteSetting() {
		return inviteSetting;
	}

	public void setInviteSetting(InviteSetting inviteSetting) {
		this.inviteSetting = inviteSetting;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getMenuUrl() {
		return menuUrl;
	}
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}
	public Integer getMenuSort() {
		return menuSort;
	}
	public void setMenuSort(Integer menuSort) {
		this.menuSort = menuSort;
	}
	
	
	
}
