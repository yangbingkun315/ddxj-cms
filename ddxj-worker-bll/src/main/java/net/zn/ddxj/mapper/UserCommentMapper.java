package net.zn.ddxj.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.UserComment;

public interface UserCommentMapper {
	
    int deleteByPrimaryKey(Integer id);

    int insert(UserComment record);

    int insertSelective(UserComment record);

    UserComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserComment record);

    int updateByPrimaryKey(UserComment record);
    
    List<UserComment> queryUserComment(Integer userId);//根据被评论用户ID，查询用户评论列表

	int countComment(Integer userId);// 根据被评论用户ID,计算评论总次数

	Integer countCommentScore(Integer userId);// 根据被评论用户ID,计算评论的总分数
	
	String countCommentsScore(Integer userId);// 根据被评论用户ID,计算评论的总分数
	
	int queryUserCommentId(@Param("fromUserId")int fromUserId,@Param("toUserId")int toUserId,@Param("recruitId")int recruitId);// 查询评论

	int deleteUserComment(Integer userId);//删除评论

	int delUserCommentRecord(Integer userId);
}