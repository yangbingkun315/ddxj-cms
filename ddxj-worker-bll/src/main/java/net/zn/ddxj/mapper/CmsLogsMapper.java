package net.zn.ddxj.mapper;

import java.util.List;

import net.zn.ddxj.entity.CmsLogs;
import net.zn.ddxj.vo.CmsRequestVo;

public interface CmsLogsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CmsLogs record);

    int insertSelective(CmsLogs record);

    CmsLogs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CmsLogs record);

    int updateByPrimaryKey(CmsLogs record);
    
    List<CmsLogs> findLogsByUserId(CmsRequestVo requestVo);
}