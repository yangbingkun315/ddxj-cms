package net.zn.ddxj.mapper;

import java.util.List;

import net.zn.ddxj.entity.Circle;
import net.zn.ddxj.vo.CmsRequestVo;

import org.apache.ibatis.annotations.Param;

public interface CircleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Circle record);

    int insertSelective(Circle record);

    Circle selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Circle record);

    int updateByPrimaryKey(Circle record);
    
    List<Circle> queryCircleAllRecord(@Param("userId")int userId,@Param("city")String city);//查询所有圈子动态
    
    Circle findCircleDetail(@Param("userId")int userId,@Param("circleId")int circleId);//查询圈子动态详情
    
    List<Circle> queryMyCircleRecord(int userId);//查询我发布的所有圈子动态
    
    int deleteMyCircleRecord(@Param("userId")int userId,@Param("circleId")int circleId);//根据ID删除圈子动态
    
    int findCircleByNumber(@Param("userId")int userId,@Param("number")String number);//根据Number查询圈子

	int deleteCricle(Integer userId);//删除圈子

	int delUserCircle(Integer userId);
	
	List<Circle> queryCircleList(CmsRequestVo requestVo);//多条件查询圈子列表
	
	int findCircleCount();
}