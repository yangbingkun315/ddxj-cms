package net.zn.ddxj.mapper;

import java.util.List;

import net.zn.ddxj.entity.CmsResource;
import net.zn.ddxj.vo.CmsRequestVo;

public interface CmsResourceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CmsResource record);

    int insertSelective(CmsResource record);

    CmsResource selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CmsResource record);

    int updateByPrimaryKey(CmsResource record);
    
    List<CmsResource> findResourceList(CmsRequestVo requestVo);
    
    List<CmsResource> findParentResourceList();
    
    List<CmsResource> findMenuResourceList();
    
    List<CmsResource> findResourceBtnGroup(Integer parentMenuId);
    
    
}