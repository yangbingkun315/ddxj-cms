package net.zn.ddxj.mapper;

import java.util.List;

import net.zn.ddxj.entity.Notice;
import net.zn.ddxj.vo.CmsRequestVo;

public interface NoticeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Notice record);

    int insertSelective(Notice record);

    Notice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Notice record);

    int updateByPrimaryKey(Notice record);
    
    List<Notice> findNoticeList(CmsRequestVo requestVo);
    
    int queryNoticeLower(Integer id);
    
    List<Notice> selectNoticeWorkerNow();
    
    List<Notice> selectNoticeForeWorkerNow();
}