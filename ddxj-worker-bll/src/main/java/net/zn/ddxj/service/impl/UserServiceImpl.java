package net.zn.ddxj.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.zn.ddxj.easemob.server.example.api.AuthTokenAPI;
import net.zn.ddxj.entity.ApplyForeman;
import net.zn.ddxj.entity.Category;
import net.zn.ddxj.entity.Complain;
import net.zn.ddxj.entity.EasemobToken;
import net.zn.ddxj.entity.EasemobUser;
import net.zn.ddxj.entity.RealAuth;
import net.zn.ddxj.entity.SensitiveKeywords;
import net.zn.ddxj.entity.User;
import net.zn.ddxj.entity.UserBank;
import net.zn.ddxj.entity.UserPassword;
import net.zn.ddxj.entity.UserRequest;
import net.zn.ddxj.entity.UserTransfer;
import net.zn.ddxj.entity.ValidateManager;
import net.zn.ddxj.entity.Version;
import net.zn.ddxj.entity.WechatUser;
import net.zn.ddxj.excel.ExcelUtil;
import net.zn.ddxj.mapper.ApplyForemanMapper;
import net.zn.ddxj.mapper.ComplainMapper;
import net.zn.ddxj.mapper.EasemobTokenMapper;
import net.zn.ddxj.mapper.EasemobUserMapper;
import net.zn.ddxj.mapper.InviteRecordMapper;
import net.zn.ddxj.mapper.PaymentRecordMapper;
import net.zn.ddxj.mapper.RealAuthMapper;
import net.zn.ddxj.mapper.SensitiveKeywordsMapper;
import net.zn.ddxj.mapper.UserBankMapper;
import net.zn.ddxj.mapper.UserMapper;
import net.zn.ddxj.mapper.UserPasswordMapper;
import net.zn.ddxj.mapper.UserRequestMapper;
import net.zn.ddxj.mapper.UserTransferMapper;
import net.zn.ddxj.mapper.ValidateManagerMapper;
import net.zn.ddxj.mapper.VersionMapper;
import net.zn.ddxj.mapper.WechatUserMapper;
import net.zn.ddxj.server.example.api.impl.EasemobAuthToken;
import net.zn.ddxj.server.example.comm.TokenUtil;
import net.zn.ddxj.service.UserService;
import net.zn.ddxj.service.UserTransferService;
import net.zn.ddxj.utils.CmsUtils;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.ExprotVo;
import net.zn.ddxj.vo.RequestVo;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Component
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
public class UserServiceImpl implements UserService{
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserRequestMapper requestMapper;
	@Autowired 
	private UserPasswordMapper userPasswordMapper;
	@Autowired 
	private UserTransferMapper userTransferMapper;
	@Autowired 
	private UserBankMapper userBankMapper;
	@Autowired 
	private RealAuthMapper realAuthMapper;
	@Autowired 
	private ComplainMapper complainMapper;
	@Autowired
	private ApplyForemanMapper applyForemanMapper;
	@Autowired
	private PaymentRecordMapper paymentRecordMapper;
	@Autowired
	private ValidateManagerMapper validateManagerMapper;
	@Autowired
	private SensitiveKeywordsMapper sensitiveKeywordsMapper;
	@Autowired
	private WechatUserMapper wechatUserMapper;
	@Autowired
	private VersionMapper versionMapper;
	@Autowired
	private InviteRecordMapper inviteRecordMapper;
	@Autowired
	private UserTransferService userTransferService;
	@Autowired
	private EasemobTokenMapper easemobTokenMapper;
	@Autowired
	private EasemobUserMapper easemobUserMapper;

	@Override
	public int insertSelective(User record) {
		
		return userMapper.insertSelective(record);
	}
	@Override
	public int updateByPrimaryKeySelective(User record) {
		
		return userMapper.updateByPrimaryKeySelective(record);
	}

	// 因为配置文件继承了CachingConfigurerSupport，所以没有指定key的话就是用默认KEY生成策略生成,我们这里指定了KEY
   // @Cacheable(value = "userInfo", key = "'userInfo#' + #phone")
	@Override
	public User SelectUser(String phone) {
		
		return userMapper.SelectUser(phone);
	}

	@Override
	public UserPassword selectPasswordByUserId(Integer userId) {
		
		return userPasswordMapper.selectPasswordByUserId(userId);
	}

	@Override
	public int updateUserPasswordByPhone(Integer userId, String password) {
		
		return userPasswordMapper.updateUserPasswordByPhone(userId, password);
		
	}
	@Override
	public List<User> queryUserLists(Map<String, Object> params) {
		
		return userMapper.queryUserLists(params);
	}
	@Override
	public User selectByPrimaryKey(Integer id) {
		return userMapper.selectByPrimaryKey(id);
	}

	@Override
	public int insertUserCategory(Map<String, Object> value) {
		
		return userMapper.insertUserCategory(value);
	}
	
	@Override
	public Integer queryUserIsRequest(Integer fromUserId,Integer toUserId) {
		
		return requestMapper.queryUserIsRequest(fromUserId, toUserId);
	}
	@Override
	public User queryUserDetail(int userId) {
		
		return userMapper.queryUserDetail(userId);
	}
	@Override
	public List<User> queryEnlistUserList(int userId, int recruitId,Integer status) {
		
		return userMapper.queryEnlistUserList(userId, recruitId, status);
	}
	@Override
	public void updateFromUserMoney(BigDecimal remainderMoney, Integer fromUserId) {
		
		userMapper.updateFromUserMoney(remainderMoney, fromUserId);
	}
	@Override
	public void updateToUserMoney(BigDecimal remainderMoney, Integer toUserId) {
		userMapper.updateToUserMoney(remainderMoney, toUserId);
		
	}

	@Override
	public UserTransfer findUserTransferByTransferId(int transferId) {
		// TODO Auto-generated method stub
		return userTransferMapper.selectByPrimaryKey(transferId);
	}
	
	@Override
	public List<UserBank> findUserBankList(int userId) {
		// TODO Auto-generated method stub
		return userBankMapper.findUserBankList(userId);
	}
	
	@Override
	public int addUserBank(UserBank bank) {
		// TODO Auto-generated method stub
		return userBankMapper.insertSelective(bank);
	}
	
	@Override
	public int updateUserBank(UserBank bank) {
		// TODO Auto-generated method stub
		return userBankMapper.updateByPrimaryKeySelective(bank);
	}
	
	@Override
	public RealAuth queryRealAuthById(int authId) {
		// TODO Auto-generated method stub
		return realAuthMapper.selectByPrimaryKey(authId);
	}
	
	@Override
	public int updateUserRealAuth(RealAuth auth) {
		// TODO Auto-generated method stub
		return realAuthMapper.updateByPrimaryKeySelective(auth);
	}
	
	@Override
	public int addUserRealAuth(RealAuth auth) {
		// TODO Auto-generated method stub
		return realAuthMapper.insertSelective(auth);
	}
	
	@Override
	public RealAuth queryUserRealAuth(int userId) {
		// TODO Auto-generated method stub
		return realAuthMapper.queryUserRealAuth(userId);
	}
	@Override
	public UserBank findUserBank(Integer userId, String bankCard) {
		// TODO Auto-generated method stub
		return userBankMapper.findUserBank(userId, bankCard);
	}
	@Override
	public int insertComplain(Complain complain) {
		// TODO Auto-generated method stub
		return complainMapper.insertSelective(complain);
	}
	@Override
	public int updatePaymentPassword(UserPassword record) {
		// TODO Auto-generated method stub
		return userPasswordMapper.updateByPrimaryKeySelective(record);
	}
	@Override
	public UserBank findUserBankByBankCard(String bankCard) {
		// TODO Auto-generated method stub
		return userBankMapper.findUserBankByBankCard(bankCard);
	}
	@Override
	public List<User> queryMyFollowUserInfo(Integer userId) {
		// TODO Auto-generated method stub
		return userMapper.queryMyFollowUserInfo(userId);
	}
	@Override
	public int updateUserRemainderMoney(BigDecimal remainderMoney, Integer userId,String transactionId) {
		// TODO Auto-generated method stub
		return userMapper.updateUserRemainderMoney(remainderMoney, userId);
	}
	@Override
	public int addApplyForeman(ApplyForeman record) {
		// TODO Auto-generated method stub
		return applyForemanMapper.insertSelective(record);
	}
	@Override
	public int addUserRequest(UserRequest req) {
		// TODO Auto-generated method stub
		return requestMapper.insertSelective(req);
	}
	@Override
	public int updateForman(ApplyForeman applyForeman) {
		return applyForemanMapper.updateByPrimaryKeySelective(applyForeman);
	}
	@Override
	public ApplyForeman selectByUserId(Integer userId) {
		return applyForemanMapper.selectByUserId(userId);
	}
	@Override
	public int deleteUserCategory(Integer userId) {
		// TODO Auto-generated method stub
		return userMapper.deleteUserCategory(userId);
	}
	@Override
	public List<User> queryManagerUserList(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return userMapper.queryManagerUserList(param);
	}
	@Override
	public int forbiddenUser(Integer userId,Integer type) {
		// TODO Auto-generated method stub
		return userMapper.forbiddenUser(userId,type);
	}
	@Override
	public User queryUserByPhone(String phone) {
		// TODO Auto-generated method stub
		return userMapper.queryUserByPhone(phone);
	}
	@Override
	public int addUserPasswordByPhone(Integer userId, String password) {
		// TODO Auto-generated method stub
		UserPassword userPassword = new UserPassword();
		userPassword.setUserId(userId);
		userPassword.setLoginPassword(password);
		userPassword.setCreateTime(new Date());
		userPassword.setUpdateTime(new Date());
		return userPasswordMapper.insertSelective(userPassword);
	}
	@Override
	public int updateLoginPassword(UserPassword record) {
		// TODO Auto-generated method stub
		return userPasswordMapper.updateByPrimaryKeySelective(record);
	}
	@Override
	public List<RealAuth> queryRealAuthList(CmsRequestVo requestVo) {
		// TODO Auto-generated method stub
		return realAuthMapper.queryRealAuthList(requestVo);
	}
	@Override
	public int updateRealAuthStatus(int realId, int status) {
		// TODO Auto-generated method stub
		return realAuthMapper.updateRealAuthStatus(realId, status);
	}
	@Override
	public int deleteRealAuth(int realId) {
		// TODO Auto-generated method stub
		return realAuthMapper.deleteRealAuth(realId);
	}
	@Override
	public int queryValidateDayCount(Integer userId, String type) {
		// TODO Auto-generated method stub
		return validateManagerMapper.queryValidateDayCount(userId, type);
	}
	@Override
	public int addValidateManager(ValidateManager validateManager) {
		// TODO Auto-generated method stub
		return validateManagerMapper.insertSelective(validateManager);
	}
	@Override
	public Integer queryUserRequestById(int fromUserId, int toUserId, int recruitId) {
		// TODO Auto-generated method stub
		return requestMapper.queryUserRequestById(fromUserId, toUserId, recruitId);
	}
	@Override
	public BigDecimal sumMoneyByTypeAndToday(int userId, int type, int toUser) {
		// TODO Auto-generated method stub
		return userTransferMapper.sumMoneyByTypeAndToday(userId, type, toUser);
	}
	@Override
	public BigDecimal totalMoneyByType(int userId, int type, int toUser) {
		// TODO Auto-generated method stub
		return userTransferMapper.totalMoneyByType(userId, type, toUser);
	}
	@Override
	public BigDecimal sumMoneyByTypeAndWeek(int userId, int type, int toUser) {
		// TODO Auto-generated method stub
		return userTransferMapper.sumMoneyByTypeAndWeek(userId, type, toUser);
	}
	@Override
	public BigDecimal sumMoneyByTypeAndMonth(int userId, int type, int toUser) {
		// TODO Auto-generated method stub
		return userTransferMapper.sumMoneyByTypeAndMonth(userId, type, toUser);
	}
	@Override
	public List<User> queryForemanUserList(RequestVo requestVo) {
		// TODO Auto-generated method stub
		return userMapper.queryForemanUserList(requestVo);
	}
	@Override
	public List<SensitiveKeywords> querySensitiveKeywords() {
		return sensitiveKeywordsMapper.querySensitiveKeywordsList();
	}
	@Override
	public int insertUserPassword(UserPassword record) {
		return userPasswordMapper.insertSelective(record);
	}
	@Override
	public RealAuth queryUserRealAuthByIdCard(String idCardNumber) {
		// TODO Auto-generated method stub
		return realAuthMapper.queryUserRealAuthByIdCard(idCardNumber);
	}
	@Override
	public int queryUserRealAuthCount(String idCardNumber) {
		// TODO Auto-generated method stub
		return realAuthMapper.queryUserRealAuthCount(idCardNumber);
	}
	@Override
	public int updateOutLogin(Integer userId) {
		// TODO Auto-generated method stub
		return userMapper.updateOutLogin(userId);
	}
	@Override
	public Map<String, BigDecimal> queryUserToUserMoneyLog(int userId) {
		// TODO Auto-generated method stub
		return userTransferMapper.queryUserToUserMoneyLog(userId);
	}
	@Override
	public List<WechatUser> queryWechatUserList(RequestVo requestVo) {
		// TODO Auto-generated method stub
		return wechatUserMapper.queryWechatUserList(requestVo);
	}
	@Override
	public List<ApplyForeman> queryApplyForeman(CmsRequestVo requestVo) {
		// TODO Auto-generated method stub
		return applyForemanMapper.queryApplyForeman(requestVo);
	}
	@Override
	public int updateApplyForemanStatus(Integer foremanId, Integer auditStatus,String validateCause) {
		// TODO Auto-generated method stub
		return applyForemanMapper.updateApplyForemanStatus(foremanId,auditStatus,validateCause);
	}
	@Override
	public ApplyForeman selectByForemanId(Integer id) {
		// TODO Auto-generated method stub
		return applyForemanMapper.selectByPrimaryKey(id);
	}
	@Override
	public int updateUserRole(Integer userId) {
		// TODO Auto-generated method stub
		return userMapper.updateUserRole(userId);
	}
	@Override
	public int deleteUserRequest(Integer userId) {
		// TODO Auto-generated method stub
		return requestMapper.deleteUserRequest(userId);
	}
	@Override
	public List<Complain> queryComplainList(CmsRequestVo requestVo) {
		// TODO Auto-generated method stub
		return complainMapper.queryComplainList(requestVo);
	}
	@Override
	public int deleteComPlain(int id) {
		// TODO Auto-generated method stub
		return complainMapper.deleteComPlain(id);
	}
	@Override
	public int handleComPlain(Complain complain) {
		// TODO Auto-generated method stub
		return complainMapper.updateByPrimaryKeySelective(complain);
	}
	@Override
	public Version queryLastVersion(String mobileSource) {
		// TODO Auto-generated method stub
		return versionMapper.queryLastVersion(mobileSource);
	}
	@Override
	public User queryUserByOpenId(String openId) {
		// TODO Auto-generated method stub
		return userMapper.queryUserByOpenId(openId);
	}
	@Override
	public int delUserInfo(Integer userId) {
		// TODO Auto-generated method stub
		return userMapper.delUserInfo(userId);
	}
	@Override
	public int delUserPasswordRecord(Integer userId) {
		// TODO Auto-generated method stub
		return userPasswordMapper.delUserPasswordRecord(userId);
	}
	@Override
	public int delUserBankRecord(Integer userId) {
		// TODO Auto-generated method stub
		return userBankMapper.delUserBankRecord(userId);
	}
	@Override
	public int delUserAuthRecord(Integer userId) {
		// TODO Auto-generated method stub
		return realAuthMapper.delUserAuthRecord(userId);
	}
	@Override
	public int delUserRequestRecord(Integer userId) {
		// TODO Auto-generated method stub
		return requestMapper.delUserRequestRecord(userId);
	}
	@Override
	public int delApplyForemanRecord(Integer userId) {
		// TODO Auto-generated method stub
		return applyForemanMapper.delApplyForemanRecord(userId);
	}
	@Override
	public int delComplainRecord(Integer userId) {
		// TODO Auto-generated method stub
		return complainMapper.delComplainRecord(userId);
	}
	@Override
	public int queryTodayUserCount(Integer type) {
		// TODO Auto-generated method stub
		return userMapper.queryTodayUserCount(type);
	}
	@Override
	public List<Map<String, Object>> queryMonthEveryDay() {
		// TODO Auto-generated method stub
		return userMapper.queryMonthEveryDay();
	}
	@Override
	public Integer findUserIdByPhone(String phone) {
		return userMapper.findByPhone(phone);
	}
	@Override
	public BigDecimal updateRemainderBnousMoney(BigDecimal bonus, Integer userId,int flag) {
		if(flag == 1){
			userMapper.updateRemainderBnousMoney(bonus, userId);
		}else{
			BigDecimal money = inviteRecordMapper.queryInviteBonusCount(userId);
			BigDecimal limit = new BigDecimal("500");
			if(limit.compareTo(money)>0){
				if(money.add(bonus).compareTo(limit)>=0){
					bonus = limit.subtract(money);
				}
				//给邀请的老用户加奖金
				userTransferService.addTransferRecord(userId,bonus,"邀请注册奖励金");
				userMapper.updateRemainderBnousMoney(bonus, userId);
				return bonus;
			}
		}
		return new BigDecimal(0.00);
	}
	@Override
	public Integer queryUserByRole(Integer role) {
		// TODO Auto-generated method stub
		return userMapper.queryUserByRole(role);
	}
	@Override
	public int deleteForemanByApplyId(Integer applyId) {
		// TODO Auto-generated method stub
		return applyForemanMapper.deleteByPrimaryKey(applyId);
	}

	@Override
	public Version queryNotExamineMaxVersion(String mobileSource) {
		// TODO Auto-generated method stub
		return versionMapper.queryNotExamineMaxVersion(mobileSource);
	}
	@Override
	public int deleteUserBank(int id) {
		// TODO Auto-generated method stub
		return userBankMapper.deleteByPrimaryKey(id);
	}
	@Override
	public List<User> queryUserByStaffNum(String realName,String phone,String staffNum,String startTime,String endTime) {
		return userMapper.queryUserInfoByStaffNum(realName,phone,staffNum,startTime,endTime);
	}
	@Override
	public User getUserByOpenid(String openid) {
		// TODO Auto-generated method stub
		return userMapper.getUserByOpenid(openid);
	}
	@Override
	public List<User> queryExtendUserInfo(String currentTime, Integer extendCode, String endTime) {
		// TODO Auto-generated method stub
		return userMapper.queryExtendUserInfo(currentTime, extendCode, endTime);
	}
	@Override
	public User queryUserByunionid(String unionid) {
		// TODO Auto-generated method stub
		return userMapper.queryUserByunionid(unionid);
	}
	@Override
	public List<User> selectUserByCategoryName(String CategoryName) {
		// TODO Auto-generated method stub
		return userMapper.selectUserByCategoryName(CategoryName);
	}
	@Override
	public List<User> queryUserFollowerByTerm(RequestVo requestVo) {
		// TODO Auto-generated method stub
		return userMapper.queryUserFollowerByTerm(requestVo);
	}
	@Override
	public String userListExport(List<Integer> userIdList) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("用户信息");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		HSSFCellStyle styleTitle = this.createTitleStyle(workbook);
		HSSFCellStyle styleContent = this.createContentStyle(workbook);
		HSSFCellStyle style = this.createStyle(workbook);
		final String[][] USER_EXPORT_TITLE = {{"用户ID","2000"},{"真实姓名","3500"},{"联系电话","3500"},{"性别","2000"},{"年龄","2000"},{"角色","2000"},{"工种","3000"},{"工龄(年)","2000"},{"工作所在地","3500"},{"详细地址","3500"},{"账户余额","2000"},{"是否认证","2000"},{"注册渠道","3500"},{"最后一次登录时间","5000"},{"最后一次登录设备","5000"},{"创建时间","5000"}};
		List<User> userList = new ArrayList<>();
		for (Integer userId : userIdList) {
			User user = userMapper.selectByPrimaryKey(userId);
			if(!CmsUtils.isNullOrEmpty(user))
			{
				userList.add(user);
			}
		}
		if(!CmsUtils.isNullOrEmpty(userList))
		{
			this.createExcelTitle(sheet, styleTitle, USER_EXPORT_TITLE);
			int userListSize = userList.size();
			for (int i = 0; i < userListSize; i++) {
				User user = userList.get(i);
				HSSFRow rowTemp = sheet.createRow((short) (i + 1));
				//用户ID
				if(!CmsUtils.isNullOrEmpty(user.getId()))
				{
					this.createCell(rowTemp, 0, styleContent, HSSFCell.CELL_TYPE_STRING, user.getId());
				}
				//真实姓名
				if(!CmsUtils.isNullOrEmpty(user.getRealName()))
				{
					this.createCell(rowTemp, 1, styleContent, HSSFCell.CELL_TYPE_STRING, user.getRealName());
				}else
				{
					this.createCell(rowTemp, 1, styleContent, HSSFCell.CELL_TYPE_STRING, "");
				}
				//联系电话
				if(!CmsUtils.isNullOrEmpty(user.getPhone()))
				{
					this.createCell(rowTemp, 2, styleContent, HSSFCell.CELL_TYPE_STRING, user.getPhone());
				}
				//性别
				if(!CmsUtils.isNullOrEmpty(user.getSex()))
				{
					String sex = "";
					if("M".equals(user.getSex()))
					{
						sex = "男";
					}else
					{
						sex = "女";
					}
					this.createCell(rowTemp, 3, styleContent, HSSFCell.CELL_TYPE_STRING, sex);
				}else
				{
					this.createCell(rowTemp, 3, styleContent, HSSFCell.CELL_TYPE_STRING, "");
				}
				//年龄
				if(!CmsUtils.isNullOrEmpty(user.getAge()))
				{
					this.createCell(rowTemp, 4, styleContent, HSSFCell.CELL_TYPE_STRING, user.getAge());
				}else
				{
					this.createCell(rowTemp, 4, styleContent, HSSFCell.CELL_TYPE_STRING, "");
				}
				//角色
				if(!CmsUtils.isNullOrEmpty(user.getRole()))
				{
					String role = "";
					if(1 == user.getRole())
					{
						role = "工人";
					}
					if(2 == user.getRole())
					{
						role = "工头";
					}
					this.createCell(rowTemp, 5, styleContent, HSSFCell.CELL_TYPE_STRING, role);
				}else
				{
					this.createCell(rowTemp, 5, styleContent, HSSFCell.CELL_TYPE_STRING, "");
				}
				//工种
				if(!CmsUtils.isNullOrEmpty(user.getCategoryList()))
				{
					String category = "";
					for (Category cate : user.getCategoryList()) {
						category += cate.getCategoryName();
					}
					this.createCell(rowTemp, 6, styleContent, HSSFCell.CELL_TYPE_STRING, category);
				}else
				{
					this.createCell(rowTemp, 6, styleContent, HSSFCell.CELL_TYPE_STRING, "");
				}
				//工龄
				if(!CmsUtils.isNullOrEmpty(user.getStanding()))
				{
					this.createCell(rowTemp, 7, styleContent, HSSFCell.CELL_TYPE_STRING, user.getStanding());
				}else
				{
					this.createCell(rowTemp, 7, styleContent, HSSFCell.CELL_TYPE_STRING, "");
				}
				//工作所在地
				if(!CmsUtils.isNullOrEmpty(user.getWorkProvince()))
				{
					String workAdress = user.getWorkProvince();
					if(!CmsUtils.isNullOrEmpty(user.getWorkCity()))
					{
						if(!CmsUtils.isNullOrEmpty(user.getWorkArea()))
						{
							workAdress = user.getWorkProvince() + user.getWorkCity() + user.getWorkArea();
						}else
						{
							workAdress = user.getWorkProvince() + user.getWorkCity();
						}
					}
					this.createCell(rowTemp, 8, styleContent, HSSFCell.CELL_TYPE_STRING, workAdress);
				}else
				{
					this.createCell(rowTemp, 8, styleContent, HSSFCell.CELL_TYPE_STRING, "");
				}
				//详细地址
				if(!CmsUtils.isNullOrEmpty(user.getWorkAddress()))
				{
					this.createCell(rowTemp, 9, styleContent, HSSFCell.CELL_TYPE_STRING, user.getWorkAddress());
				}else
				{
					this.createCell(rowTemp, 9, styleContent, HSSFCell.CELL_TYPE_STRING, "");
				}
				//账户余额
				if(!CmsUtils.isNullOrEmpty(user.getRemainderMoney()))
				{
					this.createCell(rowTemp, 10, styleContent, HSSFCell.CELL_TYPE_STRING, user.getRemainderMoney());
				}else
				{
					this.createCell(rowTemp, 10, styleContent, HSSFCell.CELL_TYPE_STRING, "");
				}
				//是否认证
				if(!CmsUtils.isNullOrEmpty(user.getIsAttestation()))
				{
					String isAttestation = "";
					if(0 == user.getIsAttestation())
					{
						isAttestation = "否";
					}else
					{
						isAttestation = "是";
					}
					this.createCell(rowTemp, 11, styleContent, HSSFCell.CELL_TYPE_STRING, isAttestation);
				}else
				{
					this.createCell(rowTemp, 11, styleContent, HSSFCell.CELL_TYPE_STRING, "");
				}
				//注册渠道
				if(!CmsUtils.isNullOrEmpty(user.getRegisterChannel()))
				{
					String registerChannel = "";
					if(1 == user.getRegisterChannel())
					{
						registerChannel = "公众号";
					}
					if(2 == user.getRegisterChannel())
					{
						registerChannel = "App";
					}
					this.createCell(rowTemp, 12, styleContent, HSSFCell.CELL_TYPE_STRING, registerChannel);
				}else
				{
					this.createCell(rowTemp, 12, styleContent, HSSFCell.CELL_TYPE_STRING, "");
				}
				//最后一次登陆时间
				if(!CmsUtils.isNullOrEmpty(user.getCurrentDateTime()))
				{
					this.createCell(rowTemp, 13, styleContent, HSSFCell.CELL_TYPE_STRING, sdf.format(user.getCurrentDateTime()));
				}else
				{
					this.createCell(rowTemp, 13, styleContent, HSSFCell.CELL_TYPE_STRING, "");
				}
				//最后一次登陆设备
				if(!CmsUtils.isNullOrEmpty(user.getLastDevice()))
				{
					this.createCell(rowTemp, 14, styleContent, HSSFCell.CELL_TYPE_STRING, user.getLastDevice());
				}else
				{
					this.createCell(rowTemp, 14, styleContent, HSSFCell.CELL_TYPE_STRING, "");
				}
				//创建时间
				if(!CmsUtils.isNullOrEmpty(user.getCreateTime()))
				{
					this.createCell(rowTemp, 15, styleContent, HSSFCell.CELL_TYPE_STRING, sdf.format(user.getCreateTime()));
				}else
				{
					this.createCell(rowTemp, 15, styleContent, HSSFCell.CELL_TYPE_STRING, "");
				}
			}
		}
		return ExcelUtil.exportAjaxExcelData("用户信息", workbook);
	}
	
	// 创建标题栏
	private void createExcelTitle(HSSFSheet sheet, HSSFCellStyle styleTitle, String[][] titleContent) {
		HSSFRow row = sheet.createRow((short) 0);// 建立新行
		for (int i = 0; i < titleContent.length; i++) {
			String[] title = titleContent[i];
			createCell(row, i, styleTitle, HSSFCell.CELL_TYPE_STRING, title[0]);
			sheet.setColumnWidth(i, Integer.parseInt(title[1]));
		}
	}

	// 创建Excel单元
	private void createCell(HSSFRow row, int column, HSSFCellStyle style, int cellType, Object value) {
		HSSFCell cell = row.createCell(column);
		if (style != null) {
			cell.setCellStyle(style);
		}
		switch (cellType) {
		case HSSFCell.CELL_TYPE_BLANK: {
			cell.setCellValue("");
		}
			break;
		case HSSFCell.CELL_TYPE_STRING: {
			cell.setCellValue(value.toString());
		}
			break;
		case HSSFCell.CELL_TYPE_NUMERIC: {
			cell.setCellValue(Double.parseDouble(value.toString()));
		}
			break;
		default:
			break;
		}
	}

	// 设置excel的title样式
	private HSSFCellStyle createTitleStyle(HSSFWorkbook wb) {
		HSSFCellStyle style = wb.createCellStyle();

		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框

		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		HSSFFont font = wb.createFont();
		font.setFontName("宋体");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
		font.setFontHeightInPoints((short) 13);

		style.setFont(font);// 选择需要用到的字体格式
		style.setWrapText(true);// 设置自动换行
		return style;
	}

	// 设置excel的内容样式
	private HSSFCellStyle createContentStyle(HSSFWorkbook wb) {
		HSSFCellStyle style = wb.createCellStyle();

		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框

		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		HSSFFont font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 12);

		style.setFont(font);// 选择需要用到的字体格式
		style.setWrapText(true);// 设置自动换行
		return style;
	}

	// 设置excel单元格格式
	private HSSFCellStyle createStyle(HSSFWorkbook wb) {
		HSSFCellStyle style = wb.createCellStyle();

		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框

		style.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		HSSFFont font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 12);

		style.setFont(font);// 选择需要用到的字体格式
		style.setWrapText(true);// 设置自动换行
		return style;
	}
	@Override
	public int updateUservalidateMessage(Integer userId) {
		// TODO Auto-generated method stub
		return userMapper.updateUservalidateMessage(userId);
	}
	@Override
	public int delValidateManager(Integer id) {
		// TODO Auto-generated method stub
		return validateManagerMapper.delValidateManager(id);
	}
	
	@Override
	public String queryEasemobToken()
    {
		// TODO Auto-generated method stub
		EasemobToken lastToken = easemobTokenMapper.queryLastToken();
        if(CmsUtils.isNullOrEmpty(lastToken))//查询数据库
        {
    		String t = TokenUtil.getAccessToken();
        	if(!CmsUtils.isNullOrEmpty(t))//验证有没有token
        	{
        		EasemobToken token = new EasemobToken();
        		token.setAccessToken(t);
        		token.setExpiresIn(5184000);
        		token.setCreateTime(new Date());
        		easemobTokenMapper.insertSelective(token);
        		return t;
        	}
        }
        else
        {
            long now = System.currentTimeMillis();
            long acctime = lastToken.getCreateTime().getTime();
            if (CmsUtils.isNullOrEmpty(lastToken.getAccessToken()) || ((now - acctime) / 1000) >= 5184000) //查询数据库中的token是否超时
            {
        		String t = TokenUtil.getAccessToken();
            	if(!CmsUtils.isNullOrEmpty(t))//验证有没有token
            	{
            		EasemobToken token = new EasemobToken();
            		token.setAccessToken(t);
            		token.setExpiresIn(5184000);
            		token.setCreateTime(new Date());
            		easemobTokenMapper.insertSelective(token);
            		return t;
            	}
            } 
            else 
            {
                return lastToken.getAccessToken();
            }
        }
        return null;
	}
	@Override
	public EasemobToken queryEasemobLastToken() {
		// TODO Auto-generated method stub
		return easemobTokenMapper.queryLastToken();
	}
	@Override
	public int countUserBindingCard(Integer userId) {
		// TODO Auto-generated method stub
		return userBankMapper.countUserBindingCard(userId);
	}
	@Override
	public int untieBankCard(String id, String bankCard) {
		// TODO Auto-generated method stub
		return userBankMapper.untieBankCard(id,bankCard);
	}
	@Override
	public int insertEasemobToken(EasemobToken token) {
		// TODO Auto-generated method stub
		return easemobTokenMapper.insertSelective(token);
	}
	@Override
	public int insertEasemobUser(EasemobUser user) {
		// TODO Auto-generated method stub
		return easemobUserMapper.insertSelective(user);
	}
	@Override
	public EasemobUser queryEasemobUserByPhone(int userId) {
		// TODO Auto-generated method stub
		return easemobUserMapper.queryEasemobUserByUserId(userId);
	}
	@Override
	public int updateEasemobUser(EasemobUser user) {
		// TODO Auto-generated method stub
		return easemobUserMapper.updateByPrimaryKeySelective(user);
	}
	@Override
	public int countUserTransfer(Integer userId, Integer payType) {
		// TODO Auto-generated method stub
		return userTransferMapper.countUserTransferTimes(userId,payType);
	}
	@Override
	public BigDecimal totalUserTransferMoney(Integer userId, Integer payType) {
		// TODO Auto-generated method stub
		return userTransferMapper.totalUserTransferMoney(userId,payType);
	}
	@Override
	public List<User> queryExprotUserList(ExprotVo exprotVo) {
		// TODO Auto-generated method stub
		return userMapper.queryExprotUserList(exprotVo);
	}
	@Override
	public User queryUserByEasemobName(String easemobId,String userId) {
		// TODO Auto-generated method stub
		return userMapper.queryUserByEasemobName(easemobId, userId);
	}
	@Override
	public Complain queryComPlainBy(Integer id) {
		// TODO Auto-generated method stub
		return complainMapper.selectByPrimaryKey(id);
	}
	@Override
	public List<WechatUser> queryWechatUserListCms(CmsRequestVo requestVo) {
		return wechatUserMapper.queryWechatUserListCms(requestVo);
	}
}
