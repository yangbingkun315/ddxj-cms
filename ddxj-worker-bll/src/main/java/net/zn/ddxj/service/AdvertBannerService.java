package net.zn.ddxj.service;

import java.util.List;

import net.zn.ddxj.entity.AdvertBanner;
import net.zn.ddxj.vo.CmsRequestVo;
import net.zn.ddxj.vo.RequestVo;

public interface AdvertBannerService {
    int deleteByPrimaryKey(Integer id);

    int insert(AdvertBanner record);

    int insertSelective(AdvertBanner record);

    AdvertBanner selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AdvertBanner record);

    int updateByPrimaryKey(AdvertBanner record);
    
    List<AdvertBanner> selectBanner();
    
    List<AdvertBanner> queryAdvertBannerList(RequestVo requestVo);//查询资讯轮播图列表
    
    List<AdvertBanner> queryCmsAdvertBannerList(CmsRequestVo requestVo);//cms查询资讯轮播图列表
    
    int deleteAdvertBanne(int bannerId);//删除资讯

	AdvertBanner queryIndexBanner();//查询首页广告图片
}
