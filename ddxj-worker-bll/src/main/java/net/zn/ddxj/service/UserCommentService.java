package net.zn.ddxj.service;

import java.util.List;
import java.util.Map;

import net.zn.ddxj.entity.CommentLabel;
import net.zn.ddxj.entity.UserComment;

public interface UserCommentService {
	
	List<UserComment> queryUserComment(Integer userId);// 根据被评论用户ID，查询用户评论列表

	int countComment(Integer userId);// 根据被评论用户ID,计算评论总次数、评分
	
	String countCommentScores(Integer userId);// 根据被评论用户ID,计算评论的总分数
	
	int addComment(UserComment comment);// 新增评论
	
	List<CommentLabel> queryCommentLabel(Integer workerType);//用户类型
	
	int addUserCommentLabel(Map<String, Object> value);// 新增评论标签
	
	int queryUserCommentId(int fromUserId,int toUserId,int recruitId);// 查询评论

	int deleteUserComment(Integer userId);//删除评论
	int delUserCommentLabel(Integer userId);//删除用户评论标签

	int delUserCommentRecord(Integer userId);
}
