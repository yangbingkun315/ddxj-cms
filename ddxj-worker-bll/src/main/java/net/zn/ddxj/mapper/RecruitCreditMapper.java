package net.zn.ddxj.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.RecruitCredit;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

public interface RecruitCreditMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RecruitCredit record);

    int insertSelective(RecruitCredit record);

    RecruitCredit selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RecruitCredit record);

    int updateByPrimaryKey(RecruitCredit record);
   
    RecruitCredit queryRecruitCredit(@Param("userId")Integer userId,@Param("recruitId")Integer recruitId);//根据用户编号查询授信记录

	RecruitCredit queryUserIsCredit(@Param("userId")Integer userId, @Param("recruitId")Integer recruitId);//根据用户编号查询授信记录

	int updateUsableMoney(@Param("userId")Integer userId,@Param("recruitId")Integer recruitId ,@Param("usableMoney")BigDecimal usableMoney);//更新授信可用金额

	List<RecruitCredit> findCreditRecord(CmsRequestVo requestVo);//查询授信记录

	int delRecruitCreditRecord(Integer userId);
	
	RecruitCredit findRecruitCredit(Integer recruitId);//根据recruitId统计授信记录
}