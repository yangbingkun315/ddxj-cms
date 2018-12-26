package net.zn.ddxj.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.zn.ddxj.entity.Notice;
import net.zn.ddxj.entity.Recruit;
import net.zn.ddxj.entity.RecruitBanner;
import net.zn.ddxj.entity.RecruitCategory;
import net.zn.ddxj.mapper.NoticeMapper;
import net.zn.ddxj.mapper.RecruitBannerMapper;
import net.zn.ddxj.mapper.RecruitCategoryMapper;
import net.zn.ddxj.mapper.RecruitMapper;
import net.zn.ddxj.service.RecruitService;
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
public class RecruitServiceImpl implements RecruitService{
	@Autowired
	private RecruitMapper recruitMapper;
	@Autowired
	private RecruitCategoryMapper recruitCategoryMapper;
	@Autowired
	private RecruitBannerMapper recruitBannerMapper;
	@Autowired
	private NoticeMapper noticeMapper;

	@Override
	public Integer queryUserRecruitPersonCount(Integer userId) {
		// TODO Auto-generated method stub
		return recruitMapper.queryUserRecruitPersonCount(userId);
	}

	@Override
	public List<Recruit> queryMyReleaseWork(Integer fromUserId,Integer toUserId) {
		// TODO Auto-generated method stub
		return recruitMapper.queryMyReleaseWork(fromUserId,toUserId);
	}

	@Override
	public List<Recruit> queryAllRecruitLists(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return recruitMapper.queryAllRecruitLists(params);
	}

	@Override
	public int updateUserCollectionById(int collectionId, int userId, int flag) {
		// TODO Auto-generated method stub
		return recruitMapper.updateUserCollectionById(collectionId, userId, flag);
	}

	@Override
	public int insertUserCollection(int userId, int recruitId) {
		// TODO Auto-generated method stub
		return recruitMapper.insertUserCollection(userId, recruitId);
	}

	@Override
	public List<Recruit> queryMyRecruitList(int userId,int recruitStatus) {
		// TODO Auto-generated method stub
		return recruitMapper.queryMyRecruitList(userId, recruitStatus);
	}

	@Override
	public Recruit selectByPrimaryKey(Integer id) {
		return recruitMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateRecruitCancelById(Integer recruitId, Integer userId) {
		return recruitMapper.updateRecruitCancelById(recruitId, userId);
	}

	@Override
	public int updateEndTimeById(Integer recruitId, Integer userId, Date stopTime) {
		return recruitMapper.updateEndTimeById(recruitId, userId, stopTime);
	}

	@Override
	public String queryRecruitReason(int userId, int recruitId) {
		// TODO Auto-generated method stub
		return recruitMapper.queryRecruitReason(userId, recruitId);
	}

	@Override
	public List<Recruit> queryMyReceivedRecruitList(Integer userId) {
		// TODO Auto-generated method stub
		return recruitMapper.queryMyReceivedRecruitList(userId);
	}

	@Override
	public int queryRecruitByUserId(int userId) {
		// TODO Auto-generated method stub
		return recruitMapper.queryRecruitByUserId(userId);
	}

	@Override
	public int addRecruit(Recruit recruit) {
		// TODO Auto-generated method stub
		return recruitMapper.insertSelective(recruit);
	}

	@Override
	public int addRecruitCategory(RecruitCategory category) {
		// TODO Auto-generated method stub
		return recruitCategoryMapper.insertSelective(category);
	}

	@Override
	public int addRecruitBanner(RecruitBanner banner) {
		// TODO Auto-generated method stub
		return recruitBannerMapper.insertSelective(banner);
	}

	@Override
	public int updateRecruit(Recruit recruit) {
		// TODO Auto-generated method stub
		return recruitMapper.updateByPrimaryKeySelective(recruit);
	}

	@Override
	public int deleteRecruitBanner(int recruitId) {
		// TODO Auto-generated method stub
		return recruitBannerMapper.deleteByPrimaryKey(recruitId);
	}

	@Override
	public int deleteRecruitCategory(int recruitId) {
		// TODO Auto-generated method stub
		return recruitCategoryMapper.deleteRecruitCategory(recruitId);
	}

	@Override
	public int deleteByRecruitId(Integer recruitId) {
		// TODO Auto-generated method stub
		return recruitMapper.deleteByRecruitId(recruitId);
	}

	@Override
	public List<Recruit> findRecruitList(CmsRequestVo requestVo) {
		// TODO Auto-generated method stub
		return recruitMapper.findRecruitList(requestVo);
	}


	@Override
	public int selectRecruitCount(Integer userId, Integer validateStatus) {
		// TODO Auto-generated method stub
		return recruitMapper.selectRecruitCount(userId, validateStatus);
	}

	@Override
	public int selectRecruitStatusCount(Integer userId, Integer recruitStatus) {
		return recruitMapper.selectRecruitStatusCount(userId, recruitStatus);
	}

	@Override
	public int updateByRecruitId(Integer recruitId, Integer validateStatus, String validateCause,
			Integer recruitStatus) {
		// TODO Auto-generated method stub
		return recruitMapper.updateByRecruitId(recruitId, validateStatus, validateCause, recruitStatus);
	}

	@Override
	public List<Recruit> queryUserRecruitPersonList(Integer userId) {
		// TODO Auto-generated method stub
		return recruitMapper.queryUserRecruitPersonList(userId);
	}

	@Override
	public List<Recruit> queryCollectionList(Integer userId) {
		// TODO Auto-generated method stub
		return recruitMapper.queryCollectionList(userId);
	}

	@Override
	public List<Recruit> queryApplyList(Integer userId) {
		// TODO Auto-generated method stub
		return recruitMapper.queryApplyList(userId);
	}

	@Override
	public int findRecruitPersonById(int recruitId) {
		// TODO Auto-generated method stub
		return recruitMapper.findRecruitPersonById(recruitId);
	}

	@Override
	public int selectRecruitStatusOver(Integer userId, Integer recruitStatus) {
		// TODO Auto-generated method stub
		return recruitMapper.selectRecruitStatusOver(userId, recruitStatus);
	}

	@Override
	public int delRecruitCategoryRecord(Integer userId) {
		// TODO Auto-generated method stub
		return recruitCategoryMapper.delRecruitCategoryRecord(userId);
	}

	@Override
	public int delRecruitBannerRecord(Integer userId) {
		// TODO Auto-generated method stub
		return recruitBannerMapper.delRecruitBannerRecord(userId);
	}

	@Override
	public int delUserRecruitRecord(Integer userId) {
		// TODO Auto-generated method stub
		return recruitMapper.delUserRecruitRecord(userId);
	}

	@Override
	public List<Recruit> selectRecruitUnderway(Integer userId) {
		// TODO Auto-generated method stub
		return recruitMapper.selectRecruitUnderway(userId);
	}

	@Override
	public Recruit queryRecruitDetail(Integer id) {
		// TODO Auto-generated method stub
		return recruitMapper.selectByPrimaryKey(id);
	}

	@Override
	public int findRecruitByUserId(int userId) {
		// TODO Auto-generated method stub
		return recruitMapper.findRecruitByUserId(userId);
	}

	@Override
	public int deleteByNoticeId(Integer noticeId) {
		return noticeMapper.deleteByPrimaryKey(noticeId);
	}

	@Override
	public List<Notice> findNoticeList(CmsRequestVo requestVo) {
		return noticeMapper.findNoticeList(requestVo);
	}

	@Override
	public Notice findNoticeById(Integer noticeId) {
		// TODO Auto-generated method stub
		return noticeMapper.selectByPrimaryKey(noticeId);
	}

	@Override
	public int updateNotice(Notice notice) {
		// TODO Auto-generated method stub
		return noticeMapper.updateByPrimaryKeySelective(notice);
	}

	@Override
	public int insertNoticeSelective(Notice notice) {
		// TODO Auto-generated method stub
		return noticeMapper.insertSelective(notice);
	}

	@Override
	public List<Notice> queryNoticeNow(Integer type) {
		if(type == 1)
		{
			return noticeMapper.selectNoticeWorkerNow();
		}else
		{
			return noticeMapper.selectNoticeForeWorkerNow();
		}
	}

	@Override
	public int changeRecruitStick(Integer id, Integer type) {
		// TODO Auto-generated method stub
		return recruitMapper.changeRecruitStick(id,type);
	}

	@Override
	public List<Recruit> queryMyNearRecruit(String provice,String longitude,String latitude) {
		
		return recruitMapper.queryMyNearRecruit(provice,longitude,latitude);
	}

	@Override
	public Notice queryByNoticeId(Integer id) {
		// TODO Auto-generated method stub
		return noticeMapper.selectByPrimaryKey(id);
	}

}
