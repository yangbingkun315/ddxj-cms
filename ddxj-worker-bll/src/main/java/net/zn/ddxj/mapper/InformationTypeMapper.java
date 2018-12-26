package net.zn.ddxj.mapper;

import java.util.List;

import net.zn.ddxj.entity.InformationType;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

public interface InformationTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(InformationType record);

    int insertSelective(InformationType record);

    InformationType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InformationType record);

    int updateByPrimaryKey(InformationType record);
    
    List<InformationType> queryInformationTypeList(RequestVo requestVo);//根据查询条件资讯分类列表
    
    List<InformationType> getCategoryByInfoId(Integer id);

	List<InformationType> queryInfoTypeList(CmsRequestVo requestVo);//查询咨询分类
}