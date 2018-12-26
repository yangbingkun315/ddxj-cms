package net.zn.ddxj.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.User;
import net.zn.ddxj.vo.ExprotVo;
import net.zn.ddxj.vo.RequestVo;

@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    int updateUserLastInfo(@Param("lastIp")String lastIp,@Param("phone")String phone,@Param("loginChannel")String loginChannel);
    
    int updateUserAppUserToken(@Param("phone")String phone,@Param("appUserToken")String appUserToken);
    
    User SelectUser(String phone);
    
    List<User> queryUserLists(Map<String, Object> params);
    
    int insertUserCategory(Map<String, Object> value);
    
    User queryUserDetail(int userId);//查询用户信息
    User queryCutUserDetail(int userId);//查询用户信息
    User queryCutUserDetailByPhone(String phone);//查询用户信息
    
    List<User> queryEnlistUserList(@Param("userId")int userId,@Param("recruitId")int recruitId,@Param("status")Integer status);//查询报名工人信息
    
    void updateFromUserMoney(@Param("remainderMoney")BigDecimal remainderMoney,@Param("fromUserId") Integer fromUserId);//更新转账人的金额
    
   	void updateToUserMoney(@Param("remainderMoney")BigDecimal remainderMoney,@Param("toUserId") Integer toUserId);//更新收款人的金额
   	
   	User queryCircleUser(int userId);
   	
   	List<User> queryMyFollowUserInfo(Integer userId);
   	
   	int updateUserRemainderMoney(@Param("remainderMoney")BigDecimal remainderMoney,@Param("userId") Integer userId);//更新用户调用微信充值后的金额
   	
   	int deleteUserCategory(Integer userId);
   	
   	List<User> queryManagerUserList(Map<String,Object> param);
   	
   	int forbiddenUser(@Param("userId")Integer userId,@Param("type")Integer type);
   	
   	User queryUserByPhone(String phone);//通过手机号查询转账用户信息
   	
   	List<User> queryForemanUserList(RequestVo requestVo);//查询工头列表
   	
   	List<User> userListByCategoryId(Integer categoryId);
   	
   	int updateOutLogin(Integer userId);

	List<User> queryApplyForeman(RequestVo requestVo);//查询申请工头列表

	int updateUserRole(Integer userId);//更新申请工头

	User queryUserByOpenId(String openId);
	
	User queryUserByunionid(String unionid);
	
	int changeUserWechatInfo(Integer userId);
	
	int delUserInfo(Integer userId);
	
	int queryTodayUserCount(@Param("type")Integer type);//查询今日注册量
	
	List<Map<String, Object>> queryMonthEveryDay();
	
	/**
	 * 根据手机号查询用户id
	 * @param phone
	 * @return
	 */
	Integer findByPhone(String phone);
	
	/**
	 * 更新金额 （加邀请奖励金）
	 * @param bonus 奖励金额
	 * @param userId 用户ID
	 * @return
	 */
	int updateRemainderBnousMoney(@Param("bnous") BigDecimal bonus,@Param("userId") Integer userId);
	
	/**
	 * 查询推广人不为空的记录
	 * @return
	 */
	List<User> queryUserInfoByStaffNum(@Param("realName")String realName,@Param("phone")String phone,@Param("staffNum")String staffNum,@Param("startTime") String startTime,@Param("endTime") String endTime);
	Integer queryUserByRole(Integer role);
	User getUserByOpenid(String openid);
	List<User> queryExtendUserInfo(@Param("currentTime")String currentTime, @Param("extendCode")Integer extendCode, @Param("endTime")String endTime);

	List<User> selectUserByCategoryName(String categoryName);//通过工种查询人
	
	List<User> queryUserFollowerByTerm(RequestVo requestVo);//多条件查询用户OpenId
	
	void updateUserAppUserTokenByUnionid(@Param("unionid")String unionid,@Param("appUserToken")String appUserToken);
	int updateUservalidateMessage(Integer userId);
	void updateUserInfoAsyncByUnionid(@Param("lastIp")String lastIp,@Param("unionid")String unionid,@Param("loginChannel")String loginChannel);
	List<User> queryExprotUserList(ExprotVo exprotVo);
	
	User queryUserByEasemobName(@Param("easemobId")String easemobId,@Param("userId")String userId);//根据环信ID查询用户头像和姓名
	
	List<Map<String,Object>> findUserCount();
}