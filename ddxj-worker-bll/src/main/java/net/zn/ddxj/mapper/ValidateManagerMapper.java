package net.zn.ddxj.mapper;

import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.ValidateManager;

public interface ValidateManagerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ValidateManager record);

    int insertSelective(ValidateManager record);

    ValidateManager selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ValidateManager record);

    int updateByPrimaryKey(ValidateManager record);
    
    int queryValidateDayCount(@Param("userId")Integer userId,@Param("type")String type);

	int delValidateManager(Integer userId);//删除认证信息
}