package net.zn.ddxj.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.Notice;
import net.zn.ddxj.entity.Recruit;
import net.zn.ddxj.entity.RecruitBanner;
import net.zn.ddxj.entity.RecruitCategory;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

public interface RecruitService {
	
	List<Recruit> queryMyReleaseWork(Integer fromUserId,Integer toUserId);//查询我发布的招聘，是否被邀请过
	
	Integer queryUserRecruitPersonCount(Integer userId);//查询工头在招职位数量
	
	List<Recruit> queryUserRecruitPersonList(Integer userId);//查询工头在招职位数量

	List<Recruit> queryAllRecruitLists(Map<String, Object> params);//查询招聘信息
	
	List<Recruit> queryMyNearRecruit(String provice,String longitude,String latitude);//查询附近的工作
	
	int findRecruitPersonById(int recruitId);//根据ID查询此项目已招人数
	
	int findRecruitByUserId(int userId);//根据ID查询工人进行中项目 
	
	int updateUserCollectionById(int collectionId, int userId, int flag);//更新用户收藏状态
	
	int insertUserCollection(int userId, int recruitId);//生成收藏记录
	
	List<Recruit> queryMyRecruitList(int userId,int recruitStatus);//查询我发布的招聘
	
	String queryRecruitReason(int userId,int recruitId);//查询招聘审核失败原因
	
	int queryRecruitByUserId(int userId);//查询用户发布的招聘数量
	
	int addRecruit(Recruit recruit);//添加招聘信息
	
	int updateRecruit(Recruit recruit);//修改招聘信息
	
	int addRecruitCategory(RecruitCategory category);//添加招聘分类
	
	int addRecruitBanner(RecruitBanner banner);//添加招聘轮播图
	
	int deleteRecruitBanner(int recruitId);//删除招聘图片
	
	int deleteRecruitCategory(int recruitId);//删除招聘分类
	
	/**
	 * 查询招聘详情
	 * @param id
	 * @return
	 */
    Recruit selectByPrimaryKey(Integer id);
    
    /**
     * 取消招聘
     * @param recruitId
     * @param userId
     * @return
     */
	int updateRecruitCancelById(Integer recruitId,Integer userId);
	
	/**
	 * 更新截止时间
	 * @param recruitId
	 * @param userId
	 * @param stopTime
	 * @return
	 */
	int updateEndTimeById(Integer recruitId,Integer userId,Date stopTime);
	/**
	 * 查询我收到的职位邀请
	 * @param userId
	 * @return
	 */
	List<Recruit> queryMyReceivedRecruitList(Integer userId);
	
	int updateByRecruitId(Integer recruitId ,Integer validateStatus,String validateCause,Integer recruitStatus);//更新审核状态	
	int deleteByRecruitId(Integer recruitId);//删除招聘记录
	List<Recruit> findRecruitList(CmsRequestVo requestVo);
	int selectRecruitCount(Integer userId,Integer validateStatus);
	
	int selectRecruitStatusCount(@Param("userId")Integer userId,@Param("recruitStatus")Integer recruitStatus);//查询进行中招聘的活动数量
	
	int selectRecruitStatusOver(@Param("userId")Integer userId,@Param("recruitStatus")Integer recruitStatus);//查询已完成招聘的活动数量
	
	List<Recruit> queryCollectionList(Integer userId);//查询收藏的活动列表
	
	List<Recruit> queryApplyList(Integer userId);//查询报名的活动列表

	int delRecruitCategoryRecord(Integer userId);

	int delRecruitBannerRecord(Integer userId);

	int delUserRecruitRecord(Integer userId);

	List<Recruit> selectRecruitUnderway(Integer userId);

	Recruit queryRecruitDetail(Integer id);
	
	int deleteByNoticeId(Integer noticeId);//删除公告
	
	List<Notice> findNoticeList(CmsRequestVo requestVo);
	
	Notice findNoticeById(Integer noticeId);
	
	int updateNotice(Notice notice);
	
	int insertNoticeSelective(Notice notice);
	
	/**
	 * 查询正在轮询的公告
	 * @return
	 */
	List<Notice> queryNoticeNow(Integer type);

	int changeRecruitStick(Integer id, Integer type);//招聘置顶

	Notice queryByNoticeId(Integer id);//根据公告ID查询公告
}
