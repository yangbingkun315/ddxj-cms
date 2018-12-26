package net.zn.ddxj.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.CmsUser;
import net.zn.ddxj.vo.CmsRequestVo;

public interface CmsUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CmsUser record);

    int insertSelective(CmsUser record);

    CmsUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CmsUser record);

    int updateByPrimaryKey(CmsUser record);
    
    CmsUser findByUserName(String userName);
    
    List<CmsUser> findUserList(CmsRequestVo requestVo);
    int changeUserPassword(@Param("id")int id, @Param("password")String password);
    int queryUserRoleCount(Integer roleId);
    CmsUser findCmsUserByUserName(String userName);
}