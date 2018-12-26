package net.zn.ddxj.mapper;

import java.util.List;

import net.zn.ddxj.entity.CircleComment;

public interface CircleCommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CircleComment record);

    int insertSelective(CircleComment record);

    CircleComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CircleComment record);

    int updateByPrimaryKey(CircleComment record);
    
    List<CircleComment> queryCircleCommentList(int circleId);//查询圈子评论列表
    
    List<CircleComment> queryCircleCommentParentList(Integer parentId);//查询圈子评论子类

	int deleteCricleComment(Integer userId);

	int delUserCircleComment(Integer userId);
	
	int deleteCricleCommentById(Integer cricleId);
	
}