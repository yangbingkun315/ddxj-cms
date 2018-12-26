package net.zn.ddxj.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.UserBank;

public interface UserBankMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserBank record);

    int insertSelective(UserBank record);

    UserBank selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserBank record);

    int updateByPrimaryKey(UserBank record);
    
    List<UserBank> findUserBankList(int userId);//查询已绑定银行卡
    
    UserBank findUserBank(@Param("userId")Integer userId ,@Param("bankCard")String bankCard);//根据卡号查询银行卡信息
    
    UserBank findUserBankByBankCard(String bankCard);

	int delUserBankRecord(Integer userId);

	int countUserBindingCard(Integer userId);

	int untieBankCard(@Param("userId")String userId ,@Param("bankCard")String bankCard);//解除绑卡
    
}