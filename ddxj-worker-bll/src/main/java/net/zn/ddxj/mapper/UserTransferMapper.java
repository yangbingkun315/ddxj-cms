package net.zn.ddxj.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.UserTransfer;
import net.zn.ddxj.vo.RequestVo;

public interface UserTransferMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserTransfer record);

    int insertSelective(UserTransfer record);

    UserTransfer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserTransfer record);

    int updateByPrimaryKey(UserTransfer record);

	int addUserWithdraw(UserTransfer userTransfer);//添加提现记录到转账记录表

	List<UserTransfer> queryCapitalChangeRecord(Integer userId);//查询资金变动列表
	
	List<UserTransfer> queryCapitalChangeRecordByType(@Param("userId")Integer userId,@Param("transferType")Integer transferType);
	
	UserTransfer querySettlementAndTransfer(@Param("transferId")Integer transferId,@Param("transferType")Integer transferType);//查询转账,结算记录
	
	BigDecimal totalMoneyByType(@Param("userId")int userId,@Param("type")int type,@Param("toUser")int toUser);//查询所有的资金变动
	BigDecimal sumMoneyByTypeAndToday(@Param("userId")int userId,@Param("type")int type,@Param("toUser")int toUser);//查询今天的资金变动
	BigDecimal sumMoneyByTypeAndWeek(@Param("userId")int userId,@Param("type")int type,@Param("toUser")int toUser);//查询本周的资金变动
	BigDecimal sumMoneyByTypeAndMonth(@Param("userId")int userId,@Param("type")int type,@Param("toUser")int toUser);//查询本月的资金变动
	Map<String, BigDecimal> queryUserToUserMoneyLog(@Param("userId")int userId);//查询收入资金记录

	List<UserTransfer> queryAllTransfer(RequestVo requestVo);
	
	List<UserTransfer> queryPayrollList(RequestVo requestVo);//查询结算记录

	List<UserTransfer> queryTransferLists(RequestVo requestVo);

	List<UserTransfer> selectWithdrawList(RequestVo requestVo);

	List<UserTransfer> querypaymentRecordList(RequestVo requestVo);

	List<UserTransfer> queryUserTransferDetails(Map<String, Object> param);//查询转账详情

	int deleteUserTransfer(Integer userId);//删除转账记录表

	int delUserTransferRecord(Integer userId);
	
	UserTransfer selectByWithdrawId(Integer withdrawId);

	int countUserTransferTimes(@Param("userId")Integer userId, @Param("transferType")Integer transferType);//查询转账次数

	BigDecimal totalUserTransferMoney(@Param("userId")Integer userId, @Param("transferType")Integer transferType);//统计转账金额

	List<UserTransfer> findUserTransferRecord(Integer userId);//查询用户转账记录
	
	BigDecimal findRegisterAwardMoneyCount();
	
	UserTransfer selectByOutTradeNo(String outTradeNo);//根据订单编号查找记录
}