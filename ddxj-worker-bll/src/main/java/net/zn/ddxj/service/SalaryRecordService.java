package net.zn.ddxj.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.SalaryRecord;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

public interface SalaryRecordService {
    int deleteByPrimaryKey(Integer id);

    int insert(SalaryRecord record);

    int insertSelective(SalaryRecord record);

    SalaryRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SalaryRecord record);

    int updateByPrimaryKey(SalaryRecord record);
    
    /**
     * 查询发放记录
     * @param userId
     * @param recruitId
     * @return
     */
    List<SalaryRecord> selectByUserIdAndRecruitId(@Param("userId")Integer userId,@Param("recruitId")Integer recruitId);
    /**
     * 统计发放金额
     * @param userId
     * @param recruitId
     * @return
     */
    int selectCount(@Param("userId")Integer userId,@Param("recruitId")Integer recruitId);
    
    BigDecimal totalMoney(@Param("userId")Integer userId,@Param("recruitId")Integer recruitId);
    
    List<SalaryRecord> querySalaryRecord(RequestVo requestVo);
    
    List<SalaryRecord> querySalaryRecordCms(CmsRequestVo requestVo);

	int updateAuditStatus(Integer salaryId, Integer auditStatus);

	int deleteSalaryRecord(Integer userId);//删除收工资记录

	int delSalaryRecord(Integer userId);
	
	
}
