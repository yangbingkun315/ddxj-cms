package net.zn.ddxj.mapper;

import java.util.List;

import net.zn.ddxj.entity.RecruitBanner;

public interface RecruitBannerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RecruitBanner record);

    int insertSelective(RecruitBanner record);

    RecruitBanner selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RecruitBanner record);

    int updateByPrimaryKey(RecruitBanner record);
    
    List<RecruitBanner> queryRecruitBannerByRecruitId(int recruitId);

	int delRecruitBannerRecord(Integer userId);
}