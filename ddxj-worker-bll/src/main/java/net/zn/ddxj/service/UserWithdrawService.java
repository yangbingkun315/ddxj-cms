package net.zn.ddxj.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import net.zn.ddxj.base.ResponseBase;
import net.zn.ddxj.entity.UserWithdraw;
import net.zn.ddxj.vo.RequestVo;

@Service
public interface UserWithdrawService {
	ResponseBase addWithdrawalsInfo(UserWithdraw userWithdraw);
	UserWithdraw selectByPrimaryKey(Integer id);//通过提现编号查询提现信息
	List<UserWithdraw> queryWithdrawRecord(RequestVo requestVo);
	int updateStatus(Integer id, Integer withdrawStatus,Integer withdrawProcess);
	int delUserWithdrawRecord(Integer userId);
	int updateWithdrawalsInfo(UserWithdraw userWithdraw);//修改
	BigDecimal totlaFrozenAmount(int userId);//统计冻结金额
}
