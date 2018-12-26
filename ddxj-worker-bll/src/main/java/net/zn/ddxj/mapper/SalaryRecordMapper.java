package net.zn.ddxj.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.SalaryRecord;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

public interface SalaryRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SalaryRecord record);

    int insertSelective(SalaryRecord record);

    SalaryRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SalaryRecord record);

    int updateByPrimaryKey(SalaryRecord record);
    
    List<SalaryRecord> selectByUserIdAndRecruitId(@Param("userId")Integer userId,@Param("recruitId")Integer recruitId);//查询发放记录
    
    int selectCount(@Param("userId")Integer userId,@Param("recruitId")Integer recruitId);//统计发放次数
    
    BigDecimal totalMoeny(@Param("userId")Integer userId,@Param("recruitId")Integer recruitId);//成功发放金额

	List<SalaryRecord> querySalaryRecord(RequestVo requestVo);
	
	List<SalaryRecord> querySalaryRecordCms(CmsRequestVo requestVo);

	int updateAuditStatus(@Param("salaryId")Integer salaryId, @Param("auditStatus")Integer auditStatus);//更改审核状态

	int deleteSalaryRecord(Integer userId);//删除申请为工头的

	int delSalaryRecord(Integer userId);

	
}