package net.zn.ddxj.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.CreditContactsImage;
import net.zn.ddxj.entity.CreditUrgentContacts;
import net.zn.ddxj.entity.RecruitRecord;

public interface RecruitRecordService {
	RecruitRecord queryWorkerDeliver(@Param("recruitId")Integer recruitId,@Param("userId")Integer userId);// 根据活动ID,工人ID 查询报名状态
	
	RecruitRecord queryWorker(Integer recruitId,Integer userId);
	
	/**
	 * 更新报名状态
	 * @param userId
	 * @param recruitId
	 * @param enlistStatus
	 * @return
	 */
	int updateWorkerStatus(Integer userId,Integer recruitId,Integer enlistStatus);
	
	int deleteByPrimaryKey(Integer id);

	int insert(RecruitRecord record);

	int insertSelective(RecruitRecord record);

	RecruitRecord selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(RecruitRecord record);

	int updateByPrimaryKey(RecruitRecord record);
	
	int updateRecordFlag(int recordId);
	
	int updateByRecordId(Integer recordId,Integer enlistStatus,String refuseCause);
	
	/**
	 * 根据工人ID查询报名数量
	 * @param userId
	 * @return
	 */
	int selectByUserId(Integer userId);
	
	int selectByUserFlag(Integer userId);
	
	int updateBlanceStatus(@Param("userId")Integer userId,@Param("recruitId")Integer recruitId,@Param("balanceStatus")Integer balanceStatus);//更新结算状态
	
	int insertImages(CreditContactsImage creditContactsImage);//添加合同照片
	
	int insertUrgent(CreditUrgentContacts contacts);//添加紧急联系人
	
	List<RecruitRecord> queryRecruitUserList(Integer recruitId);// 查询活动报名情况

	RecruitRecord  queryEnlistUserDetails(Integer recruitId,Integer userId);//查询报名详情
	
	RecruitRecord queryAllWorkerDeliver(Integer recruitId,Integer userId);// 根据活动ID,工人ID 查询全部报名状态
	
	int countSingUp(Integer recruitId);//统计通过的人数

	int updateUserSignStatus(Integer userId);//更新报名状态

	int delRecruitRecord(Integer userId);

	int delCreditContactsImageRecord(Integer userId);

	int delCreditUrgentContactRecord(Integer userId);

	RecruitRecord queryWorkerConduct(Integer userId);//查询进行中的活动

	int deleteImages(Integer id);//删除授信图片根据项目授信id

	int deleteUrgent(Integer id);//删除授信紧急联系人根据项目id

	RecruitRecord queryRecruitRecord(Integer id);//查询报名人的信息
}
