package net.zn.ddxj.mapper;

import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.CircleLaud;

public interface CircleLaudMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CircleLaud record);

    int insertSelective(CircleLaud record);

    CircleLaud selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CircleLaud record);

    int updateByPrimaryKey(CircleLaud record);
    
    CircleLaud queryCircleLaudById(@Param("userId")int userId,@Param("circleId")int circleId);//查询点赞信息
    
    int updateCircleLaudById(@Param("laudId")int laudId,@Param("userId")int userId,@Param("flag")int flag);//更新点赞信息

	int deleteCricleLaud(Integer userId);

	int delUserCircleLaud(Integer userId);
	
	int deleteCricleLaudById(Integer circleId);
}