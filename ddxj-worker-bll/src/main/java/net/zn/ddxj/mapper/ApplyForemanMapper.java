package net.zn.ddxj.mapper;

import java.util.List;

import net.zn.ddxj.entity.ApplyForeman;
import net.zn.ddxj.vo.CmsRequestVo;

import org.apache.ibatis.annotations.Param;

public interface ApplyForemanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ApplyForeman record);

    int insertSelective(ApplyForeman record);

    ApplyForeman selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ApplyForeman record);

    int updateByPrimaryKey(ApplyForeman record);
    
    ApplyForeman selectByUserId(Integer userId);

	List<ApplyForeman> queryApplyForeman(CmsRequestVo requestVo);//查询申请工头列表

	int updateApplyForemanStatus(@Param("foremanId")Integer foremanId, @Param("validateStatus")Integer validateStatus, @Param("validateCause")String validateCause);//更新申请状态

	int delApplyForemanRecord(Integer userId);

}