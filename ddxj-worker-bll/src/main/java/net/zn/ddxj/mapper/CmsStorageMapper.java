package net.zn.ddxj.mapper;

import java.util.List;

import net.zn.ddxj.entity.CmsStorage;
import net.zn.ddxj.vo.CmsRequestVo;

public interface CmsStorageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CmsStorage record);

    int insertSelective(CmsStorage record);

    CmsStorage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CmsStorage record);

    int updateByPrimaryKey(CmsStorage record);
    
    List<CmsStorage> queryAllStorage(CmsRequestVo requestVo);
}