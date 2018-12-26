package net.zn.ddxj.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.UserPassword;

@Mapper
public interface UserPasswordMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(UserPassword record);

	int insertSelective(UserPassword record);

	UserPassword selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(UserPassword record);

	int updateByPrimaryKey(UserPassword record);

	UserPassword selectPasswordByUserId(Integer userId);

	int updateUserPasswordByPhone(@Param("userId")Integer userId, @Param("password")String password);
	
	int queryLoginPasswordStatus(Integer userId);
	int queryPayPasswordStatus(Integer userId);

	int delUserPasswordRecord(Integer userId);
}