package net.zn.ddxj.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.CmsRole;
import net.zn.ddxj.vo.CmsRequestVo;

public interface CmsRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CmsRole record);

    int insertSelective(CmsRole record);

    CmsRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CmsRole record);

    int updateByPrimaryKey(CmsRole record);
    
    List<CmsRole> findRoleList(CmsRequestVo requestVo);
    
    int deleteRoleResource(Integer roleId);
    
    int addRoleResource(@Param("roleId")Integer roleId,@Param("resourceId")Integer resourceId);
}