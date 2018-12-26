package net.zn.ddxj.mapper;

import java.util.List;

import net.zn.ddxj.entity.Information;
import net.zn.ddxj.entity.UserCollection;
import net.zn.ddxj.vo.RequestVo;

import org.apache.ibatis.annotations.Param;

public interface UserCollectionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserCollection record);

    int insertSelective(UserCollection record);

    UserCollection selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserCollection record);

    int updateByPrimaryKey(UserCollection record);
    
    int selectByUserIdAndToUserId(@Param("fromUserId")Integer fromUserId,@Param("toUserId")Integer toUserId);
    
    UserCollection queryUserIsCollection(@Param("userId")int userId,@Param("recruitId")int recruitId);//查询是否有收藏记录
    
    UserCollection queryUserIsFollow(@Param("fromUserId")Integer fromUserId,@Param("toUserId")Integer toUserId);
    
    int updateUserFollowById(@Param("collectionId")int collectionId,@Param("userId")int userId,@Param("flag")int flag);//更新用户关注状态
    
    int selectRecruitCount(Integer userId);//收藏的活动数量
    
    int selectUserCount(Integer userId);//收藏的工头数量

	int deleteUserCollection(Integer userId);//删除收藏,关注

	int delUserCollectionRecord(Integer userId);
	
	UserCollection queryInformationCollection(@Param("userId")Integer userId,@Param("infoId")Integer infoId);//查询是否关注资讯
	
	int updateInformationCollection(@Param("collectionId")int collectionId,@Param("userId")int userId,@Param("flag")int flag);//更新关注资讯状态
	
	List<Information> queryCollectionInformation(RequestVo requestVo);//查询所有收藏的资讯
	
}