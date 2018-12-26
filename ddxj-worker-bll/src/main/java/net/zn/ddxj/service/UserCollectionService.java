package net.zn.ddxj.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.Information;
import net.zn.ddxj.entity.UserCollection;
import net.zn.ddxj.vo.RequestVo;

public interface UserCollectionService {
    int deleteByPrimaryKey(Integer id);

    int insert(UserCollection record);

    int insertSelective(UserCollection record);

    UserCollection selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserCollection record);

    int updateByPrimaryKey(UserCollection record);
    
    /**
     * 查询是否关注过用户
     * @param fromUserId
     * @param toUserId
     * @return
     */
    int selectByUserIdAndToUserId(Integer fromUserId,Integer toUserId);
    
    UserCollection queryUserIsCollection(@Param("userId")int userId,@Param("recruitId")int recruitId);//查询是否有收藏记录
    
    /**
     * 查询是否有关注记录
     * @param fromUserId
     * @param toUserId
     * @return
     */
    UserCollection queryUserIsFollow(Integer fromUserId,Integer toUserId);
    
    /**
     * 更新用户关注状态
     * @param collectionId
     * @param userId
     * @param flag
     * @return
     */
    int updateUserFollowById(int collectionId,int userId,int flag);
    
    /**
     * 查询收藏工作数量
     * @param userId
     * @return
     */
    int selectRecruitCount(Integer userId);
    
    
    /**
     * 查询关注工头数量
     * @param userId
     * @return
     */
    int selectUserCount(Integer userId);

	int deleteUserCollection(Integer userId);//删除关注,收藏

	int  delUserCollectionRecord(Integer userId);

	UserCollection queryInformationCollection(Integer userId,Integer infoId);//查询是否关注资讯
	
	int updateInformationCollection(int collectionId,int userId,int flag);//更新关注资讯状态
	
	List<Information> queryCollectionInformation(RequestVo requestVo);//查询所有收藏的资讯

}
