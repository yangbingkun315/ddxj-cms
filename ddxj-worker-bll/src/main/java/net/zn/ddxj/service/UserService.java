package net.zn.ddxj.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import net.zn.ddxj.entity.ApplyForeman;
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
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.ExprotVo;
import net.zn.ddxj.vo.RequestVo;

public interface UserService {
	int insertSelective(User record);//添加用户信息
	int updateByPrimaryKeySelective(User record);//更新用户信息
	User SelectUser(String phone);//通过手机号获取用户信息
	User queryUserByunionid(String unionid);//通过unionid获取用户信息
	User queryUserByOpenId(String openId);//通过OPENID获取用户信息
	UserPassword selectPasswordByUserId(Integer userId);//用户编号查询用户密码
	int insertUserCategory(Map<String, Object> value);//添加分类
	int updateUserPasswordByPhone(Integer userId, String password);//更改用户密码
	int addUserPasswordByPhone(Integer userId, String password);//更改用户密码
	List<User> queryUserLists(Map<String, Object> params);//多条件查询用户信息
    User selectByPrimaryKey(Integer id);
    Integer queryUserIsRequest(Integer fromUserId,Integer toUserId);//查询该用户是否被工头邀请过
    User queryUserDetail(int userId);//查询用户信息
    List<User> queryEnlistUserList(int userId,int recruitId,Integer status);//查询报名工人信息
    void updateFromUserMoney(BigDecimal remainderMoney,Integer fromUserId);
	void updateToUserMoney(BigDecimal remainderMoney,Integer toUserId);
    UserTransfer findUserTransferByTransferId(int transferId);//资金变动详情
    List<UserBank> findUserBankList(int userId);//查询已绑定银行卡
    int deleteUserBank(int id);//删除已绑定银行卡
    UserBank findUserBankByBankCard(String bankCard);//根据银行卡查询是否被绑定
    int addUserBank(UserBank bank);//绑定银行卡
    int updateUserBank(UserBank bank);//更新绑定银行卡
    RealAuth queryRealAuthById(int authId);//查询Id认证信息
    RealAuth queryUserRealAuth(int userId);//根据UserId查询认证信息
    int updateUserRealAuth(RealAuth auth);//更新实名认证信息
    int addUserRealAuth(RealAuth auth);//新增实名认证信息
	UserBank findUserBank(Integer userId ,String bankCard);//根据卡号查询银行卡信息
	int insertComplain(Complain complain);//添加投诉信息
	int updatePaymentPassword(UserPassword record);//修改支付密码
	List<User> queryMyFollowUserInfo(Integer userId);//查询我关注的工人信息
	int updateUserRemainderMoney(BigDecimal remainderMoney,Integer userId,String transactionId);//更新用户调用微信充值后的金额
	int addApplyForeman(ApplyForeman record);//添加申请工头记录
	int addUserRequest(UserRequest req);//添加招聘邀请
	int updateForman(ApplyForeman applyForeman);//更新申请工头记录
	ApplyForeman selectByUserId(Integer userId);//查看申请工头详情
	int deleteUserCategory(Integer userId);//删除该用户工种信息
	List<User> queryManagerUserList(Map<String,Object> param);
	int forbiddenUser(Integer userId,Integer type);
	User queryUserByPhone(String phone);//通过手机号查询转账用户信息
	int updateLoginPassword(UserPassword record);//修改登录密码
	int insertUserPassword(UserPassword record);//添加密码
	List<RealAuth> queryRealAuthList(CmsRequestVo requestVo);//查询实名认证列表
	int updateRealAuthStatus(int realId,int status);//更新实名认证状态
	int deleteRealAuth(int realId);//删除实名认证信息
	int queryValidateDayCount(Integer userId,String type);//根据用户ID和验证类型，验证用户今天操作几次
	int addValidateManager(ValidateManager validateManager);//添加接口验证
	
	Integer queryUserRequestById(int fromUserId,int toUserId,int recruitId);//查询工人是否被邀请过
	
	BigDecimal totalMoneyByType(int userId,int type,int toUser);//查询所有的资金变动
	BigDecimal sumMoneyByTypeAndToday(int userId,int type,int toUser);//查询今天的资金变动
	BigDecimal sumMoneyByTypeAndWeek(int userId,int type,int toUser);//查询本周的资金变动
	BigDecimal sumMoneyByTypeAndMonth(int userId,int type,int toUser);//查询本月的资金变动
	
	Map<String, BigDecimal> queryUserToUserMoneyLog(int userId);//查询收入资金记录
	
	List<User> queryForemanUserList(RequestVo requestVo);//查询工头列表
	List<SensitiveKeywords> querySensitiveKeywords();//查询敏感字符
	RealAuth queryUserRealAuthByIdCard(String idCardNumber);//根据身份证号码查询认证信息
	int queryUserRealAuthCount(String idCardNumber);//查询实名认证数量
	int updateOutLogin(Integer userId);//退出登录
	
	List<WechatUser> queryWechatUserList(RequestVo requestVo);//多条件查询微信用户列表
	List<WechatUser> queryWechatUserListCms(CmsRequestVo requestVo);//多条件查询微信用户列表
	List<ApplyForeman> queryApplyForeman(CmsRequestVo requestVo);//查询申请工头的列表
	int updateApplyForemanStatus(Integer foremanId, Integer validateStatus ,String validateCause);//更新申请工头状态
	ApplyForeman selectByForemanId(Integer id);//查看申请工头详情
	int updateUserRole(Integer userId);//申请工头
	int deleteUserRequest(Integer userId);//删除邀请表
	
	List<Complain> queryComplainList(CmsRequestVo requestVo);//查询投诉与建议列表
	int deleteComPlain(int id);//删除投诉与建议
	int handleComPlain(Complain complain);//处理投诉与建议
	Version queryLastVersion(String mobileSource);//查询最后一次版本号已发布的
	Version queryNotExamineMaxVersion(String mobileSource);//查询最后一次版本号
	int delUserInfo(Integer userId);
	int delUserPasswordRecord(Integer userId);
	int delUserBankRecord(Integer userId);
	int delUserAuthRecord(Integer userId);
	int delUserRequestRecord(Integer userId);
	int delApplyForemanRecord(Integer userId);
	int delComplainRecord(Integer userId);
	int queryTodayUserCount(Integer type);//查询今日注册量
	
	/**
	 * 根据手机号查询用户ID
	 * @param phone
	 * @return
	 */
	Integer findUserIdByPhone(String phone); 
	
	/**
	 * 更新金额增加邀请奖励金
	 * @param bonus
	 * @param userId
	 * @param flag 1:给新用户加奖励金 2：给老用户加奖励金
	 * @return
	 */
	BigDecimal updateRemainderBnousMoney(BigDecimal bonus,Integer userId,int flag);
	
	List<User> queryUserByStaffNum(String realName,String phone,String staffNum,String startTime,String endTime);
	
	List<Map<String, Object>> queryMonthEveryDay();
	
	Integer queryUserByRole(Integer role);
	int deleteForemanByApplyId(Integer applyId);//删除申请记录
	User getUserByOpenid(String openid);
	List<User> queryExtendUserInfo(String currentTime,Integer extendCode,String endTime);//根据时间和推广编号查询用户信息
	List<User> selectUserByCategoryName(String CategoryName);
	
	List<User> queryUserFollowerByTerm(RequestVo requestVo);//多条件查询用户OpenId
	
	/**
	 * 导出用户信息
	 * @param userList
	 */
	String userListExport(List<Integer> userIdList);
	int updateUservalidateMessage(Integer userId);//清除验证信息
	int delValidateManager(Integer id);//删除认证信息message
	String queryEasemobToken();//获取token
	EasemobToken queryEasemobLastToken();//获取最新的一条token
	int insertEasemobToken(EasemobToken token);//添加token
	int insertEasemobUser(EasemobUser user);//添加环信用户
	int updateEasemobUser(EasemobUser user);//修改环信用户
	EasemobUser queryEasemobUserByPhone(int userId);//根据用户UserId查询环信用户
	int countUserBindingCard(Integer userId);//统计绑卡个数
	int untieBankCard(String id, String bankCard);//根据用户ID,银行卡号bankCard解除绑定的银行卡
	int countUserTransfer(Integer userId, Integer transferType);//查询转账次数
	BigDecimal totalUserTransferMoney(Integer userId, Integer payType);//查询转账金额
	List<User> queryExprotUserList(ExprotVo exprotVo);
	User queryUserByEasemobName(String easemobId,String userId);//根据环信ID查询用户头像和姓名
	Complain queryComPlainBy(Integer id);
}
