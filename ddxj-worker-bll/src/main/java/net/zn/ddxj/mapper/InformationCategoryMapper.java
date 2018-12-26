package net.zn.ddxj.mapper;

import net.zn.ddxj.entity.InformationCategory;

public interface InformationCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(InformationCategory record);

    int insertSelective(InformationCategory record);

    InformationCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InformationCategory record);

    int updateByPrimaryKey(InformationCategory record);
    
    int deleteInformationCategoryByInfoId(Integer inforId);//根据资讯ID删除分类
}