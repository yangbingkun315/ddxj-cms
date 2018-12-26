package net.zn.ddxj.service;

import java.util.List;

import net.zn.ddxj.entity.AppMenu;
import net.zn.ddxj.mapper.AppMenuMapper;
import net.zn.ddxj.vo.CmsRequestVo;

public interface AppMenuService {
    int deleteByPrimaryKey(Integer id);

    int insert(AppMenu record);

    int insertSelective(AppMenu record);

    AppMenu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppMenu record);

    int updateByPrimaryKey(AppMenu record);
    
    List<AppMenu> queryAppMenuList(CmsRequestVo cmsRequestVo);
}
