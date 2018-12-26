package net.zn.ddxj.mapper;

import java.util.List;

import net.zn.ddxj.entity.AppMenu;
import net.zn.ddxj.vo.CmsRequestVo;

public interface AppMenuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AppMenu record);

    int insertSelective(AppMenu record);

    AppMenu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppMenu record);

    int updateByPrimaryKey(AppMenu record);
    
    List<AppMenu> queryAppMenuList(CmsRequestVo cmsRequestVo);
}