package net.zn.ddxj.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.RecruitRecord;

public interface RecruitRecordMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(RecruitRecord record);

	int insertSelective(RecruitRecord record);

	RecruitRecord selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(RecruitRecord record);

	int updateByPrimaryKey(RecruitRecord record);

	RecruitRecord queryWorkerDeliver(@Param("recruitId")Integer recruitId,@Param("userId")Integer userId);// 根据活动ID,工人ID 查询报名状态
	
	RecruitRecord queryWorker(@Param("recruitId")Integer recruitId,@Param("userId")Integer userId);
	
	int updateWorkerStatus(@Param("userId")Integer userId,@Param("recruitId")Integer recruitId,@Param("enlistStatus")Integer enlistStatus);
	
	RecruitRecord queryAllWorkerDeliver(@Param("recruitId")Integer recruitId,@Param("userId")Integer userId);// 根据活动ID,工人ID 查询报名状态
	
	int selectByUserId(Integer userId);//根据工人ID查询报名数量
	
	int selectByUserFlag(Integer userId);//根据工人ID查询报名数量
	
	int selectByUserIdAndRecruitId(Integer recruitId);
	
	int queryUserJobStatusCount(Integer userId);
	
	int updateBlanceStatus(@Param("userId")Integer userId,@Param("recruitId")Integer recruitId,@Param("balanceStatus")Integer balanceStatus);//更新结算状态
	
	List<RecruitRecord> queryRecruitUserList(@Param("recruitId")int recruitId);// 查询活动报名情况
	
	int updateRecordFlag(int recordId);
	
	int updateByRecordId(@Param("recordId")Integer recordId,@Param("enlistStatus")Integer enlistStatus,@Param("refuseCause")String refuseCause);

	RecruitRecord queryEnlistUserDetails(@Param("recruitId")Integer recruitId,@Param("userId")Integer userId);
	int countSingUp(Integer recruitId);//统计通过的人数

	int updateUserSignStatus(Integer userId);//更新申请工头后的活动状态

	int delRecruitRecord(Integer userId);

	RecruitRecord queryWorkerConduct(Integer userId);

	
}