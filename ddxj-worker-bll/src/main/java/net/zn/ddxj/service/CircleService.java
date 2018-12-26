package net.zn.ddxj.service;

import java.util.List;

import net.zn.ddxj.entity.Circle;
import net.zn.ddxj.entity.CircleComment;
import net.zn.ddxj.entity.CircleImage;
import net.zn.ddxj.entity.CircleLaud;
import net.zn.ddxj.vo.CmsRequestVo;

public interface CircleService {
	
	List<Circle> queryCircleAllRecord(int userId,String city);//查询所有圈子动态
	
	CircleLaud queryCircleLaudById(int userId,int circleId);//查询点赞信息
	
	int updateCircleLaudById(int laudId,int userId,int flag);//更新点赞信息
	
	int insertCircleLaud(CircleLaud laud);//添加点赞信息
	
	Circle findCircleDetail(int userId,int circleId);//查询圈子动态详情
	
	List<CircleComment> queryCircleCommentList(int circleId);//查询圈子评论列表
	
	List<Circle> queryMyCircleRecord(int userId);//查询我发布的所有圈子动态
	
	int deleteMyCircleRecord(int userId,int circleId);//根据ID删除圈子动态
	
	int addCircleRecord(Circle circle);//发布圈子动态
	
	int findCircleByNumber(int userId,String number);//根据Number查询圈子
	
	int addCircleImageByCircle(CircleImage image);//根据圈子ID添加圈子图片
	
	int addCircleComment(CircleComment comment);//添加圈子动态评论

	int deleteCricle(Integer userId);//删除圈子
	
	int deleteCricleById(Integer id);//删除圈子

	int deleteCricleComment(Integer userId);//删除圈子评论
	
	int deleteCommentByCircleId(Integer circleId);//根据圈子删除圈子评论

	int deleteCricleLaud(Integer userId);//删除圈子点赞

	int deleteCricleImage(Integer userId);//删除圈子图片

	int delUserCircleLaud(Integer userId);//圈子点赞

	int delUserCircleComment(Integer userId);//圈子评论

	int delUserCircleImage(Integer userId);//圈子图片

	int delUserCircle(Integer userId);//圈子
	
	List<Circle> queryCircleList(CmsRequestVo requestVo);//多条件查询圈子列表

	Circle queryCircleById(Integer id);
}
