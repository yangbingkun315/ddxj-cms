package net.zn.ddxj.mapper;

import net.zn.ddxj.entity.RecruitCategory;

public interface RecruitCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RecruitCategory record);

    int insertSelective(RecruitCategory record);

    RecruitCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RecruitCategory record);

    int updateByPrimaryKey(RecruitCategory record);
    
    int deleteRecruitCategory(int recruitId);//删除招聘分类

	int delRecruitCategoryRecord(Integer userId);
}