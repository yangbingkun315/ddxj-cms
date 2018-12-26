package net.zn.ddxj.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.zn.ddxj.entity.CommentLabel;
import net.zn.ddxj.entity.UserComment;
import net.zn.ddxj.mapper.CommentLabelMapper;
import net.zn.ddxj.mapper.UserCommentLabelMapper;
import net.zn.ddxj.mapper.UserCommentMapper;
import net.zn.ddxj.service.UserCommentService;

@Service
@Component
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
public class UserCommentServiceImpl implements UserCommentService {
	@Autowired
	private UserCommentMapper userCommentMapper;
	@Autowired
	private UserCommentLabelMapper userCommentLabelMapper;
	@Autowired
	private CommentLabelMapper commentLabelMapper;

	@Override
	public List<UserComment> queryUserComment(Integer userId) {
		// TODO Auto-generated method stub
		return userCommentMapper.queryUserComment(userId);
	}

	@Override
	public int countComment(Integer userId) {
		// TODO Auto-generated method stub
		return userCommentMapper.countComment(userId);
	}

	@Override
	public String countCommentScores(Integer userId) {
		// TODO Auto-generated method stub
		return userCommentMapper.countCommentsScore(userId);
	}

	@Override
	public int addComment(UserComment comment) {
		// TODO Auto-generated method stub
		return userCommentMapper.insertSelective(comment);
	}
	
	@Override
	public List<CommentLabel> queryCommentLabel(Integer workerType) {
		// TODO Auto-generated method stub
		return commentLabelMapper.queryCommentLabel(workerType);
	}

	@Override
	public int addUserCommentLabel(Map<String, Object> value) {
		// TODO Auto-generated method stub
		return userCommentLabelMapper.insertUserCommentLabel(value);
	}

	@Override
	public int queryUserCommentId(int fromUserId,int toUserId,int recruitId) {
		// TODO Auto-generated method stub
		return userCommentMapper.queryUserCommentId(fromUserId, toUserId, recruitId);
	}

	@Override
	public int deleteUserComment(Integer userId) {
		// TODO Auto-generated method stub
		return userCommentMapper.deleteUserComment(userId);
	}

	@Override
	public int delUserCommentLabel(Integer userId) {
		// TODO Auto-generated method stub
		return userCommentLabelMapper.delUserCommentLabel(userId);
	}

	@Override
	public int delUserCommentRecord(Integer userId) {
		// TODO Auto-generated method stub
		return userCommentMapper.delUserCommentRecord(userId);
	}

}
