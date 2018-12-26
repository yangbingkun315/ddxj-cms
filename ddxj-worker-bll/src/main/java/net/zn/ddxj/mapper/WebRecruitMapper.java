package net.zn.ddxj.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import net.zn.ddxj.entity.WebRecruit;
import net.zn.ddxj.vo.CmsRequestVo;

public interface WebRecruitMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WebRecruit record);

    int insertSelective(WebRecruit record);

    WebRecruit selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WebRecruit record);

    int updateByPrimaryKey(WebRecruit record);
    
    List<WebRecruit> queryWebRecruitList(CmsRequestVo requestVo);//多条件查询官网招聘列表
    
    int deleteWebRecruitById(Integer recruitId);//删除招聘信息
    
    int changeWebRecruitStick(@Param("type")Integer type,@Param("id")Integer id);//置顶

	List<WebRecruit> queryMultipleConditional(Map<String, Object> params);//官网多条件查询招聘列表
    
    List<WebRecruit> queryWebRecruitListForStick();//查询置顶的招聘

	WebRecruit queryWebSiteRecruitDetail(Integer recruitId);//查询招聘详情
}