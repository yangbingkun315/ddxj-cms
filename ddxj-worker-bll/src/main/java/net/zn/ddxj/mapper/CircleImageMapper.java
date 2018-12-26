package net.zn.ddxj.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.CircleImage;

public interface CircleImageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CircleImage record);

    int insertSelective(CircleImage record);

    CircleImage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CircleImage record);

    int updateByPrimaryKey(CircleImage record);
    
    List<CircleImage> getCircleImage(@Param("circleId")int id);

	int deleteCricleLaud(Integer userId);

	int deleteCricleImage(Integer userId);

	int delUserCircleImage(Integer userId);
}