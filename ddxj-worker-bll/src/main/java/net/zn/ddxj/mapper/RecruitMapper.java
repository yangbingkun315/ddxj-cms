package net.zn.ddxj.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.zn.ddxj.entity.Recruit;
import net.zn.ddxj.vo.CmsRequestVo;

import org.apache.ibatis.annotations.Param;

public interface RecruitMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Recruit record);

    int insertSelective(Recruit record);

    Recruit selectByPrimaryKey(Integer id);
    
    Recruit queryRecruitByIdCut(Integer id);

    int updateByPrimaryKeySelective(Recruit record);

    int updateByPrimaryKey(Recruit record);

    List<Recruit> queryAllRecruitLists(Map<String, Object> params);//查询招聘信息
    
    List<Recruit> queryMyNearRecruit(@Param("provice")String provice,@Param("longitude")String longitude,@Param("latitude")String latitude);//查询附近的工作
    
    List<Recruit> queryMyReleaseWork(@Param("fromUserId")Integer fromUserId,@Param("toUserId")Integer toUserId);//查询我发布的招聘，是否被邀请过
	
	Integer queryUserRecruitPersonCount(Integer userId);//查询工头在招职位数量
	
	List<Recruit> queryUserRecruitPersonList(Integer userId);//查询工头在招职位数量
	
	int updateUserCollectionById(@Param("collectionId")int collectionId,@Param("userId")int userId,@Param("flag")int flag);//更新用户收藏状态
	
	int insertUserCollection(@Param("userId")int userId,@Param("recruitId")int recruitId);//生成收藏记录
	
	List<Recruit> queryMyRecruitList(@Param("userId")int userId,@Param("recruitStatus")int recruitStatus);//查询我发布的招聘
	
	int updateRecruitCancelById(@Param("recruitId")Integer recruitId,@Param("userId")Integer userId);
	
	int updateEndTimeById(@Param("recruitId")Integer recruitId,@Param("userId")Integer userId,@Param("stopTime")Date stopTime);
	
	String queryRecruitReason(@Param("userId")int userId,@Param("recruitId")int recruitId);//查询招聘审核失败原因
	
	List<Recruit> queryMyReceivedRecruitList(Integer userId);//查询我收到的职位邀请
	
	int queryRecruitByUserId(int userId);//查询用户发布的招聘数量
	
	List<Recruit> findRecruitList(CmsRequestVo requestVo);
	
	int updateByRecruitId(@Param("recruitId")Integer recruitId ,@Param("validateStatus")Integer validateStatus,@Param("validateCause")String validateCause,@Param("recruitStatus")Integer recruitStatus);//更新审核状态
	
	int deleteByRecruitId(Integer recruitId);//删除招聘记录
	
	int selectRecruitCount(@Param("userId")Integer userId,@Param("validateStatus")Integer validateStatus);//查询审核的活动数量
	
	int selectRecruitStatusCount(@Param("userId")Integer userId,@Param("recruitStatus")Integer recruitStatus);//查询进行中招聘的活动数量
	
	int selectRecruitStatusOver(@Param("userId")Integer userId,@Param("recruitStatus")Integer recruitStatus);//查询已完成招聘的活动数量
	
	List<Recruit> queryCollectionList(Integer userId);//查询收藏的活动列表
	
	List<Recruit> queryApplyList(Integer userId);//查询报名的活动列表
	
	int findRecruitPersonById(int recruitId);//根据ID查询此项目已招人数
	
	int findRecruitByUserId(int userId);//根据ID查询工人进行中项目 

	int delUserRecruitRecord(Integer userId);

	List<Recruit> selectRecruitUnderway(Integer userId);

	int changeRecruitStick(@Param("id")Integer id, @Param("type")Integer type);//招聘置顶
	
	List<Map<String,Object>> findRecruitCount();

}