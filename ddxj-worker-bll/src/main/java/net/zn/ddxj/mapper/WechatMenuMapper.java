package net.zn.ddxj.mapper;

import java.util.List;

import net.zn.ddxj.entity.WechatMenu;

public interface WechatMenuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WechatMenu record);

    int insertSelective(WechatMenu record);

    WechatMenu selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WechatMenu record);

    int updateByPrimaryKey(WechatMenu record);
    
    WechatMenu queryParentMenuById(Integer parentId);//通过parentId查询子菜单
    
	List<WechatMenu> queryAllWeChatMenu(); 
	
    List<WechatMenu> queryParentMenu(Integer parentId);
    
    int deleteMeunByMenuId(Integer menuId);//删除菜单

	List<WechatMenu> queryOneLevelMenu( );//查询一级菜单

	List<WechatMenu> querySecondMenu(Integer parentId);//查询二级菜单
    
    WechatMenu queryWechatMenuByKey(String key);
}