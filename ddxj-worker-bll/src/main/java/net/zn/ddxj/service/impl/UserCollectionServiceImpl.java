package net.zn.ddxj.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.zn.ddxj.entity.Information;
import net.zn.ddxj.entity.UserCollection;
import net.zn.ddxj.mapper.UserCollectionMapper;
import net.zn.ddxj.service.UserCollectionService;
import net.zn.ddxj.vo.RequestVo;

@Service
@Component
@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
public class UserCollectionServiceImpl implements UserCollectionService{
	@Autowired
	private UserCollectionMapper userCollectionMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(UserCollection record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelective(UserCollection record) {
		// TODO Auto-generated method stub
		return userCollectionMapper.insertSelective(record);
	}

	@Override
	public UserCollection selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(UserCollection record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateByPrimaryKey(UserCollection record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int selectByUserIdAndToUserId(Integer fromUserId, Integer toUserId) {
		return userCollectionMapper.selectByUserIdAndToUserId(fromUserId, toUserId);
	}

	@Override
	public UserCollection queryUserIsCollection(int userId, int recruitId) {
		// TODO Auto-generated method stub
		return userCollectionMapper.queryUserIsCollection(userId, recruitId);
	}

	@Override
	public UserCollection queryUserIsFollow(Integer fromUserId, Integer toUserId) {
		return userCollectionMapper.queryUserIsFollow(fromUserId, toUserId);
	}

	@Override
	public int updateUserFollowById(int collectionId, int userId, int flag) {
		return userCollectionMapper.updateUserFollowById(collectionId, userId, flag);
	}

	@Override
	public int selectRecruitCount(Integer userId) {
		return userCollectionMapper.selectRecruitCount(userId);
	}

	@Override
	public int selectUserCount(Integer userId) {
		return userCollectionMapper.selectUserCount(userId);
	}

	@Override
	public int deleteUserCollection(Integer userId) {
		// TODO Auto-generated method stub
		return userCollectionMapper.deleteUserCollection(userId);
	}

	@Override
	public int delUserCollectionRecord(Integer userId) {
		// TODO Auto-generated method stub
		return userCollectionMapper.delUserCollectionRecord(userId);
	}

	@Override
	public UserCollection queryInformationCollection(Integer userId,
			Integer infoId) {
		// TODO Auto-generated method stub
		return userCollectionMapper.queryInformationCollection(userId, infoId);
	}

	@Override
	public int updateInformationCollection(int collectionId, int userId,
			int flag) {
		// TODO Auto-generated method stub
		return userCollectionMapper.updateInformationCollection(collectionId, userId, flag);
	}

	@Override
	public List<Information> queryCollectionInformation(RequestVo requestVo) {
		// TODO Auto-generated method stub
		return userCollectionMapper.queryCollectionInformation(requestVo);
	}
	
}
