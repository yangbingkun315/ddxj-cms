package net.zn.ddxj.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.UserWithdraw;
import net.zn.ddxj.vo.RequestVo;

public interface UserWithdrawMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserWithdraw record);

    int insertSelective(UserWithdraw record);

    UserWithdraw selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserWithdraw record);

    int updateByPrimaryKey(UserWithdraw record);

	List<UserWithdraw> queryWithdrawRecord(RequestVo requestVo);
	
	int updateStatus(@Param("id")Integer id,@Param("withdrawStatus")Integer withdrawStatus,@Param("withdrawProcess")Integer withdrawProcess);

	int delUserWithdrawRecord(Integer userId);

	BigDecimal totlaFrozenAmount(int userId);//统计冻结资金
	
	BigDecimal findWithdrawSuccessCount();
	
    
}