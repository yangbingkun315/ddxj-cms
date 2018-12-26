package net.zn.ddxj.service.impl;

import java.util.List;

import net.zn.ddxj.entity.Circle;
import net.zn.ddxj.entity.CircleComment;
import net.zn.ddxj.entity.CircleImage;
import net.zn.ddxj.entity.CircleLaud;
import net.zn.ddxj.mapper.CircleCommentMapper;
import net.zn.ddxj.mapper.CircleImageMapper;
import net.zn.ddxj.mapper.CircleLaudMapper;
import net.zn.ddxj.mapper.CircleMapper;
import net.zn.ddxj.service.CircleService;
import net.zn.ddxj.vo.CmsRequestVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Component
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
public class CircleServiceImpl implements CircleService{
	
	@Autowired
	private CircleMapper circleMapper;
	@Autowired
	private CircleLaudMapper circleLaudMapper;
	@Autowired
	private CircleCommentMapper circleCommentMapper;
	@Autowired
	private CircleImageMapper circleImageMapper;

	@Override
	public List<Circle> queryCircleAllRecord(int userId, String city) {
		// TODO Auto-generated method stub
		return circleMapper.queryCircleAllRecord(userId, city);
	}

	@Override
	public CircleLaud queryCircleLaudById(int userId, int circleId) {
		// TODO Auto-generated method stub
		return circleLaudMapper.queryCircleLaudById(userId, circleId);
	}

	@Override
	public int updateCircleLaudById(int laudId, int userId, int flag) {
		// TODO Auto-generated method stub
		return circleLaudMapper.updateCircleLaudById(laudId, userId, flag);
	}

	@Override
	public int insertCircleLaud(CircleLaud laud) {
		// TODO Auto-generated method stub
		return circleLaudMapper.insertSelective(laud);
	}

	@Override
	public Circle findCircleDetail(int userId, int circleId) {
		// TODO Auto-generated method stub
		return circleMapper.findCircleDetail(userId, circleId);
	}

	@Override
	public List<CircleComment> queryCircleCommentList(int circleId) {
		// TODO Auto-generated method stub
		return circleCommentMapper.queryCircleCommentList(circleId);
	}

	@Override
	public List<Circle> queryMyCircleRecord(int userId) {
		// TODO Auto-generated method stub
		return circleMapper.queryMyCircleRecord(userId);
	}

	@Override
	public int deleteMyCircleRecord(int userId,int circleId) {
		// TODO Auto-generated method stub
		return circleMapper.deleteMyCircleRecord(userId,circleId);
	}

	@Override
	public int addCircleRecord(Circle circle) {
		// TODO Auto-generated method stub
		return circleMapper.insertSelective(circle);
	}

	@Override
	public int findCircleByNumber(int userId,String number) {
		// TODO Auto-generated method stub
		return circleMapper.findCircleByNumber(userId,number);
	}

	@Override
	public int addCircleImageByCircle(CircleImage image) {
		// TODO Auto-generated method stub
		return circleImageMapper.insertSelective(image);
	}

	@Override
	public int addCircleComment(CircleComment comment) {
		// TODO Auto-generated method stub
		return circleCommentMapper.insertSelective(comment);
	}

	@Override
	public int deleteCricle(Integer userId) {
		// TODO Auto-generated method stub
		return circleMapper.deleteCricle(userId);
	}

	@Override
	public int deleteCricleComment(Integer userId) {
		// TODO Auto-generated method stub
		return circleCommentMapper.deleteCricleComment(userId);
	}

	@Override
	public int deleteCricleLaud(Integer userId) {
		// TODO Auto-generated method stub
		return circleLaudMapper.deleteCricleLaud(userId);
	}

	@Override
	public int deleteCricleImage(Integer userId) {
		// TODO Auto-generated method stub
		return circleImageMapper.deleteCricleImage(userId);
	}

	@Override
	public int delUserCircleLaud(Integer userId) {
		// TODO Auto-generated method stub
		return circleLaudMapper.delUserCircleLaud(userId);
	}

	@Override
	public int delUserCircleComment(Integer userId) {
		// TODO Auto-generated method stub
		return circleCommentMapper.delUserCircleComment(userId);
	}

	@Override
	public int delUserCircleImage(Integer userId) {
		// TODO Auto-generated method stub
		return circleImageMapper.delUserCircleImage(userId);
	}

	@Override
	public int delUserCircle(Integer userId) {
		// TODO Auto-generated method stub
		return circleMapper.delUserCircle(userId);
	}

	@Override
	public List<Circle> queryCircleList(CmsRequestVo requestVo) {
		// TODO Auto-generated method stub
		return circleMapper.queryCircleList(requestVo);
	}

	@Override
	public int deleteCricleById(Integer id) {
		// TODO Auto-generated method stub
		circleCommentMapper.deleteCricleCommentById(id);
		circleLaudMapper.deleteCricleLaudById(id);
		return circleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int deleteCommentByCircleId(Integer circleId) {
		// TODO Auto-generated method stub
		return circleCommentMapper.deleteCricleCommentById(circleId);
	}

	@Override
	public Circle queryCircleById(Integer id) {
		// TODO Auto-generated method stub
		return circleMapper.selectByPrimaryKey(id);
	}
	
}
