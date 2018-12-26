package net.zn.ddxj.mapper;

import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.InviteRecord;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

public interface InviteRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(InviteRecord record);

    int insertSelective(InviteRecord record);

    InviteRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InviteRecord record);

    int updateByPrimaryKey(InviteRecord record);
    
    /**
     * 根据openId 和状态查询
     * @param openId
     * @param status 可为null单独根据openId查询
     * @return
     */
    InviteRecord selectByOpenId(@Param("openId") String openId,@Param("status") Integer status);
    
    /**
     * 查询老用户邀请记录
     * @param userId 邀请者Id
     * @return
     */
	List<InviteRecord> queryInviteRecord(Integer userId);
	
	/**
	 * 查询老用户获取的邀请奖励金总额
	 * @param userId
	 * @return
	 */
	BigDecimal queryInviteBonusCount(Integer userId);
	
	/**
	 * 查询老用户邀请多少注册用户记录数
	 * @param userId
	 * @return
	 */
	int queryInviteRecordCount(Integer userId);
	
	   /**
     * 查询老用户邀请记录
     * @param userId 邀请者Id
     * @return
     */
	List<InviteRecord> selectInviteRecord(RequestVo requestVo);
	
	List<InviteRecord> queryInviteRecordCms(CmsRequestVo requestVo);
}